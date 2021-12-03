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
import com.uav.base.model.internetModel.NoteCivilMessage;
import com.uav.base.model.internetModel.NotePlanInfo;

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

}