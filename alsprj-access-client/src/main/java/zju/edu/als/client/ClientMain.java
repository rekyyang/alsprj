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
public class ClientMain {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("输入参数错误，没有数据根目录");
            return;
        }
        String dataRootPath = args[0];
        log.info("als data path: {}",dataRootPath );

        File rootFile = new File( dataRootPath );
        if (!rootFile.exists() || !rootFile.isDirectory()) {
            System.out.println("输入参数错误，数据根目录不存在");
            return;
        }

        // 创建到bridge的socket连接
        DataSenderManager.buildSocketConnect("localhost", 9203);

        // 读取索引文件，获取上一次文件读取的位置
        File aLSDataIndexFile = new File(dataRootPath, SURGERY_DATA_OFFSET);
        long aLSDataFileStart = 0;
        if (aLSDataIndexFile.exists()){
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(aLSDataIndexFile)));
                aLSDataFileStart = Long.valueOf(br.readLine().trim());
            } catch (Exception e) {

            }
        }

        // 文件配置对象 1.文件  2.起始索引
        File aLSDataFile = getALSDataFile(dataRootPath);
        ALSFileTailConfig alsFileTailConfig = new ALSFileTailConfig(aLSDataFile,aLSDataFileStart);


        // 获取ALSFileTail.class的一个实例，在Spring Bean生命周期的管理下会产生一个线程，主要作用为逐行发送数据。
        // 在这之前需要建立好socket连接
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
