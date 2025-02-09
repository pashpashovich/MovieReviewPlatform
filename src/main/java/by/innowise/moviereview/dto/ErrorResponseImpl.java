package by.innowise.moviereview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class ErrorResponseImpl {
    private String message;
    private HttpStatus status;
    private LocalDateTime timestamp;
}

