package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.MovieCreateDto;
import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.dto.MovieFilterRequest;
import by.innowise.moviereview.entity.Genre;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.enums.MovieRole;
import by.innowise.moviereview.exception.EntityNotFoundException;
import by.innowise.moviereview.mapper.MovieMapper;
import by.innowise.moviereview.repository.GenreRepository;
import by.innowise.moviereview.repository.MovieRepository;
import by.innowise.moviereview.repository.PersonRepository;
import by.innowise.moviereview.util.MovieSpecifications;
import by.innowise.moviereview.util.PosterLoading;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public MovieDto createMovie(MovieCreateDto movieDto) {
        Movie movie = movieMapper.toEntityFromDto(movieDto);
        movie
                .setGenres(new HashSet<>(genreRepository.findAllByName(movieDto.getGenres())))
                .setPeople(getPeopleByRoles(movieDto));
        Movie savedMovie = movieRepository.save(movie);
        MovieDto dto = movieMapper.toDto(savedMovie);
        log.info("Movie {} added", movieDto.getTitle());
        return dto;
    }

    @Transactional
    public MovieDto updateMovie(Long id, MovieCreateDto movieDto) throws EntityNotFoundException {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Фильм с ID " + id + " не найден."));
        Set<Genre> genres = genreRepository.findAllByName(movieDto.getGenres());
        existingMovie.setTitle(movieDto.getTitle())
                .setDescription(movieDto.getDescription())
                .setReleaseYear(movieDto.getReleaseYear())
                .setDuration(movieDto.getDuration())
                .setLanguage(movieDto.getLanguage())
                .setGenres(new HashSet<>(genres))
                .setPeople(getPeopleByRoles(movieDto));

        Movie savedMovie = movieRepository.save(existingMovie);
        log.info("Movie with ID {} has been changed", id);
        return movieMapper.toDto(savedMovie);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
        log.info("Movie with ID {} removed", id);
    }

    public void updateMoviePoster(Long id, MultipartFile posterFile) throws EntityNotFoundException, IOException {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Фильм с ID " + id + " не найден."));
        if (posterFile != null) {
            String posterBase64 = PosterLoading.processPosterFile(posterFile);
            existingMovie.setPosterBase64(posterBase64);
        }
        Movie savedMovie = movieRepository.save(existingMovie);
        log.info("Movie avatar with ID {} has been changed", savedMovie.getId());
    }


    public List<MovieDto> filterMoviesWithPagination(String searchQuery, int page, int size) {
        Specification<Movie> specification = MovieSpecifications.withFilters(searchQuery);
        Page<Movie> moviePage = movieRepository.findAll(specification, PageRequest.of(page - 1, size));
        return moviePage.getContent()
                .stream()
                .map(movieMapper::toDto)
                .toList();
    }

    @Transactional
    public List<MovieDto> filterMoviesWithPagination(MovieFilterRequest movieFilterRequest) {
        Specification<Movie> specification = MovieSpecifications.withFilters(movieFilterRequest.getSearchQuery(), movieFilterRequest.getGenreId(), movieFilterRequest.getLanguage(), movieFilterRequest.getYear(), movieFilterRequest.getDuration());
        Page<Movie> moviePage = movieRepository.findAll(specification, PageRequest.of(movieFilterRequest.getPage() - 1, movieFilterRequest.getSize()));
        return moviePage.getContent()
                .stream()
                .map(movieMapper::toDto)
                .toList();
    }

    private Set<Person> getPeopleByRoles(MovieCreateDto movieDto) {
        List<Person> actors = personRepository.findAllByNameAndRole(movieDto.getActors(), MovieRole.ACTOR);
        Set<Person> people = new HashSet<>(actors);
        List<Person> directors = personRepository.findAllByNameAndRole(movieDto.getDirectors(), MovieRole.DIRECTOR);
        people.addAll(directors);
        List<Person> producers = personRepository.findAllByNameAndRole(movieDto.getProducers(), MovieRole.PRODUCER);
        people.addAll(producers);
        return people;
    }

}
