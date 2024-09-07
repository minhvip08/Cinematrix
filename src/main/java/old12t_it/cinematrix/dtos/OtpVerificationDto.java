package old12t_it.cinematrix.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OtpVerificationDto {
    @NotNull
    private String email;
    @NotNull
    private String otp;
}
