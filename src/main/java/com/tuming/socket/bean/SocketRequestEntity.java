package com.tuming.socket.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cc;
 * @since 2018/3/15;
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocketRequestEntity<T> {
    /**
     * 请求类型
     */
    private String request;

    /**
     * 请求数据
     */
    private T reqInfo;

}
