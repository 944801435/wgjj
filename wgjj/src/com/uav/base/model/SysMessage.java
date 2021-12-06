package com.uav.base.model;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;

/**
 * The persistent class for the sys_message database table.
 * 
 */
@Data
@Entity
@Table(name = "sys_message")
@NamedQuery(name = "SysMessage.findAll", query = "SELECT s FROM SysMessage s")
public class SysMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "IdGenerator")
	@GenericGenerator(name = "IdGenerator", strategy = "com.uav.base.util.IdGenerator", parameters = {
			@Parameter(name = "tableName", value = "sys_message"), @Parameter(name = "idLength", value = "10") })
	@Column(name = "MSG_SEQ")
	private String msgSeq;

	@Column(name = "RCV_TYPE")
	private String rcvType;// 接收者类别 1系统用户，2APP用户。

	@Column(name = "RCV_ID")
	private String rcvId;// 接收者标识

	@Column(name = "MSG_TIME")
	private String msgTime;// 消息时间

	@Column(name = "EXP_TIME")
	private String expTime;// 过期时间

	@Column(name = "IS_NOTIFY")
	private String isNotify;// 手机通知 1是，0否。APP用户采用手机推送机制，系统用户采用短信推送机制。

	@Column(name = "NOTIFY_STS")
	private String notifySts;// 通知状态 0未推送，1推送成功，2推送失败。

	@Lob
	@Column(name = "MSG_TEXT")
	private String msgText;// 消息文本

	@Column(name = "READ_STS")
	private String readSts;// 已读状态 1已读，0未读。

	@Column(name = "MENU_URL")
	private String menuUrl;// 菜单链接

	@Column(name = "MODEL_ID")
	private String modelId;// 实体标识

}