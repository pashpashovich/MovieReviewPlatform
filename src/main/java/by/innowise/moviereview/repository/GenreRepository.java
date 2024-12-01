package by.innowise.moviereview.repository;

import by.innowise.moviereview.entity.Genre;

import java.util.List;

public interface GenreRepository {
    Genre findById(Long id);
    List<Genre> findAll();
    List<Genre> findAllById(List<Long> ids);
    void save(Genre genre);
    void update(Genre genre);
    void delete(Genre genre);
}

