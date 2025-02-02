package by.innowise.moviereview.repository;

import by.innowise.moviereview.config.TestConfig;
import by.innowise.moviereview.entity.Genre;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.Review;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.util.enums.ReviewStatus;
import by.innowise.moviereview.utils.TestBuilder;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
@Transactional
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenreRepository genreRepository;

    private Movie movie1;
    private Movie movie2;
    private User user;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        user = TestBuilder.createUser("testuser", "password");
        userRepository.save(user);

        Genre genre1 = genreRepository.save(TestBuilder.createGenre("Драмы"));
        Genre genre2 = genreRepository.save(TestBuilder.createGenre("Комедия"));

        movie1 = TestBuilder.createMovie("Movie 1", Set.of(genre1));
        movie2 = TestBuilder.createMovie("Movie 2", Set.of(genre2));
        movieRepository.save(movie1);
        movieRepository.save(movie2);
        now = LocalDateTime.now();

        Review review1 = TestBuilder.createReview(user, movie1, "Great movie!", ReviewStatus.APPROVED, now.minusDays(1));
        Review review2 = TestBuilder.createReview(user, movie2, "Not bad", ReviewStatus.PENDING, now.minusDays(2));
        Review review3 = TestBuilder.createReview(user, movie1, "Okay movie", ReviewStatus.APPROVED, now.minusDays(3));
        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);
    }

    @Test
    void shouldFindByStatus() {
        // given
        //when
        List<Review> reviews = reviewRepository.findByStatus(ReviewStatus.APPROVED);
        //then
        assertNotNull(reviews);
        assertEquals(2, reviews.size());
    }

    @Test
    void shouldFindByMovieIdAndStatus() {
        // given
        //when
        List<Review> reviews = reviewRepository.findByMovieIdAndStatus(movie1.getId(), ReviewStatus.APPROVED);
        //then
        assertNotNull(reviews);
        assertEquals(2, reviews.size());
    }

    @Test
    void shouldFindByUserIdAndCreatedAtAfter() {
        // given
        //when
        List<Review> reviews = reviewRepository.findByUserIdAndCreatedAtAfter(user.getId(), now.minusDays(2));
        //then
        assertNotNull(reviews);
        assertEquals(2, reviews.size());
    }
}