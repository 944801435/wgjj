package com.brilliance.base.util.spacial;

public class BufferLineEntity
{
    public Point ptOrigF=new Point();
    public Point ptOrigS=new Point();
    
    public Point ptFS=new Point();
    public Point ptFE=new Point();
    public double angleFS=0;
    public double angleFE=0;
    
    public Point ptSS=new Point();
    public Point ptSE=new Point();
    public double angleSS=0;
    public double angleSE=0;
    
    public BufferLineEntity(Point origF,Point origS)
    {
        ptOrigF.x=origF.x;
        ptOrigF.y=origF.y;

        ptOrigS.x=origS.x;
        ptOrigS.y=origS.y;
    }
}
