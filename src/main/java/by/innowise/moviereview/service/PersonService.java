package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.PersonDto;
import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.mapper.PersonMapper;
import by.innowise.moviereview.repository.PersonRepository;
import by.innowise.moviereview.util.enums.MovieRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public List<Person> getAllPeople(int page, int size, String searchQuery, String roleFilter) {
        Pageable pageable = PageRequest.of(page - 1, size);

        if (roleFilter != null && !roleFilter.isEmpty()) {
            return personRepository.findByRoleAndNameContainingIgnoreCase(
                    MovieRole.valueOf(roleFilter),
                    searchQuery != null ? searchQuery : "",
                    pageable
            ).getContent();
        }

        return personRepository.findByNameContainingIgnoreCase(
                searchQuery != null ? searchQuery : "",
                pageable
        ).getContent();
    }

    public long countPeople(String searchQuery, String roleFilter) {
        return personRepository.countPeople(searchQuery, roleFilter);
    }

    public List<PersonDto> getAllPeopleByRole(MovieRole role) {
        return personMapper.toDto(personRepository.findAllByRole(role));
    }


    public void addPerson(Person person) {
        personRepository.save(person);
    }

    public Person getPersonById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Человек с ID %d не найден", id)));
    }

    public void update(Person person) {
        Long id = person.getId();
        Person person1 = personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Человек с ID %d не найден", id)));
        person1.setFullName(person.getFullName());
        personRepository.save(person1);
    }

    public void deletePersonById(Long id) {
        Person person = getPersonById(id);
        personRepository.delete(person);
    }
}
