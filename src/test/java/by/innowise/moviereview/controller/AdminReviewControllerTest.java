package by.innowise.moviereview.controller;

import by.innowise.moviereview.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class AdminReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private AdminReviewController adminReviewController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminReviewController).build();
    }

    @Test
    void shouldGetPendingReviews() throws Exception {
        // given
        when(reviewService.findAllPendingReviews()).thenReturn(Collections.emptyList());
        //when
        //then
        mockMvc.perform(get("/admin/reviews"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(view().name("admin/reviews"))
                .andExpect(model().attributeExists("reviews"));

        verify(reviewService, times(1)).findAllPendingReviews();
    }

    @Test
    void shouldUpdateReviewStatus() throws Exception {
        // given

        //when
        //then
        mockMvc.perform(post("/admin/reviews")
                        .param("reviewId", "1")
                        .param("status", "APPROVED"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/reviews"));

        verify(reviewService, times(1)).updateReviewStatus(1L, "APPROVED");
    }
}