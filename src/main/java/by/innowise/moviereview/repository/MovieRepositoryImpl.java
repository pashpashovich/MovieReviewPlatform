package by.innowise.moviereview.repository;

import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.exception.DeletingException;
import by.innowise.moviereview.exception.SavingException;
import by.innowise.moviereview.exception.UpdatingException;
import by.innowise.moviereview.util.HibernateUtil;
import jakarta.persistence.EntityGraph;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Slf4j
public class MovieRepositoryImpl implements Repository<Movie> {

    @Override
    public List<Movie> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            EntityGraph<Movie> graph = session.createEntityGraph(Movie.class);
            graph.addAttributeNodes("genres", "people");
            String hql = "SELECT m FROM Movie m";
            return session.createQuery(hql, Movie.class)
                    .setHint("javax.persistence.loadgraph", graph)
                    .getResultList();
        }
    }


    @Override
    public Movie findById(Long id) {
        try (Session session = HibernateUtil.getSession()) {
            EntityGraph<Movie> graph = session.createEntityGraph(Movie.class);
            graph.addAttributeNodes("genres", "people", "watchlist", "ratings", "reviews");
            String hql = "SELECT m FROM Movie m WHERE m.id = :id";
            return session.createQuery(hql, Movie.class)
                    .setParameter("id", id)
                    .setHint("javax.persistence.loadgraph", graph)
                    .uniqueResult();
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
            EntityGraph<Movie> graph = session.createEntityGraph(Movie.class);
            graph.addAttributeNodes("genres", "people", "watchlist", "ratings", "reviews");
            Movie managedMovie = session.createQuery("SELECT m FROM Movie m WHERE m.id = :id", Movie.class)
                    .setParameter("id", movie.getId())
                    .setHint("javax.persistence.loadgraph", graph)
                    .uniqueResult();
            if (managedMovie != null) {
                session.remove(managedMovie);
            } else {
                throw new DeletingException("Фильм с ID " + movie.getId() + " не найден.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DeletingException("Ошибка удаления фильма: " + movie);
        }
    }

    public List<Movie> findByGenres(List<Long> genreIds) {
        String hql = "SELECT DISTINCT m " +
                "FROM Movie m " +
                "LEFT JOIN FETCH m.genres g " +
                "WHERE g.id IN (:genreIds)";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(hql, Movie.class)
                    .setParameter("genreIds", genreIds)
                    .getResultList();
        }
    }

    public List<Movie> findTopRatedMovies() {
        String sql = "SELECT m.* " +
                "FROM movies m " +
                "LEFT JOIN ratings r ON m.id = r.movie_id " +
                "GROUP BY m.id, m.avg_rating, m.created_at, m.description, m.duration, m.language, m.poster, m.release_year, m.title " +
                "ORDER BY COALESCE(AVG(r.rating), 0) DESC";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNativeQuery(sql, Movie.class)
                    .setMaxResults(10)
                    .getResultList();
        }

    }
}
