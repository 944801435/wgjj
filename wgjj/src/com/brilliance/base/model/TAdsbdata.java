package com.brilliance.base.model;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "t_adsbdata")
public class TAdsbdata implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;// 流水号

	@Column(name = "fly_seq")
	private String flySeq;// 飞行架次

	@Column(name = "plan_seq")
	private String planSeq;// 飞行计划

	@Column(name = "lon")
	private Double lon;// 经度

	@Column(name = "lat")
	private Double lat;// 纬度

	@Column(name = "alt")
	private Double alt;// 海高

	@Column(name = "hei")
	private Double hei;// 真高

	@Column(name = "spe")
	private Double spe;// 速度（米/秒）

	@Column(name = "speed_v")
	private Double speedV;// 垂直速度

	@Column(name = "dir")
	private Integer dir;// 航向（度）,以正北为0度的顺时针角度

	@Column(name = "tol")
	private String tol;// 起降状态，3：起飞 4：降落

	@Column(name = "time")
	private Date time;// 服务器时间

	@Column(name = "mcu")
	private Date mcu;// 信号时间

	@Column(name = "ssr_code")
	private String ssrCode;// 二次雷达代码

	@Column(name = "acid")
	private String acid;// 呼号

	@Column(name = "track_number")
	private String trackNumber;// 航迹号

}
