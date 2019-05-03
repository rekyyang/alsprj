package zju.edu.als.dao;

import org.apache.ibatis.annotations.Param;
import zju.edu.als.domain.data.GuardianData;

import java.util.List;

/**
 * Created by zzq on 2016/10/27.
 */
public interface GuardianDao {
    boolean insertGuardianData(@Param("guardianData") GuardianData guardianData);
    
    boolean batchInsertGuardianData(@Param("guardianDataList") List<GuardianData> guardianDataList);
    
    List<GuardianData> selectGuardianDataBySurgeryNo(@Param("surgeryNo") String surgeryNo);

    List<GuardianData> selectGuardianDataBySurgeryNoWithTimeRange(@Param("surgeryNo") String surgeryNo,@Param("beginTime") long beginTime,@Param("endTime") long endTime);

    List<GuardianData> selectGuardianDataBySurgeryNoListWithTimeRange(@Param("surgeryNoList") List<String> surgeryNoList,@Param("beginTime") long beginTime,@Param("endTime") long endTime);
    
}
