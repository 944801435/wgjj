package com.uav.web.view.civilaviation;

import com.uav.base.model.internetModel.NotePlanInfo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author gl
 * 民航回复信息表
 * */
@Data
public class CivilReplyDTO implements Serializable {
	
	private int noteId;//文书id

	private String permitNumber;// 民航许可编号

	private String planTime;// 计划时间

	private String upAirport;// 起飞机场

	private String downAirport;//降落机场

	private String planRoute;// 计划航线

	private String bakRoute;//备用航线

	private String fileName;// 文件名
	
	private String fileUrl;//文件存储地址

	private String createTime;// 创建时间
	

}
