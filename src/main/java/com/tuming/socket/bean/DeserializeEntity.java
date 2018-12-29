package com.tuming.socket.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xieyingchao
 * @data 2018/12/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeserializeEntity {
    private int bizType;
    private String data;
}
