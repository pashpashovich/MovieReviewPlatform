package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.PersonCreateDto;
import by.innowise.moviereview.dto.PersonDto;
import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.mapper.PersonMapper;
import by.innowise.moviereview.repository.PersonRepository;
import by.innowise.moviereview.util.enums.MovieRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

//    public List<Person> getAllPeopleWithPagination(int page, int size) {
//        return personRepository.findAll(PageRequest.of(page - 1, size)).getContent();
//    }

    public Page<PersonDto> getPeopleWithFiltersAndPagination(int page, int size, String searchQuery, String roleFilter) {
        MovieRole role = roleFilter != null && !roleFilter.isEmpty() ? MovieRole.valueOf(roleFilter) : null;
        Page<Person> personPage = personRepository.findWithFilters(searchQuery, role, PageRequest.of(page - 1, size));

        return personPage.map(personMapper::toDto);
    }

//    public long countPeopleWithFilters(String searchQuery, String roleFilter) {
//        MovieRole role = roleFilter != null && !roleFilter.isEmpty() ? MovieRole.valueOf(roleFilter) : null;
//        return personRepository.countWithFilters(searchQuery, role);
//    }

//    public long countPeople() {
//        return personRepository.findAll().size();
//    }
//
//    public List<PersonDto> getAllPeopleByRole(MovieRole role) {
//        return personMapper.toListDto(personRepository.findAllByRole(role));
//    }


    public PersonDto addPerson(PersonCreateDto dto) {
        Person entity = personMapper.toEntity(dto);
        Person person = personRepository.save(entity);
        log.info(String.format("Star %s added", person.getFullName()));
        return personMapper.toDto(person);
    }

    public PersonDto getPersonById(Long id) {
        return personMapper.toDto(personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Человек с ID %d не найден", id))));
    }

    public PersonDto update(Long id, PersonCreateDto dto) {
        Person entity = personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Человек с ID %d не найден", id)));
        entity.setFullName(dto.getFullName());
        entity.setRole(MovieRole.valueOf(dto.getRole()));
        PersonDto personDto = personMapper.toDto(personRepository.save(entity));
        log.info("Star with ID {} has been changed", id);
        return personDto;
    }

    public void deletePersonById(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Человек с ID %d не найден", id)));
        personRepository.delete(person);
        log.info(String.format("Star with ID %s has been deleted", id));
    }
}
