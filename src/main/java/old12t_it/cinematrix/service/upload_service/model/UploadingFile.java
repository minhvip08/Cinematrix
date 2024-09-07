package old12t_it.cinematrix.service.upload_service.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UploadingFile {
    @Getter
    private long id;

    @Getter
    private String path;

    private List<Chunk> chunks;

    @Getter
    private int totalChunks;

    @Getter
    private int noChunksUploaded;

    public UploadingFile(long id, String path, int totalChunks) {
        this.id = id;
        this.path = path;
        this.noChunksUploaded = 0;
        this.totalChunks = totalChunks;
        this.chunks = new ArrayList<>(totalChunks);
        for (int i = 0; i < totalChunks; ++i) {
            chunks.add(null);
        }
    }

    public Chunk getChunk(int index) throws IndexOutOfBoundsException {
        return chunks.get(index);
    }

    private void setChunk(int index, Chunk chunk) throws IndexOutOfBoundsException {
        chunks.set(index, chunk);
    }

    public void addChunk(Chunk chunk) throws IndexOutOfBoundsException {
        chunks.set(chunk.getChunkNumber(), chunk);
        ++noChunksUploaded;
    }

    public boolean isAllFileUploaded() {
        return Objects.equals(noChunksUploaded, totalChunks);
    }
}
