package crud.team.repository.post;

import crud.team.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAllPageBy(Pageable pageable);
    Page<Post> findByTitleContaining(String keyword, Pageable pageable);
    Page<Post> findByUserId(Pageable pageable, int id);
    Page<Post> findByLikeNumGreaterThanEqual(Pageable pageable, int i);

}
