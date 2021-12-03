package com.uav.web.view.caacApprove;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.uav.base.common.BaseAction;
import com.uav.base.common.MessageVo;
import com.uav.base.common.PagerVO;
import com.uav.base.common.SystemWebLog;
import com.uav.base.listener.LoginAccess;
import com.uav.base.model.CaacApprove;
import com.uav.base.model.CaacFlight;
import com.uav.base.model.CaacFlightWay;
import com.uav.base.model.Note;
import com.uav.base.model.NoteFlight;
import com.uav.base.model.NoteFlightWay;
import com.uav.base.model.SysFile;
import com.uav.base.util.DateUtil;
import com.uav.base.util.flight.FlightException;
import com.uav.web.view.note.NoteService;
import com.uav.web.view.system.sysFile.SysFileService;

import lombok.extern.slf4j.Slf4j;

/**
 * 民航意见管理
 * 
 * @Title: NoteAction.java
 * @author mq  
 * @date 2021年11月5日 上午10:02:08
 */
@Slf4j
@LoginAccess
@SystemWebLog
@RestController
@RequestMapping("/caacApprove")
public class CaacApproveAction extends BaseAction {

	@Autowired
	private CaacApproveService caacApproveService;
	@Autowired
	private NoteService noteService;
	@Autowired
	private SysFileService sysFileService;

	/**
	 * 查询民航意见列表
	 * 
	 * @Title: findList
	 * @author mq
	 * @date 2021年11月5日 上午10:02:19
	 * @param obj
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/list")
	public MessageVo list(Note obj, Integer curPage, Integer pageSize) {
		MessageVo vo = null;
		try {
			PagerVO pager = caacApproveService.findList(obj, curPage, pageSize);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", pager.getDatas());
			map.put("totalCount", pager.getTotal());
			vo = new MessageVo(MessageVo.SUCCESS, "", map);
		} catch (Exception e) {
			log.error("查询民航意见列表失败！", e);
			vo = new MessageVo(MessageVo.FAIL, "查询民航意见列表失败！", null);
		}
		return vo;
	}

	/**
	 * 上传民航意见摆渡文件
	 * 
	 * @Title: upload
	 * @author mq
	 * @date 2021年11月5日 上午10:19:40
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping("/upload")
	@SystemWebLog(methodName = "上传民航意见摆渡文件")
	public MessageVo upload(MultipartFile file, HttpServletRequest request) {
		MessageVo vo = null;
		try {
			CaacApprove obj = new CaacApprove();
			obj.setCrtDept(getCurDeptid(request));
			obj.setCrtUser(getCurUserid(request));
			obj.setCrtTime(DateUtil.getNowFullTimeString());
			caacApproveService.save(obj, file);
			vo = new MessageVo(MessageVo.SUCCESS, "导入民航意见文件成功！", null);
		} catch (FlightException e) {
			log.error(e.getMessage(), e);
			vo = new MessageVo(MessageVo.FAIL, e.getMessage(), null);
		} catch (Exception e) {
			log.error("导入民航意见文件失败！", e);
			vo = new MessageVo(MessageVo.FAIL, "导入民航意见文件失败！", null);
		}
		return vo;
	}

	/**
	 * 查看详情
	 * 
	 * @Title: detail
	 * @author mq
	 * @date 2021年11月5日 上午10:34:16
	 * @param noteSeq
	 * @return
	 */
	@RequestMapping("/detail")
	public MessageVo detail(String caSeq) {
		MessageVo vo = null;
		try {
			CaacApprove caac = caacApproveService.findById(caSeq);
			List<SysFile> caacFileList = new ArrayList<SysFile>();
			for (String fileId : caac.getCaFileId().split(",")) {
				SysFile sysFile = sysFileService.findById(fileId);
				sysFile.setFileBytes(null);
				caacFileList.add(sysFile);
			}
			List<CaacFlight> caacFlightList = caacApproveService.findFlightListByCaSeq(caSeq);
			for (CaacFlight flight : caacFlightList) {
				List<CaacFlightWay> wayList = caacApproveService.findWayListByCafSeq(flight.getCafSeq());
				flight.setWayList(wayList);
			}
			Note note = caac.getNote();
			List<SysFile> noteFileList = new ArrayList<SysFile>();
			for (String fileId : note.getNoteFileId().split(",")) {
				SysFile sysFile = sysFileService.findById(fileId);
				sysFile.setFileBytes(null);
				noteFileList.add(sysFile);
			}
			List<NoteFlight> noteFlightList = noteService.findFlightListByNoteSeq(caac.getNoteSeq());
			for (NoteFlight flight : noteFlightList) {
				List<NoteFlightWay> wayList = noteService.findWayListByNfSeq(flight.getNfSeq());
				flight.setWayList(wayList);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("caac", caac);
			map.put("caacFlightList", caacFlightList);
			map.put("caacFileList", caacFileList);
			map.put("note", note);
			map.put("noteFlightList", noteFlightList);
			map.put("noteFileList", noteFileList);
			vo = new MessageVo(MessageVo.SUCCESS, "", map);
		} catch (Exception e) {
			log.error("查询民航意见详情失败！", e);
			vo = new MessageVo(MessageVo.FAIL, "查询民航意见详情失败！", null);
		}
		return vo;
	}

}
