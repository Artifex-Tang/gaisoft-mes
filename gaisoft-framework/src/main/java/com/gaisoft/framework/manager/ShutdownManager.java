package com.gaisoft.framework.manager;

import com.gaisoft.framework.manager.AsyncManager;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ShutdownManager {
    private static final Logger logger = LoggerFactory.getLogger((String)"sys-user");

    @PreDestroy
    public void destroy() {
        this.shutdownAsyncManager();
    }

    private void shutdownAsyncManager() {
        try {
            logger.info("====关闭后台任务任务线程池====");
            AsyncManager.me().shutdown();
        }
        catch (Exception e) {
            logger.error(e.getMessage(), (Throwable)e);
        }
    }
}
