package crud.team.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionType {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생하였습니다."),
    TOKEN_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    LOGIN_FAIL_EXCEPTION(HttpStatus.UNAUTHORIZED, "유저 정보가 일치하지 않습니다."),
    ALREADY_EXISTS_EXCEPTION(HttpStatus.CONFLICT, "이미 값이 존재합니다."),
    ACCESS_DENIED_EXCEPTION(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "요청하신 자료를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
