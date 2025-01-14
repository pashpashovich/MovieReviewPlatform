package by.innowise.moviereview.dao;

import java.util.List;

public interface AbstractHibernateDao<T, ID> {
    T findById(ID id);
    List<T> findAll();
    void save(T object);
    void update(T object);
    void delete(T object);
}
