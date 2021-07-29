package com.asio.tools.configutils;

import com.alibaba.fastjson.util.IOUtils;
import com.asio.tools.loggerutils.LoggerUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

/**
 * @author: asio
 * @time: 2021-07-28 20:25
 * 描述:  项目配置加载 可指定配置文件目录(main文件夹下，默认配置都在配置文件目录下)
 */
public class ProjectConfigSupport {

    //项目property的key
    public static final String PROJECT_DIR = "project.dir";

    //项目配置property的key
    public static final String PROJECT_CONFIG_DIR = "project.config.dir";

    //日志配置property的key
    private static final String LOGGER_CONFIG_DIR = "logger.config";

    //日志文件存放目录property的key
    private static final String PROJECT_LOGS_DIR = "project.logs.dir";

    //rocketMq日志打印配置
    private static final String ROCKETMQ_CLIENT_LOG_LOAD_CONFIG = "rocketmq.client.log.loadconfig";

    //默认配置文件地址
    public static final String DEFAULT_CONFIG_RELATIVE_DIR = "/scr/main/deploy-conf";

    //默认apollo文件名
    private static final String DEFAULT_APOLLO_CONFIG_DIR = "/apollo-service.properties";


    /**
     * Description: 初始化配置
     * @author asio
     * @time 2021-07-28 21:15
     * @param applicationClazz 应用class,
     * @param configDirName 配置文件目录,
     * @param loggerConfigDirName 日志文件名,
     */
    public static <E> void initConfigs(Class<E> applicationClazz, String configDirName, String loggerConfigDirName) {
        checkProjectConfigDir(applicationClazz, configDirName, loggerConfigDirName);
        String appName = getAppName(applicationClazz);
        checkProjectLogsDir("/tmp/logs" + appName);
    }

    private static <E> String getAppName(Class<E> applicationClazz) {
        //根据Spring主类生产一个App名称（不一定十分准确）
        String appName = applicationClazz.getName();
        appName = appName.substring(appName.lastIndexOf('.') + 1);
        appName = appName.replaceAll("Application", "");
        String[] nameSplits = StringUtils.splitByCharacterTypeCamelCase(appName);
        appName = StringUtils.join(nameSplits, "-");
        appName = appName.toLowerCase();
        return appName;
    }

