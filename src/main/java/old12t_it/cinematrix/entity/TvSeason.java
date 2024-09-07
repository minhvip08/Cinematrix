package old12t_it.cinematrix.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import old12t_it.cinematrix.dtos.TvSeasonDto;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TvSeason {
    @Id
    private long id;
    private long tmdbId;
    private String imdbId;
    private int seasonNumber;
    private String title;
    private String overview;
    private List<TvEpisode> episodes;

    public TvSeason(long id, long tmdbId, String imdbId, int seasonNumber, String title, String overview) {
        this.id = id;
        this.tmdbId = tmdbId;
        this.seasonNumber = seasonNumber;
        this.title = title;
        this.overview = overview;
        this.episodes = new ArrayList<>();
    }

    public TvSeason(long id, TvSeasonDto dto) {
        this.id = id;
        this.tmdbId = dto.getTmdbId();
        this.seasonNumber = dto.getSeasonNumber();
        this.overview = dto.getOverview();
        this.episodes = new ArrayList<>();
    }
}
