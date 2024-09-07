package old12t_it.cinematrix.dtos.request;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GiveICDto {
    @NotNull(message = "Invite code ID can not empty")
    private String inviteCodeId;
    @NotNull(message = "User ID can not empty")
    private String userId;
}
