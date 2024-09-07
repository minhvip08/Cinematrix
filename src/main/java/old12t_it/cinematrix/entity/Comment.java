package old12t_it.cinematrix.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import old12t_it.cinematrix.dtos.CommentTargetTypeEnum;
import old12t_it.cinematrix.dtos.request.PostCommentDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "comments")
@AllArgsConstructor
public class Comment {
    @Transient
    public static final String SEQUENCE_NAME = "comment_sequence";

    @Id
    private long id;
    private String content;
    private CommentTargetTypeEnum targetType;
    private long targetId;
    private long ownerId;
    private LocalDateTime createdAt;
    private List<Long> repliedBy;

    public Comment() {
        this.id = 0;
        this.targetId = 0;
        this.ownerId = 0;
    }

    public Comment(long id, PostCommentDto dto, long ownerId) {
        this.id = id;
        this.content = dto.getContent();
        this.targetType = dto.getTargetType();
        this.targetId = dto.getTargetId();
        this.ownerId = ownerId;
        this.createdAt = LocalDateTime.now();
        this.repliedBy = new ArrayList<>();
    }
}
