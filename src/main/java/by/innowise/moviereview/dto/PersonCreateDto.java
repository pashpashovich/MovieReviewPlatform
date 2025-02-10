package by.innowise.moviereview.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class PersonCreateDto {
    @NotNull
    @NotEmpty
    @Size(min = 5, max = 50)
    private String fullName;
    @NotNull
    @NotEmpty
    private String role;
}
