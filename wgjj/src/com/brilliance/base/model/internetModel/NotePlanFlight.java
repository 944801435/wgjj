package com.brilliance.base.model.internetModel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

/**
 * @author gl
 * 飞行计划信息表
 * */
@Data
@Entity
@Table(name = "note_plan_flight")
public class NotePlanFlight implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "note_id")
	private int noteId;//文书id
	
	@Column(name="flight_time")
	private String flightTime;  //飞行日期
	
	@Column(name="plan_route")
	private String planRoute;//计划航线
	
	@Column(name="alternate_route")
	private String alternateRoute;//备用航线
	
	@Column(name="departure_airport")
	private String departureAirport; //起飞机场
	
	@Column(name="land_airport")
	private String landAirport; //降落机场
	
	@Column(name="entry_name")
	private String entryName; //入境点名称
	
	@Column(name="exit_name")
	private String exitName; //出境点名称
	
	@Column(name = "status")
	private Integer status; //计划类型，1：计划航线 2：备用航线
	
	@ManyToOne(targetEntity = NotePlanInfo.class)
	@JoinColumn(name = "note_id", insertable = false, updatable = false)
	private NotePlanInfo notePlanInfo;
	// 页面参数
	@Transient
	private String beginTime;
	@Transient
	private String endTime;
}
