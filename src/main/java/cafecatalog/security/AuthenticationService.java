package cafecatalog.security;

import cafecatalog.exception.AuthenticationException;
import cafecatalog.model.User;

public interface AuthenticationService {
    User register(String email, String password, String username);

    User login(String login, String password) throws AuthenticationException;
}
