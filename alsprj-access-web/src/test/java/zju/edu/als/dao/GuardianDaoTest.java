package zju.edu.als.dao;

import com.alibaba.fastjson.JSONObject;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.series.Line;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import zju.edu.als.domain.data.GuardianData;
import zju.edu.als.domain.data.GuardianData;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzq on 2016/11/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class GuardianDaoTest {
    @Resource
    private GuardianDao guardianDao;

    @Test
    public void insertTest(){
        for(int i =0;i<100;i++) {
            GuardianData guardianData = new GuardianData();
            guardianData.setSurgeryNo("161202162548");
            guardianData.setTimestamp(System.currentTimeMillis()+i*1000);
            guardianData.setBloodOxygen(75 + (int) (Math.random() * 5));
            guardianData.setDiastolicPressure(80+ (int) (Math.random() * 5));
            guardianData.setHeartRate(80 + (int) (Math.random() * 5));
            guardianData.setSystolicPressure(130 + (int) (Math.random() * 5));
            guardianDao.insertGuardianData(guardianData);
        }
    }

    @Test
    public void batchInsertTest(){
        GuardianData  guardianData2 = new GuardianData();
        guardianData2.setSurgeryNo("19920327");
        guardianData2.setTimestamp(System.currentTimeMillis());
        guardianData2.setDiastolicPressure(2);
        guardianData2.setSystolicPressure(2);
        guardianData2.setHeartRate(2);
        guardianData2.setBloodOxygen(2);
        GuardianData  guardianData3 = new GuardianData();
        guardianData3.setSurgeryNo("19920327");
        guardianData3.setTimestamp(System.currentTimeMillis());
        guardianData3.setHeartRate(3);
        guardianData3.setBloodOxygen(3);
        guardianData3.setDiastolicPressure(3);
        guardianData3.setSystolicPressure(3);
        List<GuardianData> guardianDataList = new ArrayList<>();
        guardianDataList.add(guardianData2);
        guardianDataList.add(guardianData3);

        guardianDao.batchInsertGuardianData(guardianDataList);
    }
    @Test
    public void selectBySurgeryNoTest(){
        List<GuardianData> guardianDataList = guardianDao.selectGuardianDataBySurgeryNo("19920327");
        System.out.println(JSONObject.toJSON(guardianDataList));
    }

    @Test
    public void selectBySurgeryNoWithTimeRangeTest(){
        List<GuardianData> guardianDataList = guardianDao.selectGuardianDataBySurgeryNoWithTimeRange("19920327",new Long("1478683015393"),new Long("1478683015396"));
        System.out.println(JSONObject.toJSON(guardianDataList));
    }
    @Test
    public void test(){
    }
}
