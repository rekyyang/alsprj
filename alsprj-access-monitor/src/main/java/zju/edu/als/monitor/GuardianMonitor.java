package zju.edu.als.monitor;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import zju.edu.als.domain.alarm.AlarmSetting;
import zju.edu.als.domain.data.DataBase;
import zju.edu.als.domain.data.GuardianData;

/**
 * Created by zzq on 2016/10/29.
 */
@Component
public class GuardianMonitor extends BaseMonitor{

    @Override
    protected void preHandle(DataBase dataBase) {

    }

    @Override
    protected void monitorVerify(DataBase dataBase) {
        StringBuilder alarmMessage=new StringBuilder();
        if(dataBase instanceof GuardianData){
            AlarmSetting heartRateAlarmSetting=monitorConfig.getAlarmSetting("heartRate");
            AlarmSetting systolicPressureAlarmSetting=monitorConfig.getAlarmSetting("systolicPressure");
            AlarmSetting diastolicPressureAlarmSetting=monitorConfig.getAlarmSetting("diastolicPressure");
            AlarmSetting bloodOxygenAlarmSetting=monitorConfig.getAlarmSetting("bloodOxygen");
            checkThreshold(heartRateAlarmSetting,((GuardianData) dataBase).getHeartRate(),alarmMessage);
            checkThreshold(systolicPressureAlarmSetting,((GuardianData) dataBase).getSystolicPressure(),alarmMessage);
            checkThreshold(diastolicPressureAlarmSetting,((GuardianData) dataBase).getDiastolicPressure(),alarmMessage);
            checkThreshold(bloodOxygenAlarmSetting,((GuardianData) dataBase).getBloodOxygen(),alarmMessage);
       }
       if(alarmMessage.length()>0){
           alarmCenter.sendAlarm(dataBase.getSurgeryNo(),alarmMessage.toString());
       }
    }

    @Override
    protected void postHandle(DataBase dataBase) {

    }
}
