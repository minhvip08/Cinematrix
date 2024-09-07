package old12t_it.cinematrix.entity;

import lombok.*;
import old12t_it.cinematrix.dtos.ContentDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "contents")
public abstract class Content {
    @Transient
    public static final String SEQUENCE_NAME = "contents_sequence";

    @Id
    @Field("_id")
    protected long id;
    protected String type;
    protected long tmdbId;
    protected String imdbId;
    protected String title;
    protected String overview;
    protected LocalDate releaseDate;
    @DBRef
    protected List<Genre> genres;
    protected boolean isVerified;
    private String posterPath;
    private String backdropPath; //backdrop_path
    private int runtime;
    private double voteAverage;

    public Content() { id = 0; };

    public Content(long id, long tmdbId, String imdbId, String title, String overview, LocalDate releaseDate, List<Genre> genres) {
        this.id = id;
        this.tmdbId = tmdbId;
        this.imdbId = imdbId;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genres = genres;
        this.isVerified = false;
    }

    public Content(long id, ContentDto dto) {
        this.id = id;
        this.tmdbId = dto.getTmdbId();
        this.imdbId = dto.getImdbId();
        this.title = dto.getTitle();
        this.overview = dto.getOverview();
        this.releaseDate = dto.getReleaseDate();
        this.genres = dto.getGenres();
        this.posterPath = dto.getPosterPath();
        this.backdropPath = dto.getBackdropPath();
        this.runtime = dto.getRuntime();
        this.voteAverage = dto.getVoteAverage();
    }
}
