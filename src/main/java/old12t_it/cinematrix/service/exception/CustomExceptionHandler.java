package old12t_it.cinematrix.service.exception;

import old12t_it.cinematrix.service.exception.Exception.InvalidInviteCodeException;
import old12t_it.cinematrix.service.exception.Exception.ItemAlreadyExistedException;
import old12t_it.cinematrix.service.exception.Exception.LoginFailException;
import old12t_it.cinematrix.service.exception.Exception.RecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Sorry, Server Has Some Trouble", details);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public final ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Record Not Found", details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidInviteCodeException.class)
    public final ResponseEntity<Object> handleInvalidInviteCodeException(InvalidInviteCodeException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Invalid Invite Code", details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ItemAlreadyExistedException.class)
    public final ResponseEntity<Object> handleItemAlreadyExistedException(ItemAlreadyExistedException ex, WebRequest rq) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Item already existed", details);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(LoginFailException.class)
    public final ResponseEntity<Object> handleLoginFailException(LoginFailException ex, WebRequest rq) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse erResp = new ErrorResponse("Login fail", details);
        return  new ResponseEntity<>(erResp, HttpStatus.NOT_FOUND);

    }
}
