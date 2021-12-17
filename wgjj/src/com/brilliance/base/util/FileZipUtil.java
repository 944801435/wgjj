package com.brilliance.base.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.activation.MimetypesFileTypeMap;

import org.springframework.web.multipart.MultipartFile;

import com.brilliance.base.model.SysFile;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class FileZipUtil {

	private static final String realPath = FileZipUtil.class.getResource("/").getPath();

	public static void main(String[] args) throws IOException {
		System.out.println(realPath);
//		File file = new File("D:\\work\\WGJJ\\docs\\接口文档\\测试文件\\测试文件.zip");
		String type = new MimetypesFileTypeMap().getContentType("a.png");
		System.out.println("第一种: "+type);
		
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		type = fileNameMap.getContentTypeFor("a.png");
		System.out.println("第二种：" + type);
//		System.out.println(file.getParent());
//		List<File> fileList;
//		fileList = unZip(FileUtil.getBytesByFile(file));
//		for (File f : fileList) {
//			System.out.println(f.getName());
//		}
//		File file = new File("D:\\work\\WGJJ\\docs\\接口文档\\测试文件\\testZip");
//		File[] fileList = file.listFiles();
//		for (File f : fileList) {
//			System.out.println(f.getName());
//			
//			String type = new MimetypesFileTypeMap().getContentType(f);
//			System.out.println("第二种javax.activation: "+type);
//		}
//		File zipFile = zip("202111111002123.zip", fileList);
//		System.out.println(zipFile.getName());
	}

	/**
	 * 解压压缩包
	 * 
	 * @Title: unZip
	 * @author mq
	 * @date 2021年11月11日 上午9:35:03
	 * @param data
	 * @return
	 * @throws IOException 
	 */
	public static List<File> unZip(byte[] data) throws IOException {
		System.out.println(realPath);
		List<File> fileList = new ArrayList<File>();
		ZipInputStream zipStream = null;
		BufferedInputStream is = null;
		FileOutputStream out = null;
		String uuid = UUID.randomUUID().toString();
		String tempPath = realPath + File.separator + uuid + File.separator;
		File tempPathFile = new File(tempPath);
		if (!tempPathFile.exists()) {
			tempPathFile.mkdir();
		}
		try {
			ZipEntry entry;
			zipStream = new ZipInputStream(new ByteArrayInputStream(data));
			while ((entry = zipStream.getNextEntry()) != null) {
				File tempFile = new File(tempPath + entry.getName());
				out = new FileOutputStream(tempFile);
				byte[] byteBuff = new byte[4096];
				int bytesRead = 0;
				while ((bytesRead = zipStream.read(byteBuff)) != -1) {
					out.write(byteBuff, 0, bytesRead);
				}
				out.close();
				fileList.add(tempFile);
				zipStream.closeEntry();
			}
		} catch (IOException e) {
			log.error("解压压缩文件异常！", e);
			throw e;
		} finally {
			if(fileList.size() == 0){
				// 将创建的临时目录删除
				if (tempPathFile.exists()) {
					tempPathFile.delete();
				}
			}
			try {
				if (out != null) {
					out.close();
				}
				if (is != null) {
					is.close();
				}
				if (zipStream != null) {
					zipStream.close();
				}
			} catch (IOException e) {
				log.error("解压压缩文件异常！", e);
			}
		}
		return fileList;
	}

	/**
	 * 将文件打包成zip
	 * 
	 * @Title: zipFile
	 * @author mq
	 * @date 2021年11月11日 上午10:39:38
	 * @param fileName
	 * @param fileList
	 * @return
	 * @throws IOException
	 */
	public static File zipFile(String fileName, List<File> fileList) throws IOException{
		String uuid = UUID.randomUUID().toString();
		String tempPath = realPath + File.separator + uuid + File.separator;
		File tempPathFile = new File(tempPath);
		if (!tempPathFile.exists()) {
			tempPathFile.mkdir();
		}
		File file = new File(tempPath + fileName);
		FileOutputStream bos = null;
		ZipOutputStream zos = null;
		FileInputStream fin = null;
		try {
			bos = new FileOutputStream(file);
			zos = new ZipOutputStream(bos);
			for (File sysFile : fileList) {
				zos.putNextEntry(new ZipEntry(sysFile.getName()));
				fin = new FileInputStream(sysFile);
				int length;
				byte[] buffer = new byte[1024];
				while ((length = fin.read(buffer)) > 0) {
					zos.write(buffer, 0, length);
				}
				fin.close();
				zos.closeEntry();
			}
			zos.close();
			bos.close();
		} catch (IOException e) {
			log.error("打包压缩包失败！", e);
			throw e;
		} finally {
			try {
				if (fin != null) {
					fin.close();
				}
				if (zos != null) {
					zos.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (Exception e) {
				log.error("打包压缩包失败！", e);
			}
		}
		return file;
	}
	public static File zipFileByMulti(String fileName, List<MultipartFile> fileList)
		    throws IOException
		  {
		    String uuid = UUID.randomUUID().toString();
		    String tempPath = realPath + File.separator + uuid + File.separator;
		    File tempPathFile = new File(tempPath);
		    if (!tempPathFile.exists()) {
		      tempPathFile.mkdir();
		    }
		    File file = new File(tempPath + fileName);
		    FileOutputStream bos = null;
		    ZipOutputStream zos = null;
		    InputStream fin = null;
		    try {
		      bos = new FileOutputStream(file);
		      zos = new ZipOutputStream(bos);
		      for (MultipartFile sysFile : fileList) {
		        zos.putNextEntry(new ZipEntry(sysFile.getOriginalFilename()));
		        fin = sysFile.getInputStream();

		        byte[] buffer = new byte[1024];
		        int length;
		        while ((length = fin.read(buffer)) > 0)
		        {
		          zos.write(buffer, 0, length);
		        }
		        fin.close();
		        zos.closeEntry();
		      }
		      zos.close();
		      bos.close();
		    } catch (IOException e) {
		      log.error("打包压缩包失败！", e);
		      throw e;
		    } finally {
		      try {
		        if (fin != null) {
		          fin.close();
		        }
		        if (zos != null) {
		          zos.close();
		        }
		        if (bos != null)
		          bos.close();
		      }
		      catch (Exception e) {
		        log.error("打包压缩包失败！", e);
		      }
		    }
		    return file;
		  }

		  public static byte[] zipSysFile(List<SysFile> fileList)
		    throws IOException
		  {
		    byte[] b = new byte[0];
		    ByteArrayOutputStream bos = null;
		    ZipOutputStream zos = null;
		    try {
		      bos = new ByteArrayOutputStream();
		      zos = new ZipOutputStream(bos);
		      for (SysFile sysFile : fileList) {
		        zos.putNextEntry(new ZipEntry(sysFile.getFileName()));
		        zos.write(sysFile.getFileBytes());
		        zos.closeEntry();
		      }
		      zos.close();
		      b = bos.toByteArray();
		      bos.close();
		    } catch (IOException e) {
		      log.error("打包压缩包失败！", e);
		      throw e;
		    } finally {
		      try {
		        if (zos != null) {
		          zos.close();
		        }
		        if (bos != null)
		          bos.close();
		      }
		      catch (Exception e) {
		        log.error("打包压缩包失败！", e);
		      }
		    }
		    return b;
		  }
}
