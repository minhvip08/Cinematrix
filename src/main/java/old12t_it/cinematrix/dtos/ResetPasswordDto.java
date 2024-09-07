package old12t_it.cinematrix.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResetPasswordDto {
    private String email;
    private String otp;
    private String newPassword;
}
