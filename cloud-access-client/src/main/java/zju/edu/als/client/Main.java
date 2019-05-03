package zju.edu.als.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import static zju.edu.als.client.constant.ClientConstant.SURGERY_DATA_FILE_NAME;
import static zju.edu.als.client.constant.ClientConstant.SURGERY_DATA_OFFSET;

/**
 * Created by zzq on 2016/11/29.
 */
@Slf4j
public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("输入参数错误，没有数据根目录");
            return;
        }
        String dataRootPath = args[0];
        log.info("als data path: {}",dataRootPath);
        File dataRoot = new File(dataRootPath);
        if (!dataRoot.exists() || !dataRoot.isDirectory()) {
            System.out.println("输入参数错误，数据根目录不存在");
            return;
        }
        DataSenderManager.buildSocketConnect("localhost", 9203);

        //通过读文件获取数据
        File ALSDataFile = getALSDataFile(dataRootPath);
        File ALSDataIndexFile = new File(dataRootPath, SURGERY_DATA_OFFSET);
        long ALSDataFileStart = 0;
        if (ALSDataIndexFile.exists()){
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ALSDataIndexFile)));
                ALSDataFileStart = Long.valueOf(br.readLine().trim());
            } catch (Exception e) {

            }
        }
        ALSFileTailConfig alsFileTailConfig = new ALSFileTailConfig();
        alsFileTailConfig.setALSDataFile(ALSDataFile);
        alsFileTailConfig.setALSDataFilePoint(ALSDataFileStart);
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        context.getBean(ALSFileTail.class).setConfig(alsFileTailConfig);
        context.registerShutdownHook();
    }
    public static File getALSDataFile(String dataRootPath){
        File file = new File(dataRootPath,SURGERY_DATA_FILE_NAME);
        while (!file.exists()){
            try {
                Thread.currentThread().sleep(10000);
                System.out.println("Not Find Data File");
            } catch (InterruptedException e) {
            }
        }
        return file;
    }
}
