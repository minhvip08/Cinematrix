package old12t_it.cinematrix.service.upload_service.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class AllChunksWriteSuccessEvent extends ApplicationEvent {
    @Getter
    private long id;

    @Getter
    private int totalChunks;

    public AllChunksWriteSuccessEvent(Object source, long id, int totalChunks) {
        super(source);
        this.id = id;
        this.totalChunks = totalChunks;
    }
}
