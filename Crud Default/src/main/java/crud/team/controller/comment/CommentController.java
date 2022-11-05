package crud.team.controller.comment;


import crud.team.dto.C;
import crud.team.entity.user.User;
import crud.team.exception.RequestException;
import crud.team.repository.user.UserRepository;
import crud.team.response.Response;
import crud.team.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static crud.team.exception.ExceptionType.ACCESS_DENIED_EXCEPTION;
import static crud.team.exception.ExceptionType.NOT_FOUND_EXCEPTION;
import static crud.team.response.Response.success;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;


    // 댓글 작성
    @PostMapping("/post/{postId}/comment")
    public Response create(@Valid @RequestBody C.CommentRequestDto commentRequestDto, @PathVariable int postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));

        return success(commentService.create(commentRequestDto, user, postId));
    }

    // 댓글 조회
    @GetMapping("/post/{postId}/comment")
    public Response findAllComment(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)
                                       Pageable pageable, @PathVariable int postId) {
        return success(commentService.findAllComment(pageable, postId));
    }

    // 댓글 상세 조회
    @GetMapping("/post/comment/{commentId}")
    public Response findComment(@PathVariable int commentId) {
        return success(commentService.findComment(commentId));
    }

    // 댓글 수정
    @PutMapping("/post/{postId}/comment/{commentId}")
    public Response updateComment(@PathVariable int postId, @PathVariable int commentId, @RequestBody C.CommentRequestDto commentRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));

        return success(commentService.updateComment(postId, commentId, commentRequestDto, user));
    }

    // 댓글 삭제
    @DeleteMapping("/post/{postId}/comment/{commentId}")
    public Response updateComment(@PathVariable int postId, @PathVariable int commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));
        commentService.deleteComment(postId, commentId, user);
        return success();
    }
}
