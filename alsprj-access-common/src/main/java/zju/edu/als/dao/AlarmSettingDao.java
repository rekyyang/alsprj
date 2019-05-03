package zju.edu.als.dao;

import org.apache.ibatis.annotations.Param;
import zju.edu.als.domain.alarm.AlarmSetting;

import java.util.List;

/**
 * Created by zzq on 2016/11/2.
 */
public interface AlarmSettingDao {
    List<AlarmSetting> selectAllAlarmSettings();

    Boolean updateAlarmSettings(@Param("alarmSettingList") List<AlarmSetting> alarmSettingList);

    Boolean insertAlarmSetting(@Param("alarmSetting") AlarmSetting alarmSetting);
}
