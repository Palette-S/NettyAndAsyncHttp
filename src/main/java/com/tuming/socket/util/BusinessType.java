package com.tuming.socket.util;

/**
 * socket接口功能码
 *
 * @author cc;
 * @since 2017/12/27;
 */
public class BusinessType {
    /**
     * 接口数据开始标记
     */
    public static final String START_FLAG = "*";
    /**
     * 接口数据结束标记
     */
    public static final String END_FLAG = "#";

    /**
     * 心跳功能名称
     */
    public static final String HEARTBEAT_REQUEST = "keepAlive";

    /**
     * 实时监控注册
     */
    public static final int MONITOR_REGISTER = 101;

    /**
     * 实时监控移除注册
     */
    public static final int MONITOR_UNREGISTER = 102;

    /**
     * 实时监控 心跳
     */
    public static final int MONITOR_HEARTBEAT = 103;

    /**
     * 实时监控获取图片流
     */
    public static final int MONITOR_IMAGE = 104;


    /**
     * 实时报警心跳
     */
    public static final int ALARM_HEARTBEAT = 301;

    /**
     * 实时报警返回信息
     */
    public static final int ALARM_RESPONSE = 302;
    /**
     * 布控报警心跳
     */
    public static final int BUKONG_HEARTBEAT = 201;
    /**
     * 布控报警服务码
     */
    public static final int BUKONG_IMAGEBK = 202;

    public static final String BUKONG_REQUEST = "imageBK";


    /**
     * 撤控
     */
    public static final int BUKONG_IMAGEUNBK = 203;
    public static final String UNBUKONG_REQUEST = "imageUnBK";

    /**
     * 注册业务的名称
     */
    public static final String REGISTER_REQUEST = "register";
}
