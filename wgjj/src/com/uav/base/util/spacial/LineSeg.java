package com.uav.base.util.spacial;

public class LineSeg
{
	public Point s = new Point();

	public Point e = new Point();

	public Point getE()
	{
		return e;
	}

	public void setE(Point eInput)
	{
		this.e.setX(eInput.x);
		this.e.setY(eInput.y);
	}

	public Point getS()
	{
		return s;
	}

	public void setS(Point s)
	{
		this.s.setX(s.x);
		this.s.setY(s.y);
	}
}