    /**
     * Description: 检查日志存放位置
     * @author asio
     * @time 2021-07-28 21:29
     * @param defaultProjectLogsDir 默认日志存放位置
     */
    private static void checkProjectLogsDir(String defaultProjectLogsDir) {
        String projectLogsDir = System.getProperty(PROJECT_LOGS_DIR);
        if (StringUtils.isBlank(projectLogsDir)) {
            projectLogsDir = defaultProjectLogsDir;
            Path path = Paths.get(projectLogsDir);
            projectLogsDir = path.toAbsolutePath().toString();
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                LoggerUtils.error("检查日志目录异常：" , e);
            }
            System.setProperty(PROJECT_LOGS_DIR, projectLogsDir);
        }
        LoggerUtils.debug("默认的日志目录为：{} = {}",PROJECT_LOGS_DIR , projectLogsDir);
    }

    /**
     * Description: 检查配置文件地址
     * @author asio
     * @time 2021-07-28 20:28
     */
    private static <E>  void checkProjectConfigDir(Class<E> clazz, String configDirName, String loggerConfigDirName) {
        String projectConfigDir = System.getProperty(PROJECT_CONFIG_DIR);
        if (StringUtils.isBlank(projectConfigDir)) {
            String classDir = Objects.requireNonNull(clazz.getClassLoader().getResource("")).getPath();
            //本地开发自动根据maven的target目录计算外部配置目录位置
            int pathIndex = classDir.lastIndexOf("/target");
            if (pathIndex > 0) {
                //计算项目模块层目录
                String projectDir = classDir.substring(0, pathIndex);
                if (projectDir.startsWith("/")) {
                    projectDir = projectDir.substring(1);
                }
                if (projectDir.startsWith("file:/")) {
                    projectDir = projectDir.substring(6);
                }
                //设置模块目录，提供给后面Tomcat查找DocumentRoot使用
                System.setProperty(PROJECT_DIR, projectDir);
                if (StringUtils.isNotEmpty(configDirName)) {
                    projectConfigDir = projectDir + "/scr/main/" + configDirName;
                } else {
                    projectConfigDir = projectDir + DEFAULT_CONFIG_RELATIVE_DIR;
                }
            } else {
                projectConfigDir = new File("conf").getAbsolutePath();
            }
            System.setProperty(PROJECT_CONFIG_DIR, projectConfigDir);
            LoggerUtils.debug("自动设置外部配置目录[project.config.dir] : " + projectConfigDir);
        } else {
            LoggerUtils.debug("外部配置目录[project.config.dir] : " + projectConfigDir);
        }
        String loggerConfigDir = projectConfigDir + "/" + loggerConfigDirName;
        loadLoggerConfig(loggerConfigDir);

        //设置不打印rocketMQ日志信息
        System.setProperty(ROCKETMQ_CLIENT_LOG_LOAD_CONFIG, "false");
        LoggerUtils.debug("设置打印rocketMQ日志信息: false");

        //加载apollo配置
        Properties apolloServiceProps = loadApolloServiceProps(projectConfigDir);
        if (apolloServiceProps.size() > 0) {
            setApolloServerConfig(apolloServiceProps);
        }
    }

    /**
     * Description: 设置apollo配置属性（不同环境）
     * @author asio
     * @time 2021-07-28 21:11
     * @param properties 配置属性
     */
    private static void setApolloServerConfig(Properties properties) {
        setApolloServerConfigItem("dev_meta", properties);
        setApolloServerConfigItem("fat_meta", properties);
        setApolloServerConfigItem("uta_meta", properties);
        setApolloServerConfigItem("apollo.cluster", properties);
        setApolloServerConfigItem("env", properties);
    }

    private static void setApolloServerConfigItem(String propertyName, Properties properties) {
        if (StringUtils.isEmpty(System.getProperty(propertyName)) && StringUtils.isNotEmpty(properties.getProperty(propertyName))) {
            System.setProperty(propertyName, properties.getProperty(propertyName));
        }
    }


    /**
     * Description: 加载apollo配置
     * @author asio
     * @time 2021-07-28 21:09
     * @param projectConfigDir 配置地址
     */
    private static Properties loadApolloServiceProps(String projectConfigDir) {
        //加载classPath
        Properties properties = new Properties();
        FileInputStream inputStream = null;
        String path = projectConfigDir + DEFAULT_APOLLO_CONFIG_DIR;
        try {
            inputStream = new FileInputStream(path);
            properties.load(inputStream);
            LoggerUtils.debug("加载Apollo配置成功：path=", path);
        } catch (FileNotFoundException e) {
            LoggerUtils.error("加载Apollo配置失败，未发现Apollo配置文件：path = {}" + path, e);
        } catch (Throwable e) {
            LoggerUtils.error("读取Apollo配置失败：path = {}" + path, e);
        } finally {
            IOUtils.close(inputStream);
        }
        return properties;
    }

    /**
     * Description: 加载日志配置
     * @author asio
     * @time 2021-07-28 20:56
     * @param loggerConfigDir 日志配置目录
     */
    private static void loadLoggerConfig(String loggerConfigDir) {
        String oldLoggerConfig = System.getProperty(LOGGER_CONFIG_DIR);
        if (StringUtils.isBlank(oldLoggerConfig)) {
            System.setProperty(LOGGER_CONFIG_DIR, loggerConfigDir);
        }
        LoggerUtils.debug(LOGGER_CONFIG_DIR + " : " + loggerConfigDir);
    }

}
