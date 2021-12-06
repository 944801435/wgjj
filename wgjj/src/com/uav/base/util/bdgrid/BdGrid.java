package com.uav.base.util.bdgrid;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class BdGrid {
	private static final int MAX_LEVEL=8;//只实现到第八级别
	
	//二维
	private static final String EARTH_NORTH="N";//北半球，纬度为正
	private static final String EARTH_SOUTH="S";//南半球，纬度为负
	private static final String[] CODES_LVL1_LON={
			"01","02","03","04","05","06","07","08","09","10",
			"11","12","13","14","15","16","17","18","19","20",
			"21","22","23","24","25","26","27","28","29","30",
			"31","32","33","34","35","36","37","38","39","40",
			"41","42","43","44","45","46","47","48","49","50",
			"51","52","53","54","55","56","57","58","59","60"
			};//第一级，经向编码
	private static final String[] CODES_LVL1_LAT={
			"A","B","C","D","E","F","G","H","I","J",
			"K","L","M","N","O","P","Q","R","S","T",
			"U","V"
			};//第一级，纬向编码
	private static final String[] CODES_LVL2_LON={"0","1","2","3","4","5","6","7","8","9","A","B"};//第二级，经向编码
	private static final String[] CODES_LVL2_LAT={"0","1","2","3","4","5","6","7"};//第二级，纬向编码
	private static final String[] CODES_LVL4_LON={"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E"};//第四级，经向编码
	private static final String[] CODES_LVL4_LAT={"0","1","2","3","4","5","6","7","8","9"};//第四级，纬向编码
	private static final String[] CODES_LVL5_LON={"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E"};//第五级，经向编码
	private static final String[] CODES_LVL5_LAT={"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E"};//第五级，纬向编码
	private static final String[] CODES_LVL7_LON={"0","1","2","3","4","5","6","7"};//第七级，经向编码
	private static final String[] CODES_LVL7_LAT={"0","1","2","3","4","5","6","7"};//第七级，纬向编码
	private static final String[] CODES_LVL8_LON={"0","1","2","3","4","5","6","7"};//第八级，经向编码
	private static final String[] CODES_LVL8_LAT={"0","1","2","3","4","5","6","7"};//第八级，纬向编码
	private static final double STEP_LON_1=6.0;
	private static final double STEP_LAT_1=4.0;
	private static final double STEP_LON_2=0.5;
	private static final double STEP_LAT_2=0.5;
	private static final double STEP_LON_3=0.25;
	private static final double STEP_LAT_3=0.5/3;
	private static final double STEP_LAT_3_2=STEP_LAT_3*2;
	private static final double STEP_LON_4=STEP_LON_3/15;
	private static final double STEP_LAT_4=STEP_LAT_3/10;
	private static final double STEP_LON_5=STEP_LON_4/15;
	private static final double STEP_LAT_5=STEP_LAT_4/15;
	private static final double STEP_LON_6=STEP_LON_5/2;
	private static final double STEP_LAT_6=STEP_LAT_5/2;
	private static final double STEP_LON_7=STEP_LON_6/8;
	private static final double STEP_LAT_7=STEP_LAT_6/8;
	private static final double STEP_LON_8=STEP_LON_7/8;
	private static final double STEP_LAT_8=STEP_LAT_7/8;
	private static Map<String, Integer> MAP_LVL1_IDXLON=null;//第一级，经向编码到索引的映射
	private static Map<String, Integer> MAP_LVL1_IDXLAT=null;//第一级，纬向编码到索引的映射
	private static Map<String, Integer> MAP_LVL2_IDXLON=null;//第二级，经向编码到索引的映射
	private static Map<String, Integer> MAP_LVL2_IDXLAT=null;//第二级，纬向编码到索引的映射
	private static Map<String, Integer> MAP_LVL4_IDXLON=null;//第四级，经向编码到索引的映射
	private static Map<String, Integer> MAP_LVL4_IDXLAT=null;//第四级，纬向编码到索引的映射
	private static Map<String, Integer> MAP_LVL5_IDXLON=null;//第五级，经向编码到索引的映射
	private static Map<String, Integer> MAP_LVL5_IDXLAT=null;//第五级，纬向编码到索引的映射
	private static Map<String, Integer> MAP_LVL7_IDXLON=null;//第七级，经向编码到索引的映射
	private static Map<String, Integer> MAP_LVL7_IDXLAT=null;//第七级，纬向编码到索引的映射
	private static Map<String, Integer> MAP_LVL8_IDXLON=null;//第八级，经向编码到索引的映射
	private static Map<String, Integer> MAP_LVL8_IDXLAT=null;//第八级，纬向编码到索引的映射
	private static Map<Integer, Integer> MAP_LENGTH_LEVEL_2D=null;
	private static Map<Integer, Integer> MAP_LENGTH_LEVEL_3D=null;
	
	//三维
	private static final String GROUND_UP="0";//地面以上
	private static final double STEP_HEI_1=445280.0;
	private static final double STEP_HEI_2=55660.0;
	private static final double STEP_HEI_3=27830.0;
	private static final double STEP_HEI_4=1855.333333333333;
	private static final double STEP_HEI_5=123.6888888888889;
	private static final double STEP_HEI_6=61.84444444444444;
	private static final double STEP_HEI_7=7.730555555555556;
	private static final double STEP_HEI_8=0.9663194444444444;
	private static final String[] CODES_LVL1_HEI={
			"00","01","02","03","04","05","06","07","08","09",
			"10","11","12","13","14","15","16","17","18","19",
			"20","21","22","23","24","25","26","27","28","29",
			"30","31","32","33","34","35","36","37","38","39",
			"40","41","42","43","44","45","46","47","48","49",
			"50","51","52","53","54","55","56","57","58","59",
			"60","61","62","63"};//第一级，高向编码
	private static final String[] CODES_LVL2_HEI={"0","1","2","3","4","5","6","7"};//第二级，高向编码
	private static final String[] CODES_LVL3_HEI={"0","1"};//第三级，高向编码
	private static final String[] CODES_LVL4_HEI={"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E"};//第四级，高向编码
	private static final String[] CODES_LVL5_HEI={"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E"};//第五级，高向编码
	private static final String[] CODES_LVL6_HEI={"0","1"};//第六级，高向编码
	private static final String[] CODES_LVL7_HEI={"0","1","2","3","4","5","6","7"};//第七级，高向编码
	private static final String[] CODES_LVL8_HEI={"0","1","2","3","4","5","6","7"};//第八级，高向编码
	
	static{
		MAP_LENGTH_LEVEL_2D=new HashMap<Integer, Integer>();
		MAP_LENGTH_LEVEL_2D.put(4, 1);
		MAP_LENGTH_LEVEL_2D.put(6, 2);
		MAP_LENGTH_LEVEL_2D.put(7, 3);
		MAP_LENGTH_LEVEL_2D.put(9, 4);
		MAP_LENGTH_LEVEL_2D.put(11, 5);
		MAP_LENGTH_LEVEL_2D.put(12, 6);
		MAP_LENGTH_LEVEL_2D.put(14, 7);
		MAP_LENGTH_LEVEL_2D.put(16, 8);
		
		MAP_LENGTH_LEVEL_3D=new HashMap<Integer, Integer>();
		MAP_LENGTH_LEVEL_3D.put(7, 1);
		MAP_LENGTH_LEVEL_3D.put(10, 2);
		MAP_LENGTH_LEVEL_3D.put(12, 3);
		MAP_LENGTH_LEVEL_3D.put(15, 4);
		MAP_LENGTH_LEVEL_3D.put(18, 5);
		MAP_LENGTH_LEVEL_3D.put(20, 6);
		MAP_LENGTH_LEVEL_3D.put(23, 7);
		MAP_LENGTH_LEVEL_3D.put(26, 8);
		
		MAP_LVL1_IDXLON=new HashMap<String, Integer>();
		for (int i = 0; i < CODES_LVL1_LON.length; i++)
			MAP_LVL1_IDXLON.put(CODES_LVL1_LON[i], i);
		MAP_LVL1_IDXLAT=new HashMap<String, Integer>();
		for (int i = 0; i < CODES_LVL1_LAT.length; i++)
			MAP_LVL1_IDXLAT.put(CODES_LVL1_LAT[i], i);
		
		MAP_LVL2_IDXLON=new HashMap<String, Integer>();
		for (int i = 0; i < CODES_LVL2_LON.length; i++)
			MAP_LVL2_IDXLON.put(CODES_LVL2_LON[i], i);
		MAP_LVL2_IDXLAT=new HashMap<String, Integer>();
		for (int i = 0; i < CODES_LVL2_LAT.length; i++)
			MAP_LVL2_IDXLAT.put(CODES_LVL2_LAT[i], i);
		
		MAP_LVL4_IDXLON=new HashMap<String, Integer>();
		for (int i = 0; i < CODES_LVL4_LON.length; i++)
			MAP_LVL4_IDXLON.put(CODES_LVL4_LON[i], i);
		MAP_LVL4_IDXLAT=new HashMap<String, Integer>();
		for (int i = 0; i < CODES_LVL4_LAT.length; i++)
			MAP_LVL4_IDXLAT.put(CODES_LVL4_LAT[i], i);
		
		MAP_LVL5_IDXLON=new HashMap<String, Integer>();
		for (int i = 0; i < CODES_LVL5_LON.length; i++)
			MAP_LVL5_IDXLON.put(CODES_LVL5_LON[i], i);
		MAP_LVL5_IDXLAT=new HashMap<String, Integer>();
		for (int i = 0; i < CODES_LVL5_LAT.length; i++)
			MAP_LVL5_IDXLAT.put(CODES_LVL5_LAT[i], i);
		
		MAP_LVL7_IDXLON=new HashMap<String, Integer>();
		for (int i = 0; i < CODES_LVL7_LON.length; i++)
			MAP_LVL7_IDXLON.put(CODES_LVL7_LON[i], i);
		MAP_LVL7_IDXLAT=new HashMap<String, Integer>();
		for (int i = 0; i < CODES_LVL7_LAT.length; i++)
			MAP_LVL7_IDXLAT.put(CODES_LVL7_LAT[i], i);
		
		MAP_LVL8_IDXLON=new HashMap<String, Integer>();
		for (int i = 0; i < CODES_LVL8_LON.length; i++)
			MAP_LVL8_IDXLON.put(CODES_LVL8_LON[i], i);
		MAP_LVL8_IDXLAT=new HashMap<String, Integer>();
		for (int i = 0; i < CODES_LVL8_LAT.length; i++)
			MAP_LVL8_IDXLAT.put(CODES_LVL8_LAT[i], i);
	}
	
	private boolean threeDim=false;//是否三维
	private int level=0;//网格级别
	private String code=null;//网格编码
	private BoundingBox boundingBox=null;
	
	private BdGrid(boolean threeDim, int level, String code
			, double minLon, double minLat, double maxLon, double maxLat, double minHei, double maxHei){
		this.threeDim=threeDim;
		this.level=level;
		this.code=code;
		
		//数值精度处理
		BigDecimal big=new BigDecimal(minLon);
		minLon=big.setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue();
		big=new BigDecimal(minLat);
		minLat=big.setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue();
		big=new BigDecimal(maxLon);
		maxLon=big.setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue();
		big=new BigDecimal(maxLat);
		maxLat=big.setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue();

		if(threeDim){
			big=new BigDecimal(minHei);
			minHei=big.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			big=new BigDecimal(maxHei);
			maxHei=big.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			this.boundingBox=new BoundingBox(minLon,minLat,maxLon,maxLat,minHei,maxHei);
		}else{
			this.boundingBox=new BoundingBox(minLon,minLat,maxLon,maxLat);
		}
	}
	
	public BoundingBox getBoundingBox(){
		return boundingBox;
	}
	public boolean isThreeDim(){
		return threeDim;
	}
	public String toBinaryString(){
		return this.code;
	}
	public String toBase32(){
		return this.code;
	}
	
	public static BdGrid withBitPrecision(double lat, double lon, int level){
		return withBitPrecision(lat, lon, false, 0, level);
	}
	public static BdGrid withBitPrecision(double lat, double lon, double height, int level){
		return withBitPrecision(lat, lon, true, height, level);
	}
	private static BdGrid withBitPrecision(double lat, double lon, boolean threeDim, double height, int level){
		if(level<1 || level>MAX_LEVEL)
			throw new IllegalArgumentException("级别必须为1至"+MAX_LEVEL+" ！");
		if(lon<-180 || lon>180)
			throw new IllegalArgumentException("经度必须为-180至180 ！");
		if(lat<-90 || lat>90)
			throw new IllegalArgumentException("纬度必须为-90至90 ！");
		if(height<0)
			throw new IllegalArgumentException("高度不能小于0 ！");
		
		//南北半球
		int northOrSouth=1;
		if(lat<0)
			northOrSouth=-1;
		//东西半球
		int eastOrWest=1;
		if(lon<0)
			eastOrWest=-1;
		
		int idxLon=0,idxLat=0,idxHei=0,zidx=0;
		double minLon=0,maxLon=0,minLat=0,maxLat=0,minHei=0,maxHei=0;
		StringBuilder sb=new StringBuilder();
		if (northOrSouth > 0)
			sb.append(EARTH_NORTH);
		else
			sb.append(EARTH_SOUTH);
		if(threeDim)
			sb.append(GROUND_UP);
		
		//计算第一级编码
		idxLon=(int)Math.round(Math.floor((lon+180)/STEP_LON_1));
		sb.append(CODES_LVL1_LON[idxLon]);
		minLon=idxLon*STEP_LON_1-180;
		maxLon=minLon+STEP_LON_1;
		//经度转换到东半球
		if(eastOrWest<0){
			lon=0-lon;
			double tmpLon=maxLon;
			maxLon=0-minLon;
			minLon=0-tmpLon;
		}
		//纬度转换到北半球
		if(northOrSouth<0)
			lat=0-lat;
		idxLat=(int)Math.round(Math.floor(Math.abs(lat)/STEP_LAT_1));
		sb.append(CODES_LVL1_LAT[idxLat]);
		minLat=idxLat*STEP_LAT_1;
		maxLat=minLat+STEP_LAT_1;
		if(threeDim){
			idxHei=(int)Math.round(Math.floor(height/STEP_HEI_1));
			if(idxHei>63)
				idxHei=63;
			sb.append(CODES_LVL1_HEI[idxHei]);
			minHei=idxHei*STEP_HEI_1;
			maxHei=minHei+STEP_HEI_1;
		}
		
		if(level>1){
			//计算第二级编码
			idxLon=(int)Math.round(Math.floor((lon-minLon)/STEP_LON_2));
			idxLat=(int)Math.round(Math.floor((lat-minLat)/STEP_LAT_2));
			sb.append(CODES_LVL2_LON[idxLon]);
			sb.append(CODES_LVL2_LAT[idxLat]);
			minLon=minLon+idxLon*STEP_LON_2;
			if(idxLon<11)
				maxLon=minLon+STEP_LON_2;
			minLat=minLat+idxLat*STEP_LAT_2;
			if(idxLat<7)
				maxLat=minLat+STEP_LAT_2;
			if(threeDim){
				idxHei=(int)Math.round(Math.floor((height-minHei)/STEP_HEI_2));
				sb.append(CODES_LVL2_HEI[idxHei]);
				
				minHei=minHei+idxHei*STEP_HEI_2;
				if(idxHei<7)
					maxHei=minHei+STEP_HEI_2;
			}
		}
		
		if(level>2){
			//计算第三级编码
			zidx=0;
			if((lon-minLon)>STEP_LON_3)
				zidx=1;
			if((lat-minLat)>STEP_LAT_3_2)
				zidx=zidx+4;
			else if((lat-minLat)>STEP_LAT_3)
				zidx=zidx+2;
			sb.append(zidx);
			if((zidx%2)==0)
				maxLon=minLon+STEP_LON_3;
			else
				minLon=minLon+STEP_LON_3;
			if(zidx<=1)
				maxLat=minLat+STEP_LAT_3;
			else if(zidx<=3){
				minLat=minLat+STEP_LAT_3;
				maxLat=minLat+STEP_LAT_3;
			}else
				minLat=minLat+STEP_LAT_3_2;
			if(threeDim){
				if((height-minHei)<STEP_HEI_3)
					idxHei=0;
				else
					idxHei=1;
				sb.append(CODES_LVL3_HEI[idxHei]);
				
				if(idxHei==0)
					maxHei=minHei+STEP_HEI_3;
				else
					minHei=minHei+STEP_HEI_3;
			}
		}
		
		if(level>3){
			//计算第四级编码
			idxLon=(int)Math.round(Math.floor((lon-minLon)/STEP_LON_4));
			idxLat=(int)Math.round(Math.floor((lat-minLat)/STEP_LAT_4));
			sb.append(CODES_LVL4_LON[idxLon]);
			sb.append(CODES_LVL4_LAT[idxLat]);
			minLon=minLon+idxLon*STEP_LON_4;
			if(idxLon<14)
				maxLon=minLon+STEP_LON_4;
			minLat=minLat+idxLat*STEP_LAT_4;
			if(idxLat<9)
				maxLat=minLat+STEP_LAT_4;
			if(threeDim){
				idxHei=(int)Math.round(Math.floor((height-minHei)/STEP_HEI_4));
				sb.append(CODES_LVL4_HEI[idxHei]);
				
				minHei=minHei+idxHei*STEP_HEI_4;
				if(idxHei<14)
					maxHei=minHei+STEP_HEI_4;
			}
		}
		
		if(level>4){
			//计算第五级编码
			idxLon=(int)Math.round(Math.floor((lon-minLon)/STEP_LON_5));
			idxLat=(int)Math.round(Math.floor((lat-minLat)/STEP_LAT_5));
			sb.append(CODES_LVL5_LON[idxLon]);
			sb.append(CODES_LVL5_LAT[idxLat]);
			minLon=minLon+idxLon*STEP_LON_5;
			if(idxLon<14)
				maxLon=minLon+STEP_LON_5;
			minLat=minLat+idxLat*STEP_LAT_5;
			if(idxLat<14)
				maxLat=minLat+STEP_LAT_5;
			if(threeDim){
				idxHei=(int)Math.round(Math.floor((height-minHei)/STEP_HEI_5));
				sb.append(CODES_LVL5_HEI[idxHei]);
				
				minHei=minHei+idxHei*STEP_HEI_5;
				if(idxHei<14)
					maxHei=minHei+STEP_HEI_5;
			}
		}
		
		if(level>5){
			//计算第六级编码
			zidx=0;
			if((lon-minLon)>STEP_LON_6)
				zidx=1;
			if((lat-minLat)>STEP_LAT_6)
				zidx=zidx+2;
			sb.append(zidx);
			if((zidx%2)==0)
				maxLon=minLon+STEP_LON_6;
			else
				minLon=minLon+STEP_LON_6;
			if(zidx<=1)
				maxLat=minLat+STEP_LAT_6;
			else
				minLat=minLat+STEP_LAT_6;
			if(threeDim){
				if((height-minHei)<STEP_HEI_6)
					idxHei=0;
				else
					idxHei=1;
				sb.append(CODES_LVL6_HEI[idxHei]);
				
				if(idxHei==0)
					maxHei=minHei+STEP_HEI_6;
				else
					minHei=minHei+STEP_HEI_6;
			}
		}
		
		if(level>6){
			//计算第七级编码
			idxLon=(int)Math.round(Math.floor((lon-minLon)/STEP_LON_7));
			if(idxLon>7)
				idxLon=7;
			idxLat=(int)Math.round(Math.floor((lat-minLat)/STEP_LAT_7));
			if(idxLat>7)
				idxLat=7;
			sb.append(CODES_LVL7_LON[idxLon]);
			sb.append(CODES_LVL7_LAT[idxLat]);
			minLon=minLon+idxLon*STEP_LON_7;
			if(idxLon<7)
				maxLon=minLon+STEP_LON_7;
			minLat=minLat+idxLat*STEP_LAT_7;
			if(idxLat<7)
				maxLat=minLat+STEP_LAT_7;
			if(threeDim){
				idxHei=(int)Math.round(Math.floor((height-minHei)/STEP_HEI_7));
				sb.append(CODES_LVL7_HEI[idxHei]);
				
				minHei=minHei+idxHei*STEP_HEI_7;
				if(idxHei<7)
					maxHei=minHei+STEP_HEI_7;
			}
		}
		
		if(level>7){
			//计算第八级编码
			idxLon=(int)Math.round(Math.floor((lon-minLon)/STEP_LON_8));
			if(idxLon>7)
				idxLon=7;
			idxLat=(int)Math.round(Math.floor((lat-minLat)/STEP_LAT_8));
			if(idxLat>7)
				idxLat=7;
			sb.append(CODES_LVL8_LON[idxLon]);
			sb.append(CODES_LVL8_LAT[idxLat]);
			minLon=minLon+idxLon*STEP_LON_8;
			if(idxLon<7)
				maxLon=minLon+STEP_LON_8;
			minLat=minLat+idxLat*STEP_LAT_8;
			if(idxLat<7)
				maxLat=minLat+STEP_LAT_8;
			if(threeDim){
				idxHei=(int)Math.round(Math.floor((height-minHei)/STEP_HEI_8));
				sb.append(CODES_LVL8_HEI[idxHei]);
				
				minHei=minHei+idxHei*STEP_HEI_8;
				if(idxHei<7)
					maxHei=minHei+STEP_HEI_8;
			}
		}
		
		//如果西半球，经度转回
		if(eastOrWest<0){
			double tmpLon=maxLon;
			maxLon=0-minLon;
			minLon=0-tmpLon;
		}
		//如果北半球，纬度转回
		if(northOrSouth<0){
			double tmpLat=maxLat;
			maxLat=0-minLat;
			minLat=0-tmpLat;
		}
		
		return new BdGrid(threeDim, level, sb.toString(), minLon, minLat, maxLon, maxLat, minHei, maxHei);
	}
	
	public static BdGrid fromGeohashString(String code){
		return fromBinaryString(false, code);
	}
	public static BdGrid fromBinaryString(String code){
		return fromBinaryString(false, code);
	}
	public static BdGrid fromBinaryString(boolean threeDim, String code){
		if(code==null)
			throw new IllegalArgumentException("网格编码不能为空！");
		
		code=code.trim();
		String orgCode=code;
		int length=code.length();
		int level=0;
		if(threeDim){
			if(MAP_LENGTH_LEVEL_3D.containsKey(length))
				level=MAP_LENGTH_LEVEL_3D.get(length);
			else
				throw new IllegalArgumentException("网格编码长度不正确！");
		}else{
			if(MAP_LENGTH_LEVEL_2D.containsKey(length))
				level=MAP_LENGTH_LEVEL_2D.get(length);
			else
				throw new IllegalArgumentException("网格编码长度不正确！");
		}
		
		//N002B0084020B70EE031770776
		String codeLon=null,codeLat=null,codeIdx=null,codeHei=null;;
		int idxLon=0,idxLat=0,idxHei=0,zidx=0,northOrSouth=1,eastOrWest=1;
		double minLon=0,maxLon=0,minLat=0,maxLat=0,minHei=0,maxHei=0;
		
		//判断南北半球
		String str=code.substring(0,1);//N;
		code=code.substring(1);//002B0084020B70EE031770776
		if(EARTH_NORTH.equals(str))
			northOrSouth=1;
		else if(EARTH_SOUTH.equals(str))
			northOrSouth=-1;
		else
			throw new IllegalArgumentException("网格编码不正确！");
		
		//地下地上
		if(threeDim)
			code=code.substring(1);//02B0084020B70EE031770776
		
		//第一级
		codeLon=code.substring(0,2);//02
		codeLat=code.substring(2,3);//B
		code=code.substring(3);//0084020B70EE031770776
		idxLon=MAP_LVL1_IDXLON.get(codeLon);
		minLon=idxLon*STEP_LON_1-180;
		maxLon=minLon+STEP_LON_1;
		if(minLon<0)
			eastOrWest=-1;//西半球
		else
			eastOrWest=1;//东半球
		//经度转换到东半球
		if(eastOrWest<0){
			double tmpLon=maxLon;
			maxLon=0-minLon;
			minLon=0-tmpLon;
		}
		//纬度转换到北半球
		idxLat=MAP_LVL1_IDXLAT.get(codeLat);
		minLat=idxLat*STEP_LAT_1;
		maxLat=minLat+STEP_LAT_1;
		if(threeDim){
			codeHei=code.substring(0,2);//00
			code=code.substring(2);//84020B70EE031770776
			idxHei=Integer.parseInt(codeHei);
			minHei=idxHei*STEP_HEI_1;
			maxHei=minHei+STEP_HEI_1;
		}
		
		if(code.length()>0){
			//第二级
			codeLon=code.substring(0,1);//8
			codeLat=code.substring(1,2);//4
			code=code.substring(2);//020B70EE031770776
			idxLon=MAP_LVL2_IDXLON.get(codeLon);
			idxLat=MAP_LVL2_IDXLAT.get(codeLat);
			minLon=minLon+idxLon*STEP_LON_2;
			if(idxLon<11)
				maxLon=minLon+STEP_LON_2;
			minLat=minLat+idxLat*STEP_LAT_2;
			if(idxLat<7)
				maxLat=minLat+STEP_LAT_2;
			if(threeDim){
				codeHei=code.substring(0,1);//0
				code=code.substring(1);//20B70EE031770776
				idxHei=Integer.parseInt(codeHei);
				minHei=minHei+idxHei*STEP_HEI_2;
				if(idxHei<7)
					maxHei=minHei+STEP_HEI_2;
			}
		}
		
		if(code.length()>0){
			//第三级
			codeIdx=code.substring(0,1);//2
			code=code.substring(1);//0B70EE031770776
			zidx=Integer.parseInt(codeIdx);
			if((zidx%2)==0)
				maxLon=minLon+STEP_LON_3;
			else
				minLon=minLon+STEP_LON_3;
			if(zidx<=1)
				maxLat=minLat+STEP_LAT_3;
			else if(zidx<=3){
				minLat=minLat+STEP_LAT_3;
				maxLat=minLat+STEP_LAT_3;
			}else
				minLat=minLat+STEP_LAT_3_2;
			if(threeDim){
				codeHei=code.substring(0,1);//0
				code=code.substring(1);//B70EE031770776
				idxHei=Integer.parseInt(codeHei);
				if(idxHei==0)
					maxHei=minHei+STEP_HEI_3;
				else
					minHei=minHei+STEP_HEI_3;
			}
		}
		
		if(code.length()>0){
			//第四级
			codeLon=code.substring(0,1);//B
			codeLat=code.substring(1,2);//7
			code=code.substring(2);//0EE031770776
			idxLon=MAP_LVL4_IDXLON.get(codeLon);
			idxLat=MAP_LVL4_IDXLAT.get(codeLat);
			minLon=minLon+idxLon*STEP_LON_4;
			if(idxLon<14)
				maxLon=minLon+STEP_LON_4;
			minLat=minLat+idxLat*STEP_LAT_4;
			if(idxLat<9)
				maxLat=minLat+STEP_LAT_4;
			if(threeDim){
				codeHei=code.substring(0,1);//0
				code=code.substring(1);//EE031770776
				idxHei=Integer.parseInt(codeHei);
				minHei=minHei+idxHei*STEP_HEI_4;
				if(idxHei<14)
					maxHei=minHei+STEP_HEI_4;
			}
		}
		
		if(code.length()>0){
			//第五级
			codeLon=code.substring(0,1);//E
			codeLat=code.substring(1,2);//E
			code=code.substring(2);//031770776
			idxLon=MAP_LVL5_IDXLON.get(codeLon);
			idxLat=MAP_LVL5_IDXLAT.get(codeLat);
			minLon=minLon+idxLon*STEP_LON_5;
			if(idxLon<14)
				maxLon=minLon+STEP_LON_5;
			minLat=minLat+idxLat*STEP_LAT_5;
			if(idxLat<14)
				maxLat=minLat+STEP_LAT_5;
			if(threeDim){
				codeHei=code.substring(0,1);//0
				code=code.substring(1);//31770776
				idxHei=Integer.parseInt(codeHei);
				minHei=minHei+idxHei*STEP_HEI_5;
				if(idxHei<14)
					maxHei=minHei+STEP_HEI_5;
			}
		}
		
		if(code.length()>0){
			//第六级
			codeIdx=code.substring(0,1);//3
			code=code.substring(1);//1770776
			zidx=Integer.parseInt(codeIdx);
			if((zidx%2)==0)
				maxLon=minLon+STEP_LON_6;
			else
				minLon=minLon+STEP_LON_6;
			if(zidx<=1)
				maxLat=minLat+STEP_LAT_6;
			else
				minLat=minLat+STEP_LAT_6;
			if(threeDim){
				codeHei=code.substring(0,1);//1
				code=code.substring(1);//770776
				idxHei=Integer.parseInt(codeHei);
				if(idxHei==0)
					maxHei=minHei+STEP_HEI_6;
				else
					minHei=minHei+STEP_HEI_6;
			}
		}
		
		if(code.length()>0){
			//第七级
			codeLon=code.substring(0,1);//7
			codeLat=code.substring(1,2);//7
			code=code.substring(2);//0776
			idxLon=MAP_LVL7_IDXLON.get(codeLon);
			idxLat=MAP_LVL7_IDXLAT.get(codeLat);
			minLon=minLon+idxLon*STEP_LON_7;
			if(idxLon<7)
				maxLon=minLon+STEP_LON_7;
			minLat=minLat+idxLat*STEP_LAT_7;
			if(idxLat<7)
				maxLat=minLat+STEP_LAT_7;
			if(threeDim){
				codeHei=code.substring(0,1);//0
				code=code.substring(1);//776
				idxHei=Integer.parseInt(codeHei);
				minHei=minHei+idxHei*STEP_HEI_7;
				if(idxHei<7)
					maxHei=minHei+STEP_HEI_7;
			}
		}
		
		if(code.length()>0){
			//第八级
			codeLon=code.substring(0,1);//7
			codeLat=code.substring(1,2);//7
			code=code.substring(2);//6
			idxLon=MAP_LVL8_IDXLON.get(codeLon);
			idxLat=MAP_LVL8_IDXLAT.get(codeLat);
			minLon=minLon+idxLon*STEP_LON_8;
			if(idxLon<7)
				maxLon=minLon+STEP_LON_8;
			minLat=minLat+idxLat*STEP_LAT_8;
			if(idxLat<7)
				maxLat=minLat+STEP_LAT_8;
			if(threeDim){
				codeHei=code.substring(0,1);//6
				code=code.substring(1);//
				idxHei=Integer.parseInt(codeHei);
				minHei=minHei+idxHei*STEP_HEI_8;
				if(idxHei<7)
					maxHei=minHei+STEP_HEI_8;
			}
		}
		
		//如果西半球，经度转回
		if(eastOrWest<0){
			double tmpLon=maxLon;
			maxLon=0-minLon;
			minLon=0-tmpLon;
		}
		//如果北半球，纬度转回
		if(northOrSouth<0){
			double tmpLat=maxLat;
			maxLat=0-minLat;
			minLat=0-tmpLat;
		}
		
		return new BdGrid(threeDim, level, orgCode, minLon, minLat, maxLon, maxLat, minHei, maxHei);
	}
	
	public BdGrid getEasternNeighbour(){
		BoundingBox box=this.getBoundingBox();
		double lon=(box.getMinLon()+box.getMaxLon())/2;
		double lat=(box.getMinLat()+box.getMaxLat())/2;
		double hei=(box.getMinHei()+box.getMaxHei())/2;
		if(level==1)
			lon+=STEP_LON_1;
		else if(level==2)
			lon+=STEP_LON_2;
		else if(level==3)
			lon+=STEP_LON_3;
		else if(level==4)
			lon+=STEP_LON_4;
		else if(level==5)
			lon+=STEP_LON_5;
		else if(level==6)
			lon+=STEP_LON_6;
		else if(level==7)
			lon+=STEP_LON_7;
		else
			lon+=STEP_LON_8;
		if(lon>180)
			lon=lon-360;
		return BdGrid.withBitPrecision(lat, lon, hei, level);
	}
	public BdGrid getWesternNeighbour(){
		BoundingBox box=this.getBoundingBox();
		double lon=(box.getMinLon()+box.getMaxLon())/2;
		double lat=(box.getMinLat()+box.getMaxLat())/2;
		double hei=(box.getMinHei()+box.getMaxHei())/2;
		if(level==1)
			lon-=STEP_LON_1;
		else if(level==2)
			lon-=STEP_LON_2;
		else if(level==3)
			lon-=STEP_LON_3;
		else if(level==4)
			lon-=STEP_LON_4;
		else if(level==5)
			lon-=STEP_LON_5;
		else if(level==6)
			lon-=STEP_LON_6;
		else if(level==7)
			lon-=STEP_LON_7;
		else
			lon-=STEP_LON_8;
		if(lon<-180)
			lon=lon+360;
		return BdGrid.withBitPrecision(lat, lon, hei, level);
	}
	public BdGrid getNorthernNeighbour(){
		BoundingBox box=this.getBoundingBox();
		double lon=(box.getMinLon()+box.getMaxLon())/2;
		double lat=(box.getMinLat()+box.getMaxLat())/2;
		double hei=(box.getMinHei()+box.getMaxHei())/2;
		if(level==1)
			lat+=STEP_LAT_1;
		else if(level==2)
			lat+=STEP_LAT_2;
		else if(level==3)
			lat+=STEP_LAT_3;
		else if(level==4)
			lat+=STEP_LAT_4;
		else if(level==5)
			lat+=STEP_LAT_5;
		else if(level==6)
			lat+=STEP_LAT_6;
		else if(level==7)
			lat+=STEP_LAT_7;
		else
			lat+=STEP_LAT_8;
		return BdGrid.withBitPrecision(lat, lon, hei, level);
	}
	public BdGrid getSouthernNeighbour(){
		BoundingBox box=this.getBoundingBox();
		double lon=(box.getMinLon()+box.getMaxLon())/2;
		double lat=(box.getMinLat()+box.getMaxLat())/2;
		double hei=(box.getMinHei()+box.getMaxHei())/2;
		if(level==1)
			lat-=STEP_LAT_1;
		else if(level==2)
			lat-=STEP_LAT_2;
		else if(level==3)
			lat-=STEP_LAT_3;
		else if(level==4)
			lat-=STEP_LAT_4;
		else if(level==5)
			lat-=STEP_LAT_5;
		else if(level==6)
			lat-=STEP_LAT_6;
		else if(level==7)
			lat-=STEP_LAT_7;
		else
			lat-=STEP_LAT_8;
		return BdGrid.withBitPrecision(lat, lon, hei, level);
	}
}