package old12t_it.cinematrix.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import old12t_it.cinematrix.entity.Comment;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResource extends RepresentationModel<CommentResource> {
    @JsonProperty("id")
    private long id;
    private String content;
    private LocalDateTime createdAt;

    private long ownerId;

    public CommentResource(Comment entity) {
        super();
        this.id = entity.getId();
        this.content = entity.getContent();
        this.createdAt = entity.getCreatedAt();
        this.ownerId = entity.getOwnerId();
    }
}
