package old12t_it.cinematrix.service.exception.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidInviteCodeException extends RuntimeException {
    public InvalidInviteCodeException(String message) {
        super(message);
    }
}
