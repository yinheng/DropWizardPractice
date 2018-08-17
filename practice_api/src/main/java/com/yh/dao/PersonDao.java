package com.yh.dao;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.yh.representation.Person;

import javax.xml.ws.Response;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PersonDao extends AbstractDAO<Person> {
    private SessionFactory sessionFactory;
    protected Session session;
    public PersonDao(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public Person findById(Long id) {
        return get(id);
    }

    protected void beginTransaction() {
        this.session = this.sessionFactory.openSession();
        this.session.beginTransaction();
    }

    protected void closeTransaction() {
        this.session.getTransaction().commit();
        this.session.close();
    }


    //add
    public boolean save(Person person) {
        boolean isSuccess = true;
        try {
            beginTransaction();
            session.save(person);
        }catch(Exception e) {
            isSuccess = false;
        }finally {
            closeTransaction();
        }
        return isSuccess;
    }

    //delete
    public Boolean delete(String name) {
        boolean isSuccess = true;
        try {
            beginTransaction();
            String hql = "from Person p where p.name =:name";
            Query<Person> query = session.createQuery(hql).setParameter("name", name);

            List<Person> list = query.list();

            if(list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    session.delete(list.get(i));
                }
            }
        }catch(Exception e) {
            isSuccess = false;

        }finally {
            closeTransaction();
        }
        return isSuccess;
    }

    //update
    public boolean update(Person person ) {
        boolean isSuccess = true;
        try {
            beginTransaction();
            session.update(person);
        }catch(Exception e) {
            isSuccess = false;
        }finally {
            closeTransaction();
        }
        return isSuccess;
    }

    //select
    public List<Person> select(String name) {
        List<Person> list = new ArrayList<Person>();
        try {
            beginTransaction();
            String hql = "from Person p where p.name =:name";
            Query<Person> query = session.createQuery(hql).setParameter("name", name);

            list = query.list();
        }catch(Exception e) {

        }finally {
            closeTransaction();
        }
        return list;
    }

    @Override
    protected List<Person> list(Query<Person> query) throws HibernateException {
        return super.list(query);
    }
    @Override
    protected List<Person> list(Criteria criteria) throws HibernateException {
        return super.list(criteria);
    }
}
