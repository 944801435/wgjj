package com.uav.base.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;


@Data
@Entity
@Table(name = "airport")
public class Airport implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "airport_id")
	private String airportId;//

	@Column(name = "ident")
	private String ident;// 机场四字码

	@Column(name = "name")
	private String name;// 机场名称

	@Column(name = "city")
	private String city;//

	@Column(name = "state")
	private String state;//

	@Column(name = "country")
	private String country;// 国家

	@Column(name = "region")
	private String region;// 行政区

	@Column(name = "rating")
	private Integer rating;// 等级划分

	@Column(name = "mag_var")
	private Double magVar;//

	@Column(name = "transition_altitude")
	private Integer transitionAltitude;//

	@Column(name = "altitude")
	private Integer altitude;// 高度

	@Column(name = "lonx")
	private Double lonx;// 经度

	@Column(name = "laty")
	private Double laty;// 纬度

	@Column(name = "apt_file")
	private String aptFile;//

	@Column(name = "revise_area")
	private String reviseArea;//

	@Column(name = "revise_altitude")
	private Integer reviseAltitude;//

}
