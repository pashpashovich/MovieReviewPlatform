package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.exception.EntityNotFoundException;
import by.innowise.moviereview.mapper.MovieMapper;
import by.innowise.moviereview.repository.GenreRepository;
import by.innowise.moviereview.repository.MovieRepository;
import by.innowise.moviereview.repository.PersonRepository;
import by.innowise.moviereview.util.MovieSpecifications;
import by.innowise.moviereview.util.enums.MovieRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final PersonRepository personRepository;
    private final MovieMapper movieMapper;

    @Transactional
    public List<MovieDto> getMoviesWithPagination(int page, int pageSize) {
        return movieRepository.findAll(PageRequest.of(page - 1, pageSize))
                .map(movieMapper::toDto)
                .getContent();
    }

    public MovieDto getMovieById(Long id) throws EntityNotFoundException {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Фильм не найден с id: " + id));
        return movieMapper.toDto(movie);
    }

    @Transactional
    public MovieDto createMovie(MovieDto movieDto) {
        Movie movie = movieMapper.toEntityFromDto(movieDto);
        movie.setGenres(new HashSet<>(genreRepository.findAllByName(movieDto.getGenres())));
        movie.setPeople(getPeopleByRoles(movieDto));
        Movie savedMovie = movieRepository.save(movie);
        MovieDto dto = movieMapper.toDto(savedMovie);
        log.info(String.format("Movie %s added", movieDto.getTitle()));
        return dto;
    }

    @Transactional
    public MovieDto updateMovie(Long id, MovieDto movieDto) throws EntityNotFoundException {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Фильм с ID " + id + " не найден."));
        existingMovie.setTitle(movieDto.getTitle());
        existingMovie.setDescription(movieDto.getDescription());
        existingMovie.setReleaseYear(movieDto.getReleaseYear());
        existingMovie.setDuration(movieDto.getDuration());
        existingMovie.setLanguage(movieDto.getLanguage());
        if (movieDto.getPosterBase64() != null) {
            existingMovie.setPosterBase64(movieDto.getPosterBase64());
        }
        existingMovie.setGenres(new HashSet<>(genreRepository.findAllByName(movieDto.getGenres())));
        existingMovie.setPeople(getPeopleByRoles(movieDto));
        Movie savedMovie = movieRepository.save(existingMovie);
        log.info(String.format("Movie with ID %s has been changed", id));
        return movieMapper.toDto(savedMovie);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
        log.info(String.format("Movie with ID %s removed", id));
    }

    private Set<Person> getPeopleByRoles(MovieDto movieDto) {
        Set<Person> people = new HashSet<>();
        people.addAll(personRepository.findAllByNameAndRole(movieDto.getActors(), MovieRole.ACTOR));
        people.addAll(personRepository.findAllByNameAndRole(movieDto.getDirectors(), MovieRole.DIRECTOR));
        people.addAll(personRepository.findAllByNameAndRole(movieDto.getProducers(), MovieRole.PRODUCER));
        return people;
    }

    @Transactional
    public List<MovieDto> filterMoviesWithPagination(String searchQuery, String genreId, String language, String year, String duration, int page, int size) {
        Specification<Movie> specification = MovieSpecifications.withFilters(searchQuery, genreId, language, year, duration);
        Page<Movie> moviePage = movieRepository.findAll(specification, PageRequest.of(page - 1, size));
        return moviePage.getContent()
                .stream()
                .map(movieMapper::toDto)
                .toList();
    }


    public long getTotalMoviesCount() {
        return movieRepository.count();
    }
}
