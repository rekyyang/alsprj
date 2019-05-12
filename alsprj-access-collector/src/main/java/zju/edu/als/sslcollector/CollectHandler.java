package zju.edu.als.sslcollector;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import zju.edu.als.domain.surgery.Surgery;

@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CollectHandler extends SimpleChannelInboundHandler {

    @Autowired
    SurgeryTask surgeryTask;

    @Qualifier("taskExecutor")
    @Autowired
    private EventExecutor taskExecutor;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 业务代码
        taskExecutor.execute(surgeryTask.newSubTask((String)msg));

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("设备上线  device online");
//        executor.execute(surgeryTask);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        if (!surgeryTask.isSurgeryState()){
            log.info("设备下线  device offline");
        }
        else {
//            surgeryTask.getAlarmCenter().sendAlarm(surgeryTask.getSurgeryNo(),"失去连接，请检查系统");
            log.info("失去连接，手术中断  lost connection，surgery interrupted!");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
