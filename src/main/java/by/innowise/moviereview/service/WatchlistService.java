package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.WatchlistDto;
import by.innowise.moviereview.entity.Watchlist;
import by.innowise.moviereview.dao.MovieDao;
import by.innowise.moviereview.dao.UserDao;
import by.innowise.moviereview.dao.WatchlistDao;

import java.time.LocalDateTime;
import java.util.List;

public class WatchlistService {
    private static WatchlistService instance;
    private final WatchlistDao watchlistDao;
    private final UserDao userDao;
    private final MovieDao movieDao;

    private WatchlistService() {
        this.watchlistDao = WatchlistDao.getInstance();
        this.userDao = UserDao.getInstance();
        this.movieDao = MovieDao.getInstance();
    }

    public static WatchlistService getInstance() {
        if (instance == null)
            instance = new WatchlistService();
        return instance;
    }

    public void addToWatchlist(Long userId, Long movieId) {
        if (isMovieInWatchlist(userId, movieId)) {
            throw new IllegalArgumentException("Этот фильм уже есть в списке 'Хочу посмотреть'.");
        }
        Watchlist watchlist = new Watchlist();
        watchlist.setUser(userDao.findById(userId));
        watchlist.setMovie(movieDao.findById(movieId));
        watchlist.setAddedAt(LocalDateTime.now());
        watchlistDao.save(watchlist);
    }


    public List<WatchlistDto> getWatchlistByUserId(Long userId) {
        List<Watchlist> watchlist = watchlistDao.findByUserId(userId);
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
        watchlistDao.deleteByUserIdAndMovieId(userId, movieId);
    }

    public boolean isMovieInWatchlist(Long userId, Long movieId) {
        return watchlistDao.findByUserIdAndMovieId(userId, movieId).isPresent();
    }

}
