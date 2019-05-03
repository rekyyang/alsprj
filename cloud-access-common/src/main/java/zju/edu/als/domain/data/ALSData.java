package zju.edu.als.domain.data;

/**
 * Created by zzq on 2016/11/27.
 */
public class ALSData extends DataBase implements java.io.Serializable{
    private double dPacc;//'采血压'		Pacc
    private double dPart;//'动脉压'		Part
    private double dPven;//'静脉压' 		Pven
    private double dP1st;//'血浆压P1st'
    private double  dTMP;//‘跨膜压' TMP
    private double dP2nd;// 血浆入口P2nd
    private double dP3rd;	 //滤过压 P3rd
    private long cumulativeTime;//'累计时间'
    private int iBPSpeed;//血泵BP
    private int iFPSpeed;//分离泵FP
    private int iDPSpeed;//透析泵DP
    private int iRPSpeed;//返液泵RP
    private int iFP2SPeed;//滤过泵FP2
    private int iCPSpeed;//循环泵CP
    private int iSPSpeed;//肝素泵
    private double warmer;//'加热温度'
    private double dBPT;//'血液泵累计' 精确到0.1L
    private double dFPT;//'FP累计'精确到0.1L
    private double dDPT;//'DP累计'精确到0.1L
    private double dRPT;//'RP累计'精确到0.1L
    private double dFP2T;//'FP2累计'精确到0.1L
    private double dSPT;//‘SP累计’(精确到0.1mL)

    public double getdPacc() {
        return dPacc;
    }

    public void setdPacc(double dPacc) {
        this.dPacc = dPacc;
    }

    public double getdPart() {
        return dPart;
    }

    public void setdPart(double dPart) {
        this.dPart = dPart;
    }

    public double getdPven() {
        return dPven;
    }

    public void setdPven(double dPven) {
        this.dPven = dPven;
    }

    public double getdP1st() {
        return dP1st;
    }

    public void setdP1st(double dP1st) {
        this.dP1st = dP1st;
    }

    public double getdTMP() {
        return dTMP;
    }

    public void setdTMP(double dTMP) {
        this.dTMP = dTMP;
    }

    public double getdP2nd() {
        return dP2nd;
    }

    public void setdP2nd(double dP2nd) {
        this.dP2nd = dP2nd;
    }

    public double getdP3rd() {
        return dP3rd;
    }

    public void setdP3rd(double dP3rd) {
        this.dP3rd = dP3rd;
    }

    public long getCumulativeTime() {
        return cumulativeTime;
    }

    public void setCumulativeTime(long cumulativeTime) {
        this.cumulativeTime = cumulativeTime;
    }

    public int getiBPSpeed() {
        return iBPSpeed;
    }

    public void setiBPSpeed(int iBPSpeed) {
        this.iBPSpeed = iBPSpeed;
    }

    public int getiFPSpeed() {
        return iFPSpeed;
    }

    public void setiFPSpeed(int iFPSpeed) {
        this.iFPSpeed = iFPSpeed;
    }

    public int getiDPSpeed() {
        return iDPSpeed;
    }

    public void setiDPSpeed(int iDPSpeed) {
        this.iDPSpeed = iDPSpeed;
    }

    public int getiRPSpeed() {
        return iRPSpeed;
    }

    public void setiRPSpeed(int iRPSpeed) {
        this.iRPSpeed = iRPSpeed;
    }

    public int getiFP2SPeed() {
        return iFP2SPeed;
    }

    public void setiFP2SPeed(int iFP2SPeed) {
        this.iFP2SPeed = iFP2SPeed;
    }

    public int getiCPSpeed() {
        return iCPSpeed;
    }

    public void setiCPSpeed(int iCPSpeed) {
        this.iCPSpeed = iCPSpeed;
    }

    public int getiSPSpeed() {
        return iSPSpeed;
    }

    public void setiSPSpeed(int iSPSpeed) {
        this.iSPSpeed = iSPSpeed;
    }

    public double getWarmer() {
        return warmer;
    }

    public void setWarmer(double warmer) {
        this.warmer = warmer;
    }

    public double getdBPT() {
        return dBPT;
    }

    public void setdBPT(double dBPT) {
        this.dBPT = dBPT;
    }

    public double getdFPT() {
        return dFPT;
    }

    public void setdFPT(double dFPT) {
        this.dFPT = dFPT;
    }

    public double getdDPT() {
        return dDPT;
    }

    public void setdDPT(double dDPT) {
        this.dDPT = dDPT;
    }

    public double getdRPT() {
        return dRPT;
    }

    public void setdRPT(double dRPT) {
        this.dRPT = dRPT;
    }

    public double getdFP2T() {
        return dFP2T;
    }

    public void setdFP2T(double dFP2T) {
        this.dFP2T = dFP2T;
    }

    public double getdSPT() {
        return dSPT;
    }

    public void setdSPT(double dSPT) {
        this.dSPT = dSPT;
    }
}
