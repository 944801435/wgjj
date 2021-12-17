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
 * 照会文件表
 * */
@Data
@Entity
@Table(name = "note_files")
public class NoteFiles implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "note_id")
	private int noteId;//文书id

	@Column(name = "file_name_cn")
	private String fileNameCn;// 文件中文名
	
	@Column(name = "file_name_en")
	private String fileNameEn;// 文件英文名
	
	@Column(name = "file_size")
	private int fileSize;// 文件大小
	
	@Column(name = "file_path")
	private String filePath;//文件存储地址
	
	@Column(name = "file_sort")
	private int fileSort;//文件顺序
	
	@Column(name = "file_type")
	private short fileType;//文件应用类型 1：照会原件 2：外交部文件
	
	@Column(name = "ocr_text")
	private String ocrText;//OCR识别内容
	
	@Column(name = "translation_text")
	private String translationText;//翻译内容
	
	@Column(name = "create_time")
	private String createTime;// 创建时间
	
	@Transient
	private String begTime;
	@Transient
	private String endTime;
}
