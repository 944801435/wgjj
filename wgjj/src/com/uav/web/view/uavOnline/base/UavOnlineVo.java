package com.uav.web.view.uavOnline.base;

import org.apache.commons.lang3.StringUtils;

import com.uav.base.common.Constants;

import lombok.Data;

@Data
public class UavOnlineVo {
	private String pn;// 飞机编号
	private String planid;// 计划ID
	private String lng;// 经度
	private String lat;// 纬度
	private String alt;// 海高
	private String hei;// 真高
	private String spe;// 速度
	private String dir;// 航向
	private String tol;// 起飞：3 降落：4
	private String lvl;// 飞行等级
	private String type;// 飞机类型
	private String owntype;// 所有者类型
	private String ownname;// 所有者名称
	private String phone;// 联系电话
	private String mcu;// MCU时间
	private String wrn;// 告警信息NN|NP|LC|VC|AS-[AirspceID]未登记飞机飞行NN,无计划飞行NP,计划飞行偏航LC,侵入禁飞区域AS-[AID],超出飞行高度VC
	private String time;// 接收时间
	private String lvlDesc;// 飞机等级描述
	private String typeDesc;
	private String owntypeDesc;
	private long timeSecond;
	private String wrnDesc;
	private String warnBegTime;// 告警开始时间
	private String bizFlyId;// 飞行架次id，回放用
	private String relSn;// 关联无人机sn
	private String model;// 型号
	private String src;// 来源
	private String srcDesc; 

	public UavOnlineVo(String pn, String tol, String time){
		this.pn = pn;
		this.tol = tol;
		this.time = time;
	}
	
	public UavOnlineVo(String pn, String planid, String lng, String lat, String alt, String hei, String spe, String dir, String tol, String lvl, String type,
			String owntype, String ownname, String phone, String wrn, String warnBegTime, String mcu, String time) {
		this.pn = pn;
		this.planid = (planid == null ? Constants.STR_BLANK : planid);
		this.lng = lng;
		this.lat = lat;
		this.alt = alt;
		this.hei = hei;
		this.spe = spe;
		this.dir = dir;
		this.tol = StringUtils.isBlank(tol) ? Constants.UAV_FLY_DOWN : tol;
		this.lvl = lvl;
		this.type = type;
		this.owntype = (owntype == null ? Constants.STR_BLANK : owntype);
		this.ownname = (ownname == null ? Constants.STR_BLANK : ownname);
		this.phone = (phone == null ? Constants.STR_BLANK : phone);
		this.wrn = (wrn == null ? Constants.STR_BLANK : wrn);
		this.warnBegTime = (warnBegTime == null ? Constants.STR_BLANK : warnBegTime);
		this.time = time;
	}
	
}