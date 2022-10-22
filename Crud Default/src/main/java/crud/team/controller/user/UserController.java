package crud.team.controller.user;


import crud.team.exception.RequestException;
import crud.team.dto.user.UserDto;
import crud.team.entity.user.User;
import crud.team.repository.user.UserRepository;
import crud.team.response.Response;
import crud.team.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static crud.team.exception.ExceptionType.ACCESS_DENIED_EXCEPTION;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    public Response findAllUsers() {
        return Response.success(userService.findAllUsers());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{id}")
    public Response findUser(@PathVariable int id) {
        return Response.success(userService.findUser(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users/{id}")
    public Response editUserInfo(@PathVariable int id, @RequestBody UserDto userDto) {
        return Response.success(userService.editUserInfo(id, userDto));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/users/{id}")
    public Response deleteUserInfo(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));

        userService.deleteUserInfo(user, id);
        return Response.success();
    }
}