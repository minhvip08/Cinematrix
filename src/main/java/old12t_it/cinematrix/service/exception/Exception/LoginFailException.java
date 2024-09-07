package old12t_it.cinematrix.service.exception.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LoginFailException extends RuntimeException {
    public LoginFailException(String message) {
        super(message);
    }
}
