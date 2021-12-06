package com.uav.base.model;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;

/**
 * The persistent class for the sys_role_pms database table.
 * 
 */
@Data
@Entity
@Table(name="sys_role_pms")
@NamedQuery(name="SysRolePms.findAll", query="SELECT s FROM SysRolePms s")
public class SysRolePms implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "IdGenerator")  
    @GenericGenerator(name = "IdGenerator", strategy = "com.uav.base.util.IdGenerator", 
    parameters = { 
    		@Parameter(name = "tableName", value = "sys_role_pms"),
    		@Parameter(name = "idLength", value = "10")})
	@Column(name="RP_SEQ")
	private String rpSeq;

	@Column(name="PMS_ID")
	private String pmsId;

	@Column(name="ROLE_ID")
	private String roleId;

}