package old12t_it.cinematrix.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import old12t_it.cinematrix.entity.Genre;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ContentDto {
    private ContentTypeEnum type;
    private long tmdbId;
    private String imdbId;
    private String title;
    private String overview;
    private LocalDate releaseDate;
    private List<Genre> genres;
    private String posterPath;
    private String backdropPath; //backdrop_path
    private int runtime;
    private double voteAverage;
}
