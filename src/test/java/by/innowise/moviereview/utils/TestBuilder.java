package by.innowise.moviereview.utils;

import by.innowise.moviereview.dto.EntityDto;
import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.entity.Genre;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.entity.Rating;
import by.innowise.moviereview.entity.Review;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.entity.Watchlist;
import by.innowise.moviereview.util.enums.MovieRole;
import by.innowise.moviereview.util.enums.ReviewStatus;
import by.innowise.moviereview.util.enums.Role;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@UtilityClass
public class TestBuilder {


    public static Genre createGenre(String name) {
        Genre genre = new Genre();
        genre.setName(name);
        return genre;
    }

    public static Movie createMovie(String title, Set<Genre> genres) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setGenres(genres);
        movie.setLanguage("Русский");
        movie.setReleaseYear(2023);
        movie.setDuration(134);
        return movie;
    }

    public static User createUser(String name, String email) {
        User user = new User();
        user.setUsername(name);
        user.setEmail(email);
        user.setPassword("1234567gfdsdfvb");
        user.setRole(Role.USER);
        return user;
    }

    public static Rating createRating(Movie movie, Integer value, User user) {
        Rating rating = new Rating();
        rating.setMovie(movie);
        rating.setRating(value);
        rating.setUser(user);
        return rating;
    }

    public static Person createPerson(String fullName, MovieRole movieRole) {
        Person person = new Person();
        person.setFullName(fullName);
        person.setRole(movieRole);
        person.setMovies(List.of(createMovie("Movie1", Set.of(createGenre("Драма")))));
        return person;
    }

    public static Review createReview(User user, Movie movie, String content, ReviewStatus reviewStatus, LocalDateTime time) {
        Review review = new Review();
        review.setUser(user);
        review.setMovie(movie);
        review.setRating(5);
        review.setStatus(reviewStatus);
        review.setCreatedAt(time);
        review.setContent(content);
        return review;
    }

    public static Watchlist createWatchList(User user, Movie movie) {
        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);
        watchlist.setMovie(movie);
        watchlist.setAddedAt(LocalDateTime.now());
        return watchlist;
    }


    public static UserDto createUserDto(Long id, String email, Role userRole, String username) {
        return UserDto.builder()
                .id(id)
                .email(email)
                .role(userRole)
                .username(username)
                .build();

    }

    public static EntityDto createGenreDto(Long id, String name) {
        return EntityDto.builder()
                .id(id)
                .name(name)
                .build();

    }

    public static MovieDto createMovieDto(String title) {
        MovieDto movie = new MovieDto();
        movie.setTitle(title);
        movie.setGenres(Set.of("Драма"));
        movie.setLanguage("Русский");
        movie.setReleaseYear(2023);
        movie.setDuration(134);
        return movie;
    }
}
