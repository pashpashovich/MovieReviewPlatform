package by.innowise.moviereview.util;

import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@UtilityClass
public class PosterLoading {
    private static final String DEFAULT_POSTER_PATH = "static/images/default-poster.png";

    public String processPosterFile(MultipartFile posterFile) throws IOException {
        if (posterFile != null && !posterFile.isEmpty()) {
            byte[] posterBytes = posterFile.getBytes();
            return Base64.getEncoder().encodeToString(posterBytes);
        } else {
            return getDefaultPosterBase64();
        }
    }

    private String getDefaultPosterBase64() throws IOException {
        ClassPathResource defaultPosterResource = new ClassPathResource(DEFAULT_POSTER_PATH);
        try (InputStream inputStream = defaultPosterResource.getInputStream()) {
            byte[] defaultPosterBytes = inputStream.readAllBytes();
            return Base64.getEncoder().encodeToString(defaultPosterBytes);
        }
    }
}

