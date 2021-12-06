package com.uav.base.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * The persistent class for the SYS_CONFIG database table.
 * 
 */
@Entity
@Table(name = "SYS_DEAMON_TASK_SD")
@NamedQuery(name = "SysDeamonTaskSd.findAll", query = "SELECT q FROM SysDeamonTaskSd q")
public class SysDeamonTaskSd implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "IdGenerator")  
    @GenericGenerator(name = "IdGenerator", strategy = "com.uav.base.util.IdGenerator", 
    parameters = { 
    		@Parameter(name = "tableName", value = "sys_deamon_task_sd"),
    		@Parameter(name = "idLength", value = "10")})
	@Column(name="SD_SEQ")
	private String sdSeq;

	@Column(name = "TASK_DATE", length = 10)
	private String taskDate;
	
	@Column(name = "SD_BEG", length = 5)
	private String sdBeg;
	
	@Column(name = "SD_END", length = 5)
	private String sdEnd;
	
	@Column(name = "BEG_TIME", length = 19)
	private String begTime;
	
	@Column(name = "END_TIME", length = 19)
	private String endTime;
	
	@Column(name = "SRV_IP", length = 30)
	private String srvIp;
	
	@Column(name = "TASK_STS", length = 1)
	private String taskSts;
	
	@Override
	public String toString() {
		return "SysDeamonTaskSd [sdSeq=" + sdSeq + ", taskDate=" + taskDate + ", sdBeg=" + sdBeg + ", sdEnd=" + sdEnd
				+ ", begTime=" + begTime + ", endTime=" + endTime + ", srvIp=" + srvIp + ", taskSts=" + taskSts + "]";
	}

	public SysDeamonTaskSd() {
		
	}

	public String getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}

	public String getBegTime() {
		return begTime;
	}

	public void setBegTime(String begTime) {
		this.begTime = begTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSrvIp() {
		return srvIp;
	}

	public void setSrvIp(String srvIp) {
		this.srvIp = srvIp;
	}

	public String getTaskSts() {
		return taskSts;
	}

	public void setTaskSts(String taskSts) {
		this.taskSts = taskSts;
	}

	public String getSdSeq() {
		return sdSeq;
	}

	public void setSdSeq(String sdSeq) {
		this.sdSeq = sdSeq;
	}

	public String getSdBeg() {
		return sdBeg;
	}

	public void setSdBeg(String sdBeg) {
		this.sdBeg = sdBeg;
	}

	public String getSdEnd() {
		return sdEnd;
	}

	public void setSdEnd(String sdEnd) {
		this.sdEnd = sdEnd;
	}
	
	
}