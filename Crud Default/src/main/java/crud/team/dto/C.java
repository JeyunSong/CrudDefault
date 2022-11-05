package crud.team.dto;

import crud.team.entity.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class C {


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentRequestDto {
        @NotBlank(message = "댓글을 작성해주세요")
        private String content;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentResponseDto {
        private int id;
        private String content;
        private String writer;
        private String writeTime;
        public static C.CommentResponseDto toDto(Comment comment) {
            return new C.CommentResponseDto(comment.getId(), comment.getContent(), comment.getWriter(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd").format(comment.getWriteTime()));
        }
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentSimpleResponseDto {
        private String content;
        private String writer;
        private boolean correct;
        public static C.CommentSimpleResponseDto toDto(Comment comment, String name) {
            return new C.CommentSimpleResponseDto(comment.getContent(), comment.getWriter(),
                    comment.getWriter().equals(name));
        }
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentDetailResponseDto {
        private int id;
        private String CommentContent;
        private String CommentWriter;
        private String CommentWriteTime;
        private List<NC.NestedCommentSimpleResponseDto> NestedCommentList;
        public static C.CommentDetailResponseDto toDto(Comment comment, List<NC.NestedCommentSimpleResponseDto> nestedSimple) {
            return new C.CommentDetailResponseDto(comment.getId(), comment.getContent(), comment.getWriter(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd").format(comment.getWriteTime()),
                    nestedSimple
            );
        }
    }
}
