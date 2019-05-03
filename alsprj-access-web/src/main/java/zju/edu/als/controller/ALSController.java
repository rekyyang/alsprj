package zju.edu.als.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zju.edu.als.dao.ALSDao;
import zju.edu.als.domain.data.ALSData;
import zju.edu.als.domain.result.Result;
import zju.edu.als.monitor.ALSMonitor;
import zju.edu.als.util.DateFormatUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * Created by zzq on 2016/11/27.
 */
@RequestMapping("/ALS")
@Controller
@Slf4j
public class ALSController {

    @Resource(name = "ALSDao")
    ALSDao alsDao;

    @Resource(name = "ALSMonitor")
    ALSMonitor alsMonitor;

    @ModelAttribute(value = "ALSData")
    private ALSData getALSData(HttpServletRequest request){
        String ALSDataStr=request.getParameter("ALSData");
        if(ALSDataStr==null){
            return null;
        }
        ALSData ALSData;
        try {
            ALSData = JSONObject.parseObject(ALSDataStr, ALSData.class);
        }catch (Exception e){
            log.error("Invoke getALSData JsonParseException ",e);
            return null;
        }
        return ALSData;
    }

    @ModelAttribute(value = "ALSDataList")
    private List<ALSData> getALSDataList(HttpServletRequest request){
        String ALSDataListStr=request.getParameter("ALSDataList");
        if(ALSDataListStr==null){
            return null;
        }
        List<ALSData> ALSDataList;
        try {
            ALSDataList = JSONObject.parseObject(ALSDataListStr,new TypeReference<List<ALSData>>(){});
        }catch (Exception e){
            log.error("Invoke getALSDataList JsonParseException ",e);
            return null;
        }
        return ALSDataList;

    }

    @RequestMapping("/insert")
    @ResponseBody
    public Result insert(@ModelAttribute(value = "ALSData")ALSData ALSData){
        if(ALSData==null){
            return Result.fail("NullPointException");
        }
        try {
            alsDao.insertALSData(ALSData);
            alsMonitor.handleData(ALSData);
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
        return Result.ok();
    }

    @RequestMapping("/batchInsert")
    @ResponseBody
    public Result batchInsert(@ModelAttribute(value = "ALSDataList")List<ALSData> ALSDataList){
        if(ALSDataList==null){
            return Result.fail("NullPointException");
        }
        try {
            alsDao.batchInsertALSData(ALSDataList);
            alsMonitor.handleData(ALSDataList.toArray(new ALSData[ALSDataList.size()]));
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
        return Result.ok();
    }

    @RequestMapping("get/{surgeryNo}")
    @ResponseBody
    public Result getAllBySurgeryNo(@PathVariable("surgeryNo")String surgeryNo){
        List<ALSData> ALSDataList;
        try {
            ALSDataList = alsDao.selectALSDataBySurgeryNo(surgeryNo);
            return Result.ok(ALSDataList);
        }catch (Exception e){
            log.error("Invoke getAllBySurgeryNo",e);
            return Result.fail(e);
        }
    }
    @RequestMapping("get/{surgeryNo}/{timeRange}")
    @ResponseBody
    public Result getAllBySurgeryNoWithTimeRange(@PathVariable("surgeryNo")String surgeryNo,@PathVariable("timeRange")String timeRange){
        List<ALSData> ALSDataList;
        long beginTime;
        long endTime;
        try{
            String[] times=timeRange.split("~");
            beginTime= DateFormatUtil.parseTime(times[0]);
            endTime= DateFormatUtil.parseTime(times[1]);
        } catch (ParseException e) {
            log.error(timeRange +"parse exception");
            return Result.fail(e);
        }
        try {
            ALSDataList = alsDao.selectALSDataBySurgeryNoWithTimeRange(surgeryNo,beginTime,endTime);
            return Result.ok(ALSDataList);
        }catch (Exception e){
            log.error("Invoke getAllBySurgeryNoWithTimeRange",e);
            return Result.fail(e);
        }
    }
}
