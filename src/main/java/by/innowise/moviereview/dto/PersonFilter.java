package by.innowise.moviereview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonFilter {
    private int page = 0;
    private int size = 10;
    private String search;
    private String role;
}
