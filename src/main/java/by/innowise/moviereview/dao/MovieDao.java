package by.innowise.moviereview.dao;

import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.exception.DeletingException;
import by.innowise.moviereview.exception.SavingException;
import by.innowise.moviereview.exception.UpdatingException;
import by.innowise.moviereview.util.HibernateUtil;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MovieDao implements AbstractHibernateDao<Movie, Long> {

    public static final String JAKARTA_PERSISTENCE_LOADGRAPH = "jakarta.persistence.loadgraph";
    private static MovieDao instance;

    public static MovieDao getInstance() {
        if (instance == null)
            instance = new MovieDao();
        return instance;
    }

    @Override
    public List<Movie> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            EntityGraph<Movie> graph = session.createEntityGraph(Movie.class);
            graph.addAttributeNodes("genres", "people");
            String hql = "SELECT m FROM Movie m";
            return session.createQuery(hql, Movie.class)
                    .setHint(JAKARTA_PERSISTENCE_LOADGRAPH, graph)
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
                    .setHint(JAKARTA_PERSISTENCE_LOADGRAPH, graph)
                    .uniqueResult();
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
            EntityGraph<Movie> graph = session.createEntityGraph(Movie.class);
            graph.addAttributeNodes("genres", "people", "watchlist", "ratings", "reviews");
            Movie managedMovie = session.createQuery("SELECT m FROM Movie m WHERE m.id = :id", Movie.class)
                    .setParameter("id", movie.getId())
                    .setHint(JAKARTA_PERSISTENCE_LOADGRAPH, graph)
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
                    .setMaxResults(5)
                    .getResultList();
        }
    }

    public long getTotalMoviesCount() {
        try (Session session = HibernateUtil.getSession()) {
            return session
                    .createQuery("SELECT COUNT(m) FROM Movie m", Long.class)
                    .getSingleResult();
        }
    }

    public List<Movie> findMoviesWithPagination(int page, int pageSize) {
        try (Session session = HibernateUtil.getSession()) {
            EntityGraph<Movie> graph = session.createEntityGraph(Movie.class);
            graph.addAttributeNodes("genres", "people");
            String hql = "SELECT m FROM Movie m";
            return session.createQuery(hql, Movie.class)
                    .setHint(JAKARTA_PERSISTENCE_LOADGRAPH, graph)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        }
    }

    public List<Movie> findMoviesWithFiltersAndPagination(
            String searchQuery, String genreId, String language, String year, String duration, int page, int size) {
        try (Session session = HibernateUtil.getSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);
            Root<Movie> movieRoot = cq.from(Movie.class);
            movieRoot.fetch("genres", JoinType.LEFT);
            movieRoot.fetch("people", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();
            if (searchQuery != null && !searchQuery.isEmpty()) {
                predicates.add(cb.like(cb.lower(movieRoot.get("title")), "%" + searchQuery.toLowerCase() + "%"));
            }
            if (genreId != null && !genreId.isEmpty()) {
                predicates.add(cb.equal(movieRoot.join("genres", JoinType.LEFT).get("id"), Long.valueOf(genreId)));
            }
            if (language != null && !language.isEmpty()) {
                predicates.add(cb.equal(movieRoot.get("language"), language));
            }
            if (year != null && !year.isEmpty()) {
                predicates.add(cb.equal(movieRoot.get("releaseYear"), Integer.valueOf(year)));
            }
            if (duration != null && !duration.isEmpty()) {
                predicates.add(cb.equal(movieRoot.get("duration"), Integer.valueOf(duration)));
            }
            cq.select(movieRoot).distinct(true).where(predicates.toArray(new Predicate[0]));
            Query<Movie> query = session.createQuery(cq);
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            return query.getResultList();
        }
    }

}
