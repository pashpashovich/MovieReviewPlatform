package by.innowise.moviereview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class EntityDto {
    private Long id;
    private String name;
}
