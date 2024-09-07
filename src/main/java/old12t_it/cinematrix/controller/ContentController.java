package old12t_it.cinematrix.controller;

import lombok.AllArgsConstructor;
import old12t_it.cinematrix.dtos.ContentQueryDbDto;
import old12t_it.cinematrix.dtos.ContentQueryDto;
import old12t_it.cinematrix.entity.Content;
import old12t_it.cinematrix.entity.Genre;
import old12t_it.cinematrix.repository.ContentRepository;
import old12t_it.cinematrix.repository.GenreRepository;
import old12t_it.cinematrix.service.ContentService;
import old12t_it.cinematrix.service.exception.Exception.RecordNotFoundException;
import old12t_it.cinematrix.service.tmdb_service.TmdbService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
@RequestMapping("/api/content")
public class ContentController {
    private TmdbService tmdbService;
    private ContentService contentService;

    private ContentRepository contentRepo;
    private GenreRepository genreRepo;

    private final int DEFAULT_PAGE_SIZE = 5;

    @GetMapping("/search/external")
    public ResponseEntity<?> searchContentExternally(ContentQueryDto contentDto) {
        return new ResponseEntity<>(tmdbService.getContentDetails(contentDto), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(contentRepo.findAll(), HttpStatus.OK);
    }

    @GetMapping("/genres")
    public ResponseEntity<?> getAllGenres() {
        return new ResponseEntity<>(genreRepo.findAll(), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDetail(@PathVariable Long id) {
        Optional<Content> getContent = contentRepo.findById(id);
        if (getContent.isEmpty())
            throw new RecordNotFoundException("Content not found for id:" + id);
        return new ResponseEntity<>(getContent.get(), HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteContent(@RequestBody ContentQueryDbDto querry) {
        contentRepo.deleteById(Long.parseLong(querry.getId()));
        return new ResponseEntity<>("Delete content " + querry.getId() + " success", HttpStatus.OK);
        //TODO:  Delete content media in cloud
    }

    @GetMapping("/movie/all")
    public ResponseEntity<?> getAllVerifiedMov(@PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageRq,
                                               PagedResourcesAssembler assembler) {
        Page<Content> allMovs = contentRepo.findAllVerifiedMov(pageRq);
        return new ResponseEntity<>(assembler.toModel(allMovs), HttpStatus.OK);
    }

    @GetMapping("/tv/all")
    public ResponseEntity<?> getAllVerifiedTv(@PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageRq,
                                              PagedResourcesAssembler assembler) {
        Page<Content> allMovs = contentRepo.findAllVerifiedTv(pageRq);
        return new ResponseEntity<>(assembler.toModel(allMovs), HttpStatus.OK);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyContent(String contentId) throws IOException {
        contentService.verifyContent(Long.parseLong(contentId));
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping("/remove-verified")
    public ResponseEntity<?> removeVerifiedContent(String contentId) throws IOException {
        contentService.removeVerifiedContent(Long.parseLong(contentId));
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping("/check-exist/{id}")
    public ResponseEntity<?> checkContentExist(@PathVariable Long id) {
        Content content = contentRepo.findByTmdbId(id);
        if (content != null) {
            EntityModel<Content> data = EntityModel.of(content,
                    linkTo(methodOn(ContentController.class).getDetail(content.getId())).withSelfRel());
            return new ResponseEntity<>(data, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/genres/{id}")
    public ResponseEntity<?> getContensByGenre(@PathVariable Long id) {
        Genre genre = genreRepo.getById(id);
        if (genre == null) throw new RecordNotFoundException("Genre not found for id:" + id);
        List<Content> contents = this.contentService.getContentByGenre(genre);
        return new ResponseEntity<>(contents, HttpStatus.OK);
    }
}