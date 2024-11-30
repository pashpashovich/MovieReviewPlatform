package by.innowise.moviereview.repository;

import by.innowise.moviereview.entity.Movie;

import java.util.List;

public interface MovieRepository {
    public List<Movie> findAll();
    public Movie findById(Long id);
    public void save(Movie movie);
    public void update(Movie movie);
    public void delete(Movie movie);


}
