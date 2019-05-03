package zju.edu.als.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zju.edu.als.constant.SurgeryState;
import zju.edu.als.dao.SurgeryDao;
import zju.edu.als.domain.result.Result;
import zju.edu.als.domain.surgery.Surgery;
import zju.edu.als.util.DateFormatUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static zju.edu.als.util.DateFormatUtil.DATE_TIME;

/**
 * Created by zzq on 2016/10/29.
 */
@Controller
@Slf4j
@RequestMapping("/surgery")
public class SurgeryController {
    @Resource
    private SurgeryDao surgeryDao;
    @ModelAttribute("surgery")
    public Surgery getSurgery(HttpServletRequest request){
        String surgeryStr = request.getParameter("surgery");
        if(surgeryStr==null){
            return null;
        }
        try {
            Surgery surgery = JSONObject.parseObject(surgeryStr, Surgery.class);
            log.info("Invoke getSurgery"+JSONObject.toJSONString(surgery));
            return surgery;
        }catch (Exception e){
            log.error("Invoke getSurgery JsonParseException ",e);
            return null;
        }

    }

    @RequestMapping("/{surgeryNo}/start")
    @ResponseBody
    public Result start(@PathVariable("surgeryNo")String surgeryNo){
        Surgery surgery = new Surgery();
        surgery.setSurgeryNo(surgeryNo);
        surgery.setState(SurgeryState.EXECUTING.ordinal());
        surgery.setStartTime(System.currentTimeMillis());
        try{
            Boolean success=surgeryDao.startSurgery(surgery);
            if(success==true){
                return Result.ok();
            }else {
                return Result.fail("Not exist");
            }
        }catch (Exception e){
            return Result.fail(e);
        }
    }

    @RequestMapping("/{surgeryNo}/end")
    @ResponseBody
    public Result end(@PathVariable("surgeryNo")String surgeryNo){
        Surgery surgery = new Surgery();
        surgery.setSurgeryNo(surgeryNo);
        surgery.setState(SurgeryState.COMPLETE.ordinal());
        surgery.setEndTime(System.currentTimeMillis());
        try{
            Boolean success=surgeryDao.endSurgery(surgery);
            if(success==true){
                return Result.ok();
            }else {
                return Result.fail("Not exist");
            }
        }catch (Exception e){
            return Result.fail(e);
        }
    }
    @RequestMapping("/update")
    @ResponseBody
    public Result update(@ModelAttribute("surgery") Surgery surgery){
        if(surgery==null||surgery.getSurgeryNo()==null){
            return Result.fail("Null Point");
        }
        try{
            surgeryDao.updateSurgery(surgery);
            return Result.ok();
        }catch (Exception e){
            return Result.fail(e);
        }
    }
    @RequestMapping("/get/{surgeryNo}")
    @ResponseBody
    public Result get(@PathVariable("surgeryNo")String surgeryNo){
        try{
            Surgery surgery = surgeryDao.selectSurgeryBySurgeryNo(surgeryNo);
            if(surgery == null){
                return Result.fail("Not Exist");
            }
            return Result.ok(surgery);
        }catch (Exception e){
            return Result.fail(e);
        }
    }
    @RequestMapping("/persons")
    @ResponseBody
    public Result persons(){
        try {
            List<String> doctors = surgeryDao.selectDistinctDoctor();
            List<String> patients = surgeryDao.selectDistinctPatient();
            Map<String,List<String>> persons = new HashMap<>();
            persons.put("doctors",doctors);
            persons.put("patients",patients);
            return Result.ok(persons);
        }catch (Exception e){
            return Result.fail(e);
        }
    }
    @RequestMapping("/search")
    @ResponseBody
    public Result search(HttpServletRequest request){
        String doctor = request.getParameter("doctor");
        String patient= request.getParameter("patient");
        Long startTime= System.currentTimeMillis()-DATE_TIME*30;
        Long endTime = System.currentTimeMillis();
        try {
            if (request.getParameter("timeRange") == null) {
                endTime = System.currentTimeMillis();
            } else {
                String[] timeRangeStr= request.getParameter("timeRange").split("~");
                startTime=  DateFormatUtil.parse(timeRangeStr[0]);
                endTime = DateFormatUtil.parse(timeRangeStr[1])+DateFormatUtil.DATE_TIME;
            }
        } catch (Exception e) {
            log.error("解析前端timeRange失败,使用默认配置",e);
        }
        try {
            List<Surgery> surgeryList = surgeryDao.selectSurgeryDynamic(startTime,endTime,doctor,patient);
            return Result.ok(surgeryList);
        }catch (Exception e){
            return Result.fail(e);
        }
    }
}
