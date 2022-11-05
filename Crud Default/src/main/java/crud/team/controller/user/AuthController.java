package crud.team.controller.user;

import crud.team.aop.annotation.StopWatch;
import crud.team.dto.user.*;
import crud.team.response.Response;
import crud.team.service.user.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static crud.team.response.Response.success;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;


    @StopWatch
    @PostMapping("/signup")
    public Response signup(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signup(signUpRequestDto);
        return success();
    }

    @PostMapping("/id-duplicate")
    public Response emailDuplicate(@RequestBody EmailValidDto emailValidDto) {
        authService.emailDuplicate(emailValidDto);
        return success();
    }

    @PostMapping("/name-duplicate")
    public Response nameDuplicate(@RequestBody NameValidDto nameValidDto) {
        authService.nameDuplicate(nameValidDto);
        return success();
    }

    @PostMapping("/login")
    public Response login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return success(authService.login(loginRequestDto, response));
    }


    @PostMapping("/reissue")
    public Response reissue(@RequestBody TokenRequestDto tokenRequestDto, HttpServletResponse response) {
        return success(authService.reissue(tokenRequestDto, response));
    }
}
