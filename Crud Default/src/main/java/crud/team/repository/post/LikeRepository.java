package crud.team.repository.post;

import crud.team.entity.post.Like;
import crud.team.entity.post.Post;
import crud.team.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    Like findByPostAndUser(Post post, User user);

    Page<Like> findByUserId(Pageable pageable, int id);
}
