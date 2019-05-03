package zju.edu.als.bridge;

import lombok.extern.slf4j.Slf4j;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by zzq on 2016/11/29.
 */
@Slf4j
public class DataSenderManager {

    public static LinkedBlockingQueue<String> messages=new LinkedBlockingQueue<>();

    private static String host;
    private static Integer port;
    private static Socket socket;
    private static BufferedWriter out;

    public static void buildSocketConnect(String _host,Integer _port)  {
        host=_host;
        port=_port;
        while (socket == null) {
            try {
                buildSocketAndOut();
                log.info("Connected to The Server als-server.zju.edu.cn");
            } catch (IOException e) {
                try {
                    buildFailedProcess(e);
                } catch (InterruptedException e1) {
                    log.info("Sleeping Thread is Interrupted.");
                }
            }
        }
    }

    public static void buildSocketAndOut() throws IOException {
        socket = new Socket(host,port);
        socket.setKeepAlive(true);
        out = new BufferedWriter(new OutputStreamWriter(
                socket.getOutputStream(), "UTF-8"));
    }

    public static void buildFailedProcess(IOException e) throws InterruptedException {
        log.error("build socket connect failed", e);
        Thread.currentThread().sleep(5000);
        log.info("rebuild socket connect");
        socket = null;
    }

    public static void sendData(){
        Executors.newSingleThreadExecutor().execute(() -> {
            while (true) {
                try {
                    String message = messages.take();
                    sendMessage(message);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
    }

    public static void sendMessage(String message) {
        try {
            System.out.println(message);
            out.write(message + "\n");
            out.flush();
        }  catch (IOException e) {
            log.error("socket Connect lost send data failed", e);
            socket = null;
            buildSocketConnect(host, port);
            sendMessage(message);
        }
    }
}
