package by.innowise.moviereview.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreateDto {
    private String username;
    private String email;
    private String password;
}
