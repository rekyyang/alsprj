package zju.edu.als.bridge;

import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zzq on 2016/11/28.
 */
@Component
public class Collector implements SmartLifecycle{

    private volatile boolean isRunning = false;
    private ExecutorService executorService;

    @Override
    public void start() {
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new ServerHandler(DataSenderManager.messages,9203));
        isRunning=true;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        callback.run();
        stop();
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getPhase() {
        return 1;
    }
}
