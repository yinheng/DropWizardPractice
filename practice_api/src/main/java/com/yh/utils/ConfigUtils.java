package com.yh.utils;

public class ConfigUtils {

    public static String TEMPLATE;
    public static String DAFAULT_NAME;

    public static String getTEMPLATE() {
        return TEMPLATE;
    }

    public static void setTEMPLATE(String TEMPLATE) {
        ConfigUtils.TEMPLATE = TEMPLATE;
    }

    public static String getDafaultName() {
        return DAFAULT_NAME;
    }

    public static void setDafaultName(String dafaultName) {
        DAFAULT_NAME = dafaultName;
    }
}
