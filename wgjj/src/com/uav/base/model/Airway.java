package com.uav.base.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;


@Data
@Entity
@Table(name = "airway")
public class Airway implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "airway_id")
	private String airwayId;//

	@Column(name = "airway_name")
	private String airwayName;//

	@Column(name = "airway_type")
	private String airwayType;//

	@Column(name = "airway_fragment_no")
	private Integer airwayFragmentNo;//

	@Column(name = "direction")
	private String direction;//

	@Column(name = "altitude_mode")
	private Integer altitudeMode;//

	@Column(name = "minimum_altitude")
	private Integer minimumAltitude;//

	@Column(name = "maximum_altitude")
	private Integer maximumAltitude;//

	@Column(name = "width_mode")
	private Integer widthMode;//

	@Column(name = "width")
	private Double width;//
	
	@Transient
	private List<AirwayGeomSegment> geomSegList;

	public Airway(String airwayId, String airwayName, String airwayType, Integer airwayFragmentNo, String direction, Integer altitudeMode,
			Integer minimumAltitude, Integer maximumAltitude, Integer widthMode, Double width) {
		super();
		this.airwayId = airwayId;
		this.airwayName = airwayName;
		this.airwayType = airwayType;
		this.airwayFragmentNo = airwayFragmentNo;
		this.direction = direction;
		this.altitudeMode = altitudeMode;
		this.minimumAltitude = minimumAltitude;
		this.maximumAltitude = maximumAltitude;
		this.widthMode = widthMode;
		this.width = width;
	}

	public Airway() {
		super();
	}
}
