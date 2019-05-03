package zju.edu.als.bridge;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zzq on 2016/11/28.
 */
@Slf4j
public class ServerHandler implements Runnable{
    private ExecutorService executorService;
    private BlockingQueue<String> messages;
    private Integer port;

    public ServerHandler(BlockingQueue<String> messages, Integer port) {
        this.messages=messages;
        this.port=port;
    }

    @Override
    public void run() {
        executorService = Executors.newCachedThreadPool();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            log.error("Server Socket Bind Exception", e);
        }
        while (true) {
            try {
                Socket client = serverSocket.accept();
                client.setKeepAlive(true);
                executorService.execute(new ClientHandler(messages,client));
            } catch (SocketException e) {

            } catch (IOException e) {
            }
        }
    }
}
