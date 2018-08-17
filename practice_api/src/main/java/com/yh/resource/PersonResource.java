package com.yh.resource;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yh.response.DbResponse;
import com.yh.utils.DbUtils;
import com.yh.dao.PersonDao;
import io.dropwizard.hibernate.UnitOfWork;
import com.yh.representation.Person;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.hibernate.SessionFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

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
    public DbResponse deletePerson(String name) {
        dao =new PersonDao( sessionFactory);

        boolean isSucess = dao.delete(name);

        DbResponse dbResponse = new DbResponse();
        dbResponse.setSuccess(isSucess);
        return dbResponse;
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DbResponse updatePerson (String json) {
        dao =new PersonDao( sessionFactory);

        Gson gson = new Gson();
        Person person = gson.fromJson(json, Person.class);

        boolean isSucess = dao.update(person);

        DbResponse dbResponse = new DbResponse();
        dbResponse.setSuccess(isSucess);
        return dbResponse;
    }

    @POST
    @Path("/select")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> selectPerson(String name) {
        dao =new PersonDao( sessionFactory);

        List<Person> list = dao.select(name);

        return list;
    }








}
