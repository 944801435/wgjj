package com.brilliance.web.view;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.brilliance.base.common.SystemWebLog;
import com.brilliance.base.listener.LoginAccess;
import com.brilliance.base.util.PropertiesUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Controller
@LoginAccess
@SystemWebLog(menuName = "文件上传")
@Slf4j
public class FileController {
    //本地使用,上传位置
    String rootPath = PropertiesUtil.getPropertyValue("file.upload.path","");
    //上传文件会自动绑定到MultipartFile中
    /*@RequestMapping("/upload")
    @ResponseBody
    public Map upload(@RequestParam("file") MultipartFile file){
        Map<String,Object> result=new HashMap<String,Object>();
        try {

            //String rootPath ="/www/uploads/";
            //文件的完整名称,如spring.jpeg
            String filename = file.getOriginalFilename();
            //文件名,如spring
            String name = filename.substring(0,filename.indexOf("."));
            //文件后缀,如.jpeg
            String suffix = filename.substring(filename.lastIndexOf("."));
            //创建年月文件夹
            Calendar date = Calendar.getInstance();
            File dateDirs = new File(date.get(Calendar.YEAR)
                    + File.separator + (date.get(Calendar.MONTH)+1));
            //目标文件
            File descFile = new File(rootPath+File.separator+"upload"+File.separator+dateDirs+File.separator+filename);
            int i = 1;
            //若文件存在重命名
            String newFilename = filename;
            while(descFile.exists()) {
                newFilename = name+"("+i+")"+suffix;
                descFile = new File(rootPath+File.separator+dateDirs+File.separator+newFilename);
                i++;
            }
            //判断目标文件所在的目录是否存在
            if(!descFile.getParentFile().exists()) {
                //如果目标文件所在的目录不存在，则创建父目录
                descFile.getParentFile().mkdirs();
            }
            //将内存中的数据写入磁盘
            file.transferTo(descFile);
            //完整的url
            String fileUrl =  File.separator+"upload"+File.separator+dateDirs+ File.separator +newFilename;

            Map<String,Object> dataMap=new HashMap<String,Object>();
                dataMap.put("fileUrl",fileUrl);
                dataMap.put("fileName",filename);
            result.put("code",10001);
            result.put("message","上传成功！");
            result.put("data",dataMap);
            return result;
        }catch (Exception e){
            result.put("code",10000);
            result.put("message","上传失败！");
            result.put("data","");
        }
        return result;
    }*/

    /**
     * 文件下载/预览
     */
    @RequestMapping("preview")
    public void preview(String path, HttpServletResponse response) throws IOException {
        //本地使用,上传位置
        File file = new File(rootPath, path);
        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "文件未找到！");
            return;
        }
        /*
         * 获取mimeType
         */
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        response.setContentType(mimeType);
        response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode(file.getName(), "UTF-8")));
        response.setContentLength((int) file.length());

        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        }
    }

    /**
     * 在线预览
     * @param path
     * @param response
     * @throws IOException
     */
    @RequestMapping("onlinePreview")
    public void onlinePreview(String path, HttpServletResponse response) throws IOException {
        File file = new File(rootPath, path);
        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "文件未找到！");
            return;
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));
        byte[] bs = new byte[1024];
        int len = 0;
        response.reset(); // 非常重要
        URL u = new URL("file:///" + file);
        String contentType = u.openConnection().getContentType();
        response.setContentType(contentType);
        response.setHeader("Content-Disposition", "inline;filename="
                + file.getName());
        OutputStream out = response.getOutputStream();
        while ((len = br.read(bs)) > 0) {
            out.write(bs, 0, len);
        }
        out.flush();
        out.close();
        br.close();
    }
}
