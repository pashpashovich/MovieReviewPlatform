package by.innowise.moviereview.repository;

import by.innowise.moviereview.entity.Genre;
import by.innowise.moviereview.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class GenreRepositoryImpl implements GenreRepository {

    @Override
    public Genre findById(Long id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.find(Genre.class, id);
        }
    }

    @Override
    public List<Genre> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM Genre", Genre.class).list();
        }
    }

    @Override
    public List<Genre> findAllById(List<Long> ids) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM Genre g WHERE g.id IN :ids", Genre.class)
                    .setParameter("ids", ids)
                    .list();
        }
    }

    @Override
    public void save(Genre genre) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.save(genre);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Ошибка сохранения жанра: " + genre, e);
        }
    }

    @Override
    public void update(Genre genre) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.merge(genre);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Ошибка обновления жанра: " + genre, e);
        }
    }

    @Override
    public void delete(Genre genre) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.remove(session.contains(genre) ? genre : session.merge(genre));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Ошибка удаления жанра: " + genre, e);
        }
    }
}
