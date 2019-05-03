package zju.edu.als.client;

import java.io.File;

/**
 * Created by zzq on 2016/11/29.
 */
public class ALSFileTailConfig {
    private File ALSDataFile;
    private long ALSDataFilePoint;

    public File getALSDataFile() {
        return ALSDataFile;
    }

    public void setALSDataFile(File ALSDataFile) {
        this.ALSDataFile = ALSDataFile;
    }

    public long getALSDataFilePoint() {
        return ALSDataFilePoint;
    }

    public void setALSDataFilePoint(long ALSDataFilePoint) {
        this.ALSDataFilePoint = ALSDataFilePoint;
    }
}
