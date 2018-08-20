package com.modelink.admin.service.impl;

import com.modelink.admin.bean.ExceptionLogger;
import com.modelink.admin.service.ExceptionLoggerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ExceptionLoggerServiceImplTest {

    @Resource
    private ExceptionLoggerService exceptionLoggerService;

    @Test
    public void save() {
        ExceptionLogger exceptionLogger = new ExceptionLogger();
        exceptionLogger.setLoggerDate("2000-10-10");
        exceptionLogger.setLoggerKey("上海");
        exceptionLogger.setLoggerType("flowArea");
        exceptionLogger.setLoggerDesc("该城市不存在");
        exceptionLoggerService.save(exceptionLogger);
    }

    @Test
    public void update() {
    }

    @Test
    public void findOneByParam() {
    }

    @Test
    public void findListByParam() {
    }
}