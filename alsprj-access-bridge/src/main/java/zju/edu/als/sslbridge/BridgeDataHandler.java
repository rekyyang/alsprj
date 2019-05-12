package zju.edu.als.sslbridge;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BridgeDataHandler extends SimpleChannelInboundHandler<String> {



    private final BridgeServer.BridgeClient client;
    BridgeDataHandler(BridgeServer.BridgeClient client) {
        this.client = client;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if (!client.channel().isActive()) {
//            System.out.println("keep in local " + client.channel().remoteAddress()+" " +msg);
            client.saveMessage(msg);
        } else {
//            System.out.println("send successfully to could " + client.channel().remoteAddress()+" " +msg);
            client.writeAndFlush(msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("人工肝机器 "+ ctx.channel().remoteAddress()+" 上线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("人工肝机器 "+ ctx.channel().remoteAddress()+ " 下线");
    }

}
