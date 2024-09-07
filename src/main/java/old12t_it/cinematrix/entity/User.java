package old12t_it.cinematrix.entity;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";
    @Id
    private long id;
    private String fullName;
    private String email;
    private LocalDate lastActiveDate; // optinal field
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String hashString; // password's hash string
    Long ownInviteCodeId; // id of the invite code that this user has
    Role role;
    boolean isBanned;
    @DocumentReference
    Set<InviteCode> inviteCodes; // list of invite codes that this user has

    Set<String> deviceTokens;
    public User(long _id, String _fullName, String _email, String _hashString) {
        id = _id;
        fullName = _fullName;
        email = _email;
        hashString = _hashString;
        lastActiveDate = LocalDate.now(); // get the current date
        role = Role.USER; //defaul role
        isBanned = true;
    }

    @Override
    public String toString() {
        return "User {email=" + email + ", fullName=" + fullName + ", hashString=" + hashString + ", id=" + id + "}";
    }
}
