package com.brilliance.base.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AirwayView1Id implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String airwayId;//
	
	private String fromWaypointId;//
	
	private String toWaypointId;//

}
