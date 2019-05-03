package zju.edu.als.client;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by zzq on 2016/11/29.
 */
@Slf4j
public class DataSenderManager {
    private static String host;
    private static Integer port;
    private static Socket socket;
    private static BufferedWriter out;
    public static void buildSocketConnect(String _host,Integer _port)  {
        host=_host;
        port=_port;
        while (socket == null) try {
            socket = new Socket(host, port);
            socket.setKeepAlive(true);
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
        } catch (IOException e) {
            log.error("build socket connect failed", e);
            try {
                Thread.currentThread().sleep(5000);
                log.info("rebuild socket connect");
                socket = null;
            } catch (InterruptedException e1) {

            }
        }
    }
    public static void sendData(String data){
        try {
            out.write(data+"\n");
            out.flush();
        } catch (IOException e) {
            log.error("socket Connect lost send data failed",e);
            socket=null;
            buildSocketConnect(host,port);
            sendData(data);
        }
    }
    public static void closeSocketConnect(){
        try {
            out.close();
            socket.close();
            out=null;
            socket=null;
        } catch (IOException e) {
        }
    }
}
