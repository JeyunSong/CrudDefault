package crud.team.dto.comment;


import crud.team.entity.comment.Comment;
import crud.team.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private int id;
    private String content;
    private String writer;
    private String writeTime;

    public static CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(comment.getId(), comment.getContent(), comment.getWriter(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(comment.getWriteTime()));
    }
}
