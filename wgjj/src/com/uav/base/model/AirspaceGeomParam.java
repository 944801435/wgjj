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
@Table(name = "airspace_geom_param")
public class AirspaceGeomParam implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "airspace_geom_param_id")
	private String airspaceGeomParamId;//

	@Column(name = "centerx")
	private Double centerx;// 圆心经度

	@Column(name = "centery")
	private Double centery;// 圆心纬度

	@Column(name = "radius")
	private Double radius;// 半径

	@Column(name = "long_axis")
	private Double longAxis;// 长圆半径

	@Column(name = "short_axis")
	private Double shortAxis;// 短圆半径

	@Column(name = "start_angle")
	private Double startAngle;// 起始角

	@Column(name = "end_angle")
	private Double endAngle;// 终止角

	@Column(name = "airspace_points")
	private String airspacePoints;// 多边形经纬度：多个经纬度以;分割

	@Column(name = "rotate_angle")
	private Double rotateAngle;// 旋转角

	@Column(name = "max_lon")
	private Double maxLon;// 最大精度

	@Column(name = "min_lon")
	private Double minLon;// 最小精度

	@Column(name = "max_lat")
	private Double maxLat;// 最大纬度

	@Column(name = "min_lat")
	private Double minLat;// 最小纬度

}
