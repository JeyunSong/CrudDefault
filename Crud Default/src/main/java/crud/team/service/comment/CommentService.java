package crud.team.service.comment;


import crud.team.exception.RequestException;
import crud.team.dto.comment.CommentDetailResponseDto;
import crud.team.dto.comment.CommentRequestDto;
import crud.team.dto.comment.CommentResponseDto;
import crud.team.dto.comment.NestedCommentSimpleResponseDto;
import crud.team.entity.comment.Comment;
import crud.team.entity.comment.NestedComment;
import crud.team.entity.post.Post;
import crud.team.entity.user.User;
import crud.team.repository.comment.CommentRepository;
import crud.team.repository.comment.NestedCommentRepository;
import crud.team.repository.post.PostRepository;
import crud.team.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static crud.team.exception.ExceptionType.ACCESS_DENIED_EXCEPTION;
import static crud.team.exception.ExceptionType.NOT_FOUND_EXCEPTION;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NestedCommentRepository nestedCommentRepository;

    // 댓글 작성
    @Transactional
    public CommentResponseDto create(CommentRequestDto commentRequestDto, User user, int postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));
        Comment comment = commentRepository.save(new Comment(commentRequestDto.getContent(), post, user));
        return CommentResponseDto.toDto(comment);
    }

    // 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDto> findAllComment(Pageable pageable, int postId) {

        postRepository.findById(postId).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));

        List<Comment> comment = commentRepository.findByPostId(postId, pageable);
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        comment.forEach(i -> commentResponseDtos.add(CommentResponseDto.toDto(i)));
        return commentResponseDtos;
    }

    // 댓글 상세 조회
    @Transactional(readOnly = true)
    public CommentDetailResponseDto findComment(int commentId) {

        List<NestedComment> nestedComments = nestedCommentRepository.findByCommentId(commentId);
        List<NestedCommentSimpleResponseDto> nestedSimple = new ArrayList<>();
        nestedComments.forEach(i -> nestedSimple.add(NestedCommentSimpleResponseDto.toDto(i)));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));
        return CommentDetailResponseDto.toDto(comment, nestedSimple);
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(int postId, int commentId, CommentRequestDto commentRequestDto, User user){
        postRepository.findById(postId).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));
        if (!user.equals(comment.getUser())) {
            throw new RequestException(ACCESS_DENIED_EXCEPTION);
        }
        comment.update(commentRequestDto.getContent());
        return CommentResponseDto.toDto(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(int postId, int commentId, User user){
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));
        if (!user.equals(comment.getUser())) {
            throw new RequestException(ACCESS_DENIED_EXCEPTION);
        }
        commentRepository.delete(comment);
    }
}
