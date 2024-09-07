package old12t_it.cinematrix.repository;

import old12t_it.cinematrix.entity.Content;
import old12t_it.cinematrix.entity.Genre;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface GenreRepository extends MongoRepository<Genre, Long> {
    @Query("{'_id': ?0}")
    Genre getById(long genreId);
}
