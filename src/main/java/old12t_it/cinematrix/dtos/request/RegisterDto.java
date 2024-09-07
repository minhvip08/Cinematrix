package old12t_it.cinematrix.dtos.request;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterDto {
    @NotNull(message = "Full name can not empty")
    private String fullName;
    
    @NotNull(message = "Email can not empty")
    private String email;
    
    @NotNull(message = "Password can not empty")
    private String password;
    
    @NotNull(message = "Invite code can not empty")
    private String inviteCode;
}
