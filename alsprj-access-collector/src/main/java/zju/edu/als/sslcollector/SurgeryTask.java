package zju.edu.als.sslcollector;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zju.edu.als.alarm.AlarmCenter;
import zju.edu.als.constant.SurgeryState;
import zju.edu.als.dao.ALSDao;
import zju.edu.als.dao.SurgeryDao;
import zju.edu.als.domain.data.ALSData;
import zju.edu.als.domain.surgery.Surgery;


@Slf4j
@Component
@Setter
@Getter
public class SurgeryTask {

    private String surgeryNo;
    private boolean surgeryState;

    @Autowired
    private AlarmCenter alarmCenter;

    @Autowired
    private ALSDao alsDao;
    @Autowired
    private SurgeryDao surgeryDao;

    public SubTask newSubTask(String msg) {
        return new SubTask(msg);
    }

    class SubTask implements Runnable{

        private String msg;

        public SubTask(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            try {
                if (msg.contains("START")) {
                    log.info("item : {}",msg);
                    Surgery surgery = JSONObject.parseObject(msg.substring(msg.indexOf("START")+6), Surgery.class);
                    surgery.setState(SurgeryState.EXECUTING.ordinal());
                    surgery.setStartTime(System.currentTimeMillis());
                    surgeryDao.startSurgery(surgery);
                    surgeryState = true;
                    surgeryNo = surgery.getSurgeryNo();
                }
                if (msg.startsWith("DATA")) {
                    ALSData alsData = JSONObject.parseObject(msg.substring(5), ALSData.class);
                    surgeryNo = alsData.getSurgeryNo();
                    alsDao.insertALSData(alsData);
                }
                if (msg.startsWith("END")) {
                    log.info("item : {}",msg);
                    Surgery surgery = JSONObject.parseObject(msg.substring(4), Surgery.class);
                    surgery.setState(SurgeryState.COMPLETE.ordinal());
                    surgery.setEndTime(System.currentTimeMillis());
                    surgeryDao.endSurgery(surgery);
                    surgeryNo = surgery.getSurgeryNo();
                    surgeryState = false;
                }
            }catch (Exception e){
                log.error("Data Handle Exception",e);
            }
        }
    }
}
