package com.brilliance.base.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;


@Data
@Entity
@Table(name = "fly_plan_flight")
public class FlyPlanFlight implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "IdGenerator")
	@GenericGenerator(name = "IdGenerator", strategy = "com.brilliance.base.util.IdGenerator", parameters = {
			@Parameter(name = "tableName", value = "fly_plan_flight"), @Parameter(name = "idLength", value = "10") })
	@Column(name = "fpf_seq")
	private String fpfSeq;//

	@Column(name = "plan_seq")
	private String planSeq;// 计划id

	@Column(name = "flight_file_id")
	private String flightFileId;// 航线示意图

	@Column(name = "radio_frequency")
	private Double radioFrequency;// 通信频率

	@Column(name = "plan_date")
	private String planDate;// 计划日期

	@Column(name = "other_date")
	private String otherDate;// 备用日期

	@Column(name = "flight_body")
	private String flightBody;// 航线报文
	
	@Column(name = "up_airport")
	private String upAirport;// 起飞机场

	@Column(name = "down_airport")
	private String downAirport;// 降落机场
	
	@Column(name = "in_point_ident")
	private String inPointIdent;// 入境点

	@Column(name = "out_point_ident")
	private String outPointIdent;// 出境点

	public FlyPlanFlight() {
		super();
	}

	public FlyPlanFlight(String planSeq, String flightFileId, Double radioFrequency, String planDate, String otherDate, String flightBody, String upAirport, String downAirport,
			String inPointIdent, String outPointIdent) {
		super();
		this.planSeq = planSeq;
		this.flightFileId = flightFileId;
		this.radioFrequency = radioFrequency;
		this.planDate = planDate;
		this.otherDate = otherDate;
		this.flightBody = flightBody;
		this.upAirport = upAirport;
		this.downAirport = downAirport;
		this.inPointIdent = inPointIdent;
		this.outPointIdent = outPointIdent;
	}

	@Transient
	private List<FlyPlanFlightWay> wayList;
}
