package by.innowise.moviereview.service;

import by.innowise.moviereview.dao.GenreDao;
import by.innowise.moviereview.dao.MovieDao;
import by.innowise.moviereview.dao.PersonDao;
import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.exception.EntityNotFoundException;
import by.innowise.moviereview.exception.UpdatingException;
import by.innowise.moviereview.mapper.MovieMapper;
import by.innowise.moviereview.mapper.MovieMapperImpl;
import by.innowise.moviereview.util.enums.MovieRole;
import lombok.extern.slf4j.Slf4j;

import java.lang.module.FindException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class MovieService {
    private static MovieService instance;
    private final MovieDao movieDao;
    private final MovieMapper movieMapper;
    private final PersonDao personDao;
    private final GenreDao genreDao;

    private MovieService() {
        this.movieDao = MovieDao.getInstance();
        this.movieMapper = new MovieMapperImpl();
        this.personDao = PersonDao.getInstance();
        this.genreDao = GenreDao.getInstance();
    }

    public static MovieService getInstance() {
        if (instance == null)
            instance = new MovieService();
        return instance;
    }

    public List<MovieDto> getMoviesWithPagination(int page, int pageSize) {
        return movieDao.findMoviesWithPagination(page, pageSize)
                .stream()
                .map(movieMapper::toDto)
                .toList();
    }

    public MovieDto getMovieById(Long id) {
        Movie movie = movieDao.findById(id);
        if (movie == null) {
            throw new FindException("Фильм не найден с id: " + id);
        }
        return movieMapper.toDto(movie);
    }

    public void createMovie(MovieDto movieDto) {
        Movie movie = movieMapper.toEntityFromDto(movieDto);
        movie.setGenres(genreDao.findAllByName(movieDto.getGenres()));
        movie.setPeople(getPeopleByRoles(movieDto));
        movieDao.save(movie);
    }

    public void updateMovie(Long id, MovieDto movieDto) {
        Movie existingMovie = movieDao.findById(id);
        if (existingMovie == null) {
            throw new UpdatingException("Фильм с ID " + id + " не найден.");
        }
        existingMovie.setTitle(movieDto.getTitle());
        existingMovie.setDescription(movieDto.getDescription());
        existingMovie.setReleaseYear(movieDto.getReleaseYear());
        existingMovie.setDuration(movieDto.getDuration());
        existingMovie.setLanguage(movieDto.getLanguage());
        if (movieDto.getPosterBase64() != null) existingMovie.setPosterBase64(movieDto.getPosterBase64());
        existingMovie.setGenres(genreDao.findAllByName(movieDto.getGenres()));
        existingMovie.setPeople(getPeopleByRoles(movieDto));

        movieDao.update(existingMovie);
    }

    public void deleteMovie(Long id) throws EntityNotFoundException {
        Movie movie = movieDao.findById(id);
        if (movie == null) {
            throw new EntityNotFoundException("Фильм с id " + id + " не найден.");
        }
        movieDao.delete(movie);
    }

    private Set<Person> getPeopleByRoles(MovieDto movieDto) {
        Set<Person> people = new HashSet<>();
        people.addAll(personDao.findAllByNameAndRole(movieDto.getActors(), MovieRole.ACTOR));
        people.addAll(personDao.findAllByNameAndRole(movieDto.getDirectors(), MovieRole.DIRECTOR));
        people.addAll(personDao.findAllByNameAndRole(movieDto.getProducers(), MovieRole.PRODUCER));
        return people;
    }

    public List<MovieDto> filterMoviesWithPagination(String searchQuery, String genreId, String language, String year, String duration, int page, int size) {
        return movieMapper.toDtoList(movieDao.findMoviesWithFiltersAndPagination(searchQuery, genreId, language, year, duration, page, size));
    }

    public long getTotalMoviesCount() {
        return movieDao.getTotalMoviesCount();
    }
}



