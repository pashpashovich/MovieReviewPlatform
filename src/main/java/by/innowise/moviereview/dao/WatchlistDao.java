package by.innowise.moviereview.dao;

import by.innowise.moviereview.entity.Watchlist;
import by.innowise.moviereview.util.HibernateUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WatchlistDao implements AbstractHibernateDao<Watchlist, Long> {
    private static WatchlistDao instance;

    public static WatchlistDao getInstance() {
        if (instance == null)
            instance = new WatchlistDao();
        return instance;
    }

    @Override
    public Watchlist findById(Long aLong) {
        return null;
    }

    @Override
    public List<Watchlist> findAll() {
        return null;
    }

    @Override
    public void save(Watchlist watchlist) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(watchlist);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public void update(Watchlist object) {

    }

    @Override
    public void delete(Watchlist object) {

    }

    public List<Watchlist> findByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT w FROM Watchlist w JOIN FETCH w.movie WHERE w.user.id = :userId",
                    Watchlist.class
            ).setParameter("userId", userId).getResultList();
        }
    }

    public void deleteByUserIdAndMovieId(Long userId, Long movieId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("DELETE FROM Watchlist WHERE user.id = :userId AND movie.id = :movieId")
                    .setParameter("userId", userId)
                    .setParameter("movieId", movieId)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public Optional<Watchlist> findByUserIdAndMovieId(Long userId, Long movieId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Watchlist WHERE user.id = :userId AND movie.id = :movieId", Watchlist.class)
                    .setParameter("userId", userId)
                    .setParameter("movieId", movieId)
                    .uniqueResultOptional();
        }
    }

}
