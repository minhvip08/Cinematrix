package old12t_it.cinematrix.controller;

import lombok.AllArgsConstructor;
import old12t_it.cinematrix.dtos.*;
import old12t_it.cinematrix.dtos.response.ChunkUploadResponseDto;
import old12t_it.cinematrix.entity.*;
import old12t_it.cinematrix.repository.ContentRepository;
import old12t_it.cinematrix.service.BackblazeService;
import old12t_it.cinematrix.service.ContentService;
import old12t_it.cinematrix.service.FFmpegService;
import old12t_it.cinematrix.service.SequenceGeneratorService;
import old12t_it.cinematrix.service.exception.Exception.ItemAlreadyExistedException;
import old12t_it.cinematrix.service.exception.Exception.RecordNotFoundException;
import old12t_it.cinematrix.service.tmdb_service.TmdbService;
import old12t_it.cinematrix.service.upload_service.UploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/upload")
public class UploadController {
    private TmdbService tmdbService;
    private UploadService uploadService;
    private ContentRepository contentRepo;
    private SequenceGeneratorService seqGenService;
    private ContentService contentService;
    private BackblazeService backblazeService;

    private FFmpegService ffmpegService;
    private final String writeBaseDirectory = Path.of(System.getProperty("user.dir"),"uploaded-content-from-fe").toString();

