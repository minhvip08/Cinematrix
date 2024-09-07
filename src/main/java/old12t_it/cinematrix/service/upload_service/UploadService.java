package old12t_it.cinematrix.service.upload_service;

import old12t_it.cinematrix.dtos.ContentUploadRegistrationDto;
import old12t_it.cinematrix.entity.*;
import old12t_it.cinematrix.repository.ContentRepository;
import old12t_it.cinematrix.service.BackblazeService;
import old12t_it.cinematrix.service.FFmpegService;
import old12t_it.cinematrix.service.SequenceGeneratorService;
import old12t_it.cinematrix.service.upload_service.event.AllChunksWriteSuccessEvent;
import old12t_it.cinematrix.service.upload_service.event.ChunkWriteSuccessEvent;
import old12t_it.cinematrix.service.upload_service.event.FileMergeSuccessEvent;
import old12t_it.cinematrix.service.upload_service.event.MediaConvertSuccessEvent;
import old12t_it.cinematrix.service.upload_service.model.UploadingFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class UploadService {
    @Autowired
    Publisher publisher;

    @Autowired
    MongoOperations mongoOps;

    private String writeBaseDirectory;

    @Autowired
    private FileOperator fileOperator;

    @Autowired
    private SequenceGeneratorService seqGenService;
    @Autowired
    private ContentRepository contentRepo;

    @Autowired
    private FFmpegService ffmpegService;
    @Autowired
    private BackblazeService storageService;

    private final Map<Long, UploadingFile> uploadingFiles;

    public UploadService() {
        this.writeBaseDirectory = Path.of(System.getProperty("user.dir") , "uploaded-content-from-fe").toString();
        uploadingFiles = new HashMap<>();
    }

    @EventListener
    private void onChunkWriteSuccess(ChunkWriteSuccessEvent event) {
        long id = event.getChunk().getId();
        uploadingFiles
                .get(id)
                .addChunk(event.getChunk());
        System.out.println(uploadingFiles.get(id).getNoChunksUploaded());
        if (uploadingFiles.get(id).isAllFileUploaded()) {
            publisher.publishAllChunksWriteSuccessEvent(id, uploadingFiles.get(id).getTotalChunks());
        }
    }

    @EventListener
    private void onAllChunksWriteSuccess(AllChunksWriteSuccessEvent event) {
        System.out.println("File " + event.getId() + " uploaded successfully.");
        try {
            fileOperator.assembleFileParts(event.getId());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @EventListener
    private void onFileMergeSuccess(FileMergeSuccessEvent event) {
        System.out.println("File " + event.getId() + " merged successfully.");
        try {
            //clean up chunk file, set file extension to .mp4
            fileOperator.cleanUpFileParts(event.getId());
            System.out.println("File " + event.getId() + " clean up completed.");

            ffmpegService.generateStream(Path.of(writeBaseDirectory, String.valueOf(event.getId()), "out.mp4").toString());

            ////assign new media for content
            Media newMedia = new Media(seqGenService.generateSequence(Media.MEDIA_SEQUENCE), true, uploadingFiles.get(event.getId()).getPath());
            Content ct = contentRepo.findById(event.getId());
            // Tv
            if (ct.getType().equals("TV")) {

                Tv tv = mongoOps.findOne(new Query(Criteria.where("seasons.episodes._id").is(event.getId())), Tv.class);
                for (TvSeason season : tv.getSeasons()) {
                    for (TvEpisode episode : season.getEpisodes()) {
                        if (episode.getId() == event.getId()) {
                            episode.setMedia(newMedia);
                            mongoOps.save(tv);
                            uploadingFiles.remove(event.getId());
                            return;
                        }
                    }
                }
            } else {
                // Movie
                mongoOps.findAndModify(
                        query(where("_id").is(event.getId())),
                        new Update().set("media", newMedia),
                        Movie.class
                );
            }
            //remove uploaded file from map
            uploadingFiles.remove(event.getId());

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @EventListener
    public void onMediaConvertSuccess(MediaConvertSuccessEvent event) {
        //push to storage
        try {
            System.out.println("Upload to storage");
            System.out.println("    [-]Upload path:" + event.getPath());
            storageService.uploadDirectory(event.getPath());
        } catch (Exception e) {
            System.out.println("Fail to upload to storage:" +e.getMessage());
            throw new RuntimeException("Fail to upload to storage");
        }
    }

    public void initFileUpload(ContentUploadRegistrationDto dto) throws IOException {
        long id = dto.getId();
        Path path = Paths.get(writeBaseDirectory, String.valueOf(id));

        if (uploadingFiles.containsKey(id) || Files.isDirectory(path)) {
            throw new FileAlreadyExistsException(String.format("Directory for %s's already existed.", String.valueOf(id)));
        }

        try {
            Files.createDirectory(path);
            UploadingFile newUploadingFile = new UploadingFile(id, path.toString(), dto.getTotalChunks());
            uploadingFiles.put(id, newUploadingFile);
        } catch (IOException e) {
            throw new IOException("Unable to create directory " + path);
        }
    }

    public String handleChunkUpload(MultipartFile chunk, int chunkNumber, int totalChunks, long id) throws IOException {
        InputStream chunkInputStream = chunk.getInputStream();
        String filePath = fileOperator.writeChunk(chunkInputStream, id, chunkNumber);
        return filePath;
    }


}
