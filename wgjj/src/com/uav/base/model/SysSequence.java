package com.uav.base.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the sys_sequence database table.
 * 
 */
@Entity
@Table(name="sys_sequence")
@NamedQuery(name="SysSequence.findAll", query="SELECT s FROM SysSequence s")
public class SysSequence implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="SEQ_NAME")
	private String seqName;

	@Column(name="SEQ_VALUE")
	private int seqValue;

	public SysSequence() {
	}

	public String getSeqName() {
		return this.seqName;
	}

	public void setSeqName(String seqName) {
		this.seqName = seqName;
	}

	public int getSeqValue() {
		return this.seqValue;
	}

	public void setSeqValue(int seqValue) {
		this.seqValue = seqValue;
	}

	@Override
	public String toString() {
		return "SysSequence [seqName=" + seqName + ", seqValue=" + seqValue + "]";
	}

}