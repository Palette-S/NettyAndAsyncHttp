package com.tuming.socket.util;

import com.tuming.socket.bean.Base64ImageEntity;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.codec.binary.Base64;

public class ImageBase64Utils {
	 
	/**
	 *
	 * 图片url地址转base64String编码
	 * @author cc
	 * @since 2018-07-04 12:27:00
	 * @param imgUrl 图片地址
	 * @return base64 字符串
	 */
	public static Base64ImageEntity generateBase64ImageEntity(String imgUrl){
		byte[] data =null;
		try {
			URL url = new URL(imgUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			//设置超时间为3秒
			conn.setConnectTimeout(5*1000);
			//防止屏蔽程序抓取而返回403错误
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			InputStream inStream = conn.getInputStream();
			data = readInputStream(inStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		String base64 = new Base64().encodeToString(data);
		String imgType = imgUrl.substring(imgUrl.lastIndexOf("."));
		return new Base64ImageEntity(base64,imgType);
	}
	private static byte[] readInputStream(InputStream inStream) throws Exception{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		//创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		//每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		//使用一个输入流从buffer里把数据读取出来
		while( (len=inStream.read(buffer)) != -1 ){
			//用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		//关闭输入流
		inStream.close();
		//把outStream里的数据写入内存
		return outStream.toByteArray();
	}



}
