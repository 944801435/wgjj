package com.uav.web.view.violation;

import com.uav.base.common.*;
import com.uav.base.listener.LoginAccess;
import com.uav.base.model.Violation;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 违规信息管理
 * 
 * @Title: ViolationAction.java
 */
@Slf4j
@Controller
@LoginAccess
@SystemWebLog(menuName = "违规信息管理")
public class ViolationAction extends BaseAction {

	@Autowired
	private ViolationService violationService;
	/**
	 *
	 * 描述 首次打开页面是调用，防止查询参数丢失
	 * @Title: main
	 * @author
	 * @Modified By ZSF
	 * @param request HTTP请求
	 * @param model 返回封装类
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/violationList_main")
	public String main(HttpServletRequest request, Model model) {
		Map<String, Object> queryMap = (Map<String, Object>) request.getSession().getAttribute("query_violation");
		if (queryMap == null) {
			queryMap = new HashMap<String, Object>(16);
		}
		queryMap.put("curPage", 1);
		request.getSession().setAttribute("query_violation", queryMap);
		return findList(1, null, request, model);
	}
	/**
	 *
	 * 描述 获取违规信息列表
	 * @Title: findList
	 * @author
	 * @Modified By ZSF
	 * @param curPage 当前页面
	 * @param violation 查询参数
	 * @param request HTTP请求
	 * @param model 返回封装类
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/violationList.action")
	public String findList(Integer curPage, Violation violation, HttpServletRequest request, Model model) {
		Map<String, Object> queryMap = (Map<String, Object>) request.getSession().getAttribute("query_violation");
		if (curPage == null) {
			curPage = Integer.parseInt(queryMap.get("curPage") + "");
		}
		queryMap.put("curPage", curPage);
		request.getSession().setAttribute("query_violation", queryMap);

		try {
			if (violation == null) {
				violation = new Violation();
			}
			PagerVO pv = violationService.findList(violation, curPage, pageSize);
			model.addAttribute("violationList", pv.getItems());
			model.addAttribute("totalCount", pv.getCounts());
			model.addAttribute("curPage", curPage);
			model.addAttribute("pageSize", pageSize);
		} catch (Exception e) {
			log.error("获取违规信息列表失败！", e);
			this.setMessage(request, "获取违规信息列表失败！", "red");
		}
		return "/view/violation/violation_list";
	}
	/**
	 *
	 * 描述 跳转修改页面
	 * @Title: findList
	 * @author
	 * @Modified By ZSF
	 * @param violation 查询参数
	 * @param request HTTP请求
	 * @param model 返回封装类
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/to_violation_edit")
	public String toEdit(Violation violation, Model model, HttpServletRequest request) {
		violation = violationService.findViolationById(violation.getVioId());
		model.addAttribute("violation", violation);
		return "/view/violation/violation_edit";
	}
	/**
	 *
	 * 描述 修改违规信息信息
	 * @Title: findList
	 * @author
	 * @Modified By ZSF
	 * @param violation 查询参数
	 * @param request HTTP请求
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/violationEdit.action")
	@SystemWebLog(methodName = "修改违规信息页面")
	public String edit(Violation violation, HttpServletRequest request) {
		try {
			String message = violationService.update(violation);
			if (!StringUtils.isBlank(message)) {
				this.setMessage(request, message, "red");
				return "/view/violation/violation_edit";
			}
			this.setMessage(request, "修改违规信息成功！", "green");
		} catch (Exception e) {
			log.error("修改违规信息失败！", e);
			this.setMessage(request, "修改违规信息失败！", "red");
			return "/view/violation/violation_edit";
		}
		return "redirect:/violationList.action";
	}
	/**
	 *
	 * 描述 删除违规信息信息
	 * @Title: findList
	 * @author
	 * @Modified By ZSF
	 * @param violation 查询参数
	 * @param request HTTP请求
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/violationDel.action")
	@SystemWebLog(methodName = "删除违规信息")
	public String del(Violation violation, String[] vioIds, HttpServletRequest request) {
		if (vioIds == null) {
			log.error("删除违规信息失败：参数错误！");
			this.setMessage(request, "删除违规信息失败：参数错误！", "red");
			return "redirect:/violationList.action";
		}
		try {
			violationService.delete(vioIds);
			this.setMessage(request, "删除违规信息成功！", "green");
		} catch (Exception e) {
			this.setMessage(request, "删除违规信息失败！", "red");
		}
		return "redirect:/violationList.action";
	}
	/**
	 *
	 * 描述 跳转新增违规信息页面
	 * @Title: findList
	 * @author
	 * @Modified By ZSF
	 * @param violation 查询参数
	 * @param request HTTP请求
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/to_violation_add")
	public String toAdd(Violation violation, Model model, HttpServletRequest request) {
		return "/view/violation/violation_add";
	}
	/**
	 *
	 * 描述 保存违规信息信息
	 * @Title: findList
	 * @author
	 * @Modified By ZSF
	 * @param violation 参数
	 * @param request HTTP请求
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/violationAdd.action")
	@SystemWebLog(methodName = "新增违规信息")
	public String add(Violation violation, HttpServletRequest request) {
		try {
			String message = violationService.save(violation);
			if (!StringUtils.isBlank(message)) {
				this.setMessage(request, message, "red");
				return "/view/violation/violation_add";
			}
			this.setMessage(request, "添加违规信息成功！", "green");
		} catch (Exception e) {
			log.error("添加违规信息失败！", e);
			this.setMessage(request, "添加违规信息失败！", "red");
			return "/view/violation/violation_add";
		}
		return "redirect:/violationList.action";
	}
	@RequestMapping("/violationStsEdit")
	@ResponseBody
	public Object violationStsEdit(Violation violation,HttpServletRequest request) {
		MessageVo vo = null;
		try {
			violation = violationService.findViolationById(violation.getVioId());
			if(Constants.sys_default_yes.equals(violation.getValidSts())){
				violation.setValidSts(Constants.sys_default_no);
			}else {
				violation.setValidSts(Constants.sys_default_yes);
			}
			String message = violationService.update(violation);
			if (!StringUtils.isBlank(message)) {
				vo = new MessageVo(MessageVo.FAIL, message, null);
			}
			vo = new MessageVo(MessageVo.SUCCESS, "修改违规信息状态成功", null);
		} catch (Exception e) {
			log.error("修改违规信息状态失败！", e);
			vo = new MessageVo(MessageVo.FAIL, "修改违规信息状态失败！", null);
		}
		return vo;
	}
}
