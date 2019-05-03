package zju.edu.als.collector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import zju.edu.als.alarm.AlarmCenter;
import zju.edu.als.dao.ALSDao;
import zju.edu.als.dao.GuardianDao;
import zju.edu.als.dao.SurgeryDao;
import zju.edu.als.monitor.ALSMonitor;
import zju.edu.als.monitor.GuardianMonitor;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zzq on 2016/11/28.
 */
@Slf4j
@Component
public class Collector implements SmartLifecycle{

    private ExecutorService executorService;
    @Resource
    private ALSDao alsDao;
    @Resource
    private SurgeryDao surgeryDao;
    @Resource
    private GuardianDao guardianDao;
    @Resource
    private AlarmCenter alarmCenter;
    @Resource
    private ALSMonitor alsMonitor;
    @Resource
    private GuardianMonitor guardianMonitor;
    private volatile boolean isRunning=false;

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
    public void start() {
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new ServerHandler(alsDao, surgeryDao, guardianDao, alsMonitor, guardianMonitor, alarmCenter, 9204));
        isRunning=true;
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
