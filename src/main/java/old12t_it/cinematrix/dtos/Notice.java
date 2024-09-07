package old12t_it.cinematrix.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class Notice implements Serializable {
    //Subject notification on firebase
    private String subject;
    //content
    private String content;

    private String imgUrl;

    private Map<String, String> data;

    //moi user dang nhap vao bang nhieu thiet bi
    //moi thiet bi co mot FCM registration token rieng
    private List<String> registrationTokens;


}
