package by.innowise.moviereview.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


@UtilityClass
public class HibernateUtil {

    @Getter
    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml")
//                    .addAnnotatedClass(Post.class)

                    .buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }

    public static void shutdown() {
        getSessionFactory().close();
    }

}
