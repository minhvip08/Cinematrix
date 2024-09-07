package old12t_it.cinematrix.service.upload_service;

import old12t_it.cinematrix.service.upload_service.model.Chunk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;

@Service
public class FileOperator {
    @Autowired
    Publisher publisher;

//    @Value("${file.write.directory}")
    private final String baseDirectory = System.getProperty("user.dir") + "/uploaded-content-from-fe";

    public String writeChunk(InputStream inputStream, long id, int chunkNumber) throws IOException {
        Path writeDirectoryPath = Paths.get(baseDirectory, String.valueOf(id));

        if (!Files.isDirectory(writeDirectoryPath)) {
            throw new FileNotFoundException("%s directory does not exist.".format(String.valueOf(id)));
        }

        Path filePath = Paths.get(writeDirectoryPath.toString(), String.valueOf(chunkNumber));
        if (Files.exists(filePath)) {
            throw new FileAlreadyExistsException("%s's already existed".format(filePath.toString()));
        }
        System.out.println(filePath.toString());
        try (OutputStream outputStream = Files.newOutputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            publisher.publishChunkWriteSuccessEvent(new Chunk(id, filePath.toString(), chunkNumber));
            return filePath.toString();
        }
        catch (IOException e) {
            throw new IOException("Error writing file " + filePath);
        }
    }

    public void assembleFileParts(long id) throws IOException {
        String dirPath = String.valueOf(Paths.get(baseDirectory, String.valueOf(id)));
        File dir = new File(dirPath);
        File[] fileParts = dir.listFiles();
        Arrays.sort(fileParts, Comparator.comparingInt(a -> Integer.parseInt(a.getName())));

        try (OutputStream outputStream = new FileOutputStream(String.valueOf(Paths.get(dirPath, "out")))) {
            for (File filePart: fileParts) {
                try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filePart))) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }
        }

        catch (IOException e) {
            System.out.println("Error merging file " + dir.getName());
            throw new IOException("File merge file: " + dir.getName());
        }

        publisher.publishFileMergeSuccessEvent(id);
    }

    public void cleanUpFileParts(long id) throws IOException {
        String dirPath = String.valueOf(Paths.get(baseDirectory, String.valueOf(id)));
        File dir = new File(dirPath);
        File outOldFile = new File(String.valueOf(Paths.get(dirPath, "out")));
        File outNewFile = new File(outOldFile.getParent(), "out.mp4");
        Files.move(outOldFile.toPath(), outNewFile.toPath());
        File[] fileParts = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return !(name.equals("out.mp4"));
            }
        });

        for (File filePart: fileParts) {
            if (!(filePart.delete())) {
                throw new IOException("Error cleaning up file %s.%s".format(String.valueOf(id), filePart.getName()));
            }
        }
    }

}
