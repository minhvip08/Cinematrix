package old12t_it.cinematrix.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "media")
public class Media {
    @Transient
    public static final String MEDIA_SEQUENCE = "media_sequence";

    @Id
    private long id;
    private boolean isUploaded;
    private String url;

    public Media() {
        id = 0;
        isUploaded = false;
        url = null;
    }

    public Media(long id) {
        this.id = id;
        isUploaded = false;
        url = null;
    }

    public Media(long id, boolean isUploaded, String url) {
        this.id = id;
        this.isUploaded = isUploaded;
        this.url = url;
    }
}
