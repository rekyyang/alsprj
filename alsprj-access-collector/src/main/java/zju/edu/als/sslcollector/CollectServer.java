package zju.edu.als.sslcollector;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import zju.edu.als.sslbridge.SslContextFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class CollectServer implements SmartLifecycle, Runnable {

    private ExecutorService executorService;
    private volatile boolean isRunning = false;

    @Override
    public void run() {
        // Configure the bootstrap.
        final EventLoopGroup bossGroup = new NioEventLoopGroup();
        final EventLoopGroup workerGroup = new NioEventLoopGroup();
        // Load the certificates and initiate the SSL Context
        SslContext context = SslContextFactory.sslContextService();
        try {
            ServerBootstrap b = new ServerBootstrap();
            System.out.println("start server at localhost 9204");
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new CollectServerInitializer(context))
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
        executorService = Executors.newSingleThreadExecutor();
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
