package com.uav.web.view.uavOnline.base.adsb;

import org.apache.log4j.Logger;

public class AdsbXiaopiDeamon {
	private static Logger log = Logger.getLogger(AdsbXiaopiDeamon.class);
	private static AdsbXiaopiDeamon inst=new AdsbXiaopiDeamon();
	private boolean run=false;
	private AdsbXiaopiThread thread=null;
	
	private AdsbXiaopiDeamon() {
		
	}
	
	public static AdsbXiaopiDeamon getInstance(){
		return inst;
	}
	
	synchronized public void start() {
		if(!run){
			log.info("【adsb】启动消批处理进程 ...");
			
			run=true;
			thread=new AdsbXiaopiThread();
			thread.start();
		}
	}
	
	synchronized public void stop() {
		if(run){
			log.info("【adsb】停止消批处理进程 ...");
			
			run=false;
			thread.close();
			thread=null;
		}
	}
}