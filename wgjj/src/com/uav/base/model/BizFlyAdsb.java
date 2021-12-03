package com.uav.base.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;

@Data
@Entity
@Table(name = "biz_fly_adsb")
public class BizFlyAdsb implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "IdGenerator")
	@GenericGenerator(name = "IdGenerator", strategy = "com.uav.base.util.IdGenerator", parameters = {
			@Parameter(name = "tableName", value = "biz_fly_adsb"), @Parameter(name = "idLength", value = "10") })
	@Column(name = "fly_seq")
	private String flySeq;// 架次流水号

	@Column(name = "plan_seq")
	private String planSeq;// 飞行计划

	@Column(name = "beg_time")
	private String begTime;// 起飞时间

	@Column(name = "beg_date")
	private String begDate;// 起飞日期

	@Column(name = "beg_loc")
	private String begLoc;// 起飞坐标

	@Column(name = "fly_sts")
	private String flySts;// 架次状态 3起飞；4降落。

	@Column(name = "end_time")
	private String endTime;// 降落时间

	@Column(name = "end_date")
	private String endDate;// 降落日期

	@Column(name = "end_loc")
	private String endLoc;// 降落坐标

	@Column(name = "ssr_code")
	private String ssrCode;// 二次雷达代码

	@Column(name = "acid")
	private String acid;// 呼号

	@Column(name = "track_number")
	private String trackNumber;// 航迹号

}
