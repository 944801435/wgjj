package com.uav.base.model;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;

/**
 * The persistent class for the sys_file database table.
 * 
 */
@Data
@Entity
@Table(name = "sys_file")
@NamedQuery(name = "SysFile.findAll", query = "SELECT s FROM SysFile s")
public class SysFile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "IdGenerator")
	@GenericGenerator(name = "IdGenerator", strategy = "com.uav.base.util.IdGenerator", parameters = {
			@Parameter(name = "tableName", value = "sys_file"), @Parameter(name = "idLength", value = "10") })
	@Column(name = "FILE_ID")
	private String fileId;

	@Column(name = "FILE_NAME")
	private String fileName;// 文件名称

	@Column(name = "FILE_SIZE")
	private int fileSize;// 文件大小

	@Column(name = "FILE_PATH")
	private String filePath;// 文件路径

	@Column(name = "CONTENT_TYPE")
	private String contentType;

	@Column(name = "UPLOAD_USER")
	private String uploadUser;// 上传用户

	@Column(name = "UPLOAD_TIME")
	private String uploadTime;// 上传时间

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "FILE_BYTES", columnDefinition = "BLOB")
	private byte[] fileBytes;// 文件内容

	@Column(name = "SAVE_TYPE")
	private String saveType;// 保存类别 d数据库
	
}