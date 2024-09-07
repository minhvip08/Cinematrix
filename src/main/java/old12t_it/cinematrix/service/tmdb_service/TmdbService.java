package old12t_it.cinematrix.service.tmdb_service;

import old12t_it.cinematrix.dtos.*;
import old12t_it.cinematrix.entity.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TmdbService {
    @Autowired
    TmdbApiHandle tmdbApiHandle;

    public Object getContentDetails(ContentQueryDto contentDto) {
        if (contentDto.getType() == null) {
            List<Object> results = (List<Object>) getMovieDetails(contentDto);
            results.addAll((List<Object>) getTvDetails(contentDto));
            return results;
        } else if (contentDto.getType() == ContentTypeEnum.MOVIE) {
            return getMovieDetails(contentDto);
        } else if (contentDto.getType() == ContentTypeEnum.TV) {
            return getTvDetails(contentDto);
        }
        return new HashMap<>();
    }

    private Object getMovieDetails(ContentQueryDto contentDto) {
        Map<String, String> params = new HashMap<>();
        params.put("query", contentDto.getName());
        params.put("primary_release_year", (contentDto.getYear() != null) ? contentDto.getYear().toString() : null);
        return tmdbApiHandle.queryTmdb("search/movie", params).get("results");
    }

    private Object getTvDetails(ContentQueryDto contentDto) {
        Map<String, String> params = new HashMap<>();
        params.put("query", contentDto.getName());
        params.put("first_air_date_year", (contentDto.getYear() != null) ? contentDto.getYear().toString() : null);
        return tmdbApiHandle.queryTmdb("search/tv", params).get("results");
    }

    public ContentDto getContentDetailsByTmdbId(long id, ContentTypeEnum type) {
        if (type == ContentTypeEnum.MOVIE) {
            Map<String, Object> result = tmdbApiHandle.queryTmdb("movie/" + id, null);
            if ((result != null) && !(result.isEmpty())) {
                return new ContentDto(
                        ContentTypeEnum.MOVIE,
                        id,
                        (String) result.get("imdb_id"),
                        (String) result.get("title"),
                        (String) result.get("overview"),
                        LocalDate.parse((CharSequence) result.get("release_date")),
                        (List<Genre>) result.get("genres"),
                        (String) result.get("poster_path"),
                        (String) result.get("backdrop_path"),
                        (int) result.get("runtime"),
                        (double) result.get("vote_average")
                );
            }
        } else if (type == ContentTypeEnum.TV) {
            Map<String, Object> result = tmdbApiHandle.queryTmdb("tv/" + id, null);
            if ((result != null) && !(result.isEmpty())) {
                return new ContentDto(
                        ContentTypeEnum.TV,
                        id,
                        (String) result.get("imdb_id"),
                        (String) result.get("title"),
                        (String) result.get("overview"),
                        LocalDate.parse((CharSequence) result.get("first_air_date")),
                        (List<Genre>) result.get("genres"),
                        (String) result.get("poster_path"),
                        (String) result.get("backdrop_path"),
                        (int) result.get("runtime"),
                        (double) result.get("vote_average")
                );
            }
        }
        return null;
    }

    public TvSeasonDto getTvSeasonDetails(long id, int seasonNumber) {
        Map<String, Object> result = tmdbApiHandle.queryTmdb(String.format("tv/%d/season/%d", id, seasonNumber), null);
        if ((result != null) && !(result.isEmpty())) {
            return new TvSeasonDto(
                    id,
                    (String) result.get("imdbId"),
                    seasonNumber,
                    (String) result.get("name"),
                    (String) result.get("overview"),
                    new ArrayList<>()
            );
        }
        return null;
    }

    public TvEpisodeDto getTvEpisodeDetails(long id, int seasonNumber, int episodeNumber) {
        Map<String, Object> result = tmdbApiHandle.queryTmdb(String.format("tv/%d/season/%d/episode/%d", id, seasonNumber, episodeNumber), null);
        if ((result != null) && !(result.isEmpty())) {
            return new TvEpisodeDto(
                    id,
                    (String) result.get("imdbId"),
                    episodeNumber,
                    (String) result.get("name"),
                    (String) result.get("overview")
            );
        }
        return null;
    }
}