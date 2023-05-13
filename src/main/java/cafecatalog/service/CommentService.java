package cafecatalog.service;

import java.util.List;
import cafecatalog.model.Comment;

public interface CommentService {

    Comment add(Comment comment);

    List<Comment> findAllByCafeId(Long cafeId);

    List<Comment> findAllByUserId(Long userId);

    Integer getAvgRatingByCafeId(Long cafeId);
}
