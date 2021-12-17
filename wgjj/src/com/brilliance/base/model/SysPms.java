package com.brilliance.base.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the sys_pms database table.
 * 
 */
@Entity
@Table(name="sys_pms")
public class SysPms implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "IdGenerator")
	@GenericGenerator(name = "IdGenerator", strategy = "com.brilliance.base.util.IdGenerator", parameters = {
			@Parameter(name = "tableName", value = "sys_pms"), @Parameter(name = "idLength", value = "10") })
	@Column(name="PMS_ID")
	private String pmsId;

	@Column(name="IS_SYS")
	private String isSys;

	@Column(name="PMS_NAME")
	private String pmsName;
	
	@Column(name="MENU_ID")
	private Integer menuId;
	
	@Column(name="MEMO")
	private String memo;
	
	@Column(name="VALID_STS")
	private String validSts;

	public SysPms() {
		
	}

	public String getPmsId() {
		return this.pmsId;
	}

	public void setPmsId(String pmsId) {
		this.pmsId = pmsId;
	}

	public String getIsSys() {
		return this.isSys;
	}

	public void setIsSys(String isSys) {
		this.isSys = isSys;
	}

	public String getPmsName() {
		return this.pmsName;
	}

	public void setPmsName(String pmsName) {
		this.pmsName = pmsName;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getValidSts() {
		return validSts;
	}

	public void setValidSts(String validSts) {
		this.validSts = validSts;
	}

	@Override
	public String toString() {
		return "SysPms [pmsId=" + pmsId + ", isSys=" + isSys + ", pmsName=" + pmsName + ", menuId=" + menuId + "]";
	}
}