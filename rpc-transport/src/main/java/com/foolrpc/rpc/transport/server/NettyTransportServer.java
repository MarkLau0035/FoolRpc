package com.foolrpc.rpc.transport.server;

import com.foolrpc.rpc.transport.RequestHandler;
import com.foolrpc.rpc.transport.TransportServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;


/**
 * @author guokun
 * @date 2022/5/26 0:41
 */
@Slf4j
public class NettyTransportServer implements TransportServer {
    private NioEventLoopGroup boss;
    private NioEventLoopGroup worker;
    private ServerBootstrap serverBootstrap;
    private ChannelInitializer<SocketChannel> childHandlers;
    private ChannelFuture channelFuture;
    protected int port;
    protected RequestHandler handler;

    @Override
    public void init(int port, RequestHandler handler) {
        this.port = port;
        this.handler = handler;
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();
        childHandlers = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                ch.pipeline().addLast(new HttpServerCodec());
                ch.pipeline().addLast(new HttpObjectAggregator(1024*10));
                ch.pipeline().addLast((ChannelInboundHandler) handler);
            }
        };
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.group(boss, worker);
        serverBootstrap.childHandler(childHandlers);
    }

    /**
     * 开辟一个新线程来运行服务器程序（不会阻塞当前线程）
     */
    @Override
    public void start() {
        new Thread(()->{
            try {
                channelFuture = serverBootstrap.bind(port).sync();
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                log.error("网络服务出现错误", e);
            } finally {
                stop();
            }
        }).start();
    }

    @Override
    public void stop() {
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }


}
