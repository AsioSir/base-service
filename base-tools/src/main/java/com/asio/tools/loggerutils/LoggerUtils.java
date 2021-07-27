package com.asio.tools.loggerutils;

import org.slf4j.LoggerFactory;

/**
 * @author: asio
 * @time: 2021-07-26 14:18
 * 描述: 日志工具类  需要引入slf4j-api、slf4j-nop、slf4j-log4j12
 */
public class LoggerUtils {

    /*
        error错误日志
     */
    public static void error(String msg) {
        LoggerFactory.getLogger(getClassName()).error(msg);
    }

    public static void error(String msg, Object...obj) {
        LoggerFactory.getLogger(getClassName()).error(msg, obj);
    }

    public static void error(String msg, Throwable e) {
        LoggerFactory.getLogger(getClassName()).error(msg, e);
    }

    //是否error
    public static boolean isErrorEnabled() {
        return LoggerFactory.getLogger(getClassName()).isErrorEnabled();
    }

    /*
        warn警告日志
     */
    public void warn(String msg) {
        LoggerFactory.getLogger(getClassName()).warn(msg);
    }

    public static void warn(String msg, Object...obj) {
        LoggerFactory.getLogger(getClassName()).warn(msg, obj);
    }

    public static void warn(String msg, Throwable e) {
        LoggerFactory.getLogger(getClassName()).warn(msg, e);
    }

    //是否warn
    public static boolean isWarnEnabled() {
        return LoggerFactory.getLogger(getClassName()).isWarnEnabled();
    }


    /*
      debug调试日志
   */
    public void debug(String msg) {
        LoggerFactory.getLogger(getClassName()).debug(msg);
    }

    public static void debug(String msg, Object...obj) {
        LoggerFactory.getLogger(getClassName()).debug(msg, obj);
    }

    public static void debug(String msg, Throwable e) {
        LoggerFactory.getLogger(getClassName()).debug(msg, e);
    }

    //是否debug
    public static boolean isDebugEnabled() {
        return LoggerFactory.getLogger(getClassName()).isDebugEnabled();
    }


    /*
        info信息日志
     */
    public void info(String msg) {
        LoggerFactory.getLogger(getClassName()).info(msg);
    }

    public static void info(String msg, Object...obj) {
        LoggerFactory.getLogger(getClassName()).info(msg, obj);
    }

    public static void info(String msg, Throwable e) {
        LoggerFactory.getLogger(getClassName()).info(msg, e);
    }

    //是否info
    public static boolean isInfoEnabled() {
        return LoggerFactory.getLogger(getClassName()).isInfoEnabled();
    }

    //获取日志对象的class
    private static String getClassName() {
        return new SecurityManager() {
            public String getClassName() {
                return getClassContext()[3].getName();
            }
        }.getClassName();
    }

}
