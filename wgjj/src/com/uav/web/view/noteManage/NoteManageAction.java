package com.uav.web.view.noteManage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.uav.base.common.BaseAction;
import com.uav.base.common.MessageVo;
import com.uav.base.common.PagerVO;
import com.uav.base.common.SystemWebLog;
import com.uav.base.listener.LoginAccess;
import com.uav.base.model.Note;
import com.uav.base.model.NoteFlight;
import com.uav.base.model.NoteFlightWay;
import com.uav.base.model.SysFile;
import com.uav.base.model.internetModel.NoteCivilMessage;
import com.uav.base.model.internetModel.NoteFiles;
import com.uav.base.model.internetModel.NotePlanFlight;
import com.uav.base.model.internetModel.NotePlanInfo;
import com.uav.base.model.internetModel.NoteReport;
import com.uav.base.util.DateUtil;
import com.uav.base.util.flight.FlightException;

/**
 * 照会文书管理
 * 
 * @ClassName: NoteManageAction
 * @Description:
 * @author gl
 * @date
 */
@Controller
@LoginAccess
@SystemWebLog(menuName = "照会文书管理")
@SuppressWarnings("unchecked")
//@RestController
//@RequestMapping("/noteInfo")
public class NoteManageAction extends BaseAction {

	protected static final Logger log = Logger.getLogger(NoteManageAction.class);
	@Autowired
	private NoteManageService noteManageService;

	@RequestMapping("/to_note_info_add")
	public String toAdd(NotePlanInfo planInfo, Model model, HttpServletRequest request) {
		return "/view/sysFlightNote/noteManage/addNote";
	}
	@RequestMapping("/to_add_note_flight_jsp")
	public String toAddFlight(Integer noteId, Model model, HttpServletRequest request) {
		List<NoteFiles> noteFiles = noteManageService.findFilesByNoteId(noteId);
		if(noteFiles!=null&&noteFiles.size()>0){
			Integer noteIdAc = noteFiles.get(0).getNoteId(); 
			model.addAttribute("noteFilesList", noteFiles);
			model.addAttribute("noteIdAc", noteIdAc);
		}else{
			this.setMessage(request, "获取照会信息失败！", "red");
		}
		return "/view/sysFlightNote/noteManage/addNote_flight";
	}
	/**
	 * @Title: detail_plan_flight_info
	 * @author gl
	 * @date 
	 * @param noteId
	 * @return
	 */
	@RequestMapping("/detail_plan_flight_info")
	@ResponseBody
	public MessageVo detail(String noteId) {
		MessageVo vo = null;
		try {
			List<NotePlanFlight> flightList = noteManageService.findFlightByNoteId(noteId);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("flightList", flightList);
			vo = new MessageVo(MessageVo.SUCCESS, "", map);
		} catch (Exception e) {
			log.error("查询飞行计划信息失败！", e);
			vo = new MessageVo(MessageVo.FAIL, "查询飞行计划信息失败！", null);
		}
		return vo;
	}
	@RequestMapping("/addNote")
	@SystemWebLog(methodName = "新增照会文书信息")
	@ResponseBody
	public MessageVo add(@RequestParam("file") MultipartFile[] file, NotePlanInfo obj, HttpServletRequest request) throws FlightException {
		MessageVo vo = null;
		obj.setCreator(getCurUserid(request));
		obj.setCreateTime(DateUtil.getNowFullTimeString());
		try {
			String errMsg = noteManageService.addNoteInfo(obj, file);
			if (errMsg.equals("success"))
				vo = new MessageVo("1", "新增照会信息成功！", obj);
			else
				vo = new MessageVo("0", "新增照会信息失败！", obj);
		} catch (Exception e) {
			log.error("新增照会信息失败！", e);
			vo = new MessageVo("0", "新增照会信息失败！", null);
		}
		return vo;
	}
	@RequestMapping("/save_plan_flight_info")
	@SystemWebLog(methodName = "新增飞行计划信息")
	@ResponseBody
	public MessageVo addPlanFlight(NotePlanFlight obj, HttpServletRequest request) throws FlightException {
		MessageVo vo = null;
//		obj.setCreator(getCurUserid(request));
//		obj.setCreateTime(DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
		try {
			String errMsg = noteManageService.addPlanFlightInfo(obj);
			if (errMsg.equals("success"))
				vo = new MessageVo("1", "新增飞行计划信息成功！", obj);
			else
				vo = new MessageVo("0", "新增飞行计划信息失败！", obj);
		} catch (Exception e) {
			log.error("新增飞行计划信息失败！", e);
			vo = new MessageVo("0", "新增飞行计划信息失败！", null);
		}
		return vo;
	}
	@RequestMapping("/del_plan_flight_info")
	@SystemWebLog(methodName = "删除飞行计划信息")
	@ResponseBody
	public MessageVo delPlanFlight(Integer id, HttpServletRequest request) throws FlightException {
		MessageVo vo = null;
		try {
			noteManageService.delPlanFlightInfo(id);
			vo = new MessageVo(MessageVo.SUCCESS, "删除飞行计划信息成功！", null);
		} catch (Exception e) {
			log.error("删除飞行计划信息失败！", e);
			vo = new MessageVo(MessageVo.FAIL, "删除飞行计划信息失败！", null);
		}
		return vo;
	}

