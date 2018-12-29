package com.tuming.socket.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * socket配置参数实例类,获取相关配置参数
 *
 * @author cc;
 * @since 2017/12/19;
 * @version 1.0
 */
@Getter
@Setter
public class TcpSocketProperties {

    /**
     * socket对接后端的ip地址
     */
    private String ip;
    /**
     * socket对接后端port
     * key: 功能名
     * value: 端口值
     */
    private Map<String,Integer> port;

    private Integer heartbeat;




}
