package com.tuming.socket.dao;

import com.tuming.socket.util.BusinessType;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.commons.lang.CharEncoding;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class ClientSocket{

    private NioEventLoopGroup workGroup = new NioEventLoopGroup();
    private Bootstrap bootstrap;
    /**
     * 连接后端socket端点,通过ip地址和port连接.
     * 如果网络断开,则自动连接.
     *
     * @param host
     * @param port
     */
    public void init(String host, Integer port) {
        bootstrap = new Bootstrap();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline p = socketChannel.pipeline();
                        ByteBuf delimiter = Unpooled.copiedBuffer(BusinessType.END_FLAG.getBytes(CharEncoding.UTF_8));
                        p.addLast(new IdleStateHandler(0, 0, 60));
                        p.addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, delimiter));
                        p.addLast(new ClientSocketHandler());
                    }
                });
        connect(new InetSocketAddress(host, port));
    }

    /**
     * 连接host和port的后端socket服务
     *
     * @param isa socket网络服务地址对象
     */
    public void connect(InetSocketAddress isa) {
        bootstrap
                .connect(isa)
                .addListener((ChannelFutureListener) futureListener -> {
                    if (futureListener.isSuccess()) {
                        System.err.println("连接服务成功: address:{},{}"+ isa.getHostName()+ isa.getPort());
                    } else {
                        // 否则每隔10s重新连接
                        System.err.println("连接服务失败,将在10s后重连 ----- host: {}, port: {}"+ isa.getHostName()+isa.getPort());
                        futureListener.channel().eventLoop().schedule(() -> connect(isa), 10, TimeUnit.SECONDS);
                    }
                });

    }

    public static void main(String[] args) {
        String ip = "192.168.0.157";
        int port = 6901;
        ClientSocket clientSocket = new ClientSocket();
        clientSocket.init(ip,port);
    }
}
