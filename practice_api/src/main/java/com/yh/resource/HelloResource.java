package com.yh.resource;

import com.google.common.base.Optional;
import com.yh.bean.SayingHello;
import com.yh.utils.ConfigUtils;
import io.swagger.annotations.Api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;

@Path("yh-practice")
@Api(tags = {"hello interface"})
@Produces(MediaType.APPLICATION_JSON)
public class HelloResource {
    private String template;
    private String defaultName ;
    private AtomicLong counter;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SayingHello sayHello(@QueryParam("name") Optional<String> name) {
        template = ConfigUtils.getTEMPLATE();
        defaultName = ConfigUtils.getDafaultName();
        counter = new AtomicLong();

        String content = String.format(template, name.or(defaultName));
        SayingHello sayingHello = new SayingHello(counter.incrementAndGet(), content );
        return sayingHello;
    }
}
