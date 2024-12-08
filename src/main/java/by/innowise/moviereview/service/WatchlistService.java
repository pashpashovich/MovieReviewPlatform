package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.WatchlistDto;
import by.innowise.moviereview.entity.Watchlist;
import by.innowise.moviereview.repository.MovieRepositoryImpl;
import by.innowise.moviereview.repository.UserRepositoryImpl;
import by.innowise.moviereview.repository.WatchlistRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;

public class WatchlistService {
    private final WatchlistRepositoryImpl watchlistRepository;
    private final UserRepositoryImpl userRepository;
    private final MovieRepositoryImpl movieRepository;

    public WatchlistService(WatchlistRepositoryImpl watchlistRepository, UserRepositoryImpl userRepository, MovieRepositoryImpl movieRepository) {
        this.watchlistRepository = watchlistRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    public void addToWatchlist(Long userId, Long movieId) {
        if (isMovieInWatchlist(userId, movieId)) {
            throw new IllegalArgumentException("Этот фильм уже есть в списке 'Хочу посмотреть'.");
        }
        Watchlist watchlist = new Watchlist();
        watchlist.setUser(userRepository.findById(userId));
        watchlist.setMovie(movieRepository.findById(movieId));
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

    public void removeFromWatchlist(Long userId, Long movieId) {
        watchlistRepository.deleteByUserIdAndMovieId(userId, movieId);
    }

    public boolean isMovieInWatchlist(Long userId, Long movieId) {
        return watchlistRepository.findByUserIdAndMovieId(userId, movieId).isPresent();
    }
}
