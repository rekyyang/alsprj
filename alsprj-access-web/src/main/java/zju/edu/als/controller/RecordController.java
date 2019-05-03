package zju.edu.als.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import zju.edu.als.constant.MedicalRecordState;
import zju.edu.als.dao.MedicalRecordDao;
import zju.edu.als.domain.record.MedicalRecord;
import zju.edu.als.domain.result.Result;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by zzq on 2016/12/19.
 */
@Controller
@RequestMapping("/record")
public class RecordController {
    @Resource
    private MedicalRecordDao medicalRecordDao;
    @RequestMapping("{inpatientNum}/{patient}/upload")
    @ResponseBody
    public void upload(@RequestParam("file") MultipartFile file,
                        @PathVariable("inpatientNum")Integer inpatientNum,
                        @PathVariable("patient")String  patient)  {
        try {
            File targetDir = new File("record");
            if(!targetDir.exists()){
                targetDir.mkdir();
            }
            File target = new File(targetDir,inpatientNum+"-"+System.currentTimeMillis()+".csv");
            file.transferTo(target);
            medicalRecordDao.updateMedicalRecord(inpatientNum,patient, "nihao", target.getAbsolutePath(), MedicalRecordState.complete.ordinal());
        }catch (Exception e){
            System.out.println(e);
        }
    }
    @RequestMapping("/complications")
    @ResponseBody
    public Result complications(@RequestParam("inpatientNum") Integer inpatientNum,
                                @RequestParam("patient") String patient,
                                @RequestParam("complications")  String complications,
                                @RequestParam("path")  String path){
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setInpatientNum(inpatientNum);
        medicalRecord.setComplications(complications);
        try {
            medicalRecordDao.updateMedicalRecord(inpatientNum,patient,complications,path,MedicalRecordState.complete.ordinal());
            return Result.ok();
        }catch (Exception e){
            return Result.fail(e);
        }
    }
    @RequestMapping("/insert")
    @ResponseBody
    public Result insert(@RequestParam("inpatientNum") Integer inpatientNum,
                         @RequestParam("patient") String patient){
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setInpatientNum(inpatientNum);
        medicalRecord.setPatient(patient);
        medicalRecord.setState(MedicalRecordState.init.ordinal());
        try {
            medicalRecordDao.insertMedicalRecord(medicalRecord);
            return Result.ok();
        }catch (Exception e){
            return Result.fail(e);
        }
    }
    @RequestMapping("/delete")
    @ResponseBody
    public Result delete(@RequestParam("inpatientNum") Integer inpatientNum){
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setInpatientNum(inpatientNum);
        try {
            medicalRecordDao.deleteMedicalRecord(medicalRecord);
            return Result.ok();
        }catch (Exception e){
            return Result.fail(e);
        }
    }
    @RequestMapping("/all")
    @ResponseBody
    public Result all(){
        try {
            List<MedicalRecord> medicalRecordList=medicalRecordDao.selectAllMedicalRecord();
            return Result.ok(medicalRecordList);
        }catch (Exception e){
            return Result.fail(e);
        }
    }

}
