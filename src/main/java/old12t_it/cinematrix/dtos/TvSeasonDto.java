package old12t_it.cinematrix.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import old12t_it.cinematrix.entity.TvEpisode;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TvSeasonDto {
    private long tmdbId;
    private String imdbId;
    private int seasonNumber;
    private String title;
    private String overview;
    private List<TvEpisode> episodes;
}
