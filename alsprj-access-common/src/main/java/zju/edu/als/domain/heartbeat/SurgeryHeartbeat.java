package zju.edu.als.domain.heartbeat;

/**
 * Created by zzq on 2016/11/6.
 */
public class SurgeryHeartbeat {
    private String surgeryNo;
    private long timestamp;

    public String getSurgeryNo() {
        return surgeryNo;
    }

    public void setSurgeryNo(String surgeryNo) {
        this.surgeryNo = surgeryNo;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
