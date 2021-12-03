package com.uav.web.view.system.sysMenu;

import java.util.List;

import lombok.Data;

/**
 * 用于封装生成前端树控件的数据对象.
 * @ClassName: MenuTreeNode
 * @Description: 
 * @author 
 * @date 2018年8月27日 下午1:31:17
 */
@Data
public class MenuTreeNode  {

	private String id;
	private String name;
	private String parentId;
	private boolean isParent;
	private List<MenuTreeNode> children;
	private boolean checked;
	private int sx;
	
	public List<MenuTreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<MenuTreeNode> children) {
		this.children = children;
	}
	public boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public boolean getChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}
