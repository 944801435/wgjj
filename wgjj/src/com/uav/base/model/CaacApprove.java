package com.uav.base.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;

@Data
@Entity
@Table(name = "caac_approve")
public class CaacApprove implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "IdGenerator")
	@GenericGenerator(name = "IdGenerator", strategy = "com.uav.base.util.IdGenerator", parameters = {
			@Parameter(name = "tableName", value = "caac_approve"), @Parameter(name = "idLength", value = "10") })
	@Column(name = "ca_seq")
	private String caSeq;// 民航意见编号

	@Column(name = "note_seq")
	private String noteSeq;// 文书编号

	@Column(name = "license_no")
	private String licenseNo;// 许可证号

	@Column(name = "date_sked")
	private String dateSked;// 日期类型

	@Column(name = "app_ver")
	private Integer appVer;// 民航意见版本

	@Column(name = "app_sts")
	private String appSts;// 民航意见采纳状态：1接受0未接受

	@Column(name = "ca_file_id")
	private String caFileId;// 民航意见扫描件,多个以','分割
	
	@Column(name = "crt_time")
	private String crtTime;
	
	@Column(name = "crt_dept")
	private String crtDept;

	@Column(name = "crt_user")
	private String crtUser;
	
	@Column(name = "UPLOAD_FILE_ID")
	private String uploadFileId;// 上传的zip文件id
	
	@Column(name = "caac_zip_file_id")
	private String caacZipFileId;// 民航意见文书扫描件压缩包

	@ManyToOne(targetEntity = Note.class)
	@JoinColumn(name = "NOTE_SEQ", insertable = false, updatable = false)
	private Note note;
	
}
