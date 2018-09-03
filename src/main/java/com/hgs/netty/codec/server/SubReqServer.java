package com.hgs.netty.codec.server;

import com.hgs.netty.codec.SubscribeReqProto;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * sub req server<br>
 *
 * @author huguangsheng
 * @version V1.0
 * @date 2018/8/31 下午5:15
 */
public class SubReqServer {


    public void bind(int port) throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                        sc.pipeline().addLast(new ProtobufDecoder(SubscribeReqProto.SubscribeReq.getDefaultInstance()));
                        sc.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                        sc.pipeline().addLast(new ProtobufEncoder());
                        sc.pipeline().addLast(new SubReqServerHandler());
                    }
                });

            // bind port, wait for success sync
            ChannelFuture future = bootstrap.bind(port).sync();

            // wait for server to close listening port
            future.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;

        if (args != null && args.length > 0){
            port = Integer.valueOf(args[0]);
        }
        new SubReqServer().bind(port);
    }

}
