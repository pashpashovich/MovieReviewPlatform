package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.service.GenreService;
import by.innowise.moviereview.service.MovieService;
import by.innowise.moviereview.service.PersonService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

class MovieControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MovieService movieService;

    @Mock
    private GenreService genreService;

    @Mock
    private PersonService personService;

    @Mock
    private StandardServletMultipartResolver multipartResolver;

    @InjectMocks
    private MovieController movieController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldGetMovies() throws Exception {
        // given
        when(movieService.getMoviesWithPagination(1, 5)).thenReturn(Collections.emptyList());
        when(movieService.getTotalMoviesCount()).thenReturn(0L);
        when(genreService.findAll()).thenReturn(Collections.emptyList());
        when(personService.getAllPeopleByRole(any())).thenReturn(Collections.emptyList());
        //when
        //then
        mockMvc.perform(get("/admin/movies"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin/movies"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("movies", "currentPage", "totalPages", "genres", "actors", "directors", "producers"));

        verify(movieService, times(1)).getMoviesWithPagination(1, 5);
        verify(movieService, times(1)).getTotalMoviesCount();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldCreateMovie() throws Exception {
        // given
        MockMultipartFile posterFile = new MockMultipartFile("posterFile", "poster.jpg", "image/jpeg", new byte[]{1, 2, 3});
        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
        request.addFile(posterFile);

        when(multipartResolver.resolveMultipart(any(HttpServletRequest.class))).thenReturn(request);
        //when
        //then
        mockMvc.perform(multipart("/admin/movies")
                        .file(posterFile)
                        .param("title", "Test Movie")
                        .param("description", "Test Description"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/movies"));

        verify(movieService, times(1)).createMovie(any(MovieDto.class));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldUpdateMovie() throws Exception {
        // given
        MockMultipartFile posterFile = new MockMultipartFile("posterFile", "poster.jpg", "image/jpeg", new byte[]{1, 2, 3});
        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
        request.addFile(posterFile);

        when(multipartResolver.resolveMultipart(any(HttpServletRequest.class))).thenReturn(request);
        //when
        //then
        mockMvc.perform(multipart("/admin/movies/1")
                        .file(posterFile)
                        .param("title", "Updated Movie")
                        .param("description", "Updated Description")
                        .with(request2 -> {
                            request2.setMethod("PUT");
                            return request2;
                        }))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/movies"));

        verify(movieService, times(1)).updateMovie(eq(1L), any(MovieDto.class));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldDeleteMovie() throws Exception {
        // given
        //when
        //then
        mockMvc.perform(delete("/admin/movies/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/movies"));

        verify(movieService, times(1)).deleteMovie(1L);
    }
}