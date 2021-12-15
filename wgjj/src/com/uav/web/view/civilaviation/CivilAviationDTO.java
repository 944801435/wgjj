package com.uav.web.view.civilaviation;

import lombok.Data;

import java.io.Serializable;

@Data
public class CivilAviationDTO implements Serializable {
    //文书编号
    private String documentNum;
    //照会号
    private String noteNo;
    //状态
    private Integer status;
}
