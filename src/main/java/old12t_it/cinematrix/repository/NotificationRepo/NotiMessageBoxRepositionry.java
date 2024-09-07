package old12t_it.cinematrix.repository.NotificationRepo;

import old12t_it.cinematrix.entity.NotiMessageBox;
import old12t_it.cinematrix.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface NotiMessageBoxRepositionry  extends MongoRepository<NotiMessageBox, Long> {
    @Query("{'id' : ?0}")
    public NotiMessageBox findById(long id);
    @Query("{'owner':  ?0, } ")
    public NotiMessageBox findByUser(Long id);
}
