package crud.team.dto;

import crud.team.entity.comment.Comment;
import crud.team.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MP {


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyPagePostDto {
        private String title;
        public MyPagePostDto toDto(Post post) {
            return new MyPagePostDto(post.getTitle());
        }
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyPageCommentDto {
        private String postTitle;
        private String content;
        public static MyPageCommentDto toDto(Comment comment) {
            return new MyPageCommentDto(comment.getPost().getTitle(), comment.getContent());
        }
    }
}
