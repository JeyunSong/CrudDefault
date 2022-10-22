package crud.team.exception;

import lombok.Getter;

@Getter
public class RequestException extends RuntimeException {
    private final int code;

    public RequestException(ExceptionType exceptionType) {
        super(exceptionType.getMessage()); // RuntimeException 클래스의 생성자를 호출합니다.
        this.code = exceptionType.getCode();
    }
}
