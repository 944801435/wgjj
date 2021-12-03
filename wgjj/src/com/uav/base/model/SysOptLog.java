package com.uav.base.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

/**
 * The persistent class for the sys_opt_log database table.
 * 
 */
@Data
@Entity
@Table(name = "sys_opt_log")
@NamedQuery(name = "SysOptLog.findAll", query = "SELECT s FROM SysOptLog s")
public class SysOptLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LOG_SEQ")
	private int logSeq;

	@Column(name = "DEPT_ID")
	private String deptId;// 部门ID

	@Column(name = "USER_ID")
	private String userId;// 用户ID

	@Column(name = "OPT_TIME")
	private String optTime;// 操作时间

	@Column(name = "OPT_OBJ")
	private String optObj;// 操作对象

	@Column(name = "OPT_CONTENT")
	private String optContent;// 操作内容

	@Column(name = "OP_STATE")
	private String opState;// 操作结果 0失败，1成功

	// 页面参数
	@Transient
	private String userName;
	@Transient
	private String beginTime;
	@Transient
	private String endTime;

	public SysOptLog(String deptId, String userId, String optTime, String optObj, String optContent, String opState) {
		super();
		this.deptId = deptId;
		this.userId = userId;
		this.optTime = optTime;
		this.optObj = optObj;
		this.optContent = optContent;
		this.opState = opState;
	}

	public SysOptLog() {
		super();
	}

}