package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.PersonDto;
import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.mapper.PersonMapper;
import by.innowise.moviereview.dao.PersonDao;
import by.innowise.moviereview.mapper.PersonMapperImpl;
import by.innowise.moviereview.util.enums.MovieRole;

import java.util.List;

public class PersonService {
    private static PersonService instance;
    private final PersonDao personDao;
    private final PersonMapper personMapper;

    private PersonService() {
        this.personDao = PersonDao.getInstance();
        this.personMapper = new PersonMapperImpl();
    }

    public static PersonService getInstance() {
        if (instance == null)
            instance = new PersonService();
        return instance;
    }

    public List<Person> getAllPeople() {
        return personDao.findAll();
    }
    public List<PersonDto> getAllPeopleByRole(MovieRole role) {
        return personMapper.toDto(personDao.findAllByRole(role));
    }


    public void addPerson(Person person) {
        personDao.save(person);
    }

    public Person getPersonById(Long id) {
        return personDao.findById(id);
    }

    public void update(Person person) {
        personDao.update(person);
    }

    public void deletePersonById(Long id) {
        Person person = getPersonById(id);
        personDao.delete(person);
    }
}
