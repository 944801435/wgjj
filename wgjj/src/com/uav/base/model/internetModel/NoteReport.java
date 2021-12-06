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
 * 审批许可表
 * */
@Data
@Entity
@Table(name = "note_report")
public class NoteReport implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "note_id")
	private int noteId;//文书id

	@Column(name = "file_name")
	private String fileName;// 文件中文名
	
	@Column(name = "file_path")
	private String filePath;//文件存储地址
	
	@Column(name = "document_num")
	private int documentNum;//文件顺序
	
	@Column(name = "status")
	private short status;//文件应用类型 1：照会原件 2：外交部文件
	
	@Column(name = "create_time")
	private String createTime;// 创建时间
	
	@Column(name = "creator")
	private String creator;// 创建人
	
	@ManyToOne(targetEntity = NotePlanInfo.class)
	@JoinColumn(name = "note_id", insertable = false, updatable = false)
	private NotePlanInfo notePlanInfo;
	
	@Transient
	private String beginTime;
	@Transient
	private String endTime;
}
