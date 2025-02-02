package by.innowise.moviereview.repository;

import by.innowise.moviereview.config.TestConfig;
import by.innowise.moviereview.entity.Genre;
import by.innowise.moviereview.utils.TestBuilder;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
@Transactional
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void shouldFindAllByName() {
        // given
        Genre genre1 = TestBuilder.createGenre("Драмы");
        Genre genre2 = TestBuilder.createGenre("Комедия");
        genreRepository.save(genre1);
        genreRepository.save(genre2);
        Set<String> genreNames = new HashSet<>(Arrays.asList("Драмы", "Комедия"));
        //when
        Set<Genre> genres = genreRepository.findAllByName(genreNames);
        //then
        assertNotNull(genres);
        assertEquals(2, genres.size());
    }

    @Test
    void shouldFindAllWithFilters() {
        // given
        Genre genre1 = TestBuilder.createGenre("Драмы");
        Genre genre2 = TestBuilder.createGenre("Комедия");
        genreRepository.save(genre1);
        genreRepository.save(genre2);

        PageRequest pageRequest = PageRequest.of(0, 10);
        //when
        Page<Genre> genresPage = genreRepository.findAllWithFilters("Драмы", pageRequest);
        //then
        assertNotNull(genresPage);
        assertEquals(1, genresPage.getTotalElements());
        assertEquals("Драмы", genresPage.getContent().get(0).getName());
    }
}