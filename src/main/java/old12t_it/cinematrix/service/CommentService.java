package old12t_it.cinematrix.service;

import lombok.NoArgsConstructor;
import old12t_it.cinematrix.dtos.CommentTargetTypeEnum;
import old12t_it.cinematrix.dtos.request.PostCommentDto;
import old12t_it.cinematrix.entity.Comment;
import old12t_it.cinematrix.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@NoArgsConstructor
public class CommentService {
    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private SequenceGeneratorService seqGenService;

    private Sort DEFAULT_SORT;

    public Page<Comment> getComments(Pageable pageable) {
        return commentRepo.findAll(pageable);
    }

    public Optional<Comment> getComment(long id) {
        return Optional.ofNullable(commentRepo.findById(id));
    }

    public Page<Comment> getCommentsOfContent(long contentId, Pageable pageRequest) {
        Pageable sorted = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
//        return commentRepo.findByContentId(contentId, pageRequest);
        return commentRepo.findByContentId(contentId, sorted);

    }

    private boolean addReplyCommentTo(long id, long targetId) {
        Comment target = commentRepo.findById(targetId);
        if (target != null) {
            List<Long> replyComments = target.getRepliedBy();
            replyComments.add(id);
            commentRepo.save(target);
            return true;
        }
        return false;
    }

    public Optional<Comment> addComment(PostCommentDto dto, long ownerId) {
        // TODO: verify dto's targetId and userId
        Comment comment = new Comment(seqGenService.generateSequence(Comment.SEQUENCE_NAME), dto, ownerId);
        if (dto.getTargetType() == CommentTargetTypeEnum.COMMENT) {
            if (!addReplyCommentTo(comment.getId(), comment.getTargetId())) {
                return Optional.empty();
            }
        }
        return Optional.of(commentRepo.save(comment));
    }
}
