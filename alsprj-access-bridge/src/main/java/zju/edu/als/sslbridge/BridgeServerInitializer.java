package zju.edu.als.sslbridge;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class BridgeServerInitializer extends ChannelInitializer<Channel> {

    private final BridgeServer.BridgeClient client;
    BridgeServerInitializer(BridgeServer.BridgeClient client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast("stringEncoder",new StringEncoder(CharsetUtil.UTF_8));
        pipeline.addLast("stringDecoder",new StringDecoder(CharsetUtil.UTF_8));
        pipeline.addLast("bridgeDataHandler",new BridgeDataHandler(client));

    }
}
