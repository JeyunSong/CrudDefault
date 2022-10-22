package crud.team.repository.comment;

import crud.team.entity.comment.Comment;
import crud.team.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByPostId(int id);
    List<Comment> findByPostId(int id, Pageable pageable);

    Page<Comment> findByUserId(Pageable pageable, int id);
}
