package zju.edu.als.domain.data;

/**
 * Created by zzq on 2016/10/29.
 */
public class DataBase {
    private int id;//'主键'
    private String surgeryNo ;//'手术号'
    private long timestamp;//'时间戳'
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
