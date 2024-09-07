package old12t_it.cinematrix.service.exception;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ErrorResponse {
    public ErrorResponse(String message, List<String> details) {
        super();
        this.message = message;
        this.details = details;
    }
    private String message;
    private List<String> details;
}
