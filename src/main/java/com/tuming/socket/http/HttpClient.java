package com.tuming.socket.http;

import com.alibaba.fastjson.JSONObject;
import com.tuming.socket.bean.RealTimeImage;
import com.tuming.socket.bean.RealTimePhoto;
import com.tuming.socket.util.ImageBase64Utils;
import okhttp3.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class HttpClient {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
    public RealTimePhoto realTimePhoto = new RealTimePhoto();
    public void okHttpPost(String json){
        // 创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        // 创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(JSON,json);

        // 创建一个请求对象
        Request request = new Request.Builder()
                .url("http://41.51.13.186:8080/smx_by/service/flow/tuming/receive")
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        System.out.println("发送了...");
        call.enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                System.err.println("失败");
            }

            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                System.err.println(str);
            }
        });
    }
    public void sendRealTimeImage(RealTimeImage realTimeImage){
        String base = ImageBase64Utils.generateBase64ImageEntity(realTimeImage.getImageUrl()).getImageData();
        String time = realTimeImage.getTime().substring(0,14);
        try {
            time = dateFormat.format(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        realTimePhoto.setCameraIp(String.valueOf(realTimeImage.getCameraId()));
        realTimePhoto.setCameraName("");
        realTimePhoto.setCreateTime(time);
        realTimePhoto.setImageData(base);
        okHttpPost(JSONObject.toJSONString(realTimePhoto));
    }

}
