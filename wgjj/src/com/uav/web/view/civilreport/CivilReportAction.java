package com.uav.web.view.civilreport;

import cn.hutool.core.bean.BeanUtil;
import com.uav.base.common.BaseAction;
import com.uav.base.common.PagerVO;
import com.uav.base.common.SystemWebLog;
import com.uav.base.listener.LoginAccess;
import com.uav.base.model.internetModel.NoteCivilReply;
import com.uav.base.model.internetModel.NotePlanInfo;
import com.uav.web.view.civilaviation.CivilAviationDTO;
import com.uav.web.view.civilaviation.CivilAviationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@LoginAccess
@SystemWebLog(menuName = "审批许可管理")
@Slf4j
@RequestMapping("/civilReport")
public class CivilReportAction extends BaseAction {
    @Autowired
    private CivilReportService civilReportService;

    @RequestMapping("/list.action")
    public String findList(PagerVO<CivilReportVO> pagerVO, CivilReportDTO civilReportParam, HttpServletRequest request, Model model) {

        try {
            pagerVO = civilReportService.findList(pagerVO,civilReportParam);
            model.addAttribute("pagerVO", pagerVO);
            model.addAttribute("civilReportParam", civilReportParam);
        } catch (Exception e) {
            log.error("获取照会信息失败！", e);
            this.setMessage(request, "获取照会信息失败！", "red");
        }
        return "/view/sysFlightNote/civilreport/list";
    }

    @RequestMapping("/uploadZipFile")
    @ResponseBody
    public Map uploadZipFile(@RequestParam("zipFile")MultipartFile zipFile) {

        Map<String,Object> result=new HashMap<String,Object>();
        try {
            civilReportService.uploadZipFile(zipFile);
            result.put("code",10001);
            result.put("message","审批管理信息导入成功！");
            result.put("data","civilZipPath");
        } catch (IOException e) {
            result.put("code",10000);
            result.put("message","审批信息导入失败！");
            result.put("data","");
        }
        return result;
    }

}
