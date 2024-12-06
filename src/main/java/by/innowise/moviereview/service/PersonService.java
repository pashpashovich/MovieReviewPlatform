package by.innowise.moviereview.service;

import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.repository.Repository;

import java.util.List;

public class PersonService {

    private final Repository<Person> personRepository;

    public PersonService(Repository<Person> personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAllPeople() {
        return personRepository.findAll();
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
