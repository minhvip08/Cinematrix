package old12t_it.cinematrix.service.upload_service;

import lombok.AllArgsConstructor;
import old12t_it.cinematrix.service.upload_service.event.AllChunksWriteSuccessEvent;
import old12t_it.cinematrix.service.upload_service.event.ChunkWriteSuccessEvent;
import old12t_it.cinematrix.service.upload_service.event.FileMergeSuccessEvent;
import old12t_it.cinematrix.service.upload_service.event.MediaConvertSuccessEvent;
import old12t_it.cinematrix.service.upload_service.model.Chunk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class Publisher {
    private ApplicationEventPublisher publisher;

    public void publishChunkWriteSuccessEvent(Chunk chunk) {
        publisher.publishEvent(new ChunkWriteSuccessEvent(this, chunk));
    }

    public void publishAllChunksWriteSuccessEvent(long id, int totalChunks) {
        publisher.publishEvent(new AllChunksWriteSuccessEvent(this, id, totalChunks));
    }

    public void publishFileMergeSuccessEvent(long id) {
        publisher.publishEvent(new FileMergeSuccessEvent(this, id));
    }


    public void publishMediaConvertSuccessEvent(String path) {
        publisher.publishEvent(new MediaConvertSuccessEvent(this, path));
    }
}
