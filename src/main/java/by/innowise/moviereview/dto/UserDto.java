package by.innowise.moviereview.dto;

import by.innowise.moviereview.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private Boolean isBlocked;
    private LocalDateTime createdAt;
}
