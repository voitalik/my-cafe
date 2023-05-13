package cafecatalog.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {

    @Autowired
    private CommentRepository repository;

    @Test
    void ratingCafe14Ok() {
        Integer expected = (3 + 2 + 3) / 3;
        Integer actual = repository.getAvgRatingByCafeId(14L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void ratingCafe14NotOk() {
        Integer expected = 1;
        Integer actual = repository.getAvgRatingByCafeId(14L);
        Assertions.assertNotEquals(expected, actual);
    }

    @Test
    void ratingCafe11Ok() {
        Integer expected = 4;
        Integer actual = repository.getAvgRatingByCafeId(11L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void ratingCafe11NotOk() {
        Integer expected = 1;
        Integer actual = repository.getAvgRatingByCafeId(11L);
        Assertions.assertNotEquals(expected, actual);
    }
}