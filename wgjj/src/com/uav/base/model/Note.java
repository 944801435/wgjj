package com.uav.base.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;


@Data
@Entity
@Table(name = "note")
public class Note implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "note_seq")
	private String noteSeq;// 文书编号

	@Column(name = "mission")
	private String mission;// 任务目的

	@Column(name = "nationality")
	private String nationality;// 照会国籍

	@Column(name = "reg_no")
	private String regNo;// 注册号

	@Column(name = "ssr_code")
	private String ssrCode;// 二次雷达代码

	@Column(name = "acid")
	private String acid;// 呼号

	@Column(name = "model")
	private String model;// 机型（属性）

	@Column(name = "person_number")
	private Integer personNumber;// 机组人数

	@Column(name = "operator")
	private String operator;// 运营方

	@Column(name = "other")
	private String other;// 其他

	@Column(name = "letter_unit")
	private String letterUnit;// 来函单位

	@Column(name = "person_name")
	private String personName;// 姓名

	@Column(name = "tel_no")
	private String telNo;// 电话

	@Column(name = "note_file_id")
	private String noteFileId;// 照会文书扫描件,多个以','分割
	
	@Column(name = "crt_time")
	private String crtTime;
	
	@Column(name = "crt_dept")
	private String crtDept;

	@Column(name = "crt_user")
	private String crtUser;

	@Column(name = "CAN_DEL_STS")
	private String canDelSts;// 能否删除状态，1能0否，如果导入了民航意见，则不可删除
	
	@Column(name = "UPLOAD_FILE_ID")
	private String uploadFileId;// 上传的zip文件id
	
	@Column(name = "note_zip_file_id")
	private String noteZipFileId;// 照会文书扫描件压缩包
	
	@Column(name = "LICENSE_NO")
	private String licenseNo;// 许可证号
	
	@Transient
	private String begTime;
	@Transient
	private String endTime;
}
