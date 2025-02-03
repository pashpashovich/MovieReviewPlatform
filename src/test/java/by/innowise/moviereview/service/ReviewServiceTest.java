package by.innowise.moviereview.service;

import by.innowise.moviereview.entity.Genre;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.Review;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.repository.MovieRepository;
import by.innowise.moviereview.repository.ReviewRepository;
import by.innowise.moviereview.repository.UserRepository;
import by.innowise.moviereview.util.enums.ReviewStatus;
import by.innowise.moviereview.utils.TestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private ReviewService reviewService;

    private User testUser;
    private Movie testMovie;
    private Review testReview;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = TestBuilder.createUser("test_user", "test_user@gmail.com");

        Genre genre = new Genre();

        testMovie = TestBuilder.createMovie("Test Movie", Set.of(genre));

        testReview = TestBuilder.createReview(testUser,testMovie,"Great movie!",ReviewStatus.PENDING,LocalDateTime.now());
    }

    @Test
    void shouldAddReviewSuccessfully() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));
        when(reviewRepository.save(any(Review.class))).thenReturn(testReview);
        //when
        reviewService.addReview(1L, 1L, "Great movie!", 5);
        //then
        verify(userRepository, times(1)).findById(1L);
        verify(movieRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                reviewService.addReview(1L, 1L, "Great movie!", 5));
        //then
        assertEquals("Пользователь не найден.", exception.getMessage());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void shouldThrowExceptionIfMovieNotFound() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());
        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                reviewService.addReview(1L, 1L, "Great movie!", 5));
        //then
        assertEquals("Фильм не найден.", exception.getMessage());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void shouldFindAllPendingReviews() {
        // given
        List<Review> pendingReviews = List.of(testReview);
        when(reviewRepository.findByStatus(ReviewStatus.PENDING)).thenReturn(pendingReviews);
        //when
        List<Review> result = reviewService.findAllPendingReviews();
        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ReviewStatus.PENDING, result.get(0).getStatus());
        verify(reviewRepository, times(1)).findByStatus(ReviewStatus.PENDING);
    }

    @Test
    void shouldUpdateReviewStatus() {
        // given
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(testReview));
        //when
        reviewService.updateReviewStatus(1L, "APPROVED");
        //then
        assertEquals(ReviewStatus.APPROVED, testReview.getStatus());
        verify(reviewRepository, times(1)).save(testReview);
    }

    @Test
    void shouldThrowExceptionIfReviewNotFoundOnStatusUpdate() {
        // given
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());
        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                reviewService.updateReviewStatus(1L, "APPROVED"));
        //then
        assertEquals("Рецензия не найдена.", exception.getMessage());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void shouldFindApprovedReviewsByMovieId() {
        // given
        testReview.setStatus(ReviewStatus.APPROVED);
        List<Review> approvedReviews = List.of(testReview);

        when(reviewRepository.findByMovieIdAndStatus(1L, ReviewStatus.APPROVED)).thenReturn(approvedReviews);
        //when
        List<Review> result = reviewService.findApprovedReviewsByMovieId(1L);
        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ReviewStatus.APPROVED, result.get(0).getStatus());
        verify(reviewRepository, times(1)).findByMovieIdAndStatus(1L, ReviewStatus.APPROVED);
    }

    @Test
    void shouldFindRecentReviewsByUserId() {
        // given
        List<Review> recentReviews = List.of(testReview);

        when(reviewRepository.findByUserIdAndCreatedAtAfter(eq(1L), any(LocalDateTime.class))).thenReturn(recentReviews);
        //when
        List<Review> result = reviewService.findRecentReviewsByUserId(1L);
        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reviewRepository, times(1)).findByUserIdAndCreatedAtAfter(eq(1L), any(LocalDateTime.class));
    }
}
