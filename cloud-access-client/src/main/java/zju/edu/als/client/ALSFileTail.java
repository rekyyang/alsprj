package zju.edu.als.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static zju.edu.als.client.constant.ClientConstant.SURGERY_DATA_OFFSET;

@Component
@Slf4j
public class ALSFileTail implements Runnable, SmartLifecycle {
    private volatile boolean isRunning = false;
    private static long IDLE_SLEEP_INTERVAL = 1000;
    private File ALSDataFile;
    private long filePointer;
    private ExecutorService executorService;
    private boolean isConfigInit = false;
    private RandomAccessFile ALSDataOffsetFile;



    public ALSFileTail() {
        executorService = Executors.newSingleThreadExecutor();
    }

    public void setConfig(ALSFileTailConfig alsFileTailConfig) {
        this.ALSDataFile = alsFileTailConfig.getALSDataFile();
        this.filePointer = alsFileTailConfig.getALSDataFilePoint();
        isConfigInit = true;
    }

    public void run() {
        while (!isConfigInit) {
            try {
                Thread.currentThread().sleep(IDLE_SLEEP_INTERVAL);
            } catch (InterruptedException e) {
                break;
            }
        }
        try {
            ALSDataOffsetFile = new RandomAccessFile(ALSDataFile.getParent() + "/" + SURGERY_DATA_OFFSET, "rw");
        } catch (FileNotFoundException e) {

        }
        try {
            RandomAccessFile raf = null;
            while (isConfigInit) {
                long fileLength = this.ALSDataFile.length();
                if (filePointer > fileLength) {
                    filePointer = 0;
                }
                if (fileLength > filePointer) {
                    log.info("Read data from {}",this.ALSDataFile.getName());
                    raf = new RandomAccessFile(ALSDataFile, "r");
                    raf.seek(filePointer);
                    String lineISO = raf.readLine();
                    while (lineISO != null) {
                        String line = new String(lineISO.getBytes("ISO-8859-1"), "utf-8");
                        DataSenderManager.sendData(line);
                        filePointer = raf.getFilePointer();
                        ALSDataOffsetFile.seek(0);
                        ALSDataOffsetFile.write(String.format("%20d", filePointer).getBytes());
                        lineISO = raf.readLine();
                    }
                    filePointer = raf.getFilePointer();
                    log.info("{} read over",this.ALSDataFile.getName());
                }
                try {
                    Thread.currentThread().sleep(IDLE_SLEEP_INTERVAL);
                } catch (InterruptedException e) {
                    log.info("break");
                    if (raf != null) {
                        raf.close();
                    }
                    break;
                }
            }
        } catch (IOException e) {
        }
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        callback.run();
        stop();
    }

    @Override
    public void start() {
        executorService.execute(this);
        isRunning = true;
    }

    @Override
    public void stop() {
        try {
            ALSDataOffsetFile.close();
        } catch (IOException e) {

        }
        executorService.shutdownNow();
        DataSenderManager.closeSocketConnect();
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getPhase() {
        return 0;
    }
}
