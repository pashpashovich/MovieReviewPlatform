package by.innowise.moviereview.repository;

import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.util.enums.MovieRole;

import java.util.List;

public interface PersonRepository {
    Person findById(Long id);
    List<Person> findAll();
    List<Person> findAllByRole(MovieRole role);
    List<Person> findAllById(List<Long> ids);
    List<Person> findAllByIdAndRole(List<Long> ids, MovieRole role);
    void save(Person person);
    void update(Person person);
    void delete(Person person);
}
