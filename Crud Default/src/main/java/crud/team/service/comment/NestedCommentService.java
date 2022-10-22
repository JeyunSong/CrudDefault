package crud.team.service.comment;


import crud.team.exception.RequestException;
import crud.team.dto.comment.NestedCommentRequestDto;
import crud.team.dto.comment.NestedCommentResponseDto;
import crud.team.entity.comment.Comment;
import crud.team.entity.comment.NestedComment;
import crud.team.entity.user.User;
import crud.team.repository.comment.CommentRepository;
import crud.team.repository.comment.NestedCommentRepository;
import crud.team.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static crud.team.exception.ExceptionType.ACCESS_DENIED_EXCEPTION;
import static crud.team.exception.ExceptionType.NOT_FOUND_EXCEPTION;

@RequiredArgsConstructor
@Service
public class NestedCommentService {
    private final NestedCommentRepository nestedCommentRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    // 대댓글은 댓글 상세 조회 시 확인 가능합니다.

    // 대댓글 작성
    @Transactional
    public NestedCommentResponseDto create(NestedCommentRequestDto nestedCommentRequestDto, User user, int id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));
        NestedComment nestedComment = nestedCommentRepository.save(new NestedComment(nestedCommentRequestDto.getContent(), comment, user));
        return NestedCommentResponseDto.toDto(nestedComment);
    }

    // 대댓글 수정
    @Transactional
    public NestedCommentResponseDto updateNestedComment(int id, NestedCommentRequestDto nestedCommentRequestDto, User user){
        NestedComment nestedComment = nestedCommentRepository.findById(id).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));
        if (!user.equals(nestedComment.getUser())) {
            throw new RequestException(ACCESS_DENIED_EXCEPTION);
        }
        nestedComment.update(nestedCommentRequestDto.getContent());
        return NestedCommentResponseDto.toDto(nestedComment);
    }

    // 대댓글 삭제
    @Transactional
    public void deleteNestedComment(int id, User user){
        NestedComment nestedComment = nestedCommentRepository.findById(id).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));
        if (!user.equals(nestedComment.getUser())) {
            throw new RequestException(ACCESS_DENIED_EXCEPTION);
        }
        nestedCommentRepository.delete(nestedComment);
    }
}
