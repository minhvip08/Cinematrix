package old12t_it.cinematrix.service.tmdb_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class TmdbApiHandle {
    @Value("${tmdb.api.host}")
    private String host;

    @Value("${tmdb.api.version}")
    private String version;

    @Value("${tmdb.api.access-token}")
    private String accessToken;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private OkHttpClient okHttpClient;

    private Request buildRequest(String path, Map<String, String> queryParams) {
        HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
                .scheme("https")
                .host(host)
                .addPathSegment(version)
                .addPathSegments(path);
        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        return new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
    }

    private Map<String, Object> makeApiRequest(Request request) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            return objectMapper.readValue(response.body().string(), new TypeReference<Map<String, Object>>() {});
        }
        catch (IOException e) {
            return null;
        }
    }

    public Map<String, Object> queryTmdb(String path, Map<String, String> queryParams) {
        return makeApiRequest(buildRequest(path, queryParams));
    }
}