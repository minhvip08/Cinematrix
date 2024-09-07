package old12t_it.cinematrix.dtos.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class NotiMessageListDto {
    List<Long> notiMsgLst;
}
