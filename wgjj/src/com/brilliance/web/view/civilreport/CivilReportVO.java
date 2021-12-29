package com.brilliance.web.view.civilreport;

import com.brilliance.base.model.internetModel.NotePlanInfo;
import com.brilliance.base.model.internetModel.NoteReport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CivilReportVO {
    //飞机信息与照会信息
    private NotePlanInfo planInfo;

    //审批许可管理
    private NoteReport noteReport;
}
