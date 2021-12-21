package com.brilliance.web.view.civilreport;

import lombok.Data;

@Data
public class CivilReportDTO {
    //审批许可号
    private String documentNum;
    //照会号
    private String noteNo;
    //状态
    private Integer status;

    //开始日期
    private String startDate;

    //结束日期
    private String endDate;

    //国家
    private String nationality;
    //飞行时间
    private String flightTime;
}
