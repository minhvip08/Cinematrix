package old12t_it.cinematrix.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ContentUploadRegistrationDto {
    @NotEmpty
    private long id;

    private ContentTypeEnum type;

    private long size;

    private int totalChunks;

    @NotEmpty
    private String checksum;

    @NotEmpty
    private String checksumAlgorithm;
}
