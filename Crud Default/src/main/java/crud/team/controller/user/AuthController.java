package crud.team.controller.user;

import crud.team.dto.user.*;
import crud.team.response.Response;
import crud.team.service.user.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static crud.team.response.Response.success;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public Response signup(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signup(signUpRequestDto);
        return success();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/id-duplicate")
    public Response emailDuplicate(@RequestBody EmailValidDto emailValidDto) {
        authService.emailDuplicate(emailValidDto);
        return success();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/name-duplicate")
    public Response nameDuplicate(@RequestBody NameValidDto nameValidDto) {
        authService.nameDuplicate(nameValidDto);
        return success();
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Response login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return success(authService.login(loginRequestDto, response));
    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reissue")
    public Response reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return success(authService.reissue(tokenRequestDto));
    }
}
