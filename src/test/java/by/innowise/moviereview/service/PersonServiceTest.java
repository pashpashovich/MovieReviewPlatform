package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.PersonDto;
import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.mapper.PersonMapper;
import by.innowise.moviereview.repository.PersonRepository;
import by.innowise.moviereview.util.enums.MovieRole;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonService personService;

    private Person person;
    private PersonDto personDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        person = new Person();
        person.setId(1L);
        person.setFullName("Leonardo DiCaprio");
        person.setRole(MovieRole.ACTOR);

        personDto = new PersonDto();
        personDto.setId(1L);
        personDto.setFullName("Leonardo DiCaprio");
        personDto.setRole(MovieRole.ACTOR);
    }

    @Test
    void shouldGetAllPeopleWithPagination() {
        // given
        Page<Person> personPage = new PageImpl<>(List.of(person));
        Pageable pageable = PageRequest.of(0, 10);

        when(personRepository.findAll(pageable)).thenReturn(personPage);
        //when
        List<Person> people = personService.getAllPeopleWithPagination(1, 10);
        //then
        assertEquals(1, people.size());
        assertEquals("Leonardo DiCaprio", people.get(0).getFullName());
    }

    @Test
    void shouldGetPeopleWithFiltersAndPagination() {
        // given
        Page<Person> personPage = new PageImpl<>(List.of(person));
        Pageable pageable = PageRequest.of(0, 10);

        when(personRepository.findWithFilters("Leonardo", MovieRole.ACTOR, pageable)).thenReturn(personPage);
        //when
        List<Person> filteredPeople = personService.getPeopleWithFiltersAndPagination(1, 10, "Leonardo", "ACTOR");
        //then
        assertEquals(1, filteredPeople.size());
        assertEquals("Leonardo DiCaprio", filteredPeople.get(0).getFullName());
    }

    @Test
    void shouldCountPeopleWithFilters() {
        // given
        when(personRepository.countWithFilters("Leonardo", MovieRole.ACTOR)).thenReturn(1L);
        //when
        long count = personService.countPeopleWithFilters("Leonardo", "ACTOR");
        //then
        assertEquals(1L, count);
    }

    @Test
    void shouldCountAllPeople() {
        // given
        when(personRepository.findAll()).thenReturn(List.of(person));
        //when
        long count = personService.countPeople();
        //then
        assertEquals(1L, count);
    }

    @Test
    void shouldGetAllPeopleByRole() {
        // given
        when(personRepository.findAllByRole(MovieRole.ACTOR)).thenReturn(List.of(person));
        when(personMapper.toListDto(List.of(person))).thenReturn(List.of(personDto));
        //when
        List<PersonDto> actors = personService.getAllPeopleByRole(MovieRole.ACTOR);
        //then
        assertEquals(1, actors.size());
        assertEquals("Leonardo DiCaprio", actors.get(0).getFullName());
    }

    @Test
    void shouldAddPerson() {
        // given
        //when
        personService.addPerson(person);
        //then
        verify(personRepository, times(1)).save(person);
    }

    @Test
    void shouldGetPersonById() {
        // given
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personMapper.toDto(person)).thenReturn(personDto);
        //when
        PersonDto foundPerson = personService.getPersonById(1L);
        //then
        assertNotNull(foundPerson);
        assertEquals("Leonardo DiCaprio", foundPerson.getFullName());
    }

    @Test
    void shouldThrowExceptionWhenPersonNotFound() {
        // given
        when(personRepository.findById(999L)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(NotFoundException.class, () -> personService.getPersonById(999L));
    }

    @Test
    void shouldUpdatePerson() {
        // given
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        //when
        personService.update(personDto);
        //then
        assertEquals("Leonardo DiCaprio", person.getFullName());
        verify(personRepository, times(1)).save(person);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentPerson() {
        // given
        when(personRepository.findById(999L)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(NotFoundException.class, () -> personService.update(personDto));
    }

    @Test
    void shouldDeletePersonById() {
        // given
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        //when
        personService.deletePersonById(1L);
        //then
        verify(personRepository, times(1)).delete(person);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentPerson() {
        // given
        when(personRepository.findById(999L)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(NotFoundException.class, () -> personService.deletePersonById(999L));
    }
}
