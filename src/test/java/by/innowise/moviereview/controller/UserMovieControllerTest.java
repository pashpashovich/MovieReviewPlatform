package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.EntityDto;
import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.service.GenreService;
import by.innowise.moviereview.service.MovieService;
import by.innowise.moviereview.service.RecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class UserMovieControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MovieService movieService;

    @Mock
    private GenreService genreService;

    @Mock
    private RecommendationService recommendationService;

    @InjectMocks
    private UserMovieController userMovieController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userMovieController).build();
    }

    @Test
    void shouldGetMovies() throws Exception {
        // given
        Long userId = 1L;
        int page = 1;
        int size = 9;
        String searchQuery = "";
        String genreId = "";
        String language = "";
        String year = "";
        String duration = "";

        List<MovieDto> movies = Arrays.asList(new MovieDto(), new MovieDto());
        List<EntityDto> genres = Arrays.asList(new EntityDto(1L, "first"), new EntityDto(2L, "second"));
        List<MovieDto> recommendations = Arrays.asList(new MovieDto(), new MovieDto());

        when(movieService.filterMoviesWithPagination(any(), any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(movies);
        when(movieService.getTotalMoviesCount()).thenReturn((long) movies.size());
        when(genreService.findAll()).thenReturn(genres);
        when(recommendationService.getRecommendationsForUser(userId)).thenReturn(recommendations);

        //when
        //then
        mockMvc.perform(get("/user/movies/{id}", userId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("searchQuery", searchQuery)
                        .param("genre", genreId)
                        .param("language", language)
                        .param("year", year)
                        .param("duration", duration))
                .andExpect(status().isOk())
                .andExpect(view().name("user/movieCards"))
                .andExpect(model().attribute("movies", movies))
                .andExpect(model().attribute("genres", genres))
                .andExpect(model().attribute("recommendations", recommendations))
                .andExpect(model().attribute("currentPage", page))
                .andExpect(model().attribute("totalPages", 1));
    }

    @Test
    void shouldGetMoviesWithPagination() throws Exception {
        // given
        Long userId = 1L;
        int page = 2;
        int size = 9;
        String searchQuery = "action";
        String genreId = "1";
        String language = "English";
        String year = "2021";
        String duration = "120";

        List<MovieDto> movies = Arrays.asList(new MovieDto(), new MovieDto(), new MovieDto());
        List<EntityDto> genres = Arrays.asList(new EntityDto(1L, "first"), new EntityDto(2L, "second"));
        List<MovieDto> recommendations = Arrays.asList(new MovieDto(), new MovieDto());

        when(movieService.filterMoviesWithPagination(searchQuery, genreId, language, year, duration, page, size)).thenReturn(movies);
        when(movieService.getTotalMoviesCount()).thenReturn((long) movies.size());
        when(genreService.findAll()).thenReturn(genres);
        when(recommendationService.getRecommendationsForUser(userId)).thenReturn(recommendations);

        //when
        //then
        mockMvc.perform(get("/user/movies/{id}", userId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("searchQuery", searchQuery)
                        .param("genre", genreId)
                        .param("language", language)
                        .param("year", year)
                        .param("duration", duration))
                .andExpect(status().isOk())
                .andExpect(view().name("user/movieCards"))
                .andExpect(model().attribute("movies", movies))
                .andExpect(model().attribute("genres", genres))
                .andExpect(model().attribute("recommendations", recommendations))
                .andExpect(model().attribute("currentPage", page))
                .andExpect(model().attribute("totalPages", 1));
    }
}
