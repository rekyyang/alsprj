package zju.edu.als.dao;

import org.apache.ibatis.annotations.Param;
import zju.edu.als.domain.surgery.Surgery;

import java.util.List;

/**
 * Created by zzq on 2016/11/4.
 */
public interface SurgeryDao {

    public boolean updateSurgery(@Param("surgery")Surgery surgery);

    public boolean startSurgery(@Param("surgery")Surgery surgery);

    public boolean endSurgery(@Param("surgery")Surgery surgery);

    public List<String> selectDistinctDoctor();

    public List<String> selectDistinctPatient();

    public List<Surgery> selectSurgeryByState(@Param("state")int state);

    public List<Surgery> selectSurgeryDynamic(@Param("startTime")long startTime,@Param("endTime")long endTime,@Param("doctor")String doctor,@Param("patient")String patient);

    public Surgery selectSurgeryBySurgeryNo(@Param("surgeryNo")String surgeryNo);

    public List<Surgery> selectExecutingSurgeryBySurgeryNoList(@Param("surgeryNoList")List<String> surgeryNoList);


}
