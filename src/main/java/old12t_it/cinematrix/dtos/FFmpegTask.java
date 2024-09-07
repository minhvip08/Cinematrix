package old12t_it.cinematrix.dtos;

import com.github.kokorin.jaffree.ffmpeg.FFmpegResult;

import java.util.concurrent.CompletableFuture;

public record FFmpegTask(String outputDir, CompletableFuture<FFmpegResult> future) {
}