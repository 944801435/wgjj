package com.brilliance.web.view.civilaviation;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.brilliance.base.common.BaseAction;
import com.brilliance.base.common.PagerVO;
import com.brilliance.base.common.SystemWebLog;
import com.brilliance.base.listener.LoginAccess;
import com.brilliance.base.model.internetModel.NoteCivilReply;
import com.brilliance.base.model.internetModel.NotePlanInfo;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
@LoginAccess
@SystemWebLog(menuName = "民航信息管理")
@Slf4j
@RequestMapping("/civilAviation")
public class CivilAviationAction extends BaseAction {
	@Autowired
    private CivilAviationService civilAviationService;
	/**
	 * 
	 * 描述 获取照会信息列表
	 * @Title: findList 
	 * @author 
	 * @Modified By gl
	 * @param request HTTP请求
	 * @param model 返回封装类
	 * @return    
	 * String 返回类型 
	 * @throws
	 */
	@RequestMapping("/list.action")
	public String findList(PagerVO<NotePlanInfo> pagerVO,CivilAviationDTO civilAviationParam, HttpServletRequest request, Model model) {
		try {
			Map queryParams=BeanUtil.beanToMap(civilAviationParam);
			pagerVO.setParams(queryParams);
			pagerVO = civilAviationService.findList(pagerVO);
			for (NotePlanInfo temp : pagerVO.getItems()) {
				NotePlanInfo planInfo2 = temp;
				NoteCivilReply message = civilAviationService.findCivilReplyByNoteId(planInfo2.getNoteId());
				if(message!=null){
					planInfo2.setPermitNumber(message.getPermitNumber());
					planInfo2.setRouteInfo(message.getPlanRoute());
				}
			}
			model.addAttribute("pagerVO", pagerVO);
			model.addAttribute("civilAviationParam", civilAviationParam);
		} catch (Exception e) {
			log.error("获取照会信息失败！", e);
			this.setMessage(request, "获取照会信息失败！", "red");
		}

		return "/view/sysFlightNote/civilaviation/list";
	}
	@RequestMapping("/detail.action")
	public String detail(Integer noteId, Model model) {
		CivilAviationVO civilAviationVO=civilAviationService.findCivilAviationVO(noteId);
		model.addAttribute("civilAviationVO",civilAviationVO);
		return "/view/sysFlightNote/civilaviation/detail";
	}

	/**
	 * 跳转到回复页面
	 * @param noteId
	 * @param model
	 * @return
	 */
	@RequestMapping("/reply.action")
	public String reply(Integer noteId, Model model) {
		CivilAviationVO civilAviationVO=civilAviationService.findCivilAviationVO(noteId);
		model.addAttribute("civilAviationVO",civilAviationVO);
		return "/view/sysFlightNote/civilaviation/reply";
	}

	/**
	 * 提交回复
	 * @param civilReplyDTO
	 * @return
	 */
	@RequestMapping("/submitReply.action")
	public String submitReply(CivilReplyDTO civilReplyDTO) {
		NoteCivilReply civilReply=new NoteCivilReply();
		BeanUtil.copyProperties(civilReplyDTO,civilReply);
		if(StringUtils.isBlank(civilReplyDTO.getPlanTime())){
			civilReply.setPlanTime(new Date());
		}else{
			civilReply.setPlanTime(DateUtil.parseDateTime(civilReplyDTO.getPlanTime()));
		}
		civilReply.setCreateTime(DateUtil.formatDateTime(new Date()));
		civilAviationService.saveCivilReply(civilReply);
		return "redirect:/civilAviation/list.action";
	}

	/**
	 * ajax方式提交回复
	 * @param civilReplyDTO
	 * @return
	 */
	@RequestMapping("/submitJsonReply.action")
	@ResponseBody
	public Map submitJsonReply(CivilReplyDTO civilReplyDTO) {
		Map<String,Object> result=new HashMap<String,Object>();
		NoteCivilReply civilReply=new NoteCivilReply();
		BeanUtil.copyProperties(civilReplyDTO,civilReply);
		if(StringUtils.isBlank(civilReplyDTO.getPlanTime())){
			civilReply.setPlanTime(new Date());
		}else{
			civilReply.setPlanTime(DateUtil.parseDateTime(civilReplyDTO.getPlanTime()));
		}
		civilReply.setCreateTime(DateUtil.formatDateTime(new Date()));
		boolean flag=civilAviationService.saveCivilReply(civilReply);
		if(flag){
			result.put("code",10001);
			result.put("message","回复成功！");
			result.put("data",civilReply);
		}else{
			result.put("code",10000);
			result.put("message","回复失败！");
			result.put("data",null);
		}
		return result;
	}

	/**
	 * ajax方式删除民航飞机信息
	 * @param noteId
	 * @return
	 */
	@RequestMapping("/deleteById.action")
	@ResponseBody
	public Map deleteById(Integer noteId) {
		Map<String,Object> result=new HashMap<String,Object>();
		boolean flag=civilAviationService.deletePlanInfoById(noteId);
		if(flag){
			result.put("code",10001);
			result.put("message","删除民航信息成功！");
			result.put("data",flag);
		}else{
			result.put("code",10000);
			result.put("message","删除民航信息失败！");
			result.put("data",flag);
		}
		return result;
	}
	/**
	 * ajax方式导出民航飞机信息
	 * @param noteId
	 * @return
	 */
	@RequestMapping("/exportZip.action")
	@ResponseBody
	public Map exportZip(Integer noteId) {

		Map<String,Object> result=new HashMap<String,Object>();
		String civilZipPath=civilAviationService.exportCivilZip(noteId);
		log.info("zip文件路径："+civilZipPath);
		if(StringUtils.isNotBlank(civilZipPath)){
			result.put("code",10001);
			result.put("message","民航信息导出成功！");
			result.put("data",civilZipPath);
		}else{
			result.put("code",10000);
			result.put("message","民航信息导出失败！");
			result.put("data","");
		}
		return result;
	}
	/**
	 * ajax方式批量导出民航飞机信息
	 * @param noteIds
	 * @return
	 */
	@RequestMapping("/batchExportZip.action")
	@ResponseBody
	public Map batchExportZip(Integer[] noteIds) {

		Map<String,Object> result=new HashMap<String,Object>();
		String civilZipPath=civilAviationService.exportCivilZip(noteIds);
		log.info("zip文件路径："+civilZipPath);
		if(StringUtils.isNotBlank(civilZipPath)){
			result.put("code",10001);
			result.put("message","民航信息导出成功！");
			result.put("data",civilZipPath);
		}else{
			result.put("code",10000);
			result.put("message","民航信息导出失败！");
			result.put("data","");
		}
		return result;
	}
}