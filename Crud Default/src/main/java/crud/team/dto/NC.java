package crud.team.dto;

import crud.team.entity.comment.NestedComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.format.DateTimeFormatter;

public class NC {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NestedCommentRequestDto {
        @NotBlank(message = "상세 댓글을 작성해주세요")
        private String content;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NestedCommentResponseDto {
        private int id;
        private String content;
        private String writer;
        private String writeTime;

        public static NC.NestedCommentResponseDto toDto(NestedComment nestedComment) {
            return new NC.NestedCommentResponseDto(nestedComment.getId(), nestedComment.getContent(), nestedComment.getWriter(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd").format(nestedComment.getWriteTime()));
        }
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NestedCommentSimpleResponseDto {
        private String content;
        private String writer;
        public static NC.NestedCommentSimpleResponseDto toDto(NestedComment nestedComment) {
            return new NC.NestedCommentSimpleResponseDto(nestedComment.getContent(), nestedComment.getWriter());
        }
    }
}
