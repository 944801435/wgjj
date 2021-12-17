package com.brilliance.base.model;

import lombok.Data;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "airspace")
public class Airspace implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "airspace_id")
	private String airspaceId;//

	@Column(name = "airspace_geom_param_id")
	private String airspaceGeomParamId;// 空域图形元素id

	@Column(name = "airspace_name")
	private String airspaceName;// 空域名称

	@Column(name = "airspace_type")
	private String airspaceType;// 空域类型

	@Column(name = "airspace_subclass_type")
	private Integer airspaceSubclassType;//

	@Column(name = "airspace_shape")
	private Integer airspaceShape;// 空域形状

	@Column(name = "min_altitude_type")
	private String minAltitudeType;// 最小高度类别:AGL

	@Column(name = "max_altitude_type")
	private String maxAltitudeType;// 最大高度类别:MSL

	@Column(name = "min_altitude")
	private Integer minAltitude;// 底高

	@Column(name = "max_altitude")
	private Integer maxAltitude;// 顶高

	@Column(name = "open_time")
	private Date openTime;// 开启时间

	@Column(name = "close_time")
	private Date closeTime;// 关闭时间

}
