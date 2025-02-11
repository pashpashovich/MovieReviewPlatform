package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.WatchlistDto;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.entity.Watchlist;
import by.innowise.moviereview.exception.AlreadyInWatchListException;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.mapper.WatchlistMapper;
import by.innowise.moviereview.repository.MovieRepository;
import by.innowise.moviereview.repository.UserRepository;
import by.innowise.moviereview.repository.WatchlistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final WatchlistMapper watchlistMapper;

    @Transactional
    public void addToWatchlist(Long userId, Long movieId) {
        if (isMovieInWatchlist(userId, movieId)) {
            throw new AlreadyInWatchListException("Этот фильм уже есть в списке 'Хочу посмотреть'");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new NotFoundException("Фильм не найден"));
        Watchlist watchlist = new Watchlist()
                .setUser(user)
                .setMovie(movie)
                .setAddedAt(LocalDateTime.now());
        watchlistRepository.save(watchlist);
        log.info("Movie {} added to want to watch", movieId);
    }


    public List<WatchlistDto> getWatchlistByUserId(Long userId) {
        List<Watchlist> watchlist = watchlistRepository.findByUserId(userId);
        return watchlistMapper.toListDto(watchlist);
    }

    public void removeFromWatchlist(Long userId, Long movieId) {
        Watchlist watchlist = watchlistRepository.findByUserIdAndMovieId(userId, movieId)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с таким id %d не найден.", movieId)));
        watchlistRepository.delete(watchlist);
        log.info("Movie {} removed from want to watch", movieId);
    }

    public boolean isMovieInWatchlist(Long userId, Long movieId) {
        return watchlistRepository.findByUserIdAndMovieId(userId, movieId).isPresent();
    }
}
