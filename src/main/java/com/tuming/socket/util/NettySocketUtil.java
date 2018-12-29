package com.tuming.socket.util;


import com.alibaba.fastjson.JSON;
import com.tuming.socket.bean.DeserializeEntity;
import com.tuming.socket.bean.SocketRequestEntity;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.io.UnsupportedEncodingException;

/**
 * @author cc;
 * @since 2017/12/27;
 */
public class NettySocketUtil {
    // 非协议内容的length,即起始符号,结束符号,协议类型和协议内容长度之和
    private static final int NOT_DATA_LENGTH = 10;


    /**
     * <p>序列化数据,封装成帧;</p>
     * 协议定义: <br>
     * 协议起始符号: *, 1 byte <br>
     * 协议类型: int, 4 byte, <br>
     * 协议内容长度: int, 4 byte <br>
     * 协议内容: byte, 不定长 <br>
     * 协议结束符: #, 1 byte <br>
     * 编码类型: UTF-8
     *
     * @param channel 发送数据的通道
     * @param data    发送的数据内容
     * @param bizType 命令类型
     * @return
     */
    public static void serializeAndSend(Channel channel, SocketRequestEntity data, int bizType) {
        // 序列化后的总字节长度
        byte[] content = JSON.toJSONBytes(data);
        int msgLength = NOT_DATA_LENGTH + content.length;

        ByteBuf buf = channel.alloc().buffer(msgLength);
        try {
            // 协议头,开头符: * ,一个字节
            buf.writeBytes(BusinessType.START_FLAG.getBytes("utf-8"));
            // 功能命令类型,例:104 表示获取实时浏览图片,int类型,4字节
            buf.writeInt(bizType);
            // 负载长度,协议头+负载内容长度+协议尾,int类型,4字节
            buf.writeInt(content.length);
            // 负载内容
            buf.writeBytes(content);
            // 协议尾,结束符: # , 一个字节
            buf.writeBytes(BusinessType.END_FLAG.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 反序列化数据,生成DeserializeEntity实例.
     * @param byteBuf 字节码
     * @return
     */
    public static DeserializeEntity deserialize(ByteBuf byteBuf) {
        // 减去协议头协议尾和协议码
        int len = byteBuf.readableBytes() - NOT_DATA_LENGTH;
        // 获取协议码
        int bizType = byteBuf.getInt(1);
        // 读取数据内容
        byte[] bytes = new byte[len];
        byteBuf.skipBytes(9).readBytes(bytes);
        String content = null;
        try {
            content = new String(bytes,"utf-8");
            return new DeserializeEntity(bizType,content);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        // 返回实例
    }


    public static <T> SocketRequestEntity<T> getRequestEntity(String bizName, T bean) {
        return new SocketRequestEntity<T>(bizName,  bean);
    }


}
