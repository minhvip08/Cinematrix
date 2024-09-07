package old12t_it.cinematrix.service;

import old12t_it.cinematrix.entity.Otp;
import old12t_it.cinematrix.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;


@Service
public class OtpService {
    @Autowired
    private MongoOperations mongoOps;

    @Autowired
    private OtpRepository otpRepo;

    @Autowired
    private SequenceGeneratorService seqGenService;

    @Value("${otp.duration}")
    private long expireDuration;

    private String generateOtp() {
        Random random = new Random();
        int randomNumber = random.nextInt(1_000_000);
        return String.format("%06d", randomNumber);
    }

    public Otp addNewOtp(long userId) {
        LocalDateTime currentTime = LocalDateTime.now();
        Otp newOtp = new Otp(seqGenService.generateSequence(Otp.SEQUENCE_NAME), userId, currentTime, currentTime.plusSeconds(expireDuration), generateOtp());
        mongoOps.findAndModify(
            new Query().addCriteria(Criteria.where("userId").is(userId).and("isValid").is(true)),
            new Update().set("isValid", false),
            Otp.class
        );
        return mongoOps.insert(newOtp);
    }

    public boolean verifyOtp(long userId, String otpString) {
        LocalDateTime currentTime = LocalDateTime.now();
        Otp otp =  mongoOps.findOne(
            new Query().addCriteria(
                    Criteria.where("userId").is(userId)
                    .and("isValid").is(true)
                    .and("expireTime").gte(currentTime)
                    .and("otp").is(otpString)
            ),
            Otp.class
        );

        if (otp == null) {
            return false;
        }
        otp.setVerifiedSuccessfully(true);
        otp.setValid(false);
        otpRepo.save(otp);
        return true;
    }
}
