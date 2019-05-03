package zju.edu.als.alarm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import zju.edu.als.dao.SurgeryDao;
import zju.edu.als.domain.surgery.Surgery;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzq on 2016/11/4.
 */
@Component
@Slf4j
public class AlarmCenter {
    @Resource(name = "surgeryDao")
    private SurgeryDao surgeryDao;
    public  void sendAlarm(String surgeryNo,String message){
        try {
            Surgery surgery=surgeryDao.selectSurgeryBySurgeryNo(surgeryNo);
            String[] persons = surgery.getAlarmPerson().split(",");
            for (String person:
                 persons) {
                SMSClient.sendMessage(person,surgery.getPatient()+" "+message);
            }
        }catch (Exception e){
            log.error("Invoke sendAlarm selectSurgery Failed",e);
        }
    }

    public void sendAlarms(List<String> surgeryNoList,String message){
        List<Surgery> surgeryList=new ArrayList<>();
        try {
            surgeryList= surgeryDao.selectExecutingSurgeryBySurgeryNoList(surgeryNoList);
        }catch (Exception e){
            log.error(" Invoke sendAlarms selectExecutingSurgeryBySurgeryNoList Failed",e);
        }
        if(CollectionUtils.isEmpty(surgeryList)){
            return;
        }
        for (Surgery surgery:surgeryList) {
            String[] persons = surgery.getAlarmPerson().split(",");
            for(String person:persons){
                SMSClient.sendMessage(person,surgery.getPatient()+" "+message);
            }
        }
    }
}
