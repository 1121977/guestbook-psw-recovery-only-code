package ru.incubator.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;

public abstract class DaoImpl<T> implements Dao<T> {

    final protected SessionFactory sessionFactory;
    final protected Class<T> entityClass;

    public DaoImpl(SessionFactory sessionFactory, Class<T> entityClass){
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
    }

    @Override
    public long save(T t) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(t);
        session.flush();
        return 0;
    }

    @Override
    public List<T> findAll() {
        Session session = sessionFactory.getCurrentSession();
        List<T> list = session.createQuery("select p from " + entityClass.getName() + " p", entityClass).getResultList();
        return list;
    }

}
