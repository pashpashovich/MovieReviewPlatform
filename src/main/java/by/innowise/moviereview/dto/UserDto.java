package by.innowise.moviereview.dto;

import by.innowise.moviereview.util.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private Boolean isBlocked;
    private LocalDateTime createdAt;
}
