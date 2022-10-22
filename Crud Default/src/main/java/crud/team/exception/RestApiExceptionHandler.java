package crud.team.exception;

import crud.team.response.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static crud.team.response.Response.failure;

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(value = { RequestException.class })
    public Response handleApiRequestException(RequestException e) {
        return failure(e.getCode(), e.getMessage());
    }
}
