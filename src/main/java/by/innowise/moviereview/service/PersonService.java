package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.PersonDto;
import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.mapper.PersonMapper;
import by.innowise.moviereview.mapper.PersonMapperImpl;
import by.innowise.moviereview.repository.PersonRepositoryImpl;
import by.innowise.moviereview.util.enums.MovieRole;

import java.util.List;

public class PersonService {

    private final PersonRepositoryImpl personRepository;
    private final PersonMapper personMapper;

    public PersonService(PersonRepositoryImpl personRepository, PersonMapperImpl personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    public List<Person> getAllPeople() {
        return personRepository.findAll();
    }
    public List<PersonDto> getAllPeopleByRole(MovieRole role) {
        return personMapper.toDto(personRepository.findAllByRole(role));
    }


    public void addPerson(Person person) {
        personRepository.save(person);
    }

    public Person getPersonById(Long id) {
        return personRepository.findById(id);
    }

    public void update(Person person) {
        personRepository.update(person);
    }

    public void deletePersonById(Long id) {
        Person person = getPersonById(id);
        personRepository.delete(person);
    }
}
