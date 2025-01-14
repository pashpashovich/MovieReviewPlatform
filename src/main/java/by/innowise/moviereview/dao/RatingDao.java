package by.innowise.moviereview.dao;

import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.entity.Rating;
import by.innowise.moviereview.util.HibernateUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RatingDao implements AbstractHibernateDao<Rating, Long> {

    private static RatingDao instance;

    public static RatingDao getInstance()
    {
        if (instance==null)
            instance=new RatingDao();
        return instance;
    }

    public Rating findByUserAndMovie(Long userId, Long movieId) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "FROM Rating r WHERE r.user.id = :userId AND r.movie.id = :movieId";
            return session.createQuery(hql, Rating.class)
                    .setParameter("userId", userId)
                    .setParameter("movieId", movieId)
                    .uniqueResult();
        }
    }

    @Override
    public Rating findById(Long aLong) {
        return null;
    }

    @Override
    public List<Rating> findAll() {
        return null;
    }

    @Override
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

    @Override
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

    @Override
    public void delete(Rating object) {

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
