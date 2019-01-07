package com.yh.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yh.utils.ConfigUtils;
import com.yh.utils.DbUtils;
import com.yh.configuation.PracticeConfiguation;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.server.SimpleServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import com.yh.bean.Person;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;

import javax.servlet.DispatcherType;
import java.util.EnumSet;


public class PracticeApplication extends Application<PracticeConfiguation>{

    private  HibernateBundle<PracticeConfiguation> hibernate;

    @Override
    public String getName(){
        return "post-data";
    }
    @Override
    public void initialize(Bootstrap<PracticeConfiguation> bootstrap) {
        hibernate = new HibernateBundle<PracticeConfiguation>(PracticeApplication.class, Person.class) {
            @Override
            public DataSourceFactory getDataSourceFactory(PracticeConfiguation configuration) {
                System.out.println(configuration.getDatabase().getUrl());
                return configuration.getDatabase();
            }
        };
        // 添加数据库资源bundle
        bootstrap.addBundle(hibernate);
        // 添加swagger静态资源bundle
        bootstrap.addBundle(new AssetsBundle("/api-doc", "/api-doc", "index.html", "api-doc"));
    }

    public void run(PracticeConfiguation practiceConfiguation, Environment environment) throws Exception {

        //初始化参数
        DbUtils.setSessionFactory(hibernate.getSessionFactory());
        ConfigUtils.setTEMPLATE(practiceConfiguation.getTemplate());
        ConfigUtils.setDafaultName(practiceConfiguation.getDefaultName());

        //初始化jersey
        JerseyEnvironment jersey = environment.jersey();
        jersey.register(new ApiListingResource());

        // 跨域
        environment.getApplicationContext().addFilter(org.eclipse.jetty.servlets.CrossOriginFilter.class, "/*",
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR));
        //初始化swagger ui
        initSwagger(practiceConfiguation, environment);

        // 注册资源接口
        // 注册包下的resources接口
        jersey.packages("com.yh.resource");
    }

    /**
     * 初始化swagger ui
     *
     * @param configuration
     * @param environment
     */
    private void initSwagger(PracticeConfiguation configuration, Environment environment) {
        environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // set swagger
        BeanConfig config = new BeanConfig();
        config.setTitle("practice Services");
        config.setVersion("1.0.0");
        config.setResourcePackage("com.yh.resource");
        SimpleServerFactory simpleServerFactory = (SimpleServerFactory) configuration.getServerFactory();
        String basePath = simpleServerFactory.getApplicationContextPath();

        // jdk 1.8 将String类型改为了Optional
        String rootPath = simpleServerFactory.getJerseyRootPath().get();
        rootPath = rootPath.substring(0, rootPath.indexOf(""));
        basePath = "/".equals(basePath) ? rootPath : (new StringBuilder()).append(basePath).append(rootPath).toString();
        config.setBasePath(basePath);
        config.setScan(true);
    }


    public static void main(String[] args) throws Exception {
//        "server", "F:\\test\\practice_parent\\practice_standalone\\target\\assembly\\practice\\conf\\conf.yml"
        new PracticeApplication().run(args);
    }
}
