package com.uav.base.model;

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
@Table(name = "layer_feature")
public class LayerFeature implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name="feature_id")
    private String featureId;//图元id

    @Column(name="layer_id")
    private String layerId;//

    @Column(name="feature_style")
    private String featureStyle;//

}
