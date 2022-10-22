package crud.team.controller.comment;


import crud.team.exception.RequestException;
import crud.team.dto.comment.NestedCommentRequestDto;
import crud.team.entity.user.User;
import crud.team.repository.user.UserRepository;
import crud.team.response.Response;
import crud.team.service.comment.NestedCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static crud.team.exception.ExceptionType.ACCESS_DENIED_EXCEPTION;
import static crud.team.response.Response.success;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class NestedCommentController {

    private final NestedCommentService nestedCommentService;
    private final UserRepository userRepository;

    // 대댓글 작성
    @PostMapping("/comment/{id}/nested")
    @ResponseStatus(HttpStatus.CREATED)
    public Response create(@Valid @RequestBody NestedCommentRequestDto nestedCommentRequestDto, @PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));

        return success(nestedCommentService.create(nestedCommentRequestDto, user, id));
    }

    // 댓글 수정
    @PutMapping("/comment/nested/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response updateNestedComment(@PathVariable int id, @RequestBody NestedCommentRequestDto nestedCommentRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));

        return success(nestedCommentService.updateNestedComment(id, nestedCommentRequestDto, user));
    }

    // 댓글 삭제
    @DeleteMapping("/comment/nested/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteNestedComment(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));
        nestedCommentService.deleteNestedComment(id, user);
        return success();
    }

}
