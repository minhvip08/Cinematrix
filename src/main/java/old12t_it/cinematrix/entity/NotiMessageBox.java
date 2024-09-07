package old12t_it.cinematrix.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor //phai co constructor rong thi no moi co the khoi tao trong repository
@Document(collection = "noti_message_box")
public class NotiMessageBox {
    @Transient
    public static final String SEQUENCE_NAME = "noti_message_seq";
    @Id
    Long id;
    @DocumentReference
    User owner;
    @DocumentReference
    List<NotiMessage> messages;
}
