package old12t_it.cinematrix.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import old12t_it.cinematrix.dtos.TvEpisodeDto;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
public class TvEpisode {
    @Id
    private long id;
    private long tmdbId;
    private String imdbId;
    private int episodeNumber;
    private String title;
    private String overview;
    private Media media;

    public TvEpisode() {
        this.id = 0;
        this.media = new Media();
    }

    public TvEpisode(long id, TvEpisodeDto dto) {
        this.id = id;
        this.tmdbId = dto.getTmdbId();
        this.imdbId = dto.getImdbId();
        this.episodeNumber = dto.getEpisodeNumber();
        this.title = dto.getTitle();
        this.overview = dto.getOverview();
        this.media = new Media();
    }
}
