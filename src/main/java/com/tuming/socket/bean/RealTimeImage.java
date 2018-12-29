package com.tuming.socket.bean;

import lombok.Data;

@Data
public class RealTimeImage {

    private Integer  cameraId;
    private String featureData;
    private Integer featureId;
    private String imageUrl;
    private Integer pitch;
    private String rect;
    private Integer roll;
    private String time;
    private Integer yaw;


}
