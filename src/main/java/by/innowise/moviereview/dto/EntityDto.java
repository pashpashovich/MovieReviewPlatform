package by.innowise.moviereview.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class EntityDto {
    private Long id;
    private String name;
}
