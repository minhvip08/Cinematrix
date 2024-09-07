package old12t_it.cinematrix.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import old12t_it.cinematrix.entity.Content;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContentRespDto {
    Content content;
    Boolean existed;
}
