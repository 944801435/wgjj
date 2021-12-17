package com.brilliance.base.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name = "layer_group")
public class LayerGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "group_id")
	private String groupId;//

	@Column(name = "name")
	private String name;// 分组名称

	@Column(name = "sequence_no")
	private Integer sequenceNo;// 分组排序

}
