package com.uav.base.model.internetModel;

import java.io.Serializable;

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
@Table(name = "note_civil_message")
public class NoteCivilMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "note_id")
	private int noteId;//文书id

	@Column(name = "route_info")
	private String routeInfo;// 航线信息
	
	@Column(name = "permit_number")
	private String permitNumber;// 许可号
	
	@Column(name = "file_name")
	private String fileName;// 文件名
	
	@Column(name = "file_path")
	private String filePath;//文件存储地址
	
	@Column(name = "reply_content")
	private String replyContent;//回复内容
	
	@Column(name = "create_time")
	private String createTime;// 创建时间
	
	@ManyToOne(targetEntity = NotePlanInfo.class)
	@JoinColumn(name = "note_id", insertable = false, updatable = false)
	private NotePlanInfo notePlanInfo;
	
	@Transient
	private String begTime;
	@Transient
	private String endTime;
}
