package zju.edu.als.sslcollector;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.net.ssl.SSLEngine;
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CollectServerInitializer extends ChannelInitializer<Channel> {

    @Qualifier("sslContext")
    @Resource
    private SslContext context;

    @Resource
    private ApplicationContext springContext;


    @Override
    protected void initChannel(Channel ch) throws Exception {
        if (context == null) {
            log.error("Server SSL Context is Null");
            System.exit(-1);
        }
        SSLEngine engine = context.newEngine(ch.alloc());
        engine.setUseClientMode(false);
        engine.setNeedClientAuth(false);
        ch.pipeline().addLast(
                new SslHandler(engine),
                new LineBasedFrameDecoder(64*1024),
                new StringDecoder(CharsetUtil.UTF_8),
                new StringEncoder(CharsetUtil.UTF_8),
                springContext.getBean(CollectHandler.class)
        );
    }
}
