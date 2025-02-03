package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.exception.EntityNotFoundException;
import by.innowise.moviereview.mapper.MovieMapper;
import by.innowise.moviereview.repository.GenreRepository;
import by.innowise.moviereview.repository.MovieRepository;
import by.innowise.moviereview.repository.PersonRepository;
import by.innowise.moviereview.util.enums.MovieRole;
import by.innowise.moviereview.utils.TestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private MovieService movieService;

    private Movie movie;
    private MovieDto movieDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        movie = TestBuilder.createMovie("Inception", Set.of(TestBuilder.createGenre("Sci-fi")));
        movieDto = new MovieDto();
        movieDto.setId(1L);
        movieDto.setTitle("Inception");
        movieDto.setGenres(Set.of("Sci-fi"));
        movieDto.setActors(Set.of("Leonardo DiCaprio"));
        movieDto.setDirectors(Set.of("Christopher Nolan"));
        movieDto.setProducers(Set.of("Emma Thomas"));
    }

    @Test
    void shouldGetMoviesWithPagination() {
        // given
        Page<Movie> moviePage = new PageImpl<>(List.of(movie));
        Pageable pageable = PageRequest.of(0, 10);

        when(movieRepository.findAll(pageable)).thenReturn(moviePage);
        when(movieMapper.toDto(movie)).thenReturn(movieDto);
        //when
        List<MovieDto> movies = movieService.getMoviesWithPagination(1, 10);
        //then
        assertEquals(1, movies.size());
        assertEquals(movieDto.getTitle(), movies.get(0).getTitle());
    }

    @Test
    void shouldGetMovieById() throws EntityNotFoundException {
        // given
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieMapper.toDto(movie)).thenReturn(movieDto);
        //when
        MovieDto foundMovie = movieService.getMovieById(1L);
        //then
        assertNotNull(foundMovie);
        assertEquals(movieDto.getTitle(), foundMovie.getTitle());
    }

    @Test
    void shouldThrowExceptionWhenMovieNotFound() {
        // given
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(EntityNotFoundException.class, () -> movieService.getMovieById(999L));
    }

    @Test
    void shouldCreateMovie() {
        // given
        when(movieMapper.toEntityFromDto(movieDto)).thenReturn(movie);
        when(genreRepository.findAllByName(movieDto.getGenres())).thenReturn(Set.of());
        when(personRepository.findAllByNameAndRole(movieDto.getActors(), MovieRole.ACTOR)).thenReturn(List.of());
        when(personRepository.findAllByNameAndRole(movieDto.getDirectors(), MovieRole.DIRECTOR)).thenReturn(List.of());
        when(personRepository.findAllByNameAndRole(movieDto.getProducers(), MovieRole.PRODUCER)).thenReturn(List.of());
        //when
        movieService.createMovie(movieDto);
        //then
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    void shouldUpdateMovie() throws EntityNotFoundException {
        // given
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(genreRepository.findAllByName(movieDto.getGenres())).thenReturn(Set.of());
        when(personRepository.findAllByNameAndRole(movieDto.getActors(), MovieRole.ACTOR)).thenReturn(List.of());
        when(personRepository.findAllByNameAndRole(movieDto.getDirectors(), MovieRole.DIRECTOR)).thenReturn(List.of());
        when(personRepository.findAllByNameAndRole(movieDto.getProducers(), MovieRole.PRODUCER)).thenReturn(List.of());
        //when
        movieService.updateMovie(1L, movieDto);
        //then
        assertEquals("Inception", movie.getTitle());
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentMovie() {
        // given
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(EntityNotFoundException.class, () -> movieService.updateMovie(999L, movieDto));
    }

    @Test
    void shouldDeleteMovie() throws EntityNotFoundException {
        // given
        when(movieRepository.existsById(1L)).thenReturn(true);
        //when
        movieService.deleteMovie(1L);
        //then
        verify(movieRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentMovie() {
        // given
        when(movieRepository.existsById(999L)).thenReturn(false);
        //when
        //then
        assertThrows(EntityNotFoundException.class, () -> movieService.deleteMovie(999L));
    }

    @Test
    void shouldReturnTotalMoviesCount() {
        // given
        when(movieRepository.count()).thenReturn(100L);
        //when
        long count = movieService.getTotalMoviesCount();
        //then
        assertEquals(100L, count);
    }
}
