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
@Table(name = "caac_flight_way")
public class CaacFlightWay implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "IdGenerator")
	@GenericGenerator(name = "IdGenerator", strategy = "com.brilliance.base.util.IdGenerator", parameters = {
			@Parameter(name = "tableName", value = "caac_flight_way"), @Parameter(name = "idLength", value = "10") })
	@Column(name = "cafw_seq")
	private String cafwSeq;//

	@Column(name = "ca_seq")
	private String caSeq;

	@Column(name = "caf_seq")
	private String cafSeq;// 航次id

	private String ident;// 航点名称

	private Double lonx;// 经度

	private Double laty;// 纬度

	private Double alt;// 高度

	public CaacFlightWay() {
		super();
	}

	public CaacFlightWay(String caSeq, String cafSeq, String ident, Double lonx, Double laty, Double alt) {
		super();
		this.caSeq = caSeq;
		this.cafSeq = cafSeq;
		this.ident = ident;
		this.lonx = lonx;
		this.laty = laty;
		this.alt = alt;
	}
}
