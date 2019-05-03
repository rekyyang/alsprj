package zju.edu.als.monitor;

import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import zju.edu.als.dao.AlarmSettingDao;
import zju.edu.als.domain.alarm.AlarmSetting;
import zju.edu.als.domain.result.Result;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zzq on 2016/11/2.
 */
@Component
public class MonitorConfig implements SmartLifecycle{

    @Resource(name = "alarmSettingDao")
    private AlarmSettingDao alarmSettingDao;

    private volatile boolean isRunning = false;
    private ConcurrentHashMap<String,AlarmSetting> alarmSettings;
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
        alarmSettings=new ConcurrentHashMap<>();
        List<AlarmSetting> alarmSettingList=alarmSettingDao.selectAllAlarmSettings();
        for (AlarmSetting alarmSetting:alarmSettingList
             ) {
            if(!alarmSettings.containsKey(alarmSetting.getAlarmItem()))
            alarmSettings.put(alarmSetting.getAlarmItem(),alarmSetting);
        }
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
        return 0;
    }
    public AlarmSetting getAlarmSetting(String alarmItem){
        return alarmSettings.get(alarmItem);
    }
    public Result updateAlarmSettings(List<AlarmSetting> alarmSettingList){
        List<AlarmSetting> alarmSettingWillUpdate = new ArrayList<>();
        for (AlarmSetting  alarmSetting:alarmSettingList) {
            if(alarmSettings.containsKey(alarmSetting.getAlarmItem())){
                alarmSettingWillUpdate.add(alarmSetting);
            }
        }
        if(alarmSettingWillUpdate.size()==0){
            return Result.fail("Null Point");
        }
        try {
            alarmSettingDao.updateAlarmSettings(alarmSettingWillUpdate);
            for (AlarmSetting alarmSetting:alarmSettingWillUpdate
                 ) {
                alarmSettings.put(alarmSetting.getAlarmItem(),alarmSetting);
            }
            return Result.ok();
        }catch (Exception e){
            return Result.fail(e);
        }

    }
    public Result insertAlarmSetting(AlarmSetting alarmSetting){
        if(alarmSettings.containsKey(alarmSetting.getAlarmItem())){
            return Result.fail(alarmSetting.getAlarmItem()+" has exist");
        }
        try {
            alarmSettingDao.insertAlarmSetting(alarmSetting);
            alarmSettings.put(alarmSetting.getAlarmItem(),alarmSetting);
            return Result.ok();
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }
    public Result getAllSettings(){
        return Result.ok(alarmSettings);
    }
}
