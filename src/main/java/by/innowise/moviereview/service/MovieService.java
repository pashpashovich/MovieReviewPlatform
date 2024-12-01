package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.MovieCreateDto;
import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.exception.EntityNotFoundException;
import by.innowise.moviereview.exception.UpdatingException;
import by.innowise.moviereview.mapper.MovieMapper;
import by.innowise.moviereview.repository.GenreRepository;
import by.innowise.moviereview.repository.MovieRepository;
import by.innowise.moviereview.repository.PersonRepository;
import by.innowise.moviereview.util.enums.MovieRole;
import lombok.extern.slf4j.Slf4j;

import java.lang.module.FindException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final PersonRepository personRepository;
    private final GenreRepository genreRepository;

    public MovieService(MovieRepository movieRepository, MovieMapper movieMapper, PersonRepository personRepository, GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.personRepository = personRepository;
        this.genreRepository = genreRepository;
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

    public void createMovie(MovieCreateDto movieDto) {
        Movie movie = movieMapper.toEntityFromCreateDto(movieDto);
        movie.setGenres(genreRepository.findAllById(movieDto.getGenreIds()));
        movie.setPeople(getPeopleByRoles(movieDto));
        movieRepository.save(movie);
    }

    public void updateMovie(Long id, MovieCreateDto movieDto) {
        Movie existingMovie = movieRepository.findById(id);
        if (existingMovie == null) {
            throw new UpdatingException("Фильм с ID " + id + " не найден.");
        }

        existingMovie.setTitle(movieDto.getTitle());
        existingMovie.setDescription(movieDto.getDescription());
        existingMovie.setReleaseYear(movieDto.getReleaseYear());
        existingMovie.setDuration(movieDto.getDuration());
        existingMovie.setLanguage(movieDto.getLanguage());
        existingMovie.setGenres(genreRepository.findAllById(movieDto.getGenreIds()));
        existingMovie.setPeople(getPeopleByRoles(movieDto));

        movieRepository.update(existingMovie);
    }

    public void deleteMovie(Long id) throws EntityNotFoundException {
        Movie movie = movieRepository.findById(id);
        if (movie == null) {
            throw new EntityNotFoundException("Фильм с id " + id + " не найден.");
        }
        movieRepository.delete(movie);
    }

    private List<Person> getPeopleByRoles(MovieCreateDto movieDto) {
        List<Person> people = new ArrayList<>();
        people.addAll(personRepository.findAllByIdAndRole(movieDto.getActorIds(), MovieRole.ACTOR));
        people.addAll(personRepository.findAllByIdAndRole(movieDto.getDirectorIds(), MovieRole.DIRECTOR));
        people.addAll(personRepository.findAllByIdAndRole(movieDto.getProducerIds(), MovieRole.PRODUCER));
        return people;
    }
}

