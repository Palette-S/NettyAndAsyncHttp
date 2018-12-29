package com.tuming.socket.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import java.util.Date;

/**
 * @author cc;
 * @since 2017/12/27;
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Heartbeat {
    private Integer keepAlivePeriod;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date currentTime;
}