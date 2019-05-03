package zju.edu.als.collector;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import zju.edu.als.alarm.AlarmCenter;
import zju.edu.als.constant.SurgeryState;
import zju.edu.als.dao.ALSDao;
import zju.edu.als.dao.GuardianDao;
import zju.edu.als.dao.SurgeryDao;
import zju.edu.als.domain.data.ALSData;
import zju.edu.als.domain.surgery.Surgery;
import zju.edu.als.monitor.ALSMonitor;
import zju.edu.als.monitor.GuardianMonitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by zzq on 2016/11/28.
 */
@Slf4j
public class ClientHandler implements Runnable{

    private GuardianDao guardianDao;
    private ALSMonitor alsMonitor;
    private GuardianMonitor guardianMonitor;

    private ALSDao alsDao;
    private SurgeryDao surgeryDao;
    private AlarmCenter alarmCenter;
    private Socket client;
    private String clientSurgeryNo = null;


    public ClientHandler(ALSDao alsDao, SurgeryDao surgeryDao, GuardianDao guardianDao, Socket socket){

    }

    public ClientHandler(ALSDao alsDao, SurgeryDao surgeryDao, GuardianDao guardianDao, ALSMonitor alsMonitor, GuardianMonitor guardianMonitor, AlarmCenter alarmCenter, Socket client) {
        this.alsDao=alsDao;
        this.surgeryDao = surgeryDao;
        this.guardianDao = guardianDao;
        this.alsMonitor = alsMonitor;
        this.guardianMonitor = guardianMonitor;
        this.alarmCenter=alarmCenter;
        this.client = client;
    }

    @Override
    public void run() {
        BufferedReader in= null;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream(),"utf-8"));
            while (true) {
                String dataStr = in.readLine();
                System.out.println(dataStr);
                if(dataStr==null){
                    if (clientSurgeryNo!=null) {
                        alarmCenter.sendAlarm(clientSurgeryNo, "服务器与人工肝的断连,请检查网络连接情况");
                    }
                    break;
                }
                try {
                    if (dataStr.contains("START")) {
                        Surgery surgery = JSONObject.parseObject(dataStr.substring(dataStr.indexOf("START")+6), Surgery.class);
                        surgery.setState(SurgeryState.EXECUTING.ordinal());
                        surgery.setStartTime(System.currentTimeMillis());
                        surgeryDao.startSurgery(surgery);
                        this.clientSurgeryNo = surgery.getSurgeryNo();
                    }
                    if (dataStr.startsWith("DATA")) {
                        ALSData alsData = JSONObject.parseObject(dataStr.substring(5), ALSData.class);
                        this.clientSurgeryNo = alsData.getSurgeryNo();
                        alsDao.insertALSData(alsData);
                    }
                    if (dataStr.startsWith("END")) {
                        Surgery surgery = JSONObject.parseObject(dataStr.substring(4), Surgery.class);
                        surgery.setState(SurgeryState.COMPLETE.ordinal());
                        surgery.setEndTime(System.currentTimeMillis());
                        surgeryDao.endSurgery(surgery);
                        this.clientSurgeryNo = surgery.getSurgeryNo();
                    }
                }catch (Exception e){
                    log.error("Data Handle Exception",e);
                }
            }
        } catch (IOException e) {
            log.error("Client Lost",e);
        }finally {
            try {
                in.close();
                client.close();
            } catch (IOException e) {

            }
        }

    }
}
