package by.innowise.moviereview.dao;

import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.util.HibernateUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao implements AbstractHibernateDao<User, Long> {

    private static UserDao instance;

    public static UserDao getInstance() {
        if (instance == null)
            instance = new UserDao();
        return instance;
    }

    @Override
    public User findById(Long id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.find(User.class, id);
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }

    @Override
    public void save(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Ошибка сохранения пользователя: " + user, e);
        }
    }

    @Override
    public void update(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Ошибка обновления пользователя: " + user, e);
        }
    }

    @Override
    public void delete(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.remove(session.contains(user) ? user : session.merge(user));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Ошибка удаления пользователя: " + user, e);
        }
    }

    public User findByUsername(String username) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
        }
    }

    public User findByEmail(String email) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        }
    }
}

