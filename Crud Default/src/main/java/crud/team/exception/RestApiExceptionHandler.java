package crud.team.exception;

import crud.team.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(value = { RequestException.class })
    public ResponseEntity<Response> handleApiRequestException(RequestException e) {
        return new ResponseEntity<>(Response.failure(e.getHttpStatus(), e.getMessage()),
                e.getHttpStatus());
    }
}
