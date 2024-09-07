package old12t_it.cinematrix.repository;

import old12t_it.cinematrix.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comment, Long> {
    @Query("{'_id': ?0}")
    Comment findById(long id);

    @Query("{'targetType': 'CONTENT', 'targetId': ?0}")
    Page<Comment> findByContentId(long contentId, Pageable pageable);
}
