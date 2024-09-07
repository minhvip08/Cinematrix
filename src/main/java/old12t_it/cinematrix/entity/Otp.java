package old12t_it.cinematrix.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "otp")
public class Otp {
    @Transient
    public static final String SEQUENCE_NAME = "otp_sequence";

    @Id
    private long id;
    private long userId;
    private LocalDateTime createdTime;
    private LocalDateTime expireTime;
    private boolean isValid;
    private boolean isVerifiedSuccessfully;
    private String otp;

    public Otp(long id, long userId, LocalDateTime createdTime, LocalDateTime expireTime, String otp) {
        this.id = id;
        this.userId = userId;
        this.createdTime = createdTime;
        this.expireTime = expireTime;
        this.isValid = true;
        this.isVerifiedSuccessfully = false;
        this.otp = otp;
    }
}
