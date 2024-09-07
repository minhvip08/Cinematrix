package old12t_it.cinematrix.service;

import com.backblaze.b2.client.B2StorageClient;
import com.backblaze.b2.client.contentSources.B2ContentSource;
import com.backblaze.b2.client.contentSources.B2ContentTypes;
import com.backblaze.b2.client.contentSources.B2FileContentSource;
import com.backblaze.b2.client.exceptions.B2Exception;
import com.backblaze.b2.client.structures.B2UploadFileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class BackblazeService {
    private final B2StorageClient b2StorageClient;
    @Value("${b2.bucket-id}")
    private String BUCKET_ID;

    public void uploadFile(String path, String dir) throws B2Exception {
        B2ContentSource source = B2FileContentSource.builder(new File(path)).build();
        String filename = new File(path).getName();
        if (!dir.isEmpty())
            filename = dir + "/" + filename;
        B2UploadFileRequest uploadFileRequest = B2UploadFileRequest
                .builder(BUCKET_ID, filename, B2ContentTypes.B2_AUTO, source)
                .build();
        b2StorageClient.uploadSmallFile(uploadFileRequest);
    }

    public void uploadDirectory(String path) throws Exception {
        int pathLen = path.length();
        String separate = path.contains("/") ? "/" : "\\";
        String dirName = path.substring(path.lastIndexOf(separate)  + 1, pathLen);
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    uploadFile(file.getAbsolutePath(), dirName);
                }
            }
        }
    }
}
