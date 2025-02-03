package by.innowise.moviereview.repository;


import by.innowise.moviereview.config.TestConfig;
import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.util.enums.MovieRole;
import by.innowise.moviereview.utils.TestBuilder;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
@Transactional
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    private Person director;
    private Person actor;

    @BeforeEach
    void setUp() {
        director = TestBuilder.createPerson("Director 1", MovieRole.DIRECTOR);
        actor = TestBuilder.createPerson("Actor 1", MovieRole.ACTOR);
        personRepository.save(director);
        personRepository.save(actor);
    }

    @Test
    void shouldFindWithFilters() {
        // given
        PageRequest pageable = PageRequest.of(0, 10);

        //when
        var pageWithRole = personRepository.findWithFilters(null, MovieRole.ACTOR, pageable);
        var page = personRepository.findWithFilters("Director", MovieRole.DIRECTOR, pageable);
        //then
        assertNotNull(page);
        assertEquals(1, page.getTotalElements());
        assertEquals("Director 1", page.getContent().get(0).getFullName());

        assertNotNull(pageWithRole);
        assertEquals(1, pageWithRole.getTotalElements());
        assertEquals("Actor 1", pageWithRole.getContent().get(0).getFullName());
    }

    @Test
    void shouldCountWithFilters() {
        // given
        //when
        long countByRole = personRepository.countWithFilters(null, MovieRole.DIRECTOR);
        long countBySearch = personRepository.countWithFilters("Actor", null);
        //then
        assertEquals(1, countByRole);
        assertEquals(1, countBySearch);
    }

    @Test
    void shouldFindAllByNameAndRole() {
        // given
        Set<String> names = Set.of("Director 1", "Actor 1");
        //when
        List<Person> people = personRepository.findAllByNameAndRole(names, MovieRole.DIRECTOR);
        //then
        assertNotNull(people);
        assertEquals(1, people.size());
        assertEquals("Director 1", people.get(0).getFullName());
    }
}
