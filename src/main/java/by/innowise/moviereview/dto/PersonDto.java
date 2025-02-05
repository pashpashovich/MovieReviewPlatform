package by.innowise.moviereview.dto;

import by.innowise.moviereview.util.enums.MovieRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {
    private Long id;
    @NotNull
    @Size(min = 5, max = 255)
    private String fullName;
    @NotNull
    private MovieRole role;
}
