package old12t_it.cinematrix.service.upload_service.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class FileMergeSuccessEvent extends ApplicationEvent {
    @Getter
    private long id;

    public FileMergeSuccessEvent(Object source, long id) {
        super(source);
        this.id = id;
    }
}
