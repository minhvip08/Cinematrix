package old12t_it.cinematrix.repository;

import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import old12t_it.cinematrix.entity.InviteCode;

public interface InviteCodeRepository extends MongoRepository<InviteCode, Long>{
    @Query("{'id' : ?0}")
    InviteCode findById(long id);
    @Query("{'code' : ?0}")
    InviteCode findByCode(String code);
    @Query("{}")
    Set<InviteCode> allCodes();
}
