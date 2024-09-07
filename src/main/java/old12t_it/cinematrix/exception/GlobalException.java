package old12t_it.cinematrix.exception;

import old12t_it.cinematrix.dtos.error.ErrorMessageDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ CinematrixRuntimeException.class })
    protected ResponseEntity<?> handleException(CinematrixRuntimeException ex, WebRequest request) {
        int errorCode = ex.getErrorCode();
        String message = ex.getMessage();
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(errorCode, message);
        HttpStatus status = CinematrixRuntimeException.httpStatusOf(errorCode);
        return handleExceptionInternal(ex, errorMessageDto, new HttpHeaders(), status, request);
    }
}
