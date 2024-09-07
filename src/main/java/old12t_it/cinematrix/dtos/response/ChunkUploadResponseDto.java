package old12t_it.cinematrix.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChunkUploadResponseDto {
    Integer chunkIndex;
    long id;
    String status;
}
