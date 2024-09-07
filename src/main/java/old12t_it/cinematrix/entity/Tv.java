package old12t_it.cinematrix.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import old12t_it.cinematrix.dtos.ContentDto;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "contents")
public class Tv extends Content {
    private List<TvSeason> seasons;

//    public Tv() {
//        super();
//        this.seasons = null;
//    }

    public Tv(long id, long tmdbId, String imdbId, String title, String overview, LocalDate releaseDate, List<Genre> genres, List<TvSeason> seasons) {
        super(id, tmdbId, imdbId, title, overview, releaseDate, genres);
        this.type = "TV";
        this.seasons = seasons;
    }

    public Tv(long id, ContentDto dto) {
        super(id, dto);
        this.type = "TV";
        this.seasons = new ArrayList<>();
    }
}
