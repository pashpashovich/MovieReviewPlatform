package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.PersonDto;
import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.mapper.PersonMapper;
import by.innowise.moviereview.repository.PersonRepository;
import by.innowise.moviereview.util.enums.MovieRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public List<Person> getAllPeopleWithPagination(int page, int size) {
        return personRepository.findAll(PageRequest.of(page - 1, size)).getContent();
    }

    public List<Person> getPeopleWithFiltersAndPagination(int page, int size, String searchQuery, String roleFilter) {
        MovieRole role = roleFilter != null && !roleFilter.isEmpty() ? MovieRole.valueOf(roleFilter) : null;
        return personRepository.findWithFilters(searchQuery, role, PageRequest.of(page - 1, size)).getContent();
    }

    public long countPeopleWithFilters(String searchQuery, String roleFilter) {
        MovieRole role = roleFilter != null && !roleFilter.isEmpty() ? MovieRole.valueOf(roleFilter) : null;
        return personRepository.countWithFilters(searchQuery, role);
    }

    public long countPeople() {
        return personRepository.findAll().size();
    }

    public List<PersonDto> getAllPeopleByRole(MovieRole role) {
        return personMapper.toListDto(personRepository.findAllByRole(role));
    }


    public void addPerson(Person person) {
        personRepository.save(person);
    }

    public PersonDto getPersonById(Long id) {
        return personMapper.toDto(personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Человек с ID %d не найден", id))));
    }

    public void update(PersonDto person) {
        Long id = person.getId();
        Person person1 = personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Человек с ID %d не найден", id)));
        person1.setFullName(person.getFullName());
        person1.setRole(person.getRole());
        personRepository.save(person1);
    }

    public void deletePersonById(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Человек с ID %d не найден", id)));
        personRepository.delete(person);
    }
}
