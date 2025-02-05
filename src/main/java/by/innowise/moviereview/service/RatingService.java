package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.RateCreateDto;
import by.innowise.moviereview.dto.RatingDto;
import by.innowise.moviereview.entity.Rating;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.exception.UserNotFoundException;
import by.innowise.moviereview.mapper.RateMapper;
import by.innowise.moviereview.repository.MovieRepository;
import by.innowise.moviereview.repository.RatingRepository;
import by.innowise.moviereview.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final RateMapper rateMapper;

    @Transactional
    public RatingDto saveOrUpdateRating(RateCreateDto rateDto) {
        Optional<Rating> optionalRating = ratingRepository.findByUserIdAndMovieId(rateDto.getUserId(), rateDto.getMovieId());
        Rating rating=new Rating();
        if (optionalRating.isPresent()) {
            rating = optionalRating.get();
            rating.setRating(rateDto.getRating());
            rating.setUpdatedAt(LocalDateTime.now());
            ratingRepository.save(rating);
            log.info(String.format("User %s's rating for movie %s has been changed to %s", rateDto.getUserId(), rateDto.getMovieId(), rateDto.getRating()));
        } else {
            rating.setUser(userRepository.findById(rateDto.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("Пользователь не найден")));
            rating.setMovie(movieRepository.findById(rateDto.getMovieId())
                    .orElseThrow(() -> new NotFoundException("Фильм не найден")));
            rating.setRating(rateDto.getRating());
            rating.setCreatedAt(LocalDateTime.now());
            ratingRepository.save(rating);
            log.info(String.format("User %s rating for movie %s added with tag %s", rateDto.getUserId(), rateDto.getMovieId(), rateDto.getRating()));
        }
        return rateMapper.toDto(rating);
    }

    @Transactional
    public Integer getRatingByUserAndMovie(Long userId, Long movieId) {
        return ratingRepository.findByUserIdAndMovieId(userId, movieId)
                .map(Rating::getRating)
                .orElse(null);
    }

    public Double getAverageRatingForMovie(Long movieId) {
        return ratingRepository.findAverageRatingByMovieId(movieId);
    }
}
