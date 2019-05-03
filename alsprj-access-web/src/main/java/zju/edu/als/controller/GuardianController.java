package zju.edu.als.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zju.edu.als.dao.GuardianDao;
import zju.edu.als.domain.data.GuardianData;
import zju.edu.als.domain.result.Result;
import zju.edu.als.monitor.GuardianMonitor;
import zju.edu.als.util.DateFormatUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * Created by zzq on 2016/10/29.
 */
@Controller
@RequestMapping("/guardian")
@Slf4j
public class GuardianController {

    @Resource(name = "guardianDao")
    GuardianDao guardianDao;

    @Resource(name = "guardianMonitor")
    GuardianMonitor guardianMonitor;

    @ModelAttribute(value = "guardianData")
    private GuardianData getGuardianData(HttpServletRequest request){
        String guardianDataStr=request.getParameter("guardianData");
        if(guardianDataStr==null){
            return null;
        }
        GuardianData guardianData;
        try {
            guardianData = JSONObject.parseObject(guardianDataStr, GuardianData.class);
        }catch (Exception e){
            log.error("Invoke getGuardianData JsonParseException ",e);
            return null;
        }
        return guardianData;
    }

    @ModelAttribute(value = "guardianDataList")
    private List<GuardianData> getGuardianDataList(HttpServletRequest request){
        String guardianDataListStr=request.getParameter("guardianDataList");
        if(guardianDataListStr==null){
            return null;
        }
        List<GuardianData> guardianDataList;
        try {
            guardianDataList = JSONObject.parseObject(guardianDataListStr,new TypeReference<List<GuardianData>>(){});
        }catch (Exception e){
            log.error("Invoke getGuardianDataList JsonParseException ",e);
            return null;
        }
        return guardianDataList;

    }

    @RequestMapping("/insert")
    @ResponseBody
    public Result insert(@ModelAttribute(value = "guardianData")GuardianData guardianData){
        if(guardianData==null){
            return Result.fail("NullPointException");
        }
        try {
            guardianDao.insertGuardianData(guardianData);
            guardianMonitor.handleData(guardianData);
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
        return Result.ok();
    }

    @RequestMapping("/batchInsert")
    @ResponseBody
    public Result batchInsert(@ModelAttribute(value = "guardianDataList")List<GuardianData> guardianDataList){
        if(guardianDataList==null){
            return Result.fail("NullPointException");
        }
        try {
            guardianDao.batchInsertGuardianData(guardianDataList);
            guardianMonitor.handleData(guardianDataList.toArray(new GuardianData[guardianDataList.size()]));
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
        return Result.ok();
    }

    @RequestMapping("get/{surgeryNo}")
    @ResponseBody
    public Result getAllBySurgeryNo(@PathVariable("surgeryNo")String surgeryNo){
        List<GuardianData> guardianDataList;
        try {
            guardianDataList = guardianDao.selectGuardianDataBySurgeryNo(surgeryNo);
            return Result.ok(guardianDataList);
        }catch (Exception e){
            log.error("Invoke getAllBySurgeryNo",e);
            return Result.fail(e);
        }
    }
    @RequestMapping("get/{surgeryNo}/{timeRange}")
    @ResponseBody
    public Result getAllBySurgeryNoWithTimeRange(@PathVariable("surgeryNo")String surgeryNo,@PathVariable("timeRange")String timeRange){
        List<GuardianData> guardianDataList;
        long beginTime;
        long endTime;
        try{
            String[] times=timeRange.split("~");
            beginTime= DateFormatUtil.parse(times[0]);
            endTime= DateFormatUtil.parse(times[1]);
        } catch (ParseException e) {
            log.error(timeRange +"parse exception");
            return Result.fail(e);
        }
        try {
            guardianDataList = guardianDao.selectGuardianDataBySurgeryNoWithTimeRange(surgeryNo,beginTime,endTime);
            return Result.ok(guardianDataList);
        }catch (Exception e){
            log.error("Invoke getAllBySurgeryNoWithTimeRange",e);
            return Result.fail(e);
        }
    }
}
