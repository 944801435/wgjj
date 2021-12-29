package com.brilliance.base.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import com.brilliance.base.common.BaseDAO;
import com.brilliance.base.common.Constants;
import com.brilliance.base.model.SysFile;

import net.coobird.thumbnailator.Thumbnails;

public class FileUtil {
	private static final Log log = LogFactory.getLog(FileUtil.class);

	public static String uploadFile(MultipartFile file, String deposeFilesDir) throws IOException{
		if (file.isEmpty()) {
			System.out.println("该文件无任何内容!!!");
		}
		// 获取附件原名(有的浏览器如chrome获取到的是最基本的含 后缀的文件名,如myImage.png)
		// 获取附件原名(有的浏览器如ie获取到的是含整个路径的含后缀的文件名，如C:\\Users\\images\\myImage.png)
		String fileName = file.getOriginalFilename();
		// 如果是获取的含有路径的文件名，那么截取掉多余的，只剩下文件名和后缀名
		if (fileName.indexOf("\\") > 0) {
			int index = fileName.lastIndexOf("\\");
			fileName = fileName.substring(index + 1);
		}
		// 判断单个文件大于1M
		long fileSize = file.getSize();
//		if (fileSize > 1024 * 1024) {
//			System.out.println("文件大小为(单位字节):" + fileSize);
//			System.out.println("该文件大于1M");
//		}
		// 当文件有后缀名时
		if (fileName.indexOf(".") >= 0) {
			// split()中放正则表达式; 转义字符"\\."代表 "."
			String[] fileNameSplitArray = fileName.split("\\.");
			// 加上random戳,防止附件重名覆盖原文件
			fileName = fileNameSplitArray[0] + (int) (Math.random() * 100000) + "." + fileNameSplitArray[1];
		}
		// 当文件无后缀名时(如C盘下的hosts文件就没有后缀名)
		if (fileName.indexOf(".") < 0) {
			// 加上random戳,防止附件重名覆盖原文件
			fileName = fileName + (int) (Math.random() * 100000);
		}
		System.out.println("fileName:" + fileName);
		// 根据文件的全路径名字(含路径、后缀),new一个File对象dest
		File dest = new File(deposeFilesDir +File.separator+ fileName);
		// 如果pathAll路径不存在，则创建相关该路径涉及的文件夹;
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		try {
			// 将获取到的附件file,transferTo写入到指定的位置(即:创建dest时，指定的路径)
			file.transferTo(dest);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	  }
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void download(String fileId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysFile file = null;
		try {
			BaseDAO baseDAO = (BaseDAO) SpringContextUtil.getBean("baseDAO");
			file = (SysFile) baseDAO.findById(SysFile.class, fileId);
		} catch (Exception e) {
			log.error("下载文件失败！", e);
			throw e;
		}

		download(file, request, response);
	}

	public static void download(SysFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InputStream in = null;
		OutputStream out = null;
		try {
			response.reset();
			long fileLen = file.getFileSize();
			response.setHeader("Content-Length", Long.toString(fileLen));
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getFileName(), "utf-8"));
			response.setContentType("application/x-msdownload");

			in = new ByteArrayInputStream(file.getFileBytes());
			out = response.getOutputStream();
			byte[] b = new byte[1024];
			int len = -1;
			while ((len = in.read(b, 0, 1024)) != -1) {
				out.write(b, 0, len);
			}
			out.flush();
		} catch (Exception e) {
			String simplename = e.getClass().getSimpleName();
			if (!"ClientAbortException".equals(simplename)) {
				log.error("下载文件失败！", e);
				throw e;
			}
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (Exception e) {
			}
			try {
				if (in != null)
					in.close();
			} catch (Exception e) {
			}
		}
	}

	public static String uploadToDb(InputStream in, String fileName, String contentType, String userId) throws IOException {
		try {
			SessionFactory sessionFactory = (SessionFactory) SpringContextUtil.getBean("sessionFactory");
			Session sess = sessionFactory.getCurrentSession();
			int fileSize = in.available();
			SysFile file = new SysFile();
			file.setFileName(fileName);
			file.setFileSize(fileSize);
			file.setUploadTime(DateUtil.getNowFullTimeString());
			file.setFilePath(Constants.STR_BLANK);
			file.setContentType(contentType);
			file.setUploadUser(userId);
			byte[] bytes = toByteArray(in);
			if (bytes != null) {
				file.setFileBytes(bytes);
			} else {
				file.setFileBytes(new byte[1]);
			}
			file.setSaveType(Constants.SYS_FILE_DB);
			return (String) sess.save(file);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (Exception e) {
			}
		}
	}

	public static String uploadToDb(int fileSize, byte[] bytes, String fileName, String contentType, String userId) {
		SessionFactory sessionFactory = (SessionFactory) SpringContextUtil.getBean("sessionFactory");
		Session sess = sessionFactory.getCurrentSession();
		SysFile file = new SysFile();
		file.setFileName(fileName);
		file.setFileSize(fileSize);
		file.setUploadTime(DateUtil.getNowFullTimeString());
		file.setUploadUser(userId);
		file.setFilePath(Constants.STR_BLANK);
		file.setContentType(contentType);
		file.setFileBytes(bytes);
		file.setSaveType(Constants.SYS_FILE_DB);
		String fileId = (String) sess.save(file);
		return fileId;
	}

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = null;
		try {
			output = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int n = 0;
			while (-1 != (n = input.read(buffer))) {
				output.write(buffer, 0, n);
			}
			return output.toByteArray();
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

	// 上传文件并压缩
	public static Map<String, String> uploadFileToDb(MultipartFile file, String userId, String extension) throws Exception {
		InputStream in = null;
		ByteArrayOutputStream out = null;
		byte[] bytes = null;
		try {
			in = file.getInputStream();
			out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			bytes = out.toByteArray();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (Exception e2) {
			}
			try {
				if (in != null)
					in.close();
			} catch (Exception e2) {
			}
		}

		// 保存原图到数据库
		String fileId = FileUtil.uploadToDb(bytes.length, bytes, file.getOriginalFilename(), file.getContentType(), userId);

		// 压缩原图得到小图
		String smallFileId = null;
		int targetk = 50;
		int lenk = bytes.length / 1024;
		if (lenk > targetk) {
			// 进行压缩
			int rat = targetk * 100 / lenk;
			ByteArrayInputStream is = new ByteArrayInputStream(bytes); // 将b作为输入流；
			BufferedImage img = ImageIO.read(is);
			img = Thumbnails.of(img).scale(rat / 100.0).outputQuality(1f).asBufferedImage();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(img, extension, os);
			bytes = os.toByteArray();
			smallFileId = FileUtil.uploadToDb(bytes.length, bytes, file.getOriginalFilename(), file.getContentType(), userId);
		} else {
			smallFileId = fileId;
		}

		Map<String, String> rtnMap = new HashMap<String, String>();
		rtnMap.put("fileId", fileId);
		rtnMap.put("smallFileId", smallFileId);
		return rtnMap;
	}

	// 上传文件并压缩
	public static String uploadFileToDb(MultipartFile file, String userId) throws Exception {
		InputStream in = null;
		ByteArrayOutputStream out = null;
		byte[] bytes = null;
		try {
			in = file.getInputStream();
			out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			bytes = out.toByteArray();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (Exception e2) {
			}
			try {
				if (in != null)
					in.close();
			} catch (Exception e2) {
			}
		}

		// 保存原图到数据库
		String fileId = FileUtil.uploadToDb(bytes.length, bytes, file.getOriginalFilename(), file.getContentType(), userId);

		return fileId;
	}

	/**
	 * 上传文件
	 * 
	 * @Title: uploadFileToDb
	 * @author mq
	 * @date 2021年11月11日 上午10:35:45
	 * @param file
	 * @param userId
	 * @return
	 * @throws IOException
	 */
	public static String uploadFileToDb(File file, String userId) throws IOException {
		byte[] bytes = getBytesByFile(file);
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String contentType = fileNameMap.getContentTypeFor(file.getName());
		// 保存原图到数据库
		return FileUtil.uploadToDb(bytes.length, bytes, file.getName(), contentType, userId);
	}

	/**
	 * 读文件
	 * 
	 * @Title: readFile
	 * @author maqian
	 * @date 2019年6月25日 上午10:44:21
	 * @param filePath
	 * @return
	 * @throws IOException 
	 */
	public static String readFile(String filePath) throws IOException {
		File file = new File(filePath);
		return readFile(file);
	}

	public static String readFile(File file) throws IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = null;
		Reader fileReader = null;
		String tempString = "";
		try {
			fileReader = new FileReader(file);
			reader = new BufferedReader(fileReader);
			while ((tempString = reader.readLine()) != null) {
				sb.append(tempString);
			}
			reader.close();
			fileReader.close();
		} catch (Exception e) {
			log.error("读文件出现异常！", e);
			throw e;
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (fileReader != null) {
					fileReader.close();
				}
			} catch (IOException e) {
				log.error("读文件出现异常！", e);
				throw e;
			}
		}
		return sb.toString();
	}

	/**
	 * 写文件
	 * 
	 * @Title: writeFile
	 * @author maqian
	 * @date 2019年6月28日 上午9:14:34
	 * @param content
	 * @param filePath
	 * @throws IOException 
	 */
	public static void writeFile(byte[] content, String filePath) throws IOException {
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(new File(filePath));
			os.write(content);
			os.close();
		} catch (Exception e) {
			log.error("写文件出现异常！", e);
			throw e;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				log.error("写文件出现异常！", e);
				throw e;
			}
		}
	}

	public static void download(String filename, HttpServletResponse response) throws IOException {
		BufferedInputStream in = null;
		OutputStream out = null;
		try {
			File file = new File(filename);
			out = response.getOutputStream();
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "utf-8"));
			byte[] buffer = new byte[1024];

			in = new BufferedInputStream(new FileInputStream(file));
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
				out.flush();
			}
		} catch (Exception e) {
			log.error("读文件出现异常！", e);
			throw e;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				log.error("读文件出现异常！", e);
				throw e;
			}
		}
	}

	/**
	 * 将File转成字节byte[]
	 * 
	 * @Title: getBytesByFile
	 * @author mq
	 * @date 2021年11月10日 下午5:36:11
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	public static byte[] getBytesByFile(File file) throws IOException {
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			// 获取输入流
			fis = new FileInputStream(file);
			// 新的 byte 数组输出流，缓冲区容量1024byte
			bos = new ByteArrayOutputStream(1024);
			// 缓存
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			// 改变为byte[]
			byte[] data = bos.toByteArray();
			return data;
		} catch (IOException e) {
			log.error("将File转成字节失败！", e);
			throw e;
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (Exception e) {
				log.error("将File转成字节失败！", e);
			}
		}
	}

	/**
	 * 将字节转成流
	 * 
	 * @Title: byte2Input
	 * @author mq
	 * @date 2021年11月15日 下午5:42:48
	 * @param buf
	 * @return
	 */
	public static final InputStream byte2Input(byte[] buf) {
		return new ByteArrayInputStream(buf);
	}

	@SuppressWarnings({ "resource", "rawtypes", "unchecked" })
	public static void main(String[] args) throws IOException {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		BaseDAO baseDao = (BaseDAO) context.getBean("baseDAO");
		SysFile file = (SysFile) baseDao.findById(SysFile.class, "0000000000");
		writeFile(file.getFileBytes(), "d:/" + file.getFileName());
	}
}