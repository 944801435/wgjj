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
@Table(name = "fly_plan_his")
public class FlyPlanHis implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "IdGenerator")
	@GenericGenerator(name = "IdGenerator", strategy = "com.brilliance.base.util.IdGenerator", parameters = {
			@Parameter(name = "tableName", value = "fly_plan_his"), @Parameter(name = "idLength", value = "10") })
	@Column(name = "fph_seq")
	private String fphSeq;//

	@Column(name = "plan_seq")
	private String planSeq;//

	@Column(name = "opt_dept")
	private String optDept;// 维护部门

	@Column(name = "opt_user")
	private String optUser;// 维护用户

	@Column(name = "opt_time")
	private String optTime;// 维护时间

}
