package by.innowise.moviereview.dao;

import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.util.HibernateUtil;
import by.innowise.moviereview.util.enums.MovieRole;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PersonDao implements AbstractHibernateDao<Person, Long> {

    private static PersonDao instance;

    public static PersonDao getInstance() {
        if (instance == null)
            instance = new PersonDao();
        return instance;
    }

    @Override
    public Person findById(Long id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.find(Person.class, id);
        }
    }

    @Override
    public List<Person> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM Person", Person.class).list();
        }
    }

    @Override
    public void save(Person person) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.persist(person);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Ошибка сохранения персоны: " + person, e);
        }
    }

    @Override
    public void update(Person person) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.merge(person);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Ошибка обновления персоны: " + person, e);
        }
    }

    @Override
    public void delete(Person person) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.createNativeQuery("DELETE FROM movie_person WHERE person_id = :personId")
                    .setParameter("personId", person.getId())
                    .executeUpdate();
            session.remove(session.contains(person) ? person : session.merge(person));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Ошибка удаления персоны: " + person, e);
        }
    }


    public List<Person> findAllByRole(MovieRole role) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM Person p WHERE p.role = :role", Person.class)
                    .setParameter("role", role)
                    .list();
        }
    }

    public Set<Person> findAllByNameAndRole(Set<String> names, MovieRole role) {
        try (Session session = HibernateUtil.getSession()) {
            List<Person> people = session.createQuery("FROM Person p WHERE p.fullName IN :names AND p.role = :role", Person.class)
                    .setParameter("names", names)
                    .setParameter("role", role)
                    .list();
            return new HashSet<>(people);
        }
    }

    public List<Person> findPeople(int page, int size, String searchQuery, String roleFilter) {
        String hql = "FROM Person p WHERE 1=1";
        if (searchQuery != null && !searchQuery.isEmpty()) {
            hql += " AND LOWER(p.fullName) LIKE :searchQuery";
        }
        if (roleFilter != null && !roleFilter.isEmpty()) {
            hql += " AND p.role = :roleFilter";
        }
        try (Session session = HibernateUtil.getSession()) {
            Query<Person> query = session.createQuery(hql, Person.class);

            if (searchQuery != null && !searchQuery.isEmpty()) {
                query.setParameter("searchQuery", "%" + searchQuery.toLowerCase() + "%");
            }
            if (roleFilter != null && !roleFilter.isEmpty()) {
                query.setParameter("roleFilter", MovieRole.valueOf(roleFilter));
            }
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            return query.getResultList();
        }
    }

    public long countPeople(String searchQuery, String roleFilter) {
        String hql = "SELECT COUNT(p) FROM Person p WHERE 1=1";
        if (searchQuery != null && !searchQuery.isEmpty()) {
            hql += " AND LOWER(p.fullName) LIKE :searchQuery";
        }
        if (roleFilter != null && !roleFilter.isEmpty()) {
            hql += " AND p.role = :roleFilter";
        }
        try (Session session = HibernateUtil.getSession()) {
            Query<Long> query = session.createQuery(hql, Long.class);

            if (searchQuery != null && !searchQuery.isEmpty()) {
                query.setParameter("searchQuery", "%" + searchQuery.toLowerCase() + "%");
            }
            if (roleFilter != null && !roleFilter.isEmpty()) {
                query.setParameter("roleFilter", MovieRole.valueOf(roleFilter));
            }

            return query.getSingleResult();
        }
    }
}
