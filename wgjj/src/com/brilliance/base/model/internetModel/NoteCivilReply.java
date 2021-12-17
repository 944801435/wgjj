package com.brilliance.base.model.internetModel;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

/**
 * @author gl
 * 民航回复信息表
 * */
@Data
@Entity
@Table(name = "note_civil_reply")
public class NoteCivilReply implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "note_id")
	private int noteId;//文书id

	@Column(name = "permit_number")
	private String permitNumber;// 民航许可编号

	@Column(name = "plan_time")
	private Date planTime;// 计划时间

	@Column(name = "up_airport")
	private String upAirport;// 起飞机场

	@Column(name = "down_airport")
	private String downAirport;//降落机场

	@Column(name = "plan_route")
	private String planRoute;// 计划航线

	@Column(name = "bak_route")
	private String bakRoute;//备用航线

	@Column(name = "file_name")
	private String fileName;// 文件名
	
	@Column(name = "file_url")
	private String fileUrl;//文件存储地址

	@Column(name = "create_time")
	private String createTime;// 创建时间
	
	@ManyToOne(targetEntity = NotePlanInfo.class)
	@JoinColumn(name = "note_id", insertable = false, updatable = false)
	private NotePlanInfo notePlanInfo;
	
}
