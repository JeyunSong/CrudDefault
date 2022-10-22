package crud.team.dto.comment;

import crud.team.entity.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentSimpleResponseDto {
    private String content;
    private String writer;

    public static CommentSimpleResponseDto toDto(Comment comment) {
        return new CommentSimpleResponseDto(comment.getContent(), comment.getWriter());
    }
}