package com.brilliance.base.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;

@Data
@Entity
@Table(name = "note_flight_way")
public class NoteFlightWay implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "IdGenerator")
	@GenericGenerator(name = "IdGenerator", strategy = "com.brilliance.base.util.IdGenerator", parameters = {
			@Parameter(name = "tableName", value = "note_flight"), @Parameter(name = "idLength", value = "10") })
	@Column(name = "nfw_seq")
	private String nfwSeq;//

	@Column(name = "note_seq")
	private String noteSeq;
	
	@Column(name = "nf_seq")
	private String nfSeq;//

	private String ident;// 航点名称

	private Double lonx;// 经度

	private Double laty;// 纬度

	private Double alt;// 高度

	public NoteFlightWay(String noteSeq, String nfSeq, String ident, Double lonx, Double laty, Double alt) {
		super();
		this.noteSeq = noteSeq;
		this.nfSeq = nfSeq;
		this.ident = ident;
		this.lonx = lonx;
		this.laty = laty;
		this.alt = alt;
	}

	public NoteFlightWay() {
		super();
	}
}
