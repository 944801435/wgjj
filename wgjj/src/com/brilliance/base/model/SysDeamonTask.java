package com.brilliance.base.model;

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
@Table(name = "SYS_DEAMON_TASK")
@NamedQuery(name = "SysDeamonTask.findAll", query = "SELECT q FROM SysDeamonTask q")
public class SysDeamonTask implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "IdGenerator")  
    @GenericGenerator(name = "IdGenerator", strategy = "com.brilliance.base.util.IdGenerator", 
    parameters = { 
    		@Parameter(name = "tableName", value = "sys_deamon_task"),
    		@Parameter(name = "idLength", value = "10")})
	@Column(name="TASK_SEQ")
	private String taskSeq;

	@Column(name = "TASK_TYPE", length = 20)
	private String taskType;

	@Column(name = "TASK_DATE", length = 10)
	private String taskDate;
	
	
	@Column(name = "BEG_TIME", length = 19)
	private String begTime;
	
	@Column(name = "END_TIME", length = 19)
	private String endTime;
	
	@Column(name = "SRV_IP", length = 30)
	private String srvIp;
	
	@Column(name = "TASK_STS", length = 1)
	private String taskSts;
	
	@Column(name = "SLD_DATE", length = 10)
	private String sldDate;
	
	@Override
	public String toString() {
		return "SysDeamonTask [taskSeq=" + taskSeq + ", taskType=" + taskType + ", taskDate=" + taskDate + ", begTime="
				+ begTime + ", endTime=" + endTime + ", srvIp=" + srvIp + ", taskSts=" + taskSts + ", sldDate="
				+ sldDate + "]";
	}

	public SysDeamonTask() {
		
	}

	public String getTaskSeq() {
		return taskSeq;
	}

	public void setTaskSeq(String taskSeq) {
		this.taskSeq = taskSeq;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
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

	public String getSldDate() {
		return sldDate;
	}

	public void setSldDate(String sldDate) {
		this.sldDate = sldDate;
	}

}