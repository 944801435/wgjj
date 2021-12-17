package com.brilliance.base.model;

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
@Table(name = "waypoint")
public class Waypoint implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "waypoint_id")
	private String waypointId;//

	@Column(name = "airport_id")
	private String airportId;// 机场id

	@Column(name = "ident")
	private String ident;// 航路点名称

	@Column(name = "region")
	private String region;// 行政区

	@Column(name = "type")
	private String type;//

	@Column(name = "mag_var")
	private Double magVar;//

	@Column(name = "lonx")
	private Double lonx;// 经度

	@Column(name = "laty")
	private Double laty;// 纬度

	@Column(name = "altitude")
	private Double altitude;// 高度

	@Column(name = "nav_type")
	private String navType;//

}
