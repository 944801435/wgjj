package com.brilliance.base.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class OcrV3Util {

    private static Logger logger = LoggerFactory.getLogger(OcrV3Util.class);

    private static final String YOUDAO_URL = "https://openapi.youdao.com/ocrapi";

    private static final String APP_KEY = "6558d6d60c2b9c52";

    private static final String APP_SECRET = "AxdOLyR3b4eSunx94KDqLv2mTThLsFyw";

    public static String ocrDistinguish(String filePath) throws IOException {
//    	public static void main(String[] filePath) throws IOException {

        Map<String,String> params = new HashMap<String,String>();
//        String q = loadAsBase64("D:/2021XinchenDownload/uploadFile/202112/20211213/阿尔及利亚飞行资料信息31346.bmp");
        String q = loadAsBase64(filePath);
        String salt = String.valueOf(System.currentTimeMillis());
        String detectType = "10012";
        String imageType = "1";
        String langType = "auto";
        params.put("detectType", detectType);
        params.put("imageType", imageType);
        params.put("langType", langType);
        params.put("img", q);
        params.put("docType", "json");
        params.put("signType", "v3");
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        params.put("curtime", curtime);
        String signStr = APP_KEY + truncate(q) + salt + curtime + APP_SECRET;
        String sign = getDigest(signStr);
        params.put("appKey", APP_KEY);
        params.put("salt", salt);
        params.put("sign", sign);
        String result = requestForHttp(YOUDAO_URL,params);
        /** 处理结果 */
        StringBuilder stringBuilder = new StringBuilder();
        logger.info("OCR处理结果："+result);
        try {
            JSONObject jsonObject = JSONObject.parseObject(result);
            String errorCode = jsonObject.getString("errorCode");
            JSONObject jsonArray = jsonObject.getJSONObject("Result");
            JSONArray jsonArray2 = jsonArray.getJSONArray("regions");
            if("0".equals(errorCode)&&jsonArray2!=null){
            for (int i = 0; i <jsonArray2.size(); i++) {
                JSONObject jsonArrayObjectItem = JSONObject.parseObject(jsonArray2.get(i).toString());
                JSONArray jsonTwoObj = jsonArrayObjectItem.getJSONArray("lines");
                for (int j = 0; j <jsonTwoObj.size(); j++) {
                	JSONObject jsonObjectItem = JSONObject.parseObject(jsonTwoObj.get(j).toString());
                if(jsonObjectItem!=null){
                	stringBuilder.append(jsonObjectItem.get("text"));
                }
                }
            }
            }else{
            	stringBuilder.append("暂无结果，请稍后再试!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static String requestForHttp(String url,Map<String,String> params) throws IOException {
        String result = "";

        /** 创建HttpClient */
        CloseableHttpClient httpClient = HttpClients.createDefault();

        /** httpPost */
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        Iterator<Map.Entry<String,String>> it = params.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,String> en = it.next();
            String key = en.getKey();
            String value = en.getValue();
            paramsList.add(new BasicNameValuePair(key,value));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(paramsList,"UTF-8"));
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        try{
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity,"UTF-8");
            EntityUtils.consume(httpEntity);
        }finally {
            try{
                if(httpResponse!=null){
                    httpResponse.close();
                }
            }catch(IOException e){
                logger.info("## release resouce error ##" + e);
            }
        }
        return result;
    }

    /**
     * 生成加密字段
     */
    public static String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes();
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String loadAsBase64(String imgFile)
  {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理

    File file = new File(imgFile);
    if(!file.exists()){
        logger.error("文件不存在");
        return null;
    }
    InputStream in = null;
    byte[] data = null;
      //读取图片字节数组
    try
    {
        in = new FileInputStream(imgFile);
        data = new byte[in.available()];
        in.read(data);
        in.close();
    }
    catch (IOException e)
    {
        e.printStackTrace();
    }
      //对字节数组Base64编码
    return Base64.encodeBase64String(data);
    //.getEncoder().encodeToString(data);//返回Base64编码过的字节数组字符串
  }

  public static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        String result;
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }
}