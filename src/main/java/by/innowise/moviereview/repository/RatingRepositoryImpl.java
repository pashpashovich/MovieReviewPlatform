package by.innowise.moviereview.repository;

import by.innowise.moviereview.entity.Rating;
import by.innowise.moviereview.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class RatingRepositoryImpl {
    public Rating findByUserAndMovie(Long userId, Long movieId) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "FROM Rating r WHERE r.user.id = :userId AND r.movie.id = :movieId";
            return session.createQuery(hql, Rating.class)
                    .setParameter("userId", userId)
                    .setParameter("movieId", movieId)
                    .uniqueResult();
        }
    }

    public void save(Rating rating) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.save(rating);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error saving rating", e);
        }
    }

    public void update(Rating rating) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.update(rating);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error updating rating", e);
        }
    }

    public Double findAverageRatingByMovieId(Long movieId) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "SELECT AVG(r.rating) FROM Rating r WHERE r.movie.id = :movieId";
            return (Double) session.createQuery(hql)
                    .setParameter("movieId", movieId)
                    .uniqueResult();
        }
    }

    public List<Long> findGenresByUserPreferences(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT DISTINCT g.id FROM Rating r " +
                                    "JOIN r.movie m " +
                                    "JOIN m.genres g " +
                                    "WHERE r.user.id = :userId AND r.rating >= 4", Long.class)
                    .setParameter("userId", userId)
                    .getResultList();
        }
    }
}
