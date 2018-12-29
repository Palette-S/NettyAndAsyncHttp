package com.tuming.socket.bean;

import lombok.Data;

@Data
public class RealTimePhoto {


    private String imageData;
    private String createTime;
    private String cameraIp;
    private String cameraName;

}
