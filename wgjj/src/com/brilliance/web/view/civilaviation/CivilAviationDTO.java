package com.brilliance.web.view.civilaviation;

import lombok.Data;

import java.io.Serializable;

@Data
public class CivilAviationDTO implements Serializable {
    //照会编号
    private String documentNum;
    //文书编号号
    private String noteNo;
    //状态
    private Integer status;
    //国家
    private String nationality;
    //飞行时间
    private String flightTime;
    //回复时间
    private String replyCreateTime;
}
