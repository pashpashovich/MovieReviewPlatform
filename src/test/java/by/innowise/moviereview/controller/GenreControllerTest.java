package by.innowise.moviereview.controller;


import by.innowise.moviereview.dto.EntityDto;
import by.innowise.moviereview.service.GenreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class GenreControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GenreService genreService;

    @InjectMocks
    private GenreController genreController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(genreController).build();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldGetGenres() throws Exception {
        // given
        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put("genres", Collections.emptyList());
        mockResult.put("totalPages", 1);
        mockResult.put("currentPage", 1);
        when(genreService.getGenresWithFilters(null, "id", 1, 10)).thenReturn(mockResult);
        //when
        //then
        mockMvc.perform(get("/admin/genres"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("admin/entity-management"))
                .andExpect(model().attributeExists("entities", "totalPages", "currentPage"));

        verify(genreService, times(1)).getGenresWithFilters(null, "id", 1, 10);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldAddGenre() throws Exception {
        // given
        //when
        //then
        mockMvc.perform(post("/admin/genres")
                        .param("name", "Comedy"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/genres"));

        verify(genreService, times(1)).save(any(EntityDto.class));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldUpdateGenre() throws Exception {
        // given
        //when
        //then
        mockMvc.perform(put("/admin/genres")
                        .param("id", "1")
                        .param("name", "Drama"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/genres"));

        verify(genreService, times(1)).update(any(EntityDto.class));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldDeleteGenre() throws Exception {
        // given
        //when
        //then
        mockMvc.perform(delete("/admin/genres")
                        .param("id", "1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/genres"));

        verify(genreService, times(1)).delete(1L);
    }
}