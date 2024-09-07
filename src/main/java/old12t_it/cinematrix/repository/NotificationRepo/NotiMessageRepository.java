package old12t_it.cinematrix.repository.NotificationRepo;

import old12t_it.cinematrix.entity.NotiMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface NotiMessageRepository extends MongoRepository<NotiMessage, Long> {
    @Query("{'id': ?0}")
    public NotiMessage findById(long id);
}
