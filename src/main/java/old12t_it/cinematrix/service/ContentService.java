package old12t_it.cinematrix.service;

import lombok.AllArgsConstructor;
import old12t_it.cinematrix.dtos.TvEpisodeDto;
import old12t_it.cinematrix.dtos.TvSeasonDto;
import old12t_it.cinematrix.entity.*;
import old12t_it.cinematrix.repository.ContentRepository;
import old12t_it.cinematrix.repository.GenreRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
@AllArgsConstructor
public class ContentService {

    SequenceGeneratorService seqGenService;
    private ContentRepository contentRepo;
    private GenreRepository genreRepository;
    MongoTemplate template;

    public void addTvSeason(Long id, TvSeasonDto dto) {
        Tv tv = (Tv) contentRepo.findById(id).orElse(null);
        if (tv != null) {
            TvSeason tvSeason = new TvSeason(seqGenService.generateSequence(Content.SEQUENCE_NAME), dto);
            tv.getSeasons().add(tvSeason);
            contentRepo.save(tv);
        }
    }

    public void addTvEpisode(Long id, int seasonNumber, TvEpisodeDto dto) {
        Tv tv = (Tv) contentRepo.findById(id).orElse(null);
        if (tv != null) {
            TvSeason tvSeason = tv.getSeasons().stream()
                    .filter(season -> (season.getSeasonNumber() == seasonNumber))
                    .findFirst()
                    .orElse(null);
            if (tvSeason != null) {
                TvEpisode tvEpisode = new TvEpisode(seqGenService.generateSequence(Content.SEQUENCE_NAME), dto);
                tvSeason.getEpisodes().add(tvEpisode);
                contentRepo.save(tv);
            }
        }
    }

    public void verifyContent(Long contentId) {
        template.update(Content.class)
                .matching(where("_id").is(contentId))
                .apply(new Update().set("isVerified", true))
                .first();
    }

    public void removeVerifiedContent(Long contentId) {
        template.update(Content.class)
                .matching(where("_id").is(contentId))
                .apply(new Update().set("isVerified", false))
                .first();
    }

    public List<Content> getContentByGenre(Genre genre) {
        List<Content> contents = contentRepo.findAll();
        contents = contents.stream().filter(c -> {
            List<String> ids = c.getGenres().stream().map(g -> g.getName()).toList();
            return ids.contains(genre.getName());

        }).toList();
        return contents;
    }

}
