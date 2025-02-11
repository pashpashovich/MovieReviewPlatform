package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.PersonCreateDto;
import by.innowise.moviereview.dto.PersonDto;
import by.innowise.moviereview.dto.PersonFilter;
import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.enums.MovieRole;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.mapper.PersonMapper;
import by.innowise.moviereview.repository.PersonRepository;
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


    public Page<PersonDto> getPeopleWithFiltersAndPagination(PersonFilter personFilter) {
        MovieRole role;
        if (personFilter.getRole() != null && !personFilter.getRole().isEmpty()) {
            try {
                role = MovieRole.valueOf(personFilter.getRole());
            } catch (IllegalArgumentException e) {
                throw new NotFoundException(String.format("Роли %s не найдено", personFilter.getRole()));
            }
        } else throw new NotFoundException(String.format("Роли %s не найдено", personFilter.getRole()));
        Page<Person> personPage = personRepository.findWithFilters(personFilter.getSearch(), role, PageRequest.of(personFilter.getPage() - 1, personFilter.getSize()));
        return personPage.map(personMapper::toDto);
    }

    public PersonDto addPerson(PersonCreateDto dto) {
        chechRole(dto);
        Person entity = personMapper.toEntity(dto);
        Person person = personRepository.save(entity);
        log.info("Star {} added", person.getFullName());
        return personMapper.toDto(person);
    }

    public PersonDto update(Long id, PersonCreateDto dto) {
        chechRole(dto);
        Person entity = personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Человек с ID %d не найден", id)));
        entity
                .setFullName(dto.getFullName())
                .setRole(MovieRole.valueOf(dto.getRole()));
        Person person = personRepository.save(entity);
        PersonDto personDto = personMapper.toDto(person);
        log.info("Star with ID {} has been changed", id);
        return personDto;
    }

    public void deletePersonById(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Человек с ID %d не найден", id)));
        personRepository.delete(person);
        log.info("Star with ID {} has been deleted", id);
    }

    private void chechRole(PersonCreateDto dto) {
        if (!dto.getRole().isEmpty()) {
            try {
                MovieRole.valueOf(dto.getRole());
            } catch (IllegalArgumentException e) {
                throw new NotFoundException(String.format("Роли %s не найдено", dto.getRole()));
            }
        } else throw new NotFoundException(String.format("Роли %s не найдено", dto.getRole()));

    }
}
