package crud.team.dto.comment;

import crud.team.entity.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPageCommentDto {
    private String postTitle;
    private String content;

    public static MyPageCommentDto toDto(Comment comment) {
        return new MyPageCommentDto(comment.getPost().getTitle(), comment.getContent());
    }
}