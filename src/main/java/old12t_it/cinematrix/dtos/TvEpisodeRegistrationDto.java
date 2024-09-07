package old12t_it.cinematrix.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TvEpisodeRegistrationDto {
    @NotEmpty
    private long id;
    @NotEmpty
    private int seasonNumber;
    @NotEmpty
    private int episodeNumber;
}
