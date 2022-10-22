package crud.team.dto.comment;


import crud.team.entity.comment.Comment;
import crud.team.entity.comment.NestedComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NestedCommentResponseDto {
    private int id;
    private String content;
    private String writer;
    private String writeTime;

    public static NestedCommentResponseDto toDto(NestedComment nestedComment) {
        return new NestedCommentResponseDto(nestedComment.getId(), nestedComment.getContent(), nestedComment.getWriter(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(nestedComment.getWriteTime()));
    }
}
