package by.innowise.moviereview.repository;

import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.exception.DeletingException;
import by.innowise.moviereview.exception.SavingException;
import by.innowise.moviereview.exception.UpdatingException;
import by.innowise.moviereview.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Slf4j
public class MovieRepositoryImpl implements MovieRepository {

    @Override
    public List<Movie> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "SELECT m FROM Movie m";
            List<Movie> movies = session.createQuery(hql, Movie.class).getResultList();
            movies.forEach(movie -> {
                Hibernate.initialize(movie.getGenres());
                Hibernate.initialize(movie.getPeople());
            });
            return movies;
        }
    }


    @Override
    public Movie findById(Long id) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "SELECT m FROM Movie m WHERE m.id = :id";
            Movie movie = session.createQuery(hql, Movie.class)
                    .setParameter("id", id)
                    .uniqueResult();
            if (movie != null) {
                Hibernate.initialize(movie.getGenres());
                Hibernate.initialize(movie.getPeople());
                Hibernate.initialize(movie.getWatchlist());
                Hibernate.initialize(movie.getRatings());
                Hibernate.initialize(movie.getReviews());
            }
            return movie;
        }
    }

    @Override
    public void save(Movie movie) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.save(movie);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new SavingException("Ошибка сохранения фильма: " + movie);
        }
    }

    @Override
    public void update(Movie movie) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.merge(movie);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new UpdatingException("Ошибка сохранения фильма: " + movie);
        }
    }

    @Override
    public void delete(Movie movie) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            Hibernate.initialize(movie.getGenres());
            Hibernate.initialize(movie.getPeople());
            Hibernate.initialize(movie.getWatchlist());
            Hibernate.initialize(movie.getRatings());
            Hibernate.initialize(movie.getReviews());

            session.remove(session.contains(movie) ? movie : session.merge(movie));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DeletingException("Ошибка удаления фильма: " + movie);
        }
    }

}
