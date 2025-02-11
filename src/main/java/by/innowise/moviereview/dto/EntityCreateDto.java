package by.innowise.moviereview.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class EntityCreateDto {
    @NotNull
    @NotEmpty
    private String name;
}
