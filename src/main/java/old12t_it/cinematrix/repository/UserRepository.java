package old12t_it.cinematrix.repository;

import old12t_it.cinematrix.entity.User;

import java.util.Optional;
import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;



@Component
public interface UserRepository extends MongoRepository<User, Long> {
    //Magic in the air, spring boot make this function work for us 
    @Query("{'email' : ?0}")
    User findUserByEmail(String email);     
    @Query("{'inviteCodesId' : ?0}")
    Set<String> findByInviteCodesId(long id);
    @Query("{'id' : ?0}")
    Optional<User> findUserById(long id);

    @Query("{$or:[{'username': {$regex: ?0, $options: 'i'}}, {'email': {$regex: ?1, $options: 'i'}}]}")
    List<User> findByNameOrEmail(String username, String email);
    public long count();

}
