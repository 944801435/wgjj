package com.uav.base.util;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

/**
 * 实现FTP文件上传和文件下载
 * @ClassName: FtpApche
 * @Description: 
 * @author 钟志峰
 * @date 2018年9月28日 上午9:09:26
 */
public class FtpApche {
	
	private static final Logger log = Logger.getLogger(FtpApche.class);
	
    private static FTPClient ftpClient = new FTPClient();
    
    private static String encoding = System.getProperty("file.encoding");
    /**
     * Description: 向FTP服务器上传文件
     * 
     * @Version1.0
     * @param url
     *            FTP服务器hostname
     * @param port
     *            FTP服务器端口
     * @param username
     *            FTP登录账号
     * @param password
     *            FTP登录密码
     * @param path
     *            FTP服务器保存目录,如果是根目录则为“/”
     * @param filename
     *            上传到FTP服务器上的文件名
     * @param input
     *            本地文件输入流
     * @return 成功返回true，否则返回false
     */
    public static boolean uploadFile(String url, int port, String username,
            String password, String path, String filename, InputStream input) {
        boolean result = false;

        try {
            int reply;
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
//            ftpClient.connect(url);
            ftpClient.connect(url, port);// 连接FTP服务器
            // 登录
            ftpClient.login(username, password);
            
            ftpClient.setControlEncoding(encoding);
            // 检验是否连接成功
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
            	log.error("FTP连接失败");
                ftpClient.disconnect();
                return result;
            }

            // 转移工作目录至指定目录下
            boolean change = ftpClient.changeWorkingDirectory(path);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            if (change) {
                result = ftpClient.storeFile(new String(filename.getBytes(encoding),"iso-8859-1"), input);
                if (result) {
                	log.error("上传成功!");
                }
            }
            input.close();
            ftpClient.logout();
        } catch (IOException e) {
        	log.error("FTP异常",e);
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                	log.error("FTP异常",ioe);
                }
            }
        }
        return result;
    }

    /**
     * Description: 从FTP服务器下载文件
     * 
     * @Version1.0
     * @param url
     *            FTP服务器hostname
     * @param port
     *            FTP服务器端口
     * @param username
     *            FTP登录账号
     * @param password
     *            FTP登录密码
     * @param remotePath
     *            FTP服务器上的相对路径
     * @param fileName
     *            要下载的文件名
     * @param localPath
     *            下载后保存到本地的路径
     * @return
     */
    public static byte[] downFile(String url, int port, String username,
            String password, String remotePath, String fileName) {
        try {
            int reply;
            ftpClient.setControlEncoding(encoding);
            ftpClient.connect(url, port);
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftpClient.login(username, password);// 登录
            // 设置文件传输类型为二进制
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 获取ftp登录应答代码
            reply = ftpClient.getReplyCode();
            // 验证是否登陆成功
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                log.info("FTP server refused connection.");
                return null;
            }
            // 转移到FTP服务器目录至指定的目录下
            ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(encoding),"iso-8859-1"));
            // 获取文件列表
            FTPFile[] fs = ftpClient.listFiles();
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                	
                	ByteArrayOutputStream swapStream = new ByteArrayOutputStream(); 
                	
                	InputStream inStream =  ftpClient.retrieveFileStream(fileName);
                	
                	byte[] buff = new byte[100]; //buff用于存放循环读取的临时数据 
                	int rc = 0; 
                	while ((rc = inStream.read(buff, 0, 100)) > 0) { 
                	swapStream.write(buff, 0, rc); 
                	} 
                	return swapStream.toByteArray(); //in_b为转换之后的结果 
                	
                }
            }
            ftpClient.logout();
        } catch (IOException e) {
           log.error("FTP异常",e);
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                	 log.error("FTP异常",ioe);
                }
            }
        }
        return null;
    }
}