package old12t_it.cinematrix.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import old12t_it.cinematrix.dtos.CommentTargetTypeEnum;

@Getter
@Setter
@AllArgsConstructor
public class PostCommentDto {
    private long targetId;
    private CommentTargetTypeEnum targetType;
    private String content;
}
