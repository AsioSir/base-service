package com.asio.tools;

import com.asio.tools.loggerutils.LoggerUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author: asio
 * @time: 2021-07-28 20:19
 * 描述:  工具类模块测试
 */
@RunWith(TestSpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class BaseToolAppTest {
    @Test
    public void contextLoads() {
        System.out.println("===========  TEST START...  ==================");
    }

    @BeforeClass
    public static void beforeClass() {
        LoggerUtils.debug("-----------  beforeClass  ------------");
//        System.setProperty(ProjectConfigSupport.PROJECT_CONFIG_DIR, "D:/Idea_Work_Space/base-service/base-tools/src/main/deploy-conf");
    }

    @AfterClass
    public static void afterClass() {
        LoggerUtils.debug("-----------  afterClass  ------------");
//        System.setProperty(ProjectConfigSupport.PROJECT_CONFIG_DIR, "D:/Idea_Work_Space/base-service/base-tools/src/main/deploy-conf");
    }

}
