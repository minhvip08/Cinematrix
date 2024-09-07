package old12t_it.cinematrix.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "invite_keys")
public class InviteCode {
    @Transient
    public static final String SEQUENCE_NAME = "invite_keys_sequence";
    @Id
    private long id;
    private String inviterId;
    private String ownerId;
    private LocalDate createdDate;
    private LocalDate usedDate; // date that this code is used by its owner
    private String code;
    private boolean isCanceled; // bool that this code is not accepted anymore

    public InviteCode(long _id, String _code) {
        id = _id;
        code = _code;
        createdDate = LocalDate.now(); //bitrtday of this code
        isCanceled = false;
    }
}
