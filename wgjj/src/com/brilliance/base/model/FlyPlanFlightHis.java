package com.brilliance.base.model;

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
@Table(name = "fly_plan_flight_his")
public class FlyPlanFlightHis implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "IdGenerator")
	@GenericGenerator(name = "IdGenerator", strategy = "com.brilliance.base.util.IdGenerator", parameters = {
			@Parameter(name = "tableName", value = "fly_plan_flight_his"), @Parameter(name = "idLength", value = "10") })
	@Column(name = "fpfh_seq")
	private String fpfhSeq;//

	@Column(name = "fph_seq")
	private String fphSeq;//

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

}
