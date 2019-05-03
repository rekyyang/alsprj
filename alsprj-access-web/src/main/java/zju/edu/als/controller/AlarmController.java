package zju.edu.als.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zju.edu.als.domain.alarm.AlarmSetting;
import zju.edu.als.domain.result.Result;
import zju.edu.als.monitor.MonitorConfig;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zzq on 2016/11/2.
 */
@Controller
@Slf4j
@RequestMapping("/alarm")
public class AlarmController {

    @Resource(name = "monitorConfig")
    private MonitorConfig monitorConfig;

    @ModelAttribute("alarmSettingList")
    private List<AlarmSetting> getAlarmSettingList(HttpServletRequest request){
        String alarmSettingListStr=request.getParameter("alarmSettingList");
        if(alarmSettingListStr==null){
            return null;
        }
        List<AlarmSetting> alarmSettingList;
        try {
            alarmSettingList = JSONObject.parseObject(alarmSettingListStr, new TypeReference<List<AlarmSetting>>(){});
        }catch (Exception e){
            log.error("Invoke getAlarmSettingList JsonParseException ",e);
            return null;
        }
        return alarmSettingList;
    }
    @RequestMapping("/updateAlarmSetting")
    @ResponseBody
    public Result updateAlarmSettings(@ModelAttribute("alarmSettingList")List<AlarmSetting> alarmSettingList){
        return monitorConfig.updateAlarmSettings(alarmSettingList);
    }
    
    @RequestMapping("/getAll")
    @ResponseBody
    public Result getAll(){
        return monitorConfig.getAllSettings();   
    }

}
