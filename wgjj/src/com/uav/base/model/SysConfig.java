package com.uav.base.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the SYS_CONFIG database table.
 * 
 */
@Entity
@Table(name = "SYS_CONFIG")
@NamedQuery(name = "SysConfig.findAll", query = "SELECT q FROM SysConfig q")
public class SysConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CFG_ID", length = 30)
	private String cfgId;

	@Column(name = "CFG_DESC", length = 50)
	private String cfgDesc;

	@Column(name = "CFG_VALUE", length = 50)
	private String cfgValue;

	public SysConfig() {
	}

	public String getCfgId() {
		return cfgId;
	}

	public void setCfgId(String cfgId) {
		this.cfgId = cfgId;
	}

	public String getCfgDesc() {
		return cfgDesc;
	}

	public void setCfgDesc(String cfgDesc) {
		this.cfgDesc = cfgDesc;
	}

	public String getCfgValue() {
		return cfgValue;
	}

	public void setCfgValue(String cfgValue) {
		this.cfgValue = cfgValue;
	}

	@Override
	public String toString() {
		return "SysConfig [cfgId=" + cfgId + ", cfgDesc=" + cfgDesc + ", cfgValue=" + cfgValue + "]";
	}

}