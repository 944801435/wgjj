package com.uav.base.model;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;
/**
 * The persistent class for the sys_dept database table.
 * 
 */
@Data
@Entity
@Table(name="sys_dept")
@NamedQuery(name="SysDept.findAll", query="SELECT s FROM SysDept s")
public class SysDept implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "IdGenerator")  
    @GenericGenerator(name = "IdGenerator", strategy = "com.uav.base.util.IdGenerator", 
    parameters = { 
    		@Parameter(name = "tableName", value = "sys_dept"),
    		@Parameter(name = "idLength", value = "10")})  
	@Column(name="DEPT_ID")
	private String deptId;// 部门ID

	@Column(name="DEPT_TYPE")
	private String deptType;// 部门类别 N内部、J军方、M民航、G公安

	@Column(name="DEPT_NAME")
	private String deptName;// 部门名称

	@Column(name="DEPT_STS")
	private String deptSts;// 启用状态 1启用；0禁用。
	
	@Column(name="INIT_MENU_ID")
	private Integer initMenuId; // 初始菜单

	@Lob
	@Column(name="RANGE_LOC")
	private String rangeLoc;// 可视范围
}