package zju.edu.als.sslcollector;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import zju.edu.als.sslbridge.SslContextFactory;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class CollectServer implements SmartLifecycle, Runnable {

    @Resource
    private CollectServerInitializer collectServerInitializer;

    @Bean(name = "sslContext")
    private SslContext context(){
        return SslContextFactory.sslContextService();
    }

    @Bean(name = "taskExecutor")
    private EventExecutor taskExecutor() {
        return new DefaultEventExecutor();
    }

    private volatile boolean isRunning = false;


    @Override
    public void run() {
        // Configure the bootstrap.
        final EventLoopGroup bossGroup = new NioEventLoopGroup();
        final EventLoopGroup workerGroup = new NioEventLoopGroup();
        // Load the certificates and initiate the SSL Context
//        SslContext context = SslContextFactory.sslContextService();
        try {
            ServerBootstrap b = new ServerBootstrap();
            log.info("start server at localhost 9204");
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
//                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(collectServerInitializer)
                    .bind("localhost",9204).sync()
                    .channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        callback.run();
        stop();
    }

    @Override
    public void start() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(this);
        isRunning = true;
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getPhase() {
        return 1;
    }

}
