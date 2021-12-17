package com.brilliance.base.model;

import java.io.Serializable;

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
@Table(name = "airway_geom_segment")
public class AirwayGeomSegment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "airway_geom_segment_id")
	private String airwayGeomSegmentId;//

	@Column(name = "airway_id")
	private String airwayId;// 航路id

	@Column(name = "from_waypoint_id")
	private String fromWaypointId;// 上一航路点

	@Column(name = "to_waypoint_id")
	private String toWaypointId;// 下一航路点

	@Column(name = "sequence_no")
	private Integer sequenceNo;// 排序

	@Column(name = "minimum_altitude")
	private Integer minimumAltitude;//

	@Column(name = "maximum_altitude")
	private Integer maximumAltitude;//

	@Column(name = "width_1")
	private Double width1;//

	@Column(name = "width_2")
	private Double width2;//

    @Transient
    private String fromIdent;//

    @Transient
    private String fromRegion;//

    @Transient
    private Double fromLonx;//

    @Transient
    private Double fromLaty;//

    @Transient
    private String toIdent;//

    @Transient
    private String toRegion;//

    @Transient
    private Double toLonx;//

    @Transient
    private Double toLaty;//

	public AirwayGeomSegment(String airwayGeomSegmentId, String airwayId, String fromWaypointId, String toWaypointId, Integer sequenceNo,
			Integer minimumAltitude, Integer maximumAltitude, Double width1, Double width2, String fromIdent, String fromRegion, Double fromLonx,
			Double fromLaty, String toIdent, String toRegion, Double toLonx, Double toLaty) {
		super();
		this.airwayGeomSegmentId = airwayGeomSegmentId;
		this.airwayId = airwayId;
		this.fromWaypointId = fromWaypointId;
		this.toWaypointId = toWaypointId;
		this.sequenceNo = sequenceNo;
		this.minimumAltitude = minimumAltitude;
		this.maximumAltitude = maximumAltitude;
		this.width1 = width1;
		this.width2 = width2;
		this.fromIdent = fromIdent;
		this.fromRegion = fromRegion;
		this.fromLonx = fromLonx;
		this.fromLaty = fromLaty;
		this.toIdent = toIdent;
		this.toRegion = toRegion;
		this.toLonx = toLonx;
		this.toLaty = toLaty;
	}

	public AirwayGeomSegment() {
		super();
	}
}
