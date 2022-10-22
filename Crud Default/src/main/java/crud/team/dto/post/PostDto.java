package crud.team.dto.post;

import crud.team.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private int id;
    private String title;
    private String content;
    private String writer;
    private int like;
    private int commentNum;
    private String writeTime;

    public static PostDto toDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getName(),
                post.getLikeNum(),
                post.getCommentNum(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(post.getWriteTime())
        );
    }
}