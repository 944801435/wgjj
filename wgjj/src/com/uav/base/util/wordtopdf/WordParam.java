package com.uav.base.util.wordtopdf;

import lombok.Data;

/**
 * 生成word文档参数类
 */
@Data
public class WordParam {
    private String pc;      //批次
    private String dw;      //单位
    private String xm;      //姓名
    private String dh;      //电话
    private String zhh;     //国家/照会号
    private String jx;      //机型
    private String js;      //架数
    private String fxrq;    //飞行日期
    private String rwmd;    //任务目的
    private String tzdw;    //通知单位，多行用"\n"换行符隔开
    private String cbdw;    //承办单位
    private String lxr;     //联系人
    private String lxdh;    //联系电话
    private String cbyj;    //呈办意见

	public WordParam() {
		super();
	}

	public WordParam(String pc, String dw, String xm, String dh, String zhh, String jx, String js, String fxrq, String rwmd) {
		super();
		this.pc = pc;
		this.dw = dw;
		this.xm = xm;
		this.dh = dh;
		this.zhh = zhh;
		this.jx = jx;
		this.js = js;
		this.fxrq = fxrq;
		this.rwmd = rwmd;
		this.cbyj = "外方申请的飞行航线符合批准的外国军机临时进出我国领空航线，已对外开放，如无不妥，建议回复外交部我们没有不同意见。";
	}
}
