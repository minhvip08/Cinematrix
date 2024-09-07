package old12t_it.cinematrix.service.upload_service.event;

import lombok.Getter;
import old12t_it.cinematrix.service.upload_service.model.Chunk;
import org.springframework.context.ApplicationEvent;

public class ChunkWriteSuccessEvent extends ApplicationEvent {
    @Getter
    private Chunk chunk;

    public ChunkWriteSuccessEvent(Object source, Chunk chunk) {
        super(source);
        this.chunk = chunk;
    }
}
