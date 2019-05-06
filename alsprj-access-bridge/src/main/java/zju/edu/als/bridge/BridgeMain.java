package zju.edu.als.bridge;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by zzq on 2016/12/21.
 */
@Slf4j
public class BridgeMain {

    public static void main(String[] args) {

        log.info("The ALS Could System is Starting....");

        // 创建一个到服务器的客户端连接  需要加密
        DataSenderManager.buildSocketConnect("als-server.zju.edu.cn",9204);
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        context.registerShutdownHook();

        //发送数据
        DataSenderManager.sendData();

    }
}
