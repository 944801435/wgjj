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
@Table(name = "layer")
public class Layer implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name="layer_id")
    private String layerId;//

    @Column(name="name")
    private String name;//图层名称

    @Column(name="sequence_no")
    private Integer sequenceNo;//排序

    @Column(name="group_id")
    private String groupId;//

    @Column(name="layer_style")
    private String layerStyle;//图层样式

    @Column(name="shape")
    private String shape;//图层形状

}
