package crud.team.dto.comment;

import crud.team.entity.comment.NestedComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NestedCommentSimpleResponseDto {
    private String content;
    private String writer;

    public static NestedCommentSimpleResponseDto toDto(NestedComment nestedComment) {
        return new NestedCommentSimpleResponseDto(nestedComment.getContent(), nestedComment.getWriter());
    }
}
