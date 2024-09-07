package old12t_it.cinematrix.service.upload_service.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MediaConvertSuccessEvent extends ApplicationEvent {
    private String path;

    public MediaConvertSuccessEvent(Object source, String _path) {
        super(source);
        this.path = _path;
    }
}
