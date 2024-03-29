package zju.edu.als.sslbridge;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecureBridgeHandler extends SimpleChannelInboundHandler<String> {

    private final BridgeServer.BridgeClient client;
    SecureBridgeHandler(BridgeServer.BridgeClient client) {
        this.client = client;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Data Can be Sent to Cloud Server");
        client.flushMessages();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("Connection failed, Trying to Restart");
        client.restart();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
