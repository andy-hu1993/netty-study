package com.hgs.netty.codec.server;

import com.hgs.netty.codec.SubscribeReqProto;
import com.hgs.netty.codec.SubscribeRespProto;
import com.hgs.netty.codec.SubscribeRespProto.SubscribeResp;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * server handler<br>
 *
 * @author huguangsheng
 * @version V1.0
 * @date 2018/8/31 下午5:30
 */
@Sharable
public class SubReqServerHandler extends ChannelHandlerAdapter {

    private static String userName = "huguangsheng";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq)msg;
        if (userName.equalsIgnoreCase(req.getUserName())){
            System.out.println("server accept client req message [" +req.toString() + "]");
            ctx.writeAndFlush(resp(req.getSubReqID()));
        }
    }

    private SubscribeResp resp(int subReqID){
        SubscribeRespProto.SubscribeResp.Builder builder =  SubscribeRespProto.SubscribeResp.newBuilder();
        builder.setSubReqID(subReqID);
        builder.setRespCode(0);
        builder.setDesc("netty success");
        return builder.build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // occur a exception, close the link
        ctx.close();
    }
}
