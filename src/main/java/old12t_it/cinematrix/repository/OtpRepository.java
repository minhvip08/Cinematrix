package old12t_it.cinematrix.repository;

import old12t_it.cinematrix.entity.Otp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OtpRepository extends MongoRepository<Otp, Long> {
    @Query("{'userId': ?0, 'expireTime': {$gte: ?1}}")
    List<Otp> findValidOtpByUserId(long userId, LocalDateTime currentTime);
}
