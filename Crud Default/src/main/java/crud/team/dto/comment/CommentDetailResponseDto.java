package crud.team.dto.comment;

import crud.team.entity.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDetailResponseDto {
    private int id;
    private String CommentContent;
    private String CommentWriter;
    private String CommentWriteTime;
    private List<NestedCommentSimpleResponseDto> NestedCommentList;

    public static CommentDetailResponseDto toDto(Comment comment, List<NestedCommentSimpleResponseDto> nestedSimple) {
        return new CommentDetailResponseDto(comment.getId(), comment.getContent(), comment.getWriter(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(comment.getWriteTime()),
                nestedSimple
                );
    }
}
