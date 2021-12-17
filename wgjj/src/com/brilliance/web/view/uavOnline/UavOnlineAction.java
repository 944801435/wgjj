package com.brilliance.web.view.uavOnline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.brilliance.base.common.BaseAction;
import com.brilliance.base.common.MessageVo;
import com.brilliance.base.common.SystemWebLog;
import com.brilliance.base.listener.LoginAccess;
import com.brilliance.web.view.uavOnline.base.Airline;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@LoginAccess
@SystemWebLog(menuName = "飞行监视")
public class UavOnlineAction extends BaseAction {

	@Autowired
	private UavOnlineService uavOnlineService;

	/**
	 * 查看飞行监视页面
	 * 
	 * @Title: init
	 * @author maqian
	 * @date 2018年12月10日 下午4:07:16
	 * @param request
	 * @return
	 */
	@RequestMapping("/uavOnlineInit")
	public String init(HttpServletRequest request) {
		return "/view/uavOnline/uavOnline";
	}

	/**
	 * 获取计划信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/uavOnlineGetReqPlanList")
	@ResponseBody
	public MessageVo getReqPlanList(HttpServletRequest request) {
		MessageVo vo = null;
		try {
			// 计划信息
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("planList", new Object[]{});
			vo = new MessageVo(MessageVo.SUCCESS, "", map);
		} catch (Exception e) {
			vo = new MessageVo(MessageVo.FAIL, "获取计划信息失败", null);
			log.error("获取计划信息", e);
		}
		return vo;
	}

	/**
	 * 获取民航航线
	 * 
	 * @param pn
	 * @param request
	 * @return
	 */
	@RequestMapping("/uavOnlineGetPlaneLinesList")
	@ResponseBody
	public MessageVo getPlaneLinesList(HttpServletRequest request) {
		MessageVo vo = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Airline> planeLinesList = uavOnlineService.getAirlineList();
			map.put("planeLinesList", planeLinesList);
			vo = new MessageVo(MessageVo.SUCCESS, "", map);
		} catch (Exception e) {
			log.error("获取民航航线出现异常", e);
			vo = new MessageVo(MessageVo.FAIL, "获取民航航线失败", null);
		}
		return vo;
	}
}