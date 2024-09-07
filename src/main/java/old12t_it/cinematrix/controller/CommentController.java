package old12t_it.cinematrix.controller;

import lombok.NoArgsConstructor;
import old12t_it.cinematrix.controller.mapper.CommentResourceMapper;
import old12t_it.cinematrix.dtos.request.PostCommentDto;
import old12t_it.cinematrix.dtos.response.CommentResource;
import old12t_it.cinematrix.entity.Comment;
import old12t_it.cinematrix.security.service.UserDetailImp;
import old12t_it.cinematrix.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@NoArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentResourceMapper resourceMapper;

    private final int DEFAULT_PAGE_NUMBER = 0;

    private final int DEFAULT_PAGE_SIZE = 10;

    @GetMapping("/get_one/{id}")
    public ResponseEntity<Object> getComment(@PathVariable long id) {
        Optional<Comment> commentEntity = commentService.getComment(id);
        Optional<CommentResource> commentResource = commentEntity.map(entity -> resourceMapper.map(entity));
        return commentResource.isPresent() ? new ResponseEntity<>(commentResource, HttpStatus.OK) : new ResponseEntity<>("Comment not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<PagedModel<CommentResource>> getComments(
            @PathVariable long contentId,
            @PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable pageRequest,
            PagedResourcesAssembler assembler
    ) {
        Page<Comment> commentPage = commentService.getCommentsOfContent(contentId, pageRequest);
        Page<CommentResource> commentResources = resourceMapper.map(commentPage, pageRequest);
        return new ResponseEntity<>(assembler.toModel(commentResources), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Object> postComment(@AuthenticationPrincipal UserDetailImp userDetails, @RequestBody PostCommentDto dto) {
        long ownerId = userDetails.getId();
        Optional<Comment> entity = commentService.addComment(dto, ownerId);
        Optional<CommentResource> resource = entity.map(comment -> resourceMapper.map(comment));
        return resource.isPresent() ? new ResponseEntity<>(resource, HttpStatus.CREATED) : new ResponseEntity<>("Error posting your comment", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}