package org.fatmansoft.teach;

import org.fatmansoft.teach.service.SystemService;
import org.fatmansoft.teach.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * SystemApplicationListener 系统应用实践处理程序
 */
@Component
@Order(0)
public class SystemApplicationListener implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger logger = LoggerFactory.getLogger(SpringBootSecurityJwtApplication.class);
    @Autowired
    private SystemService systemService;  //系统服务对象自动注入
    @Autowired
    private TestService testService;

    /**
     * 系统实践处理方法 系统启动后自动加载数据字典
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("SystemInitStart");
        systemService.initDictionary();
        testService.test();
        logger.info("systemInitEnd");
    }

}