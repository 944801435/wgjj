package com.uav.base.model;

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
@Table(name = "fly_plan_data")
public class FlyPlanData implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "IdGenerator")
	@GenericGenerator(name = "IdGenerator", strategy = "com.uav.base.util.IdGenerator", parameters = {
			@Parameter(name = "tableName", value = "fly_plan_data"), @Parameter(name = "idLength", value = "10") })
	@Column(name = "fpd_seq")
	private String fpdSeq;//

	@Column(name = "plan_seq")
	private String planSeq;// 计划id

	@Column(name = "data_type")
	private String dataType;// 数据包类别：1外交部2民航局3军内

	@Column(name = "app_file_id")
	private String appFileId;// 审批文书

	@Column(name = "flydata_file_id")
	private String flydataFileId;// 航迹回放数据包

	@Column(name = "opt_dept")
	private String optDept;// 维护部门

	@Column(name = "opt_user")
	private String optUser;// 维护用户

	@Column(name = "opt_time")
	private String optTime;// 维护时间

}
