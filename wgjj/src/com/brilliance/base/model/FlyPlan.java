package com.brilliance.base.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;


@Data
@Entity
@Table(name = "fly_plan")
public class FlyPlan implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "IdGenerator")
	@GenericGenerator(name = "IdGenerator", strategy = "com.brilliance.base.util.IdGenerator", parameters = {
			@Parameter(name = "tableName", value = "fly_plan"), @Parameter(name = "idLength", value = "10") })
	@Column(name = "plan_seq")
	private String planSeq;//

	@Column(name = "note_seq")
	private String noteSeq;// 文书编号

	@Column(name = "license_no")
	private String licenseNo;// 许可证号

	@Column(name = "mission")
	private String mission;// 任务目的

	@Column(name = "nationality")
	private String nationality;// 国别

	@Column(name = "reg_no")
	private String regNo;// 注册号

	@Column(name = "ssr_code")
	private String ssrCode;// 二次雷达代码

	@Column(name = "note_file_id")
	private String noteFileId;// 文书扫描件

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

	@Column(name = "acid")
	private String acid;// 呼号

	@Column(name = "plan_sts")
	private String planSts;// 计划状态0未提交1审核中2批准3驳回

	@Column(name = "reject_msg")
	private String rejectMsg;// 计划驳回原因

	@Column(name = "his_sts")
	private String hisSts;// 是否插入的历史计划
	
	@Column(name = "crt_time")
	private String crtTime;
	
	@Column(name = "crt_dept")
	private String crtDept;

	@Column(name = "crt_user")
	private String crtUser;

	@Column(name = "app_opt_dept")
	private String appOptDept;// 审批文书维护部门

	@Column(name = "app_opt_user")
	private String appOptUser;// 审批文书维护用户

	@Column(name = "app_opt_time")
	private String appOptTime;// 审批文书维护时间

	@Column(name = "app_file_id")
	private String appFileId;// 审批文书文件

	@Column(name = "back_opt_dept")
	private String backOptDept;// 反馈审批文书维护部门

	@Column(name = "back_opt_user")
	private String backOptUser;// 反馈审批文书维护用户

	@Column(name = "back_opt_time")
	private String backOptTime;// 反馈审批文书维护时间

	@Column(name = "back_file_id")
	private String backFileId;// 反馈审批文书文件
	
	@Column(name = "rpt_file_id")
	private String rptFileId;// 许可函WORD文件

	@Column(name = "rpt_opt_dept")
	private String rptOptDept;// 许可函维护部门

	@Column(name = "rpt_opt_user")
	private String rptOptUser;// 许可函维护用户

	@Column(name = "rpt_pdf_file_id")
	private String rptPdfFileId;// 许可函PDF文件

	@Column(name = "rpt_opt_time")
	private String rptOptTime;// 许可函维护时间

	@Column(name = "rpt_sts")
	private String rptSts;// 许可函下发状态0未下发1已下发
	
	@Transient
	private String begTime;
	@Transient
	private String endTime;
}
