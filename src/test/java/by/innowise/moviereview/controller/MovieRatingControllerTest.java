package by.innowise.moviereview.controller;


import by.innowise.moviereview.service.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

class MovieRatingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RatingService ratingService;

    @InjectMocks
    private MovieRatingController movieRatingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(movieRatingController).build();
    }

    @Test
    void shouldRateMovie() throws Exception {
        // given
        //when
        //then
        mockMvc.perform(post("/user/movies/rate")
                        .param("userId", "1")
                        .param("movieId", "10")
                        .param("rating", "5")
                        .header("Referer", "/user/movies/1"))
                .andExpect(redirectedUrl("/user/movies/1"));

        verify(ratingService).saveOrUpdateRating(1L, 10L, 5);
    }
}