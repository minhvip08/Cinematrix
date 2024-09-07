package old12t_it.cinematrix.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TvEpisodeDto {
    private long tmdbId;
    private String imdbId;
    private int episodeNumber;
    private String title;
    private String overview;
}
