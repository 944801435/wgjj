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
@Table(name = "fly_plan_flight_way_his")
public class FlyPlanFlightWayHis implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "IdGenerator")
	@GenericGenerator(name = "IdGenerator", strategy = "com.uav.base.util.IdGenerator", parameters = {
			@Parameter(name = "tableName", value = "fly_plan_flight_way_his"), @Parameter(name = "idLength", value = "10") })
	@Column(name = "fpfwh_seq")
	private String fpfwhSeq;//

	@Column(name = "fpfh_seq")
	private String fpfhSeq;//

	private String ident;// 航点名称

	private Double lonx;// 经度

	private Double laty;// 纬度

	private Double alt;// 高度

}
