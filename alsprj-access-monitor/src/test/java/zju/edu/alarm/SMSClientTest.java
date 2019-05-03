package zju.edu.alarm;

import org.junit.Test;
import zju.edu.als.alarm.SMSClient;

/**
 * Created by zzq on 2016/11/3.
 */
public class SMSClientTest {
    @Test
    public void sentMessageTest(){
        SMSClient.sendMessage("18768113730","你好");
    }
}