    @PostMapping("/register")
    public ResponseEntity<Object> registerContentTitle(@RequestBody ContentTitleRegistrationDto dto) {
        Content checkExist = contentRepo.findByTmdbId(dto.getTmdbId());
        if (checkExist != null) {
            Map<String, String> response = new HashMap<>();
            response.put("id", String.valueOf(checkExist.getId()));
            response.put("message", "Content's already existed");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        ContentDto contentDto = tmdbService.getContentDetailsByTmdbId(dto.getTmdbId(), dto.getType());
        if (contentDto == null) {
            return new ResponseEntity<>("TMDB API call failed", HttpStatus.NOT_FOUND);
        }

        Content newContent = null;
        switch (contentDto.getType()) {
            case MOVIE:
                newContent = new Movie(seqGenService.generateSequence(Content.SEQUENCE_NAME), contentDto);
                break;
            case TV:
                newContent = new Tv(seqGenService.generateSequence(Content.SEQUENCE_NAME), contentDto);
                break;
        }
        contentRepo.save(newContent);

        Map<String, String> response = new HashMap<>();
        response.put("id", String.valueOf(newContent.getId()));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register/tv-season")
    public ResponseEntity<Object> addTvSeason(@RequestBody TvSeasonRegistrationDto dto) {
        Tv existingContent = (Tv) contentRepo.findById(dto.getId());
        if (existingContent == null) {
            return new ResponseEntity<>("Content does not exist.", HttpStatus.NOT_FOUND);
        }
        if (contentRepo.findTvSeason(dto.getId(), dto.getSeasonNumber()) != null) {
            return new ResponseEntity<>("Season's already registered", HttpStatus.CONFLICT);
        }
        TvSeasonDto tvSeasonDto = tmdbService.getTvSeasonDetails(existingContent.getTmdbId(), dto.getSeasonNumber());
        if (tvSeasonDto == null) {
            return new ResponseEntity<>("TV season does not exist.", HttpStatus.NOT_FOUND);
        }
        contentService.addTvSeason(dto.getId(), tvSeasonDto);
        return new ResponseEntity<>(tvSeasonDto, HttpStatus.CREATED);
    }

    @PostMapping("/register/tv-episode")
    public ResponseEntity<Object> addTvSEpisode(@RequestBody TvEpisodeRegistrationDto dto) {
        Tv existingContent = (Tv) contentRepo.findById(dto.getId());
        if ((existingContent == null) || (contentRepo.findTvSeason(dto.getId(), dto.getSeasonNumber()) == null)) {
            return new ResponseEntity<>("Content does not exists", HttpStatus.NOT_FOUND);
        }
        if (contentRepo.findTvEpisode(dto.getId(), dto.getSeasonNumber(), dto.getEpisodeNumber()) != null) {
            return new ResponseEntity<>("Episode's already registered", HttpStatus.CONFLICT);
        }
        TvEpisodeDto tvEpisodeDto = tmdbService.getTvEpisodeDetails(existingContent.getTmdbId(), dto.getSeasonNumber(), dto.getEpisodeNumber());
        if (tvEpisodeDto == null) {
            return new ResponseEntity<>("TV episode does not exist.", HttpStatus.NOT_FOUND);
        }
        contentService.addTvEpisode(dto.getId(), dto.getSeasonNumber(), tvEpisodeDto);
        return new ResponseEntity<>(tvEpisodeDto, HttpStatus.CREATED);
    }

    @PostMapping("/sv/init")
    public ResponseEntity<Object> initUpload(@RequestBody ContentUploadRegistrationDto dto) {
        switch (dto.getType()) {
            case MOVIE:
                Movie movie = (Movie) contentRepo.findById(dto.getId());
                if (movie == null) {
                    throw new RecordNotFoundException("Content not found for id:" + dto.getId());
                }
                if (movie.getMedia().isUploaded()) {
                    throw new ItemAlreadyExistedException("Item already existed for id:" + dto.getId());
                }
                break;
            case TV:
                TvEpisode tvEpisode = contentRepo.findTvEpisode(dto.getId());
                if (tvEpisode == null) {
                    throw new ItemAlreadyExistedException("Item already existed for id:" + dto.getId());
                }
                if (tvEpisode.getMedia().isUploaded()) {
                    return new ResponseEntity<>("Media's already existed", HttpStatus.CONFLICT);
                }
                break;
        }

        Map<String, String> response = new HashMap<>();
        try {
            uploadService.initFileUpload(dto);
            response.put("route", "/api/content/upload-chunk/");
            response.put("id", String.valueOf(dto.getId()));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (Exception e) {
            throw new RuntimeException("Error initializing file upload:" + e.getMessage());
        }
    }
    @PostMapping("/sv/chunk")
    public ResponseEntity<?> chunkUpload(
            @RequestParam("chunk") MultipartFile chunk,
            @RequestParam("chunkNumber") int chunkNumber,
            @RequestParam("totalChunks") int totalChunks,
            @RequestParam("id") long id
    ) {
        try {
            uploadService.handleChunkUpload(chunk, chunkNumber, totalChunks, id);
            ChunkUploadResponseDto dto = new ChunkUploadResponseDto(chunkNumber, id, "success" );
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        }
        catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, String.format("Failed to handle chunk upload: identifier: %d, chunk number: %d", String.valueOf(id), chunkNumber));
        }
    }

    @PostMapping("/storage/dir")
    public ResponseEntity<?> upload2Storage(@RequestBody String folderId) {
        String dirPath = Path.of(writeBaseDirectory, folderId).toString();
        String outFilePth = Path.of(dirPath, "out.mp4").toString();
        File file = new File(outFilePth);
        System.out.println("    [-]upload 2 storage folder:" + outFilePth);
        if (file.exists()) {
            if (!file.delete())
                throw new RuntimeException("Fail to delete file out.file in directory");
        }
        try {
            this.backblazeService.uploadDirectory(dirPath);
        } catch (Exception e) {
            return new ResponseEntity<>("upload 2 storage fail:" + e.getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity<>("upload 2 storage sc", HttpStatus.OK);
    }
    @PostMapping("/convert/file")
    public ResponseEntity<?> testConvert(@RequestBody String id) {
        try {
        FFmpegTask fFmpegTask =  ffmpegService.generateStream(Path.of(writeBaseDirectory, id, "out.mp4").toString());
        return new ResponseEntity<>("ok", HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Convert error:" + e.getMessage(), HttpStatus.OK);
        }

    }
    @GetMapping("/check/upload-fe")
    public ResponseEntity<?> checkUploadFolderFe() {
        File uploadDir = new File(writeBaseDirectory);
        File[] files = uploadDir.listFiles();
        return new ResponseEntity<>(files, HttpStatus.OK);
    }
    @GetMapping("/check/upload-fe/folder/{id}")
    public ResponseEntity<?> checkUploadFolderFeDetail(@PathVariable String id) {
        File uploadDir = new File(Path.of(writeBaseDirectory, id).toString()) ;
        System.out.println("    [-]check upload dir:" + uploadDir);
        File[] files = uploadDir.listFiles();
        String countFile = String.valueOf(files.length);
        String mpdPath = writeBaseDirectory + "/" + id + "/stream.mpd";
        File mpdFile = new File(mpdPath);
        String fileExist = mpdFile.exists() ? "true" : "false";
        Dictionary<String,String> rt = new Hashtable<>();
        rt.put("number of file", countFile);
        rt.put("file exists", fileExist);
        return new ResponseEntity<>(rt, HttpStatus.OK);
    }
}
