package by.innowise.moviereview.dao;

import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.util.HibernateUtil;
import by.innowise.moviereview.util.enums.MovieRole;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
}
