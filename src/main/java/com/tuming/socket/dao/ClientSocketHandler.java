package com.tuming.socket.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tuming.socket.bean.DeserializeEntity;
import com.tuming.socket.bean.Heartbeat;
import com.tuming.socket.bean.RealTimeImage;
import com.tuming.socket.bean.SocketRequestEntity;
import com.tuming.socket.http.HttpClient;
import com.tuming.socket.util.BusinessType;
import com.tuming.socket.util.NettySocketUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@ChannelHandler.Sharable
public class ClientSocketHandler extends SimpleChannelInboundHandler<ByteBuf> {

    public static ConcurrentHashMap<InetSocketAddress, Channel> channelMap = new ConcurrentHashMap<InetSocketAddress, Channel>();

    public static final int MONITOR_PORT = 6901;

    private ClientSocket clientSocket = new ClientSocket();
    private HttpClient httpClient = new HttpClient();

    public ClientSocket getClientSocket() {
        return clientSocket;
    }
    public void setClientSocket(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        // 服务器返回的数据,反序列化
        DeserializeEntity entity = NettySocketUtil.deserialize(byteBuf);
        int bizType = entity.getBizType();
        String data = entity.getData();
        switch (bizType) {
            case BusinessType.MONITOR_HEARTBEAT:
                // 接收到心跳  返回心跳
                break;
            case BusinessType.MONITOR_IMAGE:
                // 实时监控处理
                SocketRequestEntity<RealTimeImage> requestEntity = JSON.parseObject(data, new TypeReference<SocketRequestEntity<RealTimeImage>>() {});
                httpClient.sendRealTimeImage(requestEntity.getReqInfo());
                break;
            default:
                break;
        }

    }

    /**
     * @Descriptions 用户事件触发
     * @param ctx  通道句柄上下文
     * @param evt  事件
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }
    /**
     * @Descriptions 客户端发送心跳检测ping
     * @param ctx
     */
    protected void sendPingMsg(ChannelHandlerContext ctx, int bizType) {
        // 新建一个心跳请求
        SocketRequestEntity entity =
                NettySocketUtil.getRequestEntity(BusinessType.HEARTBEAT_REQUEST, new Heartbeat(60, new Date()));

        // 序列化并发送请求
        NettySocketUtil.serializeAndSend(ctx.channel(), entity, bizType);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        InetSocketAddress isa = (InetSocketAddress) channel.remoteAddress();
        channelMap.put(isa, channel);
    }


    /**
     * 当触发了通道没有激活的状态,则重连.
     * @param ctx 通道句柄上下文接口,关联着channel和pipeline
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        InetSocketAddress isa = (InetSocketAddress) ctx.channel().remoteAddress();
        channelMap.remove(isa);
        clientSocket.connect(isa);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        System.err.println("连接实时服务失败{},请检查!"+ctx.channel().remoteAddress());
    }


    /**
     * 如果长时间无读写操作,则发送心跳
     *
     * @param ctx
     */
    protected void handleAllIdle(ChannelHandlerContext ctx) {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        int port = inetSocketAddress.getPort();
        // 根据channel连接 端口确认不同业务,然后发送不同的业务心跳
        switch (port) {
            case MONITOR_PORT:
                sendPingMsg(ctx, BusinessType.MONITOR_HEARTBEAT);
                break;
            default:
                break;
        }
    }

    protected void handleReaderIdle(ChannelHandlerContext ctx) {
        System.err.println("READER_IDLE---------读空闲");
    }

    protected void handleWriterIdle(ChannelHandlerContext ctx) {
        System.err.println("WRITER_IDLE---------写空闲");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


}
