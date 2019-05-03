package zju.edu.als.domain.data;

/**
 * Created by zzq on 2016/10/27.
 */
public class GuardianData extends DataBase  implements java.io.Serializable{

    private int heartRate;//'心率'
    private int systolicPressure;//'收缩压'
    private int diastolicPressure;//'舒张压'
    private int bloodOxygen;//'血氧'

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getSystolicPressure() {
        return systolicPressure;
    }

    public void setSystolicPressure(int systolicPressure) {
        this.systolicPressure = systolicPressure;
    }

    public int getDiastolicPressure() {
        return diastolicPressure;
    }

    public void setDiastolicPressure(int diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
    }

    public int getBloodOxygen() {
        return bloodOxygen;
    }

    public void setBloodOxygen(int bloodOxygen) {
        this.bloodOxygen = bloodOxygen;
    }
}
