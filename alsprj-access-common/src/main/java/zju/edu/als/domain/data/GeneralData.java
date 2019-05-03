package zju.edu.als.domain.data;

import java.util.List;

/**
 * Created by zzq on 2016/12/1.
 */
public class GeneralData {
    String surgeryNo;
    ALSData alsData;
    List<GuardianData> guardianDataList;

    public String getSurgeryNo() {
        return surgeryNo;
    }

    public void setSurgeryNo(String surgeryNo) {
        this.surgeryNo = surgeryNo;
    }

    public ALSData getAlsData() {
        return alsData;
    }

    public void setAlsData(ALSData alsData) {
        this.alsData = alsData;
    }

    public List<GuardianData> getGuardianDataList() {
        return guardianDataList;
    }

    public void setGuardianDataList(List<GuardianData> guardianDataList) {
        this.guardianDataList = guardianDataList;
    }
}
