package com.uav.base.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public class StreamUtil {
	private static final Logger log = Logger.getLogger(StreamUtil.class);
	public static final int BUF_SIZE = 10 * 1024;

	static Base64 base64 = new Base64();

	/**
	 * 由文件流转换成String字符串
	 * 
	 * @return
	 */
	public static String inputStreamToString(InputStream in) {
		
		try {
			 ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
		     byte[] data = new byte[BUF_SIZE];  
		     int count = -1;  
		     while((count = in.read(data,0,BUF_SIZE)) != -1){
		            outStream.write(data, 0, count);  
		     }
		     data = null;  
		     return base64.encodeToString(outStream.toByteArray()).trim(); 
		} catch (Exception e) {
//			e.printStackTrace();
			log.error("StreamUtil.inputStreamToString(InputStream in)出现异常",e);
		}
		return null;
		
	}

	/**
	 * 由字符串转换成文件流
	 * 
	 * @param base64String
	 */
	public static InputStream stringToInputStream(String base64String) {
		try {
			byte bytes[] = base64.decode(base64String);
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			return bais;
		} catch (Exception e) {
			log.error("StreamUtil.stringToInputStream(String base64String)出现异常",e);
//			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 把字节数组转换成16进制字符串
	 * @param bArray
	 * @return
	 */
	public static String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString((int) bArray[i] & 0xff);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp);
		}
		return sb.toString().toUpperCase();
	} 
	
	/**
	 * 
	 * 16进制转为byte[]形式(return null : 表明函数调用失败；数据可能不是0-F;)
	 * @Title: hexString2Bytes 
	 * @param @param src
	 * @param @return    
	 * @return byte[] 返回类型 
	 * @throws
	 */
	public static byte[] hexString2Bytes(String src) {
		try {
			int n = src.length() / 2;
			byte[] ret = new byte[n];
			byte[] tmp = src.getBytes();
			for (int i = 0; i < n; ++i) {
				ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return new byte[0];
		}

	}
	
	/**
	 * 将两个byte合并成一个byte(第一个byte占高8位，第二个byte占低8位)
	 * @param src0
	 * @param src1
	 * @return
	 */
	private static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte ret = (byte) (_b0 | _b1);
		return ret;
	}
	

}
