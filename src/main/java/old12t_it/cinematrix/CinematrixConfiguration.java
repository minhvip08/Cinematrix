package old12t_it.cinematrix;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

/**
 * CinematrixConfiguration
 */
// @Configuration
public class CinematrixConfiguration {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        String activeProfile = System.getProperty("spring.profiles.active", "dev");
        String propertiesFilename = "application-" + activeProfile + ".properties";

        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource(propertiesFilename));

        String path = System.getProperty("user.dir")  + "/" + "uploaded-content-from-fe";
        File folder = new File(path);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (created) {
                System.out.println("    [-]uploaded-content-from-fe folder created successfully.");
            } else {
                System.err.println("    [-]fail uploaded-content-from-fe folder to create folder.");
            }
        } else {
            System.out.println("    [-]uploaded-content-from-fe folder already exists.");
        }
        return configurer;
    }
}