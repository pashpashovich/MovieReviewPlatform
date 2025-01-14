package by.innowise.moviereview.util.daofactory;


import by.innowise.moviereview.dao.AbstractHibernateDao;
import by.innowise.moviereview.dao.GenreDao;
import by.innowise.moviereview.dao.MovieDao;
import by.innowise.moviereview.dao.PersonDao;
import by.innowise.moviereview.dao.RatingDao;
import by.innowise.moviereview.dao.ReviewDao;
import by.innowise.moviereview.dao.UserDao;
import by.innowise.moviereview.dao.WatchlistDao;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DaoFactory {
    @SuppressWarnings("unchecked")
    public static <T, ID> AbstractHibernateDao<T, ID> getDAO(Class<? extends AbstractHibernateDao<T, ID>> daoClass) {
        if (GenreDao.class.isAssignableFrom(daoClass)) {
            return (AbstractHibernateDao<T, ID>) GenreDao.getInstance();
        } else if (MovieDao.class.isAssignableFrom(daoClass)) {
            return (AbstractHibernateDao<T, ID>) MovieDao.getInstance();
        } else if (PersonDao.class.isAssignableFrom(daoClass)) {
            return (AbstractHibernateDao<T, ID>) PersonDao.getInstance();
        } else if (UserDao.class.isAssignableFrom(daoClass)) {
            return (AbstractHibernateDao<T, ID>) UserDao.getInstance();
        } else if (WatchlistDao.class.isAssignableFrom(daoClass)) {
            return (AbstractHibernateDao<T, ID>) WatchlistDao.getInstance();
        } else if (RatingDao.class.isAssignableFrom(daoClass)) {
            return (AbstractHibernateDao<T, ID>) RatingDao.getInstance();
        } else if (ReviewDao.class.isAssignableFrom(daoClass)) {
            return (AbstractHibernateDao<T, ID>) ReviewDao.getInstance();
        }

        throw new IllegalArgumentException("Unknown DAO class: " + daoClass.getName());
    }
}
