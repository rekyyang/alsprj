package zju.edu.als.bridge;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Created by zzq on 2016/11/28.
 */
@Slf4j
public class ClientHandler implements Runnable{
    private BlockingQueue<String> messages;
    private Socket socket;
    public ClientHandler(BlockingQueue<String> messages, Socket socket){
        this.messages=messages;
        this.socket=socket;
    }
    @Override
    public void run() {
        BufferedReader in= null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
            while (true) {
                try {
                    String dataStr = in.readLine();
                    if(dataStr==null){
                        break;
                    }
                    log.info("ClientHandler : {}",dataStr);
                     messages.add(dataStr);
                }catch (Exception e){
                    log.error("Data Handle Exception",e);
                }
            }
        } catch (IOException e) {
            log.error("Client Lost",e);
        }finally {
            try {
                in.close();
                socket.close();
            } catch (IOException e) {

            }
        }
    }
}
