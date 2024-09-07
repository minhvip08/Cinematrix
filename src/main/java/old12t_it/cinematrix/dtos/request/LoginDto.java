package old12t_it.cinematrix.dtos.request;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginDto {
    @NotNull(message = "Email can not empty")
    private String email;
    @NotNull(message = "Password can not empty")
    private String password;
}
