package by.innowise.moviereview.repository;


import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.enums.MovieRole;
import by.innowise.moviereview.utils.TestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@DataJpaTest
@ActiveProfiles(profiles = "test")
class PersonRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("test")
                    .withUsername("news_user")
                    .withPassword("news_pass");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

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
        var pageWithRole = personRepository.findWithFilters("Actor", MovieRole.ACTOR, pageable);
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
        long countByRole = personRepository.countWithFilters("Director", MovieRole.DIRECTOR);
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
