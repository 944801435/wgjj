package com.uav.web.view.system.sysFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uav.base.common.BaseAction;
import com.uav.base.listener.LoginAccess;
import com.uav.base.model.SysFile;
import com.uav.base.util.FileUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件管理
 * 
 * @Title: SysFileAction.java
 * @author mq  
 * @date 2021年11月5日 上午11:10:13
 */
@Slf4j
@LoginAccess
@RestController
public class SysFileAction extends BaseAction {

	@Autowired
	private SysFileService sysFileService;

	/**
	 * 下载文件
	 * 
	 * @Title: download
	 * @author mq
	 * @date 2021年11月5日 上午11:22:18
	 * @param fileId
	 * @param request
	 * @param response
	 */
	@RequestMapping("/download")
	public void download(String fileId, HttpServletRequest request, HttpServletResponse response) {
		try {
			FileUtil.download(fileId, request, response);
		} catch (Exception e) {
			log.error("下载文件出现异常！", e);
		}
	}

	/**
	 * 图片格式的文件展示
	 * 
	 * @Title: photo
	 * @author mq
	 * @date 2021年11月5日 上午11:22:26
	 * @param fileId
	 * @param response
	 * @param request
	 */
	@RequestMapping("/photo")
	public void photo(String fileId, HttpServletResponse response, HttpServletRequest request) {
		InputStream fis = null;
		try {
			SysFile sysFile = sysFileService.findById(fileId);
			if (sysFile == null) {
				log.error("文件不存在[" + fileId + "]");
				return;
			}
			response.setContentType(sysFile.getContentType());
			OutputStream out = response.getOutputStream();
			out.write(sysFile.getFileBytes());
			out.flush();
		} catch (Exception e) {
			log.error("读取文件失败！", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					log.error("读取文件失败！", e);
				}
			}
		}
	}

}
