package by.innowise.moviereview.controller;


import by.innowise.moviereview.dto.WatchlistDto;
import by.innowise.moviereview.service.WatchlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class WatchlistControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WatchlistService watchlistService;

    @InjectMocks
    private WatchlistController watchlistController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(watchlistController).build();
    }

    @Test
    void shouldGetWatchlist() throws Exception {
        // given
        Long userId = 1L;
        WatchlistDto watchlistDto = WatchlistDto
                .builder()
                .movieId(1L)
                .build();
        List<WatchlistDto> watchlist = Collections.singletonList(watchlistDto);

        when(watchlistService.getWatchlistByUserId(userId)).thenReturn(watchlist);
        //when
        //then
        mockMvc.perform(get("/user/watchlist/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(view().name("user/watchlist"))
                .andExpect(model().attribute("watchlist", watchlist))
                .andExpect(model().attribute("userId", userId));
    }

    @Test
    void shouldAddToWatchlist() throws Exception {
        // given
        Long movieId = 1L;
        Long userId = 1L;
        String referer = "/user/movies";

        when(watchlistService.isMovieInWatchlist(userId, movieId)).thenReturn(false);
        Mockito.doNothing().when(watchlistService).addToWatchlist(userId, movieId);
        //when
        //then
        mockMvc.perform(post("/user/watchlist/add")
                        .param("movieId", movieId.toString())
                        .param("userId", userId.toString())
                        .header("Referer", referer))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", referer));
    }

    @Test
    void shouldAddToWatchlist_AlreadyInWatchlist() throws Exception {
        // given
        Long movieId = 1L;
        Long userId = 1L;
        String referer = "/user/movies";

        when(watchlistService.isMovieInWatchlist(userId, movieId)).thenReturn(true);
        //when
        //then
        mockMvc.perform(post("/user/watchlist/add")
                        .param("movieId", movieId.toString())
                        .param("userId", userId.toString())
                        .header("Referer", referer))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", referer + "?error=already_in_watchlist"));
    }

    @Test
    void shouldRemoveFromWatchlist() throws Exception {
        // given
        Long movieId = 1L;
        Long userId = 1L;

        Mockito.doNothing().when(watchlistService).removeFromWatchlist(userId, movieId);
        //when
        //then
        mockMvc.perform(delete("/user/watchlist")
                        .param("movieId", movieId.toString())
                        .param("userId", userId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/user/watchlist/" + userId));
    }

}
