package com.yh.utils;

import org.hibernate.SessionFactory;

public class DbUtils {

    public static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void setSessionFactory(SessionFactory sessionFactory) {
        DbUtils.sessionFactory = sessionFactory;
    }



}
