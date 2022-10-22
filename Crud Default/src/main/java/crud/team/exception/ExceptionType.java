package crud.team.exception;

import lombok.Getter;

@Getter
public enum ExceptionType {
    BAD_REQUEST(400, "잘못된 요청입니다."),

    INTERNAL_SERVER_ERROR(500, "알 수 없는 오류가 발생하였습니다."),

    TOKEN_EXPIRED_EXCEPTION(400, "토큰이 만료되었습니다."),
    LOGIN_FAIL_EXCEPTION(401, "유저 정보가 일치하지 않습니다."),
    ALREADY_EXISTS_EXCEPTION(401, "이미 값이 존재합니다."),

    ACCESS_DENIED_EXCEPTION(403, "접근 권한이 없습니다."),

    NOT_FOUND_EXCEPTION(404, "요청하신 자료를 찾을 수 없습니다.");

    private final int code;
    private final String message;

    ExceptionType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
