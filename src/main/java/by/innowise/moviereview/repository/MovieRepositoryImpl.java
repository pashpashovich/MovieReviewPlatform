package by.innowise.moviereview.repository;

import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.exception.DeletingException;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.exception.SavingException;
import by.innowise.moviereview.exception.UpdatingException;
import by.innowise.moviereview.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MovieRepositoryImpl implements MovieRepository {

    @Override
    public List<Movie> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM Movie", Movie.class).list();
        } catch (Exception e) {
            throw new NotFoundException("Ошибка чтения фильмов");
        }
    }

    @Override
    public Movie findById(Long id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.find(Movie.class, id);
        } catch (Exception e) {
            throw new NotFoundException("Невозможно найти фильм с id: " + id);
        }
    }

    @Override
    public void save(Movie movie) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.persist(movie);
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
            session.remove(movie);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DeletingException("Ошибка удаления фильма: " + movie);
        }
    }
}
