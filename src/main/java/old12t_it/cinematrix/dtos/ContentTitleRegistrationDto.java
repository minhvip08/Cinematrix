package old12t_it.cinematrix.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ContentTitleRegistrationDto {
    @NotEmpty
    private long tmdbId;
    @NotEmpty
    private ContentTypeEnum type;
}
