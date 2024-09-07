package old12t_it.cinematrix.repository;

import old12t_it.cinematrix.entity.Content;
import old12t_it.cinematrix.entity.Media;
import old12t_it.cinematrix.entity.Movie;
import old12t_it.cinematrix.entity.TvEpisode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

public interface ContentRepository extends MongoRepository<Content, Long> {
    @Query("{'_id': ?0}")
    Content findById(long id);

    @Query("{'tmdbId': ?0}")
    Content findByTmdbId(long id);

    @Query("{'_id': ?0, 'seasons.seasonNumber': ?1}")
    Content findTvSeason(long id, int seasonNumber);

    @Query("{'_id': ?0, 'seasons.seasonNumber': ?1, 'seasons.episodes.episodeNumber': ?2}")
    Content findTvEpisode(long id, int seasonNumber, int episodeNumber);

    @Query(value = "{'seasons.episodes._id': ?0}, {'seasons.episodes.$': 1}")
    TvEpisode findTvEpisode(long id);
    @Query("{'isVerified' : true}")
    List<Content> findAllVerifiedCt();
    @Query("{'type': MOVIE, 'isVerified': true}")
    Page<Content> findAllVerifiedMov(Pageable pageRq);
    @Query("{'type': TV, 'isVerified': true}")
    Page<Content> findAllVerifiedTv(Pageable pageRq);
}
