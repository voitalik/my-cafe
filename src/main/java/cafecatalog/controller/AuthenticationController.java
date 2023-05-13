package cafecatalog.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import cafecatalog.dto.mapper.UserMapper;
import cafecatalog.dto.request.UserLoginDto;
import cafecatalog.dto.request.UserRegistrationDto;
import cafecatalog.dto.response.UserLoginResponseInfo;
import cafecatalog.dto.response.UserResponseDto;
import cafecatalog.exception.AuthenticationException;
import cafecatalog.model.Role;
import cafecatalog.model.User;
import cafecatalog.security.AuthenticationService;
import cafecatalog.security.jwt.JwtTokenProvider;
import cafecatalog.security.token.Token;
import cafecatalog.security.token.TokenService;
import cafecatalog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;
    private UserService userService;

    public AuthenticationController(AuthenticationService authenticationService,
                                    UserMapper userMapper,
                                    JwtTokenProvider jwtTokenProvider,
                                    TokenService tokenService,
                                    UserService userService) {
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        User user = authenticationService.register(userRegistrationDto.getEmail(),
                userRegistrationDto.getPassword(), userRegistrationDto.getUsername());
        return userMapper.mapToDto(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid UserLoginDto userLoginDto)
            throws AuthenticationException {
        User user = authenticationService.login(userLoginDto.getUsername(),
                userLoginDto.getPassword());

        revokeAllUserTokens(user);
        String accessToken = getToken(user, false);
        String refreshToken = getToken(user, true);
        UserLoginResponseInfo responseBody =
                userMapper.mapToLoginResponseInfo(user, accessToken, refreshToken);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.resolveToken(request);
        String email = jwtTokenProvider.getUsername(refreshToken);
        User user = userService.findByEmail(email).orElseThrow();
        UserLoginResponseInfo responseBody = null;
        // this validation can be removed, because it validates at JwtFilter
        if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
            revokeAllUserTokens(user);
            String accessToken = getToken(user, false);
            responseBody =
                    userMapper.mapToLoginResponseInfo(user, accessToken, refreshToken);
        }
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    private String getToken(User user, boolean isRefreshToken) {
        String token = jwtTokenProvider.createToken(user.getEmail(),
                user.getRoles().stream()
                        .map(Role::getRoleName)
                        .map(Enum::name)
                        .collect(Collectors.toList()), isRefreshToken);
        if (! isRefreshToken) {
            Token modelToken = Token.builder()
                    .user(user)
                    .token(token)
                    .expired(false)
                    .revoked(false)
                    .build();
            tokenService.save(modelToken);
        }
        return token;
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validTokens = tokenService.findAllValidTokensByUser(user.getId());
        if (! validTokens.isEmpty()) {
            validTokens.forEach(token -> {
                token.setRevoked(true);
                token.setExpired(true);
            });
            tokenService.saveAll(validTokens);
        }
    }
}
