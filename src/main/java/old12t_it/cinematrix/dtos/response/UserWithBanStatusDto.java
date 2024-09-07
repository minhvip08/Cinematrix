package old12t_it.cinematrix.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import old12t_it.cinematrix.entity.Role;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserWithBanStatusDto {
    Long id;
    String fullName;
    String email;
    LocalDate lastactiveDate;
    Role role;
    Boolean isBanned;
}
