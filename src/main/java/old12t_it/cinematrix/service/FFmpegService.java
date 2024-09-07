package old12t_it.cinematrix.service;

import com.github.kokorin.jaffree.ffmpeg.*;
import old12t_it.cinematrix.dtos.FFmpegTask;
import old12t_it.cinematrix.service.upload_service.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;

@Service
public class FFmpegService {
    @Autowired
    Publisher publisher;

    public FFmpegTask generateStream(String inputPath) {
        String baseDir = System.getProperty("user.dir");
        String outputDir = (inputPath.contains("\\")) ? inputPath.substring(0, inputPath.lastIndexOf('\\')) :
                inputPath.substring(0, inputPath.lastIndexOf('/'));
        System.out.println("    [-]output path to convert:" + outputDir);
        return new FFmpegTask(
                outputDir,
                FFmpeg.atPath()
                        .addInput(UrlInput.fromUrl(inputPath))
                        .addArguments("-vf", "scale='min(1920, iw)':-1")
                        .addArguments("-c:v", "libsvtav1")
                        .addArguments("-preset", "11")
                        .addArguments("-crf", "30")
                        .addArguments("-maxrate", "2M")
                        .addArguments("-svtav1-params", "tune=0")
                        .addArguments("-c:a", "libopus")
                        .addArguments("-b:a", "128k")
                        .addArgument("-sn")
                        .addArguments("-ac", "2")
                        .addArguments("-dash_segment_type", "mp4")
                        .addArguments("-f", "dash")
                        .addOutput(UrlOutput.toPath(Path.of(outputDir, "stream.mpd")))
                        .executeAsync().toCompletableFuture().thenApply(rs -> {
                            System.out.println("Convert done");
                            File dir = new File(baseDir);
                            File[] files = dir.listFiles(file -> file.getName().contains("chunk-stream") || file.getName().contains("init-stream"));
                            if (files != null) {
                                for (File file : files) {
                                    String currName = file.getName();
                                    File outDir = new File(Path.of(outputDir, currName).toString());
                                    if (!file.renameTo(outDir)) {
                                        System.out.println("Fail to move chunk stream:" + currName);
                                    }
                                    ;
                                }
                            }
                            File file = new File(inputPath);
                            if (file.exists()) {
                                if (!file.delete())
                                    throw new RuntimeException("Fail to delete file out.file in directory");
                            }

                            publisher.publishMediaConvertSuccessEvent(outputDir);
                            return null;
                        })
        );
    }
}
