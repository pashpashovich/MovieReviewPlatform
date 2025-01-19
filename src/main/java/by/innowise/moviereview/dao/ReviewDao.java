package by.innowise.moviereview.dao;

import by.innowise.moviereview.entity.Review;
import by.innowise.moviereview.util.HibernateUtil;
import by.innowise.moviereview.util.enums.ReviewStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewDao implements AbstractHibernateDao<Review, Long> {
    private static ReviewDao instance;

    public static ReviewDao getInstance() {
        if (instance == null)
            instance = new ReviewDao();
        return instance;
    }

    @Override
    public void save(Review review) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(review);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public Review findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Review.class, id);
        }
    }

    @Override
    public void update(Review review) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(review);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public void delete(Review review) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(review);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public List<Review> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Review", Review.class).getResultList();
        }
    }

    public List<Review> findByStatus(ReviewStatus reviewStatus) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Review WHERE status = :status", Review.class)
                    .setParameter("status", reviewStatus)
                    .getResultList();
        }
    }

    public List<Review> findByMovieIdAndStatus(Long movieId, ReviewStatus reviewStatus) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Review WHERE movie.id = :movieId AND status = :status", Review.class)
                    .setParameter("movieId", movieId)
                    .setParameter("status", reviewStatus)
                    .getResultList();
        }
    }

    public List<Review> findByUserIdAndCreatedAtAfter(Long userId, LocalDateTime fiveDaysAgo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Review WHERE user.id = :userId AND createdAt > :fiveDaysAgo", Review.class)
                    .setParameter("userId", userId)
                    .setParameter("fiveDaysAgo", fiveDaysAgo)
                    .getResultList();
        }
    }
}
