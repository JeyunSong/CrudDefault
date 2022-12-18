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
    public static class PostDetailResponseDto {
        private int postId;
        private String title;
        private String content;
        private String img;
        private String name;
        private String createTime;
        private int likeNum;
        private int commentNum;
        public static P.PostDetailResponseDto toDto(Post post) {
            return new P.PostDetailResponseDto(
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
    public static class PostSimpleResponseDto {
        private int postId;
        private String title;
        private boolean imgStatus;
        private String name;
        private String createTime;
        private int likeNum;
        private int commentNum;

        public static P.PostSimpleResponseDto toDto(Post post) {
            return new P.PostSimpleResponseDto(
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
