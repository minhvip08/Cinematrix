package old12t_it.cinematrix.entity;

import lombok.Getter;
import lombok.Setter;
import old12t_it.cinematrix.dtos.ContentDto;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Document(collection = "contents")
public class Movie extends Content {
    private Media media;

    public Movie() {
        super();
    }

    public Movie(long id, long tmdbId, String imdbId, String title, String overview, LocalDate releaseDate, List<Genre> genres) {
        super(id, tmdbId, imdbId, title, overview, releaseDate, genres);
        this.type = "MOVIE";
        this.media = new Media();
    }

    public Movie(long id, ContentDto dto) {
        super(id, dto);
        this.type = "MOVIE";
        this.media = new Media();
    }
}
