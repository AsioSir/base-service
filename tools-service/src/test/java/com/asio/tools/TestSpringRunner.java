package com.asio.tools;

import com.asio.tools.configutils.ProjectConfigSupport;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author: asio
 * @time: 2021-07-28 20:17
 * 描述:  测试启动方法
 */
public class TestSpringRunner extends SpringJUnit4ClassRunner {

    public TestSpringRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
        ProjectConfigSupport.initConfigs(clazz, "deploy-conf", "logback.xml");
    }
}
