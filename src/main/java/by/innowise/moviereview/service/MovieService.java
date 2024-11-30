package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.MovieCreateDto;
import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.mapper.MovieMapper;
import by.innowise.moviereview.repository.MovieRepository;

import java.lang.module.FindException;
import java.util.List;
import java.util.stream.Collectors;

public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MovieService(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(movieMapper::toDto)
                .collect(Collectors.toList());
    }

    public MovieDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id);
        if (movie == null) {
            throw new FindException("Фильм не найден с id: " + id);
        }
        return movieMapper.toDto(movie);
    }

    public void createMovie(MovieCreateDto movieCreateDTO) {
        Movie movie = movieMapper.toEntityFromCreateDto(movieCreateDTO);
        movieRepository.save(movie);
    }

    public void updateMovie(Long id, MovieCreateDto movieCreateDTO) {
        Movie existingMovie = movieRepository.findById(id);
        if (existingMovie == null) {
            throw new FindException("Фильм не найден с id: " + id);
        }
        Movie movie = movieMapper.toEntityFromCreateDto(movieCreateDTO);
        movie.setId(id);
        movieRepository.update(movie);
    }

    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id);
        if (movie == null) {
            throw new FindException("Фильм не найден с id: " + id);
        }
        movieRepository.delete(movie);
    }
}

