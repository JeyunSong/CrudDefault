package crud.team.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL) //  null 값을 가지는 필드는, JSON 응답에 포함되지 않음
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Response {
    private boolean result;
    private HttpStatus httpStatus;
    private Result check;

    public static Response success() {
        return new Response(true, HttpStatus.OK,null);
    }

    public static <T> Response success(T data) {
        return new Response(true, HttpStatus.OK, new Success<>(data));
    }

    public static Response failure(HttpStatus httpStatus, String msg) {
        return new Response(false, httpStatus, new Failure(msg));
    }
}

