package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.mapper.MovieMapper;
import by.innowise.moviereview.repository.MovieRepository;
import by.innowise.moviereview.repository.RatingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RatingRepository ratingRepository;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    @Transactional
    public List<MovieDto> getRecommendationsForUser(Long userId) {
        List<Long> likedGenres = ratingRepository.findGenresByUserPreferences(userId);
        if (userId == null || likedGenres.isEmpty()) {
            List<Movie> topRatedMovies = movieRepository.findTopRatedMovies();
            return movieMapper.toListDtoForRecommendations(topRatedMovies);
        } else {
            List<Movie> recommendedMovies = movieRepository.findByGenres(likedGenres);
            return recommendedMovies.stream()
                    .limit(5)
                    .map(movieMapper::toDtoForRecommendations)
                    .toList();
        }
    }

}
