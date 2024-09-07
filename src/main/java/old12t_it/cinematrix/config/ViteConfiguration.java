package old12t_it.cinematrix.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import old12t_it.cinematrix.dtos.configuration.ViteManifest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.ModelAndView;

@Configuration
@RequiredArgsConstructor
public class ViteConfiguration {
    @Value("${vite.entry-path}")
    private String entryPath;
    private final Environment environment;

    @Bean
    public ModelAndView indexModelAndView() {
        ModelAndView modelAndView = new ModelAndView("index");
        Optional<String> currentProfile = Arrays.stream(environment.getActiveProfiles()).findFirst();
        if (currentProfile.isPresent() && currentProfile.get().equals("dev")) {
            modelAndView.setViewName("index-dev");
            modelAndView.addObject("entryPath", entryPath);
            return modelAndView;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = new ClassPathResource(
                "static/.vite/manifest.json").getInputStream();
            JsonNode manifest = objectMapper.readTree(inputStream);
            ViteManifest viteManifest = new ViteManifest(
                manifest.get(entryPath).get("file").asText(),
                manifest.get(entryPath).get("css").get(0).asText()
            );
            modelAndView.addObject("entryScript", viteManifest.getEntryScript());
            modelAndView.addObject("entryStylesheet", viteManifest.getEntryStylesheet());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return modelAndView;
    }
}
