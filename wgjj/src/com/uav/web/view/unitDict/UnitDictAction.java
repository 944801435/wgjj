package com.uav.web.view.unitDict;

import com.uav.base.common.*;
import com.uav.base.listener.LoginAccess;
import com.uav.base.model.UnitDict;

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
 * 单位字典管理
 * 
 * @Title: UnitDictAction.java
 */
@Slf4j
@Controller
@LoginAccess
@SystemWebLog(menuName = "单位字典管理")
public class UnitDictAction extends BaseAction {

	@Autowired
	private UnitDictService unitDictService;
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
	@RequestMapping("/unitDictList_main")
	public String main(HttpServletRequest request, Model model) {
		Map<String, Object> queryMap = (Map<String, Object>) request.getSession().getAttribute("query_unitDict");
		if (queryMap == null) {
			queryMap = new HashMap<String, Object>(16);
		}
		queryMap.put("curPage", 1);
		request.getSession().setAttribute("query_unitDict", queryMap);
		return findList(1, null, request, model);
	}
	/**
	 *
	 * 描述 获取单位字典列表
	 * @Title: findList
	 * @author
	 * @Modified By ZSF
	 * @param curPage 当前页面
	 * @param unitDict 查询参数
	 * @param request HTTP请求
	 * @param model 返回封装类
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/unitDictList.action")
	public String findList(Integer curPage, UnitDict unitDict, HttpServletRequest request, Model model) {
		Map<String, Object> queryMap = (Map<String, Object>) request.getSession().getAttribute("query_unitDict");
		if (curPage == null) {
			curPage = Integer.parseInt(queryMap.get("curPage") + "");
		}
		queryMap.put("curPage", curPage);
		request.getSession().setAttribute("query_unitDict", queryMap);

		try {
			if (unitDict == null) {
				unitDict = new UnitDict();
			}
			PagerVO pv = unitDictService.findList(unitDict, curPage, pageSize);
			model.addAttribute("unitDictList", pv.getItems());
			model.addAttribute("totalCount", pv.getCounts());
			model.addAttribute("curPage", curPage);
			model.addAttribute("pageSize", pageSize);
		} catch (Exception e) {
			log.error("获取单位字典列表失败！", e);
			this.setMessage(request, "获取单位字典列表失败！", "red");
		}
		return "/view/unitDict/unitDict_list";
	}
	/**
	 *
	 * 描述 跳转修改页面
	 * @Title: findList
	 * @author
	 * @Modified By ZSF
	 * @param unitDict 查询参数
	 * @param request HTTP请求
	 * @param model 返回封装类
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/to_unitDict_edit")
	public String toEdit(UnitDict unitDict, Model model, HttpServletRequest request) {
		unitDict = unitDictService.findUnitDictById(unitDict.getDictId());
		model.addAttribute("unitDict", unitDict);
		return "/view/unitDict/unitDict_edit";
	}
	/**
	 *
	 * 描述 修改单位字典信息
	 * @Title: findList
	 * @author
	 * @Modified By ZSF
	 * @param unitDict 查询参数
	 * @param request HTTP请求
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/unitDictEdit.action")
	@SystemWebLog(methodName = "修改单位字典页面")
	public String edit(UnitDict unitDict, HttpServletRequest request) {
		try {
			String message = unitDictService.update(unitDict);
			if (!StringUtils.isBlank(message)) {
				this.setMessage(request, message, "red");
				return "/view/unitDict/unitDict_edit";
			}
			this.setMessage(request, "修改单位字典成功！", "green");
		} catch (Exception e) {
			log.error("修改单位字典失败！", e);
			this.setMessage(request, "修改单位字典失败！", "red");
			return "/view/unitDict/unitDict_edit";
		}
		return "redirect:/unitDictList.action";
	}
	/**
	 *
	 * 描述 删除单位字典信息
	 * @Title: findList
	 * @author
	 * @Modified By ZSF
	 * @param unitDict 查询参数
	 * @param request HTTP请求
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/unitDictDel.action")
	@SystemWebLog(methodName = "删除单位字典")
	public String del(UnitDict unitDict, String[] dictIds, HttpServletRequest request) {
		if (dictIds == null) {
			log.error("删除单位字典失败：参数错误！");
			this.setMessage(request, "删除单位字典失败：参数错误！", "red");
			return "redirect:/unitDictList.action";
		}
		try {
			unitDictService.delete(dictIds);
			this.setMessage(request, "删除单位字典成功！", "green");
		} catch (Exception e) {
			this.setMessage(request, "删除单位字典失败！", "red");
		}
		return "redirect:/unitDictList.action";
	}
	/**
	 *
	 * 描述 跳转新增单位字典页面
	 * @Title: findList
	 * @author
	 * @Modified By ZSF
	 * @param unitDict 查询参数
	 * @param request HTTP请求
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/to_unitDict_add")
	public String toAdd(UnitDict unitDict, Model model, HttpServletRequest request) {
		return "/view/unitDict/unitDict_add";
	}
	/**
	 *
	 * 描述 保存单位字典信息
	 * @Title: findList
	 * @author
	 * @Modified By ZSF
	 * @param unitDict 参数
	 * @param request HTTP请求
	 * @return
	 * String 返回类型
	 * @throws
	 */
	@RequestMapping("/unitDictAdd.action")
	@SystemWebLog(methodName = "新增单位字典")
	public String add(UnitDict unitDict, HttpServletRequest request) {
		try {
			String message = unitDictService.save(unitDict);
			if (!StringUtils.isBlank(message)) {
				this.setMessage(request, message, "red");
				return "/view/unitDict/unitDict_add";
			}
			this.setMessage(request, "添加单位字典成功！", "green");
		} catch (Exception e) {
			log.error("添加单位字典失败！", e);
			this.setMessage(request, "添加单位字典失败！", "red");
			return "/view/unitDict/unitDict_add";
		}
		return "redirect:/unitDictList.action";
	}

	@RequestMapping("/unitDictStsEdit")
	@ResponseBody
	public Object unitDictStsEdit(UnitDict unitDict,HttpServletRequest request) {
		MessageVo vo = null;
		try {
			unitDict = unitDictService.findUnitDictById(unitDict.getDictId());
			if(Constants.sys_default_yes.equals(unitDict.getValidSts())){
				unitDict.setValidSts(Constants.sys_default_no);
			}else {
				unitDict.setValidSts(Constants.sys_default_yes);
			}
			String message = unitDictService.update(unitDict);
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
