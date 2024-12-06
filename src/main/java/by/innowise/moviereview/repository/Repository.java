package by.innowise.moviereview.repository;

import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.util.enums.MovieRole;

import java.util.List;

public interface Repository<T> {
    T findById(Long id);
    List<T> findAll();
    void save(T object);
    void update(T object);
    void delete(T object);
}
