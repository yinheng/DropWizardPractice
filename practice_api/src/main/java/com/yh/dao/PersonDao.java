package com.yh.dao;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.yh.bean.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Boolean delete(long id) {
        boolean isSuccess = true;
        try {
            beginTransaction();

            String hql = "from Person p where p.id =:id";
            Query<Person> query = session.createQuery(hql).setParameter("id", id);

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
           /* String name = person.getName();
            String hql = "select p.id from Person p where p.name =:name";
            Query<Long> query = session.createQuery(hql).setParameter("name", name);

            List<Long> list = query.list();
            if(list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Person person1 = findById(list.get(i));
                    person1.setAge(person.getAge());
                    person1.setEmail(person.getEmail());
                    person1.setPassword(person.getPassword());
                    session.update(person1);
                }*/
           long id = person.getId();
           String hql = "select p.id from Person p where p.id =:id";
           Query<Long> query = session.createQuery(hql).setParameter("id", id);

           List<Long> list = query.list();

           if(list.size() == 0) {
               isSuccess = false;
           }else {
               session.update(person);
           }
        }catch(Exception e) {
            isSuccess = false;
        }finally {
            closeTransaction();
        }
        return isSuccess;
    }

    //select by name
    public Map<String, List> select(String name, Map<String, Integer> pageMap) {
        List<Person> dataList;
        List<Object> countList;
        Map<String, List> resultMap = new HashMap<String, List>();
        try {
            beginTransaction();
            /* //也可以使用sql查询
            String sql = "select * from person where name = " + " \"" + name + "\" " + "limit " + pageMap.get("start") + ", " + pageMap.get("limit");
            Query<Person> query = session.createSQLQuery(sql);
            */


            String hql = "from Person p where p.name =:name";
            Query<Person> query = session.createQuery(hql).setParameter("name", name);
            //hql不支持limit的语法，使用setFirstResult和setMaxResults方法来设置查询的条数
            query.setMaxResults(pageMap.get("limit"));
            query.setFirstResult(pageMap.get("start"));

            dataList = query.list();

            String totalHql = "select count(*) as total from Person p where p.name =:name";
            Query<Object> totalQuery = session.createQuery(totalHql).setParameter("name", name);
            countList = totalQuery.list();

            resultMap.put("total", countList);
            resultMap.put("data", dataList);
        }catch(Exception e) {

        }finally {
            closeTransaction();
        }
        return resultMap;
    }

    //select all
    public Map<String, List> select(Map<String, Integer> pageMap) {
        List<Person> dataList;
        List<Object> countList;
        Map<String, List> resultMap = new HashMap<String, List>();
        try {
            beginTransaction();
            //查询数据
            String hql = "from Person p";
            Query<Person> query = session.createQuery(hql);

            query.setMaxResults(pageMap.get("limit"));
            query.setFirstResult(pageMap.get("start"));

            dataList = query.list();
            //查询总数
            String totalHql = "select count(*) as total from Person p";
            Query<Object> totalQuery = session.createQuery(totalHql);
            countList = totalQuery.list();

            resultMap.put("total", countList);
            resultMap.put("data", dataList);


        }catch(Exception e) {

        }finally {
            closeTransaction();
        }
        return resultMap;
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
