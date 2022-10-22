package crud.team.dto.post;

import crud.team.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostSimpleDto {
    private int postId;
    private String title;
    private String content;
    private String img;
    private String name;
    private String createTime;
    private int likeNum;
    private int commentNum;


    public PostSimpleDto toDto(Post post, int commentNum) {
        return new PostSimpleDto(post.getId() ,post.getTitle(), post.getContent(),
                post.getImgUrl(), post.getUser().getName(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(post.getWriteTime()),
                post.getLikeNum(), commentNum);
    }
}