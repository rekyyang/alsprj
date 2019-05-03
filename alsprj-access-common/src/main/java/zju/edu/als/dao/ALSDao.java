package zju.edu.als.dao;

import org.apache.ibatis.annotations.Param;
import zju.edu.als.domain.data.ALSData;

import java.util.List;

/**
 * Created by zzq on 2016/11/27.
 */
public interface ALSDao {
    boolean insertALSData(@Param("ALSData") ALSData ALSData);

    boolean batchInsertALSData(@Param("ALSDataList") List<ALSData> ALSDataList);

    List<ALSData> selectALSDataBySurgeryNo(@Param("surgeryNo") String surgeryNo);

    List<ALSData> selectALSDataBySurgeryNoWithTimeRange(@Param("surgeryNo") String surgeryNo,@Param("beginTime") long beginTime,@Param("endTime") long endTime);

    List<ALSData> selectALSDataBySurgeryNoListWithTimeRange(@Param("surgeryNoList") List<String> surgeryNoList,@Param("beginTime") long beginTime,@Param("endTime") long endTime);
}
