package com.brilliance.base.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Immutable
@AllArgsConstructor
@NoArgsConstructor
@IdClass(AirwayView1Id.class)
@Subselect("select * from airway_view_1")
public class AirwayView1 implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
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

	@Column(name = "sequence_no")
	private Integer sequenceNo;//

	@Column(name = "segment_minimum_altitude")
	private Integer segmentMinimumAltitude;//

	@Column(name = "segment_maximum_altitude")
	private Integer segmentMaximumAltitude;//

	@Column(name = "width_1")
	private Double width1;//

	@Column(name = "width_2")
	private Double width2;//

	@Id
	@Column(name = "from_waypoint_id")
	private String fromWaypointId;//

	@Id
	@Column(name = "to_waypoint_id")
	private String toWaypointId;//

	@Column(name = "airway_geom_segment_id")
	private String airwayGeomSegmentId;//

	@Column(name = "from_ident")
	private String fromIdent;//

	@Column(name = "from_region")
	private String fromRegion;//

	@Column(name = "from_lonx")
	private Double fromLonx;//

	@Column(name = "from_laty")
	private Double fromLaty;//

	@Column(name = "to_ident")
	private String toIdent;//

	@Column(name = "to_region")
	private String toRegion;//

	@Column(name = "to_lonx")
	private Double toLonx;//

	@Column(name = "to_laty")
	private Double toLaty;//

}
