package old12t_it.cinematrix.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
public class FirebaseConfig {
    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    Resource[] resources;
    InputStream inputStream = null;
    {
        try {
            resources = resolver.getResources("classpath*:secrect/*.json");

            for (Resource r : resources) {
                inputStream = r.getInputStream();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(inputStream);
        FirebaseOptions firebaseOptions = FirebaseOptions
                .builder()
                .setCredentials(googleCredentials)
                .build();
        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "cinematrix-notification");
        return FirebaseMessaging.getInstance(app);
    }
}
