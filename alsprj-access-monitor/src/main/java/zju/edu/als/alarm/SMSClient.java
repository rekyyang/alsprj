package zju.edu.als.alarm;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static zju.edu.als.alarm.AlarmConfig.*;

/**
 * Created by zzq on 2016/11/3.
 */
@Slf4j
public class SMSClient {
    private static PoolingHttpClientConnectionManager cm ;
    private static RequestConfig requestConfig;
    private static CloseableHttpClient httpClient;
    private static final String url="http://utf8.sms.webchinese.cn/";
    static {
        cm= new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        cm.setDefaultMaxPerRoute(10);
        requestConfig= RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();
        httpClient= HttpClientBuilder.create()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }
    public static void sendMessage(String smsMob,String smsText){
        List<NameValuePair> nameValuePairList= new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("Uid", ALARM_UID));
        nameValuePairList.add(new BasicNameValuePair("key", ALARM_KEY));
        nameValuePairList.add(new BasicNameValuePair("smsMob", smsMob));
        nameValuePairList.add(new BasicNameValuePair("smsText", smsText));
        try {
            String params = EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairList, Consts.UTF_8));
            HttpGet request = new HttpGet(url+"?"+params);
            CloseableHttpResponse response  = httpClient.execute(request);
            try {
                response.close();
            }catch (IOException e){
                log.error("Invoke sendMessage Response Close Exception ",e);
            }
        } catch (Exception e) {
           log.error("Invoke sendMessage httpClient execute Exception",e);
        }
    }
}
