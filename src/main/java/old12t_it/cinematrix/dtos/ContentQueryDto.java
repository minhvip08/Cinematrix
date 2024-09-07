package old12t_it.cinematrix.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ContentQueryDto {
    private String id;
    private String name;
    private Integer year;
    private ContentTypeEnum type;
}