package zju.edu.als.monitor;

import org.springframework.stereotype.Component;
import zju.edu.als.domain.alarm.AlarmSetting;
import zju.edu.als.domain.data.ALSData;
import zju.edu.als.domain.data.DataBase;
import zju.edu.als.domain.data.ALSData;

/**
 * Created by zzq on 2016/11/27.
 */
@Component
public class ALSMonitor extends BaseMonitor{
    @Override
    protected void preHandle(DataBase dataBase) {

    }

    @Override
    protected void monitorVerify(DataBase dataBase) {
        StringBuilder alarmMessage=new StringBuilder();
        if (dataBase instanceof ALSData) {
            AlarmSetting dPaccAlarmSetting = monitorConfig.getAlarmSetting("dPacc");
            AlarmSetting dPartAlarmSetting = monitorConfig.getAlarmSetting("dPart");
            AlarmSetting dPvenAlarmSetting = monitorConfig.getAlarmSetting("dPven");
            AlarmSetting dP1stAlarmSetting = monitorConfig.getAlarmSetting("dP1st");
            AlarmSetting dTMPAlarmSetting = monitorConfig.getAlarmSetting("dTMP");
            AlarmSetting dP2ndAlarmSetting = monitorConfig.getAlarmSetting("dP2nd");
            AlarmSetting dP3rdAlarmSetting = monitorConfig.getAlarmSetting("dP3rd");
            AlarmSetting iBPSpeedAlarmSetting = monitorConfig.getAlarmSetting("iBPSpeed");
            AlarmSetting iFPSpeedAlarmSetting = monitorConfig.getAlarmSetting("iFPSpeed");
            AlarmSetting iDPSpeedAlarmSetting = monitorConfig.getAlarmSetting("iDPSpeed");
            AlarmSetting iRPSpeedAlarmSetting = monitorConfig.getAlarmSetting("iRPSpeed");
            AlarmSetting iFP2SPeedAlarmSetting = monitorConfig.getAlarmSetting("iFP2SPeed");
            AlarmSetting iCPSpeedAlarmSetting = monitorConfig.getAlarmSetting("iCPSpeed");
            AlarmSetting iSPSpeedAlarmSetting = monitorConfig.getAlarmSetting("iSPSpeed");
            AlarmSetting warmerAlarmSetting = monitorConfig.getAlarmSetting("warmer");
            checkThreshold(dPaccAlarmSetting,((ALSData) dataBase).getdPacc(),alarmMessage);
            checkThreshold(dPartAlarmSetting,((ALSData) dataBase).getdPart(),alarmMessage);
            checkThreshold(dPvenAlarmSetting,((ALSData) dataBase).getdPven(),alarmMessage);
            checkThreshold(dP1stAlarmSetting,((ALSData) dataBase).getdP1st(),alarmMessage);
            checkThreshold(dTMPAlarmSetting,((ALSData) dataBase).getdTMP(),alarmMessage);
            checkThreshold(dP2ndAlarmSetting,((ALSData) dataBase).getdP2nd(),alarmMessage);
            checkThreshold(dP3rdAlarmSetting,((ALSData) dataBase).getdP3rd(),alarmMessage);
            checkThreshold(iBPSpeedAlarmSetting,((ALSData) dataBase).getiBPSpeed(),alarmMessage);
            checkThreshold(iFPSpeedAlarmSetting,((ALSData) dataBase).getiFPSpeed(),alarmMessage);
            checkThreshold(iDPSpeedAlarmSetting,((ALSData) dataBase).getiDPSpeed(),alarmMessage);
            checkThreshold(iRPSpeedAlarmSetting,((ALSData) dataBase).getiRPSpeed(),alarmMessage);
            checkThreshold(iFP2SPeedAlarmSetting,((ALSData) dataBase).getiFP2SPeed(),alarmMessage);
            checkThreshold(iCPSpeedAlarmSetting,((ALSData) dataBase).getiCPSpeed(),alarmMessage);
            checkThreshold(iSPSpeedAlarmSetting,((ALSData) dataBase).getiSPSpeed(),alarmMessage);
            checkThreshold(warmerAlarmSetting,((ALSData) dataBase).getWarmer(),alarmMessage);
            if(alarmMessage.length()>0){
                alarmCenter.sendAlarm(dataBase.getSurgeryNo(),alarmMessage.toString());
            }
        }
    }

    @Override
    protected void postHandle(DataBase dataBase) {

    }
}
