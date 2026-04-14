package com.gaisoft.framework.manager;

import com.gaisoft.common.utils.Threads;
import com.gaisoft.common.utils.spring.SpringUtils;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AsyncManager {
    private final int OPERATE_DELAY_TIME = 10;
    private ScheduledExecutorService executor = (ScheduledExecutorService)SpringUtils.getBean((String)"scheduledExecutorService");
    private static AsyncManager me = new AsyncManager();

    private AsyncManager() {
    }

    public static AsyncManager me() {
        return me;
    }

    public void execute(TimerTask task) {
        this.executor.schedule(task, 10L, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        Threads.shutdownAndAwaitTermination((ExecutorService)this.executor);
    }
}
