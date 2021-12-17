package com.brilliance.base.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the UnitDict database table.
 * 
 */
@Entity
@Table(name="unit_dict")
public class UnitDict implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="DICT_ID")
	private String dictId;

	@Column(name="UNIT_NAME")
	private String unitName;

	@Column(name="VALID_STS")
	private String validSts;

	public UnitDict() {
		
	}

	public String getDictId() {
		return dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getValidSts() {
		return validSts;
	}

	public void setValidSts(String validSts) {
		this.validSts = validSts;
	}

	@Override
	public String toString() {
		return "UnitDict [dictId=" + dictId + ", unitName=" + unitName + ", validSts=" + validSts + "]";
	}
}