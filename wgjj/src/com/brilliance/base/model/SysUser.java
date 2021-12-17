package com.brilliance.base.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;

/**
 * The persistent class for the sys_user database table.
 * 
 */
@Data
@Entity
@Table(name = "sys_user")
@NamedQuery(name = "SysUser.findAll", query = "SELECT s FROM SysUser s")
public class SysUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "IdGenerator")
	@GenericGenerator(name = "IdGenerator", strategy = "com.brilliance.base.util.IdGenerator", parameters = {
			@Parameter(name = "tableName", value = "sys_user"), @Parameter(name = "idLength", value = "10") })
	@Column(name = "USER_ID")
	private String userId;// 用户ID

	@Column(name = "DEPT_ID")
	private String deptId;// 部门ID

	@Column(name = "USER_NAME")
	private String userName;// 姓名

	@Column(name = "USER_PWD")
	private String userPwd;// 密码 MD5(USER_ID+密码明文)

	private String phone;// 电话

	@Column(name = "USER_DESC")
	private String userDesc;// 备注

	@Column(name = "IS_ADMIN")
	private String isAdmin;// 是否管理员

	@Column(name = "USER_STS")
	private String userSts;// 启用状态 0禁用、1启用

	@Column(name = "LAST_IP")
	private String lastIp;//最后IP

	@Column(name = "LAST_TIME")
	private String lastTime;// 最后时间

	@Column(name = "INIT_MENU_ID")
	private Integer initMenuId;// 初始菜单
	
	@Column(name = "RANGE_LOC")
	private String rangeLoc;// 可视范围
	
	@Column(name = "ROLE_ID")
	private String roleId;

	@ManyToOne(targetEntity = SysDept.class)
	@JoinColumn(name = "DEPT_ID", insertable = false, updatable = false)
	private SysDept sysDept;
	
	@ManyToOne(targetEntity = SysRole.class)
	@JoinColumn(name = "ROLE_ID", insertable = false, updatable = false)
	private SysRole sysRole;
	
	// 页面参数
	@Transient
	private String deptName;
	@Transient
	private String roleName;

}