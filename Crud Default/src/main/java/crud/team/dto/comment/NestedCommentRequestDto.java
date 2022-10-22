package crud.team.dto.comment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NestedCommentRequestDto {

    @NotBlank(message = "상세 댓글을 작성해주세요")
    private String content;
}
