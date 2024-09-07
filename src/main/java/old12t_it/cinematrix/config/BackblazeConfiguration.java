package old12t_it.cinematrix.config;

import com.backblaze.b2.client.B2StorageClient;
import com.backblaze.b2.client.B2StorageClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BackblazeConfiguration {
    @Value("${b2.application-key-id}")
    private String APPLICATION_KEY_ID;
    @Value("${b2.application-key}")
    private String APPLICATION_KEY;

    @Bean
    public B2StorageClient b2StorageClient() {
        return B2StorageClientFactory.createDefaultFactory().create(APPLICATION_KEY_ID, APPLICATION_KEY, "b2-sdk-java/6.1.1+java/21.0.2");
    }
}
