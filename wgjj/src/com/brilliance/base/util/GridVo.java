package com.brilliance.base.util;

import java.util.LinkedList;
import java.util.List;

import com.brilliance.base.util.spacial.Point;

public class GridVo {
	private Point[] points=null;//四个定点
	public int gridLvl=0;//网格等级，1级四分后为2级，2级四分后为3级，以此类推
	public int maxLvl=0;//最高等级，即最细粒度等级
	private List<GridVo> childGrids=null;
	public int xidx=0;
	public int yidx=0;
	
	public GridVo(int gridLvl,int maxLvl){
		points=new Point[4];
		this.gridLvl=gridLvl;
		this.maxLvl=maxLvl;
	}
	
	public void setPoint(int idx,Point point){
		points[idx]=point;
	}
	
	public void addChild(GridVo child){
		if(childGrids==null)
			childGrids=new LinkedList<GridVo>();
		childGrids.add(child);
	}
	
	public Point[] getPoints(){
		return points;
	}
	
	public List<GridVo> getChildGrids(){
		return childGrids;
	}
}