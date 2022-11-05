package crud.team.dto;

import crud.team.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.format.DateTimeFormatter;

public class P {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostCreateRequestDto {
        @NotBlank(message = "게시글 제목을 입력해주세요.")
        private String title;
        @NotBlank(message = "게시글 본문을 입력해주세요.")
        private String content;
    }


    @Data
    @AllArgsConstructor
    @NotBlank
    public static class PostCreateResponseDto {
        private int postId;
        private String title;
        private String content;
        private String name;
        private String image;
        private String createTime;
        private int likeNum;
        private int commentNum;
        public static P.PostCreateResponseDto toDto(Post post){
            return new P.PostCreateResponseDto(post.getId(), post.getTitle(),
                    post.getContent(), post.getWriter(), post.getImgUrl(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd").format(post.getWriteTime()),
                    0,0);
        }
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostDetailRequestDto {
        private int postId;
        private String title;
        private String content;
        private String img;
        private String name;
        private String createTime;
        private int likeNum;
        private int commentNum;
        public static P.PostDetailRequestDto toDto(Post post) {
            return new P.PostDetailRequestDto(
                    post.getId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getImgUrl(),
                    post.getUser().getName(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd").format(post.getWriteTime()),
                    post.getLikeNum(),
                    post.getCommentNum()
            );
        }
    }



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostSimpleRequestDto {
        private int postId;
        private String title;
        private boolean imgStatus;
        private String name;
        private String createTime;
        private int likeNum;
        private int commentNum;
        public static P.PostSimpleRequestDto toDto(Post post) {
            return new P.PostSimpleRequestDto(
                    post.getId() ,
                    post.getTitle(),
                    (!post.getImgUrl().equals("")),
                    post.getUser().getName(),
                    DateTimeFormatter.ofPattern("MM-dd").format(post.getWriteTime()),
                    post.getLikeNum(),
                    post.getCommentNum()
            );
        }
    }



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostUpdateRequestDto {
        @NotBlank(message = "수정할 제목을 입력해주세요.")
        private String title;
        @NotBlank(message = "수정할 내용을 입력해주세요.")
        private String content;
    }
}
