package old12t_it.cinematrix.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Getter
public class CinematrixRuntimeException extends RuntimeException{
    private final int errorCode;
    public CinematrixRuntimeException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    // 4xxxx: Access error
    // 400xx: Bad request
    public static final int BAD_REQUEST = 40000;
    // 401xx: Unauthorized
    public static final int UNAUTHORIZED = 40100;
    public static final int USER_ALREADY_EXISTS = 40101;
    public static final int INVALID_CREDENTIALS = 40102;
    public static final int INVALID_TOKEN = 40103;
    // 403xx: Forbidden
    public static final int ACCESS_FORBIDDEN = 40300;

    // 5xxxx: Server error
    public static final int INTERNAL_SERVER_ERROR = 50000;
    public static final int TASK_NOT_FOUND = 50001;
    public static final int USER_NOT_FOUND = 50002;

    public static HttpStatus httpStatusOf(int errorCode) {
        return Optional.ofNullable(HttpStatus.resolve(errorCode)).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
