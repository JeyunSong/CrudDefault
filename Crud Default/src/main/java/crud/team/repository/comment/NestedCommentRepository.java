package crud.team.repository.comment;

import crud.team.entity.comment.NestedComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NestedCommentRepository extends JpaRepository<NestedComment, Integer> {
    List<NestedComment> findByCommentId(int commentId);
}

