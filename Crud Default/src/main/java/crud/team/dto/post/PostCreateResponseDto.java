package crud.team.dto.post;

import crud.team.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NotBlank
public class PostCreateResponseDto {
    private int postId;
    private String title;
    private String content;
    private String name;
    private String image;
    private String createTime;

    public static PostCreateResponseDto toDto(Post post){
        return new PostCreateResponseDto(post.getId(), post.getTitle(),
                post.getContent(), post.getWriter(), post.getImgUrl(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(post.getWriteTime()));
    }
}
