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
@Table(name = "fly_plan_flight_way")
public class FlyPlanFlightWay implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "IdGenerator")
	@GenericGenerator(name = "IdGenerator", strategy = "com.brilliance.base.util.IdGenerator", parameters = {
			@Parameter(name = "tableName", value = "fly_plan_flight_way"), @Parameter(name = "idLength", value = "10") })
	@Column(name = "fpfw_seq")
	private String fpfwSeq;//

	@Column(name = "fpf_seq")
	private String fpfSeq;// 航次id

	private String ident;// 航点名称

	private Double lonx;// 经度

	private Double laty;// 纬度

	private Double alt;// 高度

	public FlyPlanFlightWay() {
		super();
	}

	public FlyPlanFlightWay(String fpfSeq, String ident, Double lonx, Double laty, Double alt) {
		super();
		this.fpfSeq = fpfSeq;
		this.ident = ident;
		this.lonx = lonx;
		this.laty = laty;
		this.alt = alt;
	}
}
