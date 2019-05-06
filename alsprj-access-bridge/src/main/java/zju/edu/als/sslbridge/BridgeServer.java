package zju.edu.als.sslbridge;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BridgeServer {
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private final BridgeClient client;
    private Channel serverChannel;

    public static void main(String[] args) {
        final EventLoopGroup bossGroup = new NioEventLoopGroup();
        final EventLoopGroup workerGroup = new NioEventLoopGroup();

        SslContext context = SslContextFactory.sslContextClient();
        BridgeServer.BridgeClient client = new BridgeServer.BridgeClient(workerGroup,context);

        InetSocketAddress couldAddress = new InetSocketAddress("als-server.zju.edu.cn", 9204);
        client.start(couldAddress); //启动客户端

        BridgeServer server = new BridgeServer(bossGroup, workerGroup, client);
        InetSocketAddress address = new InetSocketAddress("als-server.zju.edu.cn", 9203);
        ChannelFuture future = server.start(address) //启动服务器
                .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);

        Runtime.getRuntime().addShutdownHook(new Thread(server::destroy));
        future.channel().closeFuture().syncUninterruptibly();
    }

    public BridgeServer(EventLoopGroup bossGroup, EventLoopGroup workerGroup, BridgeClient client) {
        this.bossGroup = bossGroup;
        this.workerGroup = workerGroup;
        this.client = client;
    }

    public ChannelFuture start(InetSocketAddress address) {

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new BridgeServerInitializer(client));
        ChannelFuture future = bootstrap.bind(address).syncUninterruptibly();
        serverChannel = future.channel();
        return future;

    }

    public void destroy() {
        client.destroy(workerGroup);
        if (serverChannel != null) {
            serverChannel.close();
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    static class BridgeClient {

        private final EventLoopGroup group;
        private final SslContext context;
        private InetSocketAddress address;
        private Channel channel;
        private final ConnectionListener CONNECTION_LISTENER
                = new ConnectionListener(this);
        private LinkedBlockingQueue<String> messages = new LinkedBlockingQueue<>();


        public Channel channel() {
            return this.channel;
        }

        public EventLoop eventLoop() {
            return channel().eventLoop();
        }

        BridgeClient(EventLoopGroup group, SslContext context) {
            this.group = group;
            this.context = context;
        }

        public ChannelFuture start(InetSocketAddress address) {
            this.address = address;
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new SecureBridgeClientInitializer(this,context));
            ChannelFuture future = bootstrap.connect(address);
            future.addListener(CONNECTION_LISTENER);
            channel = future.channel();
            return future;
        }

        public void restart() {
            eventLoop().execute(()-> {
                start(address);
            });
        }

        public void destroy(EventLoopGroup group) {
            if (channel != null) {
                channel.close();
            }
            if (this.group != group) {
                group.shutdownGracefully();
            }
        }

        public void writeAndFlush(Object msg) {
             channel.writeAndFlush(msg);
        }

        public void flushMessages() throws InterruptedException {
            while(!messages.isEmpty()) {
                String msg = messages.take();
                System.out.print(msg);
                writeAndFlush(msg);
            }
        }

        public void saveMessage(String msg) {
            messages.offer(msg);
        }
    }

    static class ConnectionListener implements ChannelFutureListener {
        private BridgeClient client;
        private AtomicInteger count = new AtomicInteger();
        ConnectionListener(BridgeClient client) {
            this.client = client;
        }
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if (!future.isSuccess()) {
                final EventLoop loop = future.channel().eventLoop();
                loop.schedule(() -> {
                    System.err.println("Reconnecting to Could Server");
                    client.restart();
                }, count.incrementAndGet()*2, TimeUnit.SECONDS);
            } else {
                count.getAndSet(0);
                System.out.println("Bridge Start Successfully");
            }
        }
    }

}
