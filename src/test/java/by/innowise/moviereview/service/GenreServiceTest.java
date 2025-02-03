package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.EntityDto;
import by.innowise.moviereview.entity.Genre;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.mapper.GenreMapper;
import by.innowise.moviereview.repository.GenreRepository;
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
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private GenreMapper genreMapper;

    @InjectMocks
    private GenreService genreService;

    private Genre genre;
    private EntityDto genreDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        genre = TestBuilder.createGenre("Action");
        genreDto = TestBuilder.createGenreDto(genre.getId(), genre.getName());
    }

    @Test
    void shouldFindAllGenres() {
        // given
        when(genreRepository.findAll()).thenReturn(List.of(genre));
        when(genreMapper.toListDto(List.of(genre))).thenReturn(List.of(genreDto));
        //when
        List<EntityDto> genres = genreService.findAll();
        //then
        assertEquals(1, genres.size());
        assertEquals(genreDto.getName(), genres.get(0).getName());
    }

    @Test
    void shouldFindById() {
        // given
        when(genreRepository.findById(genre.getId())).thenReturn(Optional.of(genre));
        //when
        Genre foundGenre = genreService.findById(genre.getId());
        //then
        assertNotNull(foundGenre);
        assertEquals(genre.getName(), foundGenre.getName());
    }

    @Test
    void shouldThrowExceptionWhenGenreNotFound() {
        // given
        when(genreRepository.findById(999L)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(NotFoundException.class, () -> genreService.findById(999L));
    }

    @Test
    void shouldSaveGenre() {
        // given
        when(genreMapper.toEntity(genreDto)).thenReturn(genre);
        //when
        genreService.save(genreDto);
        //then
        verify(genreRepository, times(1)).save(genre);
    }

    @Test
    void shouldUpdateGenre() {
        // given
        when(genreRepository.findById(genreDto.getId())).thenReturn(Optional.of(genre));
        //when
        genreService.update(genreDto);
        //then
        assertEquals("Action", genre.getName());
        verify(genreRepository, times(1)).save(genre);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentGenre() {
        // given
        when(genreRepository.findById(999L)).thenReturn(Optional.empty());
        //when
        EntityDto dto = TestBuilder.createGenreDto(999L, "Drama");
        //then
        assertThrows(NotFoundException.class, () -> genreService.update(dto));
    }

    @Test
    void shouldDeleteGenre() {
        // given
        when(genreRepository.findById(genre.getId())).thenReturn(Optional.of(genre));
        //when
        genreService.delete(genre.getId());
        //then
        verify(genreRepository, times(1)).delete(genre);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentGenre() {
        // given
        when(genreRepository.findById(999L)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(NotFoundException.class, () -> genreService.delete(999L));
    }

    @Test
    void shouldReturnGenresWithFilters() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Genre> genrePage = new PageImpl<>(List.of(genre));

        when(genreRepository.findAllWithFilters(null, pageable)).thenReturn(genrePage);
        //when
        Map<String, Object> result = genreService.getGenresWithFilters(null, "id", 1, 10);
        //then
        assertEquals(1, ((List<?>) result.get("genres")).size());
        assertEquals(1, result.get("totalPages"));
        assertEquals(1, result.get("currentPage"));
    }
}
