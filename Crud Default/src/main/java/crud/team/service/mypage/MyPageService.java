package crud.team.service.mypage;

import crud.team.dto.MP;
import crud.team.entity.comment.Comment;
import crud.team.entity.post.Like;
import crud.team.entity.post.Post;
import crud.team.entity.user.User;
import crud.team.repository.comment.CommentRepository;
import crud.team.repository.post.LikeRepository;
import crud.team.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    // 내가 작성한 게시물 조회
    @Transactional(readOnly = true)
    public List<MP.MyPagePostDto> findMyPost(Pageable pageable, User user) {

        Page<Post> posts = postRepository.findByUserId(pageable, user.getId());
        List<MP.MyPagePostDto> myPagePostDtos = new ArrayList<>();
        posts.stream().forEach(i -> myPagePostDtos.add(new MP.MyPagePostDto().toDto(i)));
        return myPagePostDtos;
    }

    // 내가 작성한 댓글 조회
    @Transactional(readOnly = true)
    public List<MP.MyPageCommentDto> findMyComment(Pageable pageable, User user) {

        Page<Comment> comments = commentRepository.findByUserId(pageable, user.getId());
        List<MP.MyPageCommentDto> myPageCommentDtos = new ArrayList<>();
        comments.stream().forEach(i -> myPageCommentDtos.add(MP.MyPageCommentDto.toDto(i)));
        return myPageCommentDtos;
    }

    // 내가 좋아요 한 게시물 조회
    @Transactional(readOnly = true)
    public List<MP.MyPagePostDto> findMyLike(Pageable pageable, User user) {

        Page<Like> likes = likeRepository.findByUserId(pageable, user.getId());
        List<MP.MyPagePostDto> myPagePostDtos = new ArrayList<>();
        likes.stream().forEach(i -> myPagePostDtos.add(new MP.MyPagePostDto().toDto(i.getPost())));
        return myPagePostDtos;
    }
}
