package zju.edu.als.domain.record;

/**
 * Created by zzq on 2016/12/20.
 */
public class MedicalRecord {
    private Long id;
    private Integer inpatientNum;
    private String patient;
    private String complications;
    private String path;
    private Integer state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getInpatientNum() {
        return inpatientNum;
    }

    public void setInpatientNum(Integer inpatientNum) {
        this.inpatientNum = inpatientNum;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getComplications() {
        return complications;
    }

    public void setComplications(String complications) {
        this.complications = complications;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
