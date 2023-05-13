package cafecatalog.service;

import java.util.Optional;
import cafecatalog.model.Cafe;
import cafecatalog.model.User;

public interface UserService {
    User save(User user);

    User findById(Long id);

    Optional<User> findByEmail(String email);

    void addToFavourites(Cafe cafe, User user);

    void removeFromFavourites(Cafe cafe, User user);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmailOrUsername(String email, String username);

    String getUserEmail();
}
