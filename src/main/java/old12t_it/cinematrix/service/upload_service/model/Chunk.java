package old12t_it.cinematrix.service.upload_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Chunk {
    private long id;
    private String path;
    private Integer chunkNumber;
}
