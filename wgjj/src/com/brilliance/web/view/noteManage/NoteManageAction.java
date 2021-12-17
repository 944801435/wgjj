package com.brilliance.web.view.noteManage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.brilliance.base.common.BaseAction;
import com.brilliance.base.common.MessageVo;
import com.brilliance.base.common.PagerVO;
import com.brilliance.base.common.SystemWebLog;
import com.brilliance.base.listener.LoginAccess;
import com.brilliance.base.model.Note;
import com.brilliance.base.model.NoteFlight;
import com.brilliance.base.model.NoteFlightWay;
import com.brilliance.base.model.SysFile;
import com.brilliance.base.model.SysPms;
import com.brilliance.base.model.SysUser;
import com.brilliance.base.model.internetModel.NoteCivilReply;
import com.brilliance.base.model.internetModel.NoteFiles;
import com.brilliance.base.model.internetModel.NotePlanFlight;
import com.brilliance.base.model.internetModel.NotePlanInfo;
import com.brilliance.base.model.internetModel.NoteReport;
import com.brilliance.base.util.DateUtil;
import com.brilliance.base.util.flight.FlightException;

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
	@RequestMapping("/to_edit_Plan")
	public String toEditPlanInfo(String noteId, Model model, HttpServletRequest request) {
		model.addAttribute("noteIdAc", noteId);
		return "/view/sysFlightNote/noteManage/editNote";
	}
	@RequestMapping("/to_detail_Plan")
	public String toDetailPlanInfo(String noteId, Model model, HttpServletRequest request) {
		model.addAttribute("noteIdAc", noteId);
		return "/view/sysFlightNote/noteManage/detailNote";
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
		List<NotePlanFlight> flightList = new ArrayList<>();
		List<NoteFiles> noteFiles = new ArrayList<>();
		NotePlanInfo notePlanInfo = null;
		try {
			if(noteId!=null&&!"".equals(noteId)){
				notePlanInfo = noteManageService.findById(Integer.valueOf(noteId));
				flightList = noteManageService.findFlightByNoteId(noteId);
				noteFiles = noteManageService.findFilesByNoteId(Integer.valueOf(noteId));
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("notePlanInfo", notePlanInfo);
			map.put("flightList", flightList);
			map.put("noteFilesList", noteFiles);
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
	@RequestMapping("/update_plan_info_flight")
	@SystemWebLog(methodName = "修改照会文书信息")
	@ResponseBody
	public MessageVo editPlanInfo(@RequestParam("file") MultipartFile[] file, NotePlanInfo obj, HttpServletRequest request) throws FlightException {
		MessageVo vo = null;
//		obj.setCreator(getCurUserid(request));
//		obj.setCreateTime(DateUtil.getNowFullTimeString());
		try {
			String errMsg = noteManageService.editNoteInfo(obj, file);
			if (errMsg.equals("success"))
				vo = new MessageVo("1", "修改照会信息成功！", obj);
			else
				vo = new MessageVo("0", "修改照会信息失败！", obj);
		} catch (Exception e) {
			log.error("修改照会信息失败！", e);
			vo = new MessageVo("0", "修改照会信息失败！", null);
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
	@RequestMapping("/ocr_distinguish_note_info")
	@SystemWebLog(methodName = "照会原件ocr识别")
	@ResponseBody
	public MessageVo ocrDistinguish(NoteFiles obj, HttpServletRequest request) throws FlightException {
		MessageVo vo = null;
		try {
			String errMsg = noteManageService.ocrDistinguish(obj);
			if (errMsg.equals("success"))
				vo = new MessageVo("1", "ocr识别成功！", obj);
			else
				vo = new MessageVo("0", "ocr识别失败！", obj);
		} catch (Exception e) {
			log.error("ocr识别失败！", e);
			vo = new MessageVo("0", "ocr识别失败！", null);
		}
		return vo;
	}
	@RequestMapping("/translation_note_info")
	@SystemWebLog(methodName = "ocr识别结果翻译")
	@ResponseBody
	public MessageVo translationNoteInfo(NoteFiles obj, HttpServletRequest request) throws FlightException {
		MessageVo vo = null;
		try {
			String errMsg = noteManageService.translationNoteInfo(obj);
			if (errMsg.equals("success"))
				vo = new MessageVo("1", "翻译成功！", obj);
			else
				vo = new MessageVo("0", "翻译失败！", obj);
		} catch (Exception e) {
			log.error("翻译失败！", e);
			vo = new MessageVo("0", "翻译失败！", null);
		}
		return vo;
	}
	/**
	 * 批量导出照会信息
	 * @param noteIds
	 * @return
	 */
	@RequestMapping("/batch_note_exportzip")
	@ResponseBody
	public Map batchExportZip(NotePlanInfo planInfo, Integer[] noteIds, HttpServletRequest request) {
		Map<String,Object> result=new HashMap<String,Object>();
		String noteZipPath=noteManageService.exportNoteZip(noteIds);
		log.info("zip文件路径："+noteZipPath);
		if(StringUtils.isNotBlank(noteZipPath)){
			result.put("code",10001);
			result.put("message","照会信息导出成功！");
			result.put("data",noteZipPath);
		}else{
			result.put("code",10000);
			result.put("message","照会信息导出失败！");
			result.put("data","");
		}
		return result;
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
	 * 当前页面 @param planInfo 查询参数 @param request HTTP请求 @param model 返回封装类 @return
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
			model.addAttribute("noteList", pv.getItems());
			model.addAttribute("totalCount", pv.getCounts());
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
	 * 描述照会信息 @Title: del @author @param planInfo @param noteIds @param
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
	 * 描述照会信息 @Title: apply @author @param planInfo @param noteIds @param
	 * request @return String 返回类型 @throws
	 */
	@RequestMapping("/noteInfoApply.action")
	@SystemWebLog(methodName = "申请照会信息")
	public String apply(NotePlanInfo planInfo, Integer[] noteIds, HttpServletRequest request) {
		if (noteIds == null) {
			log.error("申请照会信息失败：参数错误！");
			this.setMessage(request, "申请照会信息失败：参数错误！", "red");
			return "redirect:/noteInfoManageList.action";
		}
		try {
			noteManageService.apply(noteIds);
			this.setMessage(request, "申请照会信息成功！", "green");
		} catch (Exception e) {
			e.printStackTrace();
			this.setMessage(request, "申请照会信息失败！", "red");
		}
		return "redirect:/noteInfoManageList.action";
	}
	/**
	 * 
	 * 描述 获取照会信息列表 @Title: findList @author @Modified By gl @param curPage
	 * 当前页面 @param planInfo 查询参数 @param request HTTP请求 @param model 返回封装类 @return
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
			for (Object temp : pv.getItems()) {
				if (temp instanceof NotePlanInfo) {
					NotePlanInfo planInfo2 = (NotePlanInfo) temp;
					// NoteCivilMessage message =
					// noteManageService.findCivilMessageById(planInfo2.getNoteId());
					NoteCivilReply message = noteManageService
							.findNoteCivilMessageByCreateTime(planInfo2.getNoteId());
					if (message != null) {
						planInfo2.setPermitNumber(message.getPermitNumber());
						planInfo2.setRouteInfo(message.getPlanRoute());
					}
				}
			}
			model.addAttribute("noteList", pv.getItems());
			model.addAttribute("totalCount", pv.getCounts());
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
	 * 当前页面 @param noteReport 查询参数 @param request HTTP请求 @param model 返回封装类 @return
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
			model.addAttribute("noteReportList", pv.getItems());
			model.addAttribute("totalCount", pv.getCounts());
			model.addAttribute("curPage", curPage);
			model.addAttribute("pageSize", pageSize);
		} catch (Exception e) {
			log.error("获取审批许可失败！", e);
			this.setMessage(request, "获取审批许可失败！", "red");
		}
		return "/view/sysFlightNote/noteManage/noteReportManage";
	}
}