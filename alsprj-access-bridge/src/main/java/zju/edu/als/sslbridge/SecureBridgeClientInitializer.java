package zju.edu.als.sslbridge;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLEngine;

@Slf4j
public class SecureBridgeClientInitializer extends ChannelInitializer<Channel> {

    private final BridgeServer.BridgeClient client;
    private final SslContext context;

    public SecureBridgeClientInitializer(BridgeServer.BridgeClient client, SslContext context) {
        this.client = client;
        this.context = context;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();
        if (context == null) {
            log.error("Client SSL Context is Null");
            System.exit(-1);
        }
        SSLEngine engine = context.newEngine(ch.alloc());
        engine.setUseClientMode(true);
        ch.pipeline().addFirst(new SslHandler(engine));

        pipeline.addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));
        pipeline.addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
        pipeline.addLast("secureBridgeHandler", new SecureBridgeHandler(client));

    }
}
