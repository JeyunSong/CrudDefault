package crud.team.controller.mypage;


import crud.team.exception.RequestException;
import crud.team.entity.user.User;
import crud.team.repository.user.UserRepository;
import crud.team.response.Response;
import crud.team.service.mypage.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static crud.team.exception.ExceptionType.ACCESS_DENIED_EXCEPTION;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MyPageController {

    private final MyPageService myPageService;
    private final UserRepository userRepository;

    @GetMapping("/mypage/post")
    @ResponseStatus(HttpStatus.OK)
    public Response findMyPost(@PageableDefault(size = 5, sort = "writeTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));

        return Response.success(myPageService.findMyPost(pageable, user));
    }

    @GetMapping("/mypage/comment")
    @ResponseStatus(HttpStatus.OK)
    public Response findMyComment(@PageableDefault(size = 5, sort = "writeTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));

        return Response.success(myPageService.findMyComment(pageable, user));
    }

    @GetMapping("/mypage/like")
    @ResponseStatus(HttpStatus.OK)
    public Response findMyLike(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));

        return Response.success(myPageService.findMyLike(pageable, user));
    }



}
