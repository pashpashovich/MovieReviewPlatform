package by.innowise.moviereview.controller;

import by.innowise.moviereview.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private MockHttpSession session;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
        session = new MockHttpSession();
    }

    @Test
    void shouldAddReviewSuccess() throws Exception {
        // given
        Long userId = 1L;
        Long movieId = 100L;
        String content = "Отличный фильм!";
        int rating = 5;
        Mockito.doNothing().when(reviewService).addReview(any(Long.class), any(Long.class), any(String.class), any(Integer.class));
        //when
        //then
        mockMvc.perform(post("/user/movies/review")
                        .param("userId", String.valueOf(userId))
                        .param("movieId", String.valueOf(movieId))
                        .param("content", content)
                        .param("rating", String.valueOf(rating))
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/movies/movie/" + movieId + "?userId=" + userId))
                .andReturn();

        assert session.getAttribute("successMessage").equals("Ваша рецензия добавлена в список обработки у администратора, ожидайте.");
    }
}
