package old12t_it.cinematrix.dtos.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class UserNotiMsgDto {
    private Long userId;
    //Subject notification on firebase
    private String subject;
    //content
    private String content;

    private String imgUrl;

    private Map<String, String> data;
}
