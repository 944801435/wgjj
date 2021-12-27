package com.brilliance.base.model.internetModel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

/**
 * @author gl
 * 照会信息表
 * */
@Data
@Entity
@Table(name = "note_plan_info")
public class NotePlanInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "note_id")
	private int noteId;
	
	@Column(name = "document_num")
	private String documentNum;// 文书编号
	
	@Column(name = "letter_unit")
	private String letterUnit;// 来函单位

	@Column(name = "person_name")
	private String personName;// 姓名

	@Column(name = "tel_no")
	private String telNo;// 电话
	
	@Column(name = "mission")
	private String mission;// 任务目的

	@Column(name = "nationality")
	private String nationality;// 照会国籍

	@Column(name = "reg_no")
	private String regNo;// 注册号

	@Column(name = "call_number")
	private String callNumber;// 呼号

	@Column(name = "model")
	private String model;// 机型（属性）

	@Column(name = "person_number")
	private Integer personNumber;// 机组人数

	@Column(name = "operator")
	private String operator;// 运营方

	@Column(name = "other")
	private String other;// 其他
	
	@Column(name="air_number")
	private Integer airNumber; //架数
	
	@Column(name="flight_time")
	private String flightTime;  //飞行时间
	
	@Column(name="flight_plan")
	private String flightPlan;//飞行计划
	
	@Column(name = "create_time")
	private String createTime;// 创建时间
	
	@Column(name="creator")
	private String creator; //创建人
	
	@Column(name = "note_no")
	private String noteNo; //照会号
	
	@Column(name = "status")
	private Integer status; //状态
	@Column(name = "del_status")
	private Integer delStatus; //删除状态
	// 页面参数
	//回复时间
	@Transient
	private String replyCreateTime;
	@Transient
	private String permitNumber;// 许可号
	@Transient
	private String replyContent;//回复内容
	@Transient
	private String routeInfo;// 航线信息
	@Transient
	private String entryName; //入境点名称
	@Transient
	private String exitName; //出境点名称
	@Transient
	private String action;
	@Transient
	private String beginTime;
	@Transient
	private String endTime;
	
}
