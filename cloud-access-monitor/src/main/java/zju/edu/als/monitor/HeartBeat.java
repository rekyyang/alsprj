package zju.edu.als.monitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import zju.edu.als.alarm.AlarmCenter;
import zju.edu.als.domain.data.DataBase;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static zju.edu.als.alarm.AlarmConfig.HEARTBEAT_SENSITIVITY;

/**
 * Created by root on 16-11-23.
 */
@Component
@Slf4j
public class HeartBeat implements SmartLifecycle{

    @Resource
    private AlarmCenter alarmCenter;

    private TreeSet<DataBase> surgeryHeartbeatSet;
    private ReentrantLock lock;
    private Condition available;
    private ExecutorService executorService;
    private ConcurrentHashMap<String,DataBase> surgeryHeartbeatMap;

    private volatile boolean isRunning = false;
    public void check(DataBase dataBase){
        try {
            if (surgeryHeartbeatMap.containsKey(dataBase.getSurgeryNo())) {
                DataBase last = surgeryHeartbeatMap.get(dataBase.getSurgeryNo());
                if(last.getTimestamp()<dataBase.getTimestamp()) {
                    surgeryHeartbeatSet.remove(last);
                    surgeryHeartbeatSet.add(dataBase);
                    surgeryHeartbeatMap.put(dataBase.getSurgeryNo(), dataBase);
                    signalDetector(dataBase);
                }
            }else {
                surgeryHeartbeatSet.add(dataBase);
                surgeryHeartbeatMap.put(dataBase.getSurgeryNo(), dataBase);
                signalDetector(dataBase);
            }
        }catch (Exception e) {
            log.error("Invoke heatBeatCheck Failed", e);
        }
    }
    public  void signalDetector(DataBase dataBase){
        if(dataBase!=null&&dataBase.equals(surgeryHeartbeatSet.first())) {
            lock.lock();
            try {
                available.signal();
            } catch (IllegalMonitorStateException e) {
                log.error("Signal Failed", e);
            } finally {
                lock.unlock();
            }
        }
    };

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable runnable) {
        runnable.run();
        stop();
    }

    @Override
    public void start() {
        lock= new ReentrantLock();

        available = lock.newCondition();

        executorService = Executors.newFixedThreadPool(3);

        surgeryHeartbeatSet = new TreeSet<>(new Comparator<DataBase>() {
            @Override
            public int compare(DataBase db1, DataBase db2) {
                if(db1.getTimestamp()>db2.getTimestamp()){
                    return 1;
                }else if(db1.getTimestamp()==db2.getTimestamp()){
                    return db1.getSurgeryNo().compareTo(db2.getSurgeryNo());
                }else {
                    return -1;
                }
            }
        });
        surgeryHeartbeatMap = new ConcurrentHashMap<>();
        executorService.execute(new DetectController());
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

    class DetectController implements Runnable {
        public void run() {
            lock.lock();
            try {
                while (true) {
                    try {
                        if (surgeryHeartbeatSet==null||surgeryHeartbeatSet.size() == 0) {
                            available.await();
                        } else {
                            long currentTime = System.currentTimeMillis();
                            Map<String, DataBase> detectMap = new HashMap<>();
                            long delay=0;
                            while (surgeryHeartbeatSet.size() > 0) {
                                DataBase dataBase = surgeryHeartbeatSet.first();
                                delay = dataBase.getTimestamp() - currentTime + HEARTBEAT_SENSITIVITY;
                                if (delay <= 0) {
                                    DataBase dataBasePoll = surgeryHeartbeatSet.pollFirst();
                                    detectMap.put(dataBasePoll.getSurgeryNo(), dataBasePoll);
                                }else {
                                    break;
                                }
                            }
                            finishDetect(detectMap);
                            if(delay > 0) {
                                available.await(delay, TimeUnit.MILLISECONDS);
                            }
                        }
                    } catch (InterruptedException e) {
                        log.info("Class :DetectorController Interrupted", e);
                        break;
                    }
                }
            } catch (Exception e) {
                log.info("Class :DetectorController Exception", e);
            } finally {
                lock.unlock();
            }
        }
    }
    class DetectWorker implements Runnable{
        Map<String,DataBase> detectMap;
        public DetectWorker(Map<String,DataBase> detectMap){
            this.detectMap=detectMap;
        }
        @Override
        public void run() {
            if(CollectionUtils.isEmpty(detectMap)){
                return;
            }
            List<String> surgeryNoList = new ArrayList<>();
            for(DataBase dataBase:detectMap.values()){
                surgeryNoList.add(dataBase.getSurgeryNo());
            }
            alarmCenter.sendAlarms(surgeryNoList,"服务器长时间没有收到人工肝的信息,请检查网络连接情况");
        }
    }

    private void finishDetect(Map<String,DataBase> detectMap) {
        DetectWorker detectWorker = new DetectWorker(detectMap);
        executorService.execute(detectWorker);
    }
}
