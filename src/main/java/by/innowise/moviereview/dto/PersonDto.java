package by.innowise.moviereview.dto;

import by.innowise.moviereview.util.enums.MovieRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {
    private Long id;
    private String fullName;
    private MovieRole role;
}
