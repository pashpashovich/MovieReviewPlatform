package by.innowise.moviereview.dto;

import by.innowise.moviereview.enums.MovieRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class PersonDto {
    private Long id;
    @NotNull
    @Size(min = 5, max = 255)
    private String fullName;
    @NotNull
    private MovieRole role;
}
