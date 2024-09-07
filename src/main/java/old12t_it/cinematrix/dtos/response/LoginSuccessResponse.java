package old12t_it.cinematrix.dtos.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class LoginSuccessResponse {
    String jwtToken;
    String id;
    String email;
    List<String> roles;

}