	/**
	 * 
	 * 描述 获取照会信息列表 @Title: findList @author @Modified By gl @param curPage
	 * 当前页面 @param sysUser 查询参数 @param request HTTP请求 @param model 返回封装类 @return
	 * String 返回类型 @throws
	 */
	@RequestMapping("/noteInfoManageList.action")
	public String findNoteInfoList(Integer curPage, NotePlanInfo planInfo, HttpServletRequest request, Model model) {
		if (curPage == null) {
			curPage = 1;
		}
		try {
			if (planInfo == null) {
				planInfo = new NotePlanInfo();
			}
			PagerVO pv = noteManageService.findNoteInfoList(planInfo, curPage, pageSize);
			model.addAttribute("noteList", pv.getDatas());
			model.addAttribute("totalCount", pv.getTotal());
			model.addAttribute("curPage", curPage);
			model.addAttribute("pageSize", pageSize);
		} catch (Exception e) {
			log.error("获取照会信息失败！", e);
			this.setMessage(request, "获取照会信息失败！", "red");
		}
		return "/view/sysFlightNote/noteManage/noteManage";
	}

	/**
	 * 
	 * 描述照会信息 @Title: del @author @param sysUser @param userIds @param
	 * request @return String 返回类型 @throws
	 */
	@RequestMapping("/noteInfoDel.action")
	@SystemWebLog(methodName = "删除照会信息")
	public String del(NotePlanInfo planInfo, Integer[] noteIds, HttpServletRequest request) {
		if (noteIds == null) {
			log.error("删除照会信息失败：参数错误！");
			this.setMessage(request, "删除照会信息失败：参数错误！", "red");
			return "redirect:/noteInfoManageList.action";
		}
		try {
			noteManageService.delete(noteIds);
			this.setMessage(request, "删除照会信息成功！", "green");
		} catch (Exception e) {
			e.printStackTrace();
			this.setMessage(request, "删除照会信息失败！", "red");
		}
		return "redirect:/noteInfoManageList.action";
	}

	/**
	 * 
	 * 描述 获取照会信息列表 @Title: findList @author @Modified By gl @param curPage
	 * 当前页面 @param sysUser 查询参数 @param request HTTP请求 @param model 返回封装类 @return
	 * String 返回类型 @throws
	 */
	@RequestMapping("/noteManageList.action")
	public String findList(Integer curPage, NotePlanInfo planInfo, HttpServletRequest request, Model model) {
		if (curPage == null) {
			curPage = 1;
		}
		try {
			if (planInfo == null) {
				planInfo = new NotePlanInfo();
			}
			PagerVO pv = noteManageService.findList(planInfo, curPage, pageSize);
			for (Object temp : pv.getDatas()) {
				if (temp instanceof NotePlanInfo) {
					NotePlanInfo planInfo2 = (NotePlanInfo) temp;
					// NoteCivilMessage message =
					// noteManageService.findCivilMessageById(planInfo2.getNoteId());
					NoteCivilMessage message = noteManageService
							.findNoteCivilMessageByCreateTime(planInfo2.getNoteId());
					if (message != null) {
						planInfo2.setPermitNumber(message.getPermitNumber());
						planInfo2.setReplyContent(message.getReplyContent());
						planInfo2.setRouteInfo(message.getRouteInfo());
					}
				}
			}
			model.addAttribute("noteList", pv.getDatas());
			model.addAttribute("totalCount", pv.getTotal());
			model.addAttribute("curPage", curPage);
			model.addAttribute("pageSize", pageSize);
		} catch (Exception e) {
			log.error("获取照会信息失败！", e);
			this.setMessage(request, "获取照会信息失败！", "red");
		}
		if (planInfo.getAction() != null & "1".equals(planInfo.getAction())) {
			return "/view/sysFlightNote/noteManage/noteManage";
		} else {
			return "/view/sysFlightNote/noteManage/civilMessageManage";
		}
	}

	/**
	 * 
	 * 描述 获取审批许可列表 @Title: findList @author @Modified By gl @param curPage
	 * 当前页面 @param sysUser 查询参数 @param request HTTP请求 @param model 返回封装类 @return
	 * String 返回类型 @throws
	 */
	@RequestMapping("/noteReportList.action")
	public String findReportList(Integer curPage, NoteReport noteReport, HttpServletRequest request, Model model) {
		if (curPage == null) {
			curPage = 1;
		}
		try {
			if (noteReport == null) {
				noteReport = new NoteReport();
			}
			PagerVO pv = noteManageService.findReportList(noteReport, curPage, pageSize);
			model.addAttribute("noteReportList", pv.getDatas());
			model.addAttribute("totalCount", pv.getTotal());
			model.addAttribute("curPage", curPage);
			model.addAttribute("pageSize", pageSize);
		} catch (Exception e) {
			log.error("获取审批许可失败！", e);
			this.setMessage(request, "获取审批许可失败！", "red");
		}
		return "/view/sysFlightNote/noteManage/noteReportManage";
	}
}