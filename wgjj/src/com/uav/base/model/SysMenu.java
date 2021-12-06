package com.uav.base.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * The persistent class for the WO_AUTHMENU database table.
 * 
 */
@Entity
@Table(name="SYS_MENU")
public class SysMenu implements Serializable {
	private static final long serialVersionUID = 1L;
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SysMenu){
			if(this.menuId.intValue()==((SysMenu)obj).getMenuId().intValue()){
				return true;
			}
		}
		return false;
	}

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="MENU_ID")
	private Integer menuId;
	
	@Column(name="ENABLE_FLAG")
	private String enableFlag;
	
	@Column(name="MENU_NAME")
	private String menuName;
	
	@Column(name="NOTE")
	private String note;
	
	@Column(name="PARENT_ID")
	private Integer parentId;
	
	@Column(name="URL")
	private String url;
	
	@Column(name="SX")
	private Integer sx;
	
	
	/*******页面参数***********************/
	@Transient
	private List<SysMenu> childMenus;
	@Transient
	private boolean checked;

	public SysMenu() {
	}
	public SysMenu(Integer menuId,Integer parentId) {
		this.menuId=menuId;
		this.parentId=parentId;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<SysMenu> getChildMenus() {
		return childMenus;
	}

	public void setChildMenus(List<SysMenu> childMenus) {
		this.childMenus = childMenus;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public Integer getSx() {
		return sx;
	}
	public void setSx(Integer sx) {
		this.sx = sx;
	}


}