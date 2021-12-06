package com.uav.base.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the violation database table.
 * 
 */
@Entity
@Table(name="violation")
public class Violation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="VIO_ID")
	private String vioId;

	@Column(name="NATIONALITY")
	private String nationality;

	@Column(name="MODEL")
	private String model;

	@Column(name="ACID")
	private String acid;

	@Column(name="INFO")
	private String info;

	@Column(name="CREATE_TIME")
	private String createTime;

	@Column(name="VALID_STS")
	private String validSts;

	public Violation() {
		
	}

	public String getVioId() {
		return vioId;
	}

	public void setVioId(String vioId) {
		this.vioId = vioId;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getAcid() {
		return acid;
	}

	public void setAcid(String acid) {
		this.acid = acid;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getValidSts() {
		return validSts;
	}

	public void setValidSts(String validSts) {
		this.validSts = validSts;
	}

	@Override
	public String toString() {
		return "Violation [vioId=" + vioId + ", nationality=" + nationality + ", model=" + model +", createTime=" + createTime +", validSts=" + validSts + ", info=" + info + "]";
	}
}