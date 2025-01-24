package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.WatchlistDto;
import by.innowise.moviereview.entity.Watchlist;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.repository.MovieRepository;
import by.innowise.moviereview.repository.UserRepository;
import by.innowise.moviereview.repository.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    @Transactional
    public void addToWatchlist(Long userId, Long movieId) {
        if (isMovieInWatchlist(userId, movieId)) {
            throw new IllegalArgumentException("Этот фильм уже есть в списке 'Хочу посмотреть'.");
        }
        Watchlist watchlist = new Watchlist();
        watchlist.setUser(userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден")));
        watchlist.setMovie(movieRepository.findById(movieId).orElseThrow(() -> new NotFoundException("Фильм не найден")));
        watchlist.setAddedAt(LocalDateTime.now());
        watchlistRepository.save(watchlist);
    }

    public List<WatchlistDto> getWatchlistByUserId(Long userId) {
        List<Watchlist> watchlist = watchlistRepository.findByUserId(userId);
        return watchlist.stream()
                .map(w -> new WatchlistDto(
                        w.getMovie().getId(),
                        w.getMovie().getTitle(),
                        w.getMovie().getPosterBase64(),
                        w.getAddedAt()
                ))
                .toList();
    }

    @Transactional
    public void removeFromWatchlist(Long userId, Long movieId) {
        Watchlist watchlist = watchlistRepository.findByUserIdAndMovieId(userId, movieId)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с таким id %d не найден.", movieId)));
        watchlistRepository.delete(watchlist);
    }

    public boolean isMovieInWatchlist(Long userId, Long movieId) {
        return watchlistRepository.findByUserIdAndMovieId(userId, movieId).isPresent();
    }
}
