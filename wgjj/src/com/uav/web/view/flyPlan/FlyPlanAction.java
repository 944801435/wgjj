package com.uav.web.view.flyPlan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uav.base.common.BaseAction;
import com.uav.base.common.Constants;
import com.uav.base.common.MessageVo;
import com.uav.base.common.PagerVO;
import com.uav.base.common.SystemWebLog;
import com.uav.base.listener.LoginAccess;
import com.uav.base.model.FlyPlan;
import com.uav.base.model.FlyPlanFlight;
import com.uav.base.model.Note;
import com.uav.base.model.NoteFlight;
import com.uav.base.util.DateUtil;
import com.uav.web.view.note.NoteService;

import lombok.extern.slf4j.Slf4j;

/**
 * 飞行计划管理
 * 
 * @Title: FlyPlanAction.java
 * @author mq
 * @date 2021年11月12日 下午3:36:42
 */
@Slf4j
@LoginAccess
@RestController
@RequestMapping("/flyPlan")
@SystemWebLog(menuName = "飞行计划管理")
public class FlyPlanAction extends BaseAction {

	@Autowired
	private NoteService noteService;
	@Autowired
	private FlyPlanService flyPlanService;

	/**
	 * 查询飞行计划列表，只能查询本机构申请的
	 * 
	 * @Title: findList
	 * @author mq
	 * @date 2021年11月15日 上午11:29:26
	 * @param obj
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/list")
	public MessageVo findList(FlyPlan obj, Integer curPage, Integer pageSize, HttpServletRequest request) {
		MessageVo vo = null;
		try {
			obj.setCrtDept(getCurDeptid(request));
			PagerVO pager = flyPlanService.findList(obj, curPage, pageSize);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", pager.getItems());
			map.put("totalCount", pager.getCounts());
			vo = new MessageVo(MessageVo.SUCCESS, "", null);
		} catch (Exception e) {
			log.error("查询飞行计划列表失败！", e);
			vo = new MessageVo(MessageVo.FAIL, "查询飞行计划列表失败！", null);
		}
		return vo;
	}

	/**
	 * 获得未生成计划并且存在民航意见的照会
	 * 
	 * @Title: getNoteList
	 * @author mq
	 * @date 2021年11月15日 上午11:19:51
	 * @return
	 */
	@RequestMapping("/getNoteList")
	public MessageVo getNoteList() {
		MessageVo vo = null;
		try {
			List<Note> list = noteService.findNoteNoPlanList();
			vo = new MessageVo(MessageVo.SUCCESS, "", list);
		} catch (Exception e) {
			log.error("查询外交照会列表失败！", e);
			vo = new MessageVo(MessageVo.FAIL, "查询外交照会列表失败！", null);
		}
		return vo;
	}

	/**
	 * 保存飞行计划
	 * 
	 * @Title: save
	 * @author mq
	 * @date 2021年11月15日 下午2:39:29
	 * @param noteSeq
	 * @param request
	 * @return
	 */
	@RequestMapping("/save")
	@SystemWebLog(methodName = "保存飞行计划")
	public MessageVo save(String noteSeq, HttpServletRequest request) {
		MessageVo vo = null;
		try {
			FlyPlan flyPlan = new FlyPlan();
			flyPlan.setCrtDept(getCurDeptid(request));
			flyPlan.setCrtUser(getCurUserid(request));
			flyPlan.setCrtTime(DateUtil.getNowFullTimeString());
			flyPlan.setHisSts(Constants.sys_default_no);
			flyPlan.setRptSts(Constants.sys_default_no);
			flyPlanService.save(flyPlan, noteSeq);
			vo = new MessageVo(MessageVo.SUCCESS, "保存飞行计划成功！", null);
		} catch (Exception e) {
			log.error("保存飞行计划失败！", e);
			vo = new MessageVo(MessageVo.FAIL, "保存飞行计划失败！", null);
		}
		return vo;
	}

	/**
	 * 获得审批表内容
	 * 
	 * @Title: getCommitObj
	 * @author mq
	 * @date 2021年11月15日 下午2:19:52
	 * @param planSeq
	 * @return
	 */
	@RequestMapping("/getCommitObj")
	public MessageVo getCommitObj(String planSeq) {
		MessageVo vo = null;
		try {
			FlyPlan plan = flyPlanService.findById(planSeq);
			List<FlyPlanFlight> planFlightList = flyPlanService.findFlightListByPlanSeq(planSeq);
			for (FlyPlanFlight flight : planFlightList) {
				flight.setWayList(flyPlanService.findWayListByNfSeq(flight.getFpfSeq()));
			}
			List<NoteFlight> noteFlightList = noteService.findFlightListByNoteSeq(plan.getNoteSeq());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("plan", plan);
			map.put("planFlightList", planFlightList);
			map.put("noteFlightList", noteFlightList);
			vo = new MessageVo(MessageVo.SUCCESS, "", map);
		} catch (Exception e) {
			log.error("获得审批表内容失败！", e);
			vo = new MessageVo(MessageVo.FAIL, "获得审批表内容失败！", null);
		}
		return vo;
	}

	/**
	 * 上报飞行计划
	 * 
	 * @Title: commit
	 * @author mq
	 * @date 2021年11月15日 上午11:44:52
	 * @param planSeq
	 * @param request
	 * @return
	 */
	@RequestMapping("/commit")
	@SystemWebLog(methodName = "上报飞行计划")
	public MessageVo commit(String planSeq, String[] flightBodys, HttpServletRequest request) {
		MessageVo vo = null;
		try {
			FlyPlan plan = flyPlanService.findById(planSeq);
			plan.setAppOptDept(getCurDeptid(request));
			plan.setAppOptTime(DateUtil.getNowFullTimeString());
			plan.setAppOptUser(getCurUserid(request));
			plan.setPlanSts(Constants.BIZ_SPACE_REQ_AUDIT);
			plan.setRptOptDept(getCurDeptid(request));
			plan.setRptOptTime(DateUtil.getNowFullTimeString());
			plan.setRptOptUser(getCurUserid(request));
			plan.setRptSts(Constants.sys_default_no);// 未下发
			flyPlanService.commit(plan, flightBodys);
			vo = new MessageVo(MessageVo.SUCCESS, "上报飞行计划成功！", null);
		} catch (Exception e) {
			log.error("上报飞行计划失败！", e);
			vo = new MessageVo(MessageVo.FAIL, "上报飞行计划失败！", null);
		}
		return vo;
	}
}
