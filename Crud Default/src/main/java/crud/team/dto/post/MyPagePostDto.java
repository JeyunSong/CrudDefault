package crud.team.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import crud.team.entity.post.Post;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPagePostDto {

    private String title;

    public MyPagePostDto toDto(Post post) {
        return new MyPagePostDto(post.getTitle());
    }

}