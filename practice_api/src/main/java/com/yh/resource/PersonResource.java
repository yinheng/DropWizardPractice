package com.yh.resource;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.yh.response.DbResponse;
import com.yh.utils.DbUtils;
import com.yh.dao.PersonDao;
import io.dropwizard.hibernate.UnitOfWork;
import com.yh.bean.Person;
import io.swagger.annotations.Api;
import org.hibernate.SessionFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("person")
@Api(tags = {"person interface"})
public class PersonResource {

    private SessionFactory sessionFactory = DbUtils.getSessionFactory();
    PersonDao dao;

    @POST
    @Path("/add")
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DbResponse adddPerson(String json) {
        dao =new PersonDao( sessionFactory);
        Gson gson = new Gson();
        Person person = gson.fromJson(json, Person.class);

        boolean isSucess = dao.save(person);

        DbResponse dbResponse = new DbResponse();
        dbResponse.setSuccess(isSucess);
        return dbResponse;
    }

    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DbResponse deletePeople(String json) {
        dao =new PersonDao( sessionFactory);
        Gson gson = new Gson();
        boolean isSucess = false;
        DbResponse dbResponse = new DbResponse();
        Person person = gson.fromJson(json, Person.class);
        if(deletePerson(person)) {
            isSucess = true;
        }
        dbResponse.setSuccess(isSucess);
        return dbResponse;
    }
    //删除单个person
    public boolean deletePerson(Person person) {
        boolean isSucess = false;
        long id = person.getId();
        if(dao.delete(id)) {
            isSucess = true;
        }
        return isSucess;
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DbResponse updatePeople (String json) {
        dao =new PersonDao( sessionFactory);
        Gson gson = new Gson();
        boolean isSucess = false;
        DbResponse dbResponse = new DbResponse();
        Person person = gson.fromJson(json, Person.class);
        if(dao.update(person)) {
            isSucess = true;
        }
        dbResponse.setSuccess(isSucess);
        return dbResponse;
    }


    @GET
    @Path("/select")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, List> selectPerson(@QueryParam("name") String name, @QueryParam("page") Integer page, @QueryParam("start") Integer start, @QueryParam("limit") Integer limit) {
        dao =new PersonDao( sessionFactory);

        start=limit*(page-1);
        Map<String, Integer> pageMap = new HashMap<String, Integer>();
        pageMap.put("page", page);
        pageMap.put("start", start);
        pageMap.put("limit", limit);

        Map<String, List>  reultMap = dao.select(name, pageMap);
        return reultMap;
    }

    @GET
    @Path("/selectAll")
    @Produces(MediaType.APPLICATION_JSON)
    //@QueryParam从url中获取参数
    public Map<String, List> selectPerson(@QueryParam("page") Integer page, @QueryParam("start") Integer start, @QueryParam("limit") Integer limit) {
        dao =new PersonDao( sessionFactory);

        start=limit*(page-1);
        Map<String, Integer> pageMap = new HashMap<String, Integer>();
        pageMap.put("page", page);
        pageMap.put("start", start);
        pageMap.put("limit", limit);

        Map<String, List>  reultMap = dao.select(pageMap);
        return reultMap;
    }

}
