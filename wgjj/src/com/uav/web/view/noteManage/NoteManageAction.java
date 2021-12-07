package com.uav.web.view.noteManage;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uav.base.common.BaseAction;
import com.uav.base.common.PagerVO;
import com.uav.base.common.SystemWebLog;
import com.uav.base.listener.LoginAccess;
import com.uav.base.model.SysPms;
import com.uav.base.model.SysUser;
import com.uav.base.model.internetModel.NoteCivilMessage;
import com.uav.base.model.internetModel.NotePlanInfo;
import com.uav.base.model.internetModel.NoteReport;

/**
 * 照会文书管理
 * @ClassName: NoteManageAction
 * @Description: 
 * @author gl
 * @date 
 */
@Controller
@LoginAccess
@SystemWebLog(menuName = "照会文书管理")
@SuppressWarnings("unchecked")
public class NoteManageAction extends BaseAction {
	
	protected static final Logger log = Logger.getLogger(NoteManageAction.class);
	@Autowired
    private NoteManageService noteManageService;
	
	@RequestMapping("/to_note_info_add")
	public String toAdd(NotePlanInfo planInfo, Model model, HttpServletRequest request) {
		return "/view/sysFlightNote/noteManage/addNote";
	}
	/**
	 * 
	 * 描述 获取照会信息列表
	 * @Title: findList 
	 * @author 
	 * @Modified By gl
	 * @param curPage 当前页面
	 * @param sysUser 查询参数
	 * @param request HTTP请求
	 * @param model 返回封装类
	 * @return    
	 * String 返回类型 
	 * @throws
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
	 * 描述照会信息
	 * @Title: del 
	 * @author 
	 * @param sysUser
	 * @param userIds
	 * @param request
	 * @return    
	 * String 返回类型 
	 * @throws
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
	 * 描述 获取照会信息列表
	 * @Title: findList 
	 * @author 
	 * @Modified By gl
	 * @param curPage 当前页面
	 * @param sysUser 查询参数
	 * @param request HTTP请求
	 * @param model 返回封装类
	 * @return    
	 * String 返回类型 
	 * @throws
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
				if(temp instanceof NotePlanInfo){
					NotePlanInfo planInfo2 = (NotePlanInfo) temp;
//					NoteCivilMessage message = noteManageService.findCivilMessageById(planInfo2.getNoteId());
					NoteCivilMessage message = noteManageService.findNoteCivilMessageByCreateTime(planInfo2.getNoteId());
					if(message!=null){
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
		if(planInfo.getAction()!=null&"1".equals(planInfo.getAction())){
			return "/view/sysFlightNote/noteManage/noteManage";
		}else{
			return "/view/sysFlightNote/noteManage/civilMessageManage";
		}
	}
	/**
	 * 
	 * 描述 获取审批许可列表
	 * @Title: findList 
	 * @author 
	 * @Modified By gl
	 * @param curPage 当前页面
	 * @param sysUser 查询参数
	 * @param request HTTP请求
	 * @param model 返回封装类
	 * @return    
	 * String 返回类型 
	 * @throws
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