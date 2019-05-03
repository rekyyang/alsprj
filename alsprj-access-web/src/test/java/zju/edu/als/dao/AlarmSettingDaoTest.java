package zju.edu.als.dao;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import zju.edu.als.domain.alarm.AlarmSetting;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzq on 2016/11/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class AlarmSettingDaoTest {
    @Resource
    private AlarmSettingDao alarmSettingDao;
    @Test
    public void selectAllTest(){
        System.out.println(JSONObject.toJSONString(alarmSettingDao.selectAllAlarmSettings()));
    }
    @Test
    public void insertAlarmSettingTest(){
        AlarmSetting  alarmSetting = new AlarmSetting();
        alarmSetting.setAlarmItem("kk");
        alarmSetting.setCeiling(100);
        alarmSetting.setFloor(100);
        alarmSetting.setNote("");
        alarmSetting.setType(4);
        alarmSettingDao.insertAlarmSetting(alarmSetting);
    }

    @Test
    public void updateAlarmSettingTest(){
        AlarmSetting  alarmSetting = new AlarmSetting();
        alarmSetting.setAlarmItem("heartRate");
        alarmSetting.setCeiling(100);
        alarmSetting.setFloor(100);
        alarmSetting.setNote("");
        alarmSetting.setType(4);
        List<AlarmSetting> alarmSettingList = new ArrayList<>();
        alarmSettingList.add(alarmSetting);
        alarmSettingDao.updateAlarmSettings(alarmSettingList);
    }
}
