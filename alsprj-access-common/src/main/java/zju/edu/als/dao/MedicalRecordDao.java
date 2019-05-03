package zju.edu.als.dao;

import org.apache.ibatis.annotations.Param;
import zju.edu.als.domain.record.MedicalRecord;

import java.util.List;

/**
 * Created by zzq on 2016/12/20.
 */
public interface MedicalRecordDao {
    public boolean insertMedicalRecord(@Param("medicalRecord") MedicalRecord medicalRecord);
    public boolean deleteMedicalRecord(@Param("medicalRecord")MedicalRecord medicalRecord);
    public List<MedicalRecord> selectAllMedicalRecord();
    public List<MedicalRecord> selectDynamic(@Param("inpatientNum")String inpatientNum,
                                             @Param("password")String password,
                                             @Param("role")String role);
    public boolean updateMedicalRecord(@Param("inpatientNum")Integer inpatientNum,
                                       @Param("patient")String patient,
                                        @Param("complications")String complications,
                                        @Param("path")String path,
                                        @Param("state")Integer state);
}
