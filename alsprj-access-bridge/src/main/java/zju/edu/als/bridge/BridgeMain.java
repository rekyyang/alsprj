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

        DataSenderManager.buildSocketConnect("als-server.zju.edu.cn",9204);
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        context.registerShutdownHook();
        DataSenderManager.sendData();

    }
}
