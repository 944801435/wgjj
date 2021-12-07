package com.uav.web.view.note;

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
import com.uav.base.common.Constants;
import com.uav.base.common.MessageVo;
import com.uav.base.common.PagerVO;
import com.uav.base.common.SystemWebLog;
import com.uav.base.listener.LoginAccess;
import com.uav.base.model.Note;
import com.uav.base.model.NoteFlight;
import com.uav.base.model.NoteFlightWay;
import com.uav.base.model.SysFile;
import com.uav.base.util.DateUtil;
import com.uav.base.util.flight.FlightException;
import com.uav.web.view.system.sysFile.SysFileService;

import lombok.extern.slf4j.Slf4j;


/**
 * 外交照会管理
 * 
 * @Title: NoteAction.java
 * @author mq  
 * @date 2021年11月5日 上午10:02:08
 */
@Slf4j
@LoginAccess
@SystemWebLog
@RestController
@RequestMapping("/note")
public class NoteAction extends BaseAction {

	@Autowired
	private NoteService noteService;
	@Autowired
	private SysFileService sysFileService;

	/**
	 * 查询外交照会列表
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
			PagerVO pager = noteService.findList(obj, curPage, pageSize);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", pager.getDatas());
			map.put("totalCount", pager.getTotal());
			vo = new MessageVo(MessageVo.SUCCESS, "", map);
		} catch (Exception e) {
			log.error("查询外交照会列表失败！", e);
			vo = new MessageVo(MessageVo.FAIL, "查询外交照会列表失败！", null);
		}
		return vo;
	}

	/**
	 * 上传外交照会摆渡文件
	 * 
	 * @Title: upload
	 * @author mq
	 * @date 2021年11月5日 上午10:19:40
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping("/upload")
	@SystemWebLog(methodName = "上传外交照会摆渡文件")
	public MessageVo upload(MultipartFile file, HttpServletRequest request) {
		MessageVo vo = null;
		try {
			Note obj = new Note();
			obj.setCrtDept(getCurDeptid(request));
			obj.setCrtUser(getCurUserid(request));
			obj.setCrtTime(DateUtil.getNowFullTimeString());
			noteService.save(obj, file);
			vo = new MessageVo(MessageVo.SUCCESS, "导入外交照会文件成功！", null);
		} catch (FlightException e) {
			log.error(e.getMessage(), e);
			vo = new MessageVo(MessageVo.FAIL, e.getMessage(), null);
		} catch (Exception e) {
			log.error("导入外交照会文件失败！", e);
			vo = new MessageVo(MessageVo.FAIL, "导入外交照会文件失败！", null);
		}
		return vo;
	}

	/**
	 * 删除外交照会
	 * 
	 * @Title: upload
	 * @author mq
	 * @date 2021年11月5日 上午10:21:06
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping("/del")
	@SystemWebLog(methodName = "删除外交照会")
	public MessageVo del(String noteSeq, HttpServletRequest request) {
		MessageVo vo = null;
		try {
			Note obj = noteService.findById(noteSeq);
			noteService.del(noteSeq);
			vo = new MessageVo(MessageVo.SUCCESS, "删除外交照会成功！", null);
		} catch (Exception e) {
			log.error("删除外交照会失败！", e);
			vo = new MessageVo(MessageVo.FAIL, "删除外交照会失败！", null);
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
	public MessageVo detail(String noteSeq) {
		MessageVo vo = null;
		try {
			Note note = noteService.findById(noteSeq);
			List<SysFile> noteFileList = new ArrayList<SysFile>();
			for (String fileId : note.getNoteFileId().split(",")) {
				SysFile sysFile = sysFileService.findById(fileId);
				sysFile.setFileBytes(null);
				noteFileList.add(sysFile);
			}
			List<NoteFlight> flightList = noteService.findFlightListByNoteSeq(noteSeq);
			for (NoteFlight flight : flightList) {
				List<NoteFlightWay> wayList = noteService.findWayListByNfSeq(flight.getNfSeq());
				flight.setWayList(wayList);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("note", note);
			map.put("flightList", flightList);
			map.put("noteFileList", noteFileList);
			vo = new MessageVo(MessageVo.SUCCESS, "", map);
		} catch (Exception e) {
			log.error("查询外交照会详情失败！", e);
			vo = new MessageVo(MessageVo.FAIL, "查询外交照会详情失败！", null);
		}
		return vo;
	}

}
