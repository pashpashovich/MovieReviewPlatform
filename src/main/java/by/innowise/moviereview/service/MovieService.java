package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.entity.Rating;
import by.innowise.moviereview.exception.EntityNotFoundException;
import by.innowise.moviereview.exception.UpdatingException;
import by.innowise.moviereview.mapper.MovieMapper;
import by.innowise.moviereview.repository.GenreRepositoryImpl;
import by.innowise.moviereview.repository.PersonRepositoryImpl;
import by.innowise.moviereview.repository.Repository;
import by.innowise.moviereview.util.HibernateUtil;
import by.innowise.moviereview.util.enums.MovieRole;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.lang.module.FindException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class MovieService {
    private final Repository<Movie> movieRepository;
    private final MovieMapper movieMapper;
    private final PersonRepositoryImpl personRepository;
    private final GenreRepositoryImpl genreRepository;

    public MovieService(Repository<Movie> movieRepository, MovieMapper movieMapper, PersonRepositoryImpl personRepository, GenreRepositoryImpl genreRepository) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.personRepository = personRepository;
        this.genreRepository = genreRepository;
    }

    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(movieMapper::toDto)
                .toList();
    }

    public MovieDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id);
        if (movie == null) {
            throw new FindException("Фильм не найден с id: " + id);
        }
        return movieMapper.toDto(movie);
    }

    public void createMovie(MovieDto movieDto) {
        Movie movie = movieMapper.toEntityFromDto(movieDto);
        movie.setGenres(genreRepository.findAllByName(movieDto.getGenres()));
        movie.setPeople(getPeopleByRoles(movieDto));
        movieRepository.save(movie);
    }

    public void updateMovie(Long id, MovieDto movieDto) {
        Movie existingMovie = movieRepository.findById(id);
        if (existingMovie == null) {
            throw new UpdatingException("Фильм с ID " + id + " не найден.");
        }

        existingMovie.setTitle(movieDto.getTitle());
        existingMovie.setDescription(movieDto.getDescription());
        existingMovie.setReleaseYear(movieDto.getReleaseYear());
        existingMovie.setDuration(movieDto.getDuration());
        existingMovie.setLanguage(movieDto.getLanguage());
        existingMovie.setGenres(genreRepository.findAllByName(movieDto.getGenres()));
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

    private Set<Person> getPeopleByRoles(MovieDto movieDto) {
        Set<Person> people = new HashSet<>();
        people.addAll(personRepository.findAllByNameAndRole(movieDto.getActors(), MovieRole.ACTOR));
        people.addAll(personRepository.findAllByNameAndRole(movieDto.getDirectors(), MovieRole.DIRECTOR));
        people.addAll(personRepository.findAllByNameAndRole(movieDto.getProducers(), MovieRole.PRODUCER));
        return people;
    }

    public List<MovieDto> filterMovies(String searchQuery, String genreId, String language, String year, String duration) {
        String hql = "SELECT DISTINCT m FROM Movie m LEFT JOIN m.genres g WHERE 1=1";

        if (searchQuery != null && !searchQuery.isEmpty()) {
            hql += " AND LOWER(m.title) LIKE :searchQuery";
        }
        if (genreId != null && !genreId.isEmpty()) {
            hql += " AND g.id = :genreId";
        }
        if (language != null && !language.isEmpty()) {
            hql += " AND m.language = :language";
        }
        if (year != null && !year.isEmpty()) {
            hql += " AND m.releaseYear = :year";
        }
        if (duration != null && !duration.isEmpty()) {
            hql += " AND m.duration = :duration";
        }

        try (Session session = HibernateUtil.getSession()) {
            Query<Movie> query = session.createQuery(hql, Movie.class);

            if (searchQuery != null && !searchQuery.isEmpty()) {
                query.setParameter("searchQuery", "%" + searchQuery.toLowerCase() + "%");
            }
            if (genreId != null && !genreId.isEmpty()) {
                query.setParameter("genreId", Long.valueOf(genreId));
            }
            if (language != null && !language.isEmpty()) {
                query.setParameter("language", language);
            }
            if (year != null && !year.isEmpty()) {
                query.setParameter("year", Integer.valueOf(year));
            }
            if (duration != null && !duration.isEmpty()) {
                query.setParameter("duration", Integer.valueOf(duration));
            }

            List<Movie> movies = query.list();
            return movies.stream()
                    .map(movieMapper::toDto)
                    .toList();
        }
    }

    public MovieDto findMovieById(Long id) {
        try (Session session = HibernateUtil.getSession()) {
            Movie movie = session.get(Movie.class, id);
            if (movie == null) {
                return null;
            }
            return movieMapper.toDto(movie);
        }
    }

}



