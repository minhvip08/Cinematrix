package old12t_it.cinematrix.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "noti_message")
public class NotiMessage {
    @Transient
    public static final String SEQUENCE_NAME = "not-msg-seq";
    @Id
    long id;
    String subject;
    String content;
    String imgUrl;
    LocalDate releaseTime;
    Boolean isSeen;
}
