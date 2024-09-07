package old12t_it.cinematrix.controller.mapper;

import lombok.NoArgsConstructor;
import old12t_it.cinematrix.controller.CommentController;
import old12t_it.cinematrix.dtos.response.CommentResource;
import old12t_it.cinematrix.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@NoArgsConstructor
public class CommentResourceMapper {
    public CommentResource map(Comment entity) {
        CommentResource resource = new CommentResource(entity);
        addLinks(resource, entity);
        return resource;
    }

    public Page<CommentResource> map(Page<Comment> entities, Pageable pageable) {
        List<CommentResource> list =  entities.stream()
                .map(entity -> this.map(entity))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, entities.getTotalElements());
    }

    private void addLinks(CommentResource resource, Comment entity) {
        Link selfLink = WebMvcLinkBuilder.linkTo(CommentController.class).slash(entity.getId()).withSelfRel();
        resource.add(selfLink);

        entity.getRepliedBy().stream().forEach(reply -> {
            Link replyCommentLink = WebMvcLinkBuilder
                    .linkTo(methodOn(CommentController.class).getComment(reply))
                    .withRel("reply");
            resource.add(replyCommentLink);
        });
    }
}
