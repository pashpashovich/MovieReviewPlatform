package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.service.ReviewService;
import by.innowise.moviereview.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class UserProfileControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private UserProfileController userProfileController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userProfileController).build();
    }

    @Test
    void shouldViewProfile_UserExists() throws Exception {
        Long userId = 1L;
        UserDto userDto = UserDto.builder()
                .id(userId)
                .username("testuser")
                .build();
        when(userService.findById(userId)).thenReturn(userDto);
        when(reviewService.findRecentReviewsByUserId(userId)).thenReturn(null);

        mockMvc.perform(get("/user/profile/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(view().name("user/profile"))
                .andExpect(model().attribute("user", userDto))
                .andExpect(model().attribute("recentReviews", nullValue()));
    }

    @Test
    void shouldViewProfile_UserNotFound() throws Exception {
        Long userId = 1L;

        when(userService.findById(userId)).thenReturn(null);

        mockMvc.perform(get("/user/profile/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("error", "Пользователь не найден"));
    }
}