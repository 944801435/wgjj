package com.uav.web.view.uavOnline.base;

import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;

@ServerEndpoint("/uavOnlineWebSocket")
public class UavOnlineWebSocketServer {
	private static final Logger log = Logger.getLogger(UavOnlineWebSocketServer.class);
	
	private static LinkedList<UavOnlineWebSocketServer> wsList = new LinkedList<UavOnlineWebSocketServer>();
	private static Map<String, UavOnlineWebSocketServer> wsMap = new Hashtable<String, UavOnlineWebSocketServer>();
	private static Map<String,RecurHandler> recurMap = new Hashtable<String,RecurHandler>();//放入重演MAP的终端将不再推送正常航迹、告警信息
	private static int onlineCount = 0;
	private Session session;
	
	private static final String CMD_RECUR_START="recurStart";//开始
	private static final String CMD_RECUR_STOP="recurStop";
	private static final String CMD_RECUR_PAUSE="recurPause";
	private static final String CMD_RECUR_CONTINUE="recurContinue";
	private static final String CMD_RECUR_REPLAY="recurReplay";//再次重放
	
	@OnOpen
	public void onOpen(Session session) throws IOException {
		this.session = session;
		addOnline(session.getId(), this);
		log.info("有新连接加入！当前在线人数为" + onlineCount);
	}

	@OnClose
	public void onClose() throws IOException {
		String sessionId=this.session.getId();
		
		subOnline(sessionId, this);
		
		//如果正在重演，停止重演处理
		if(recurMap.containsKey(sessionId)){
			RecurHandler handler=recurMap.get(sessionId);
			handler.stop();
			recurMap.remove(sessionId);
		}
		
		log.info("有一连接关闭！当前在线人数为" + onlineCount);
	}
	
	private static synchronized void addOnline(String sessionId, UavOnlineWebSocketServer ws) {
		UavOnlineWebSocketServer.onlineCount++;
		wsList.add(ws);
		wsMap.put(sessionId, ws);
	}

	private static synchronized void subOnline(String sessionId, UavOnlineWebSocketServer ws) {
		UavOnlineWebSocketServer.onlineCount--;
		wsList.remove(ws);
		wsMap.remove(sessionId);
	}
	
	private static synchronized UavOnlineWebSocketServer[] getClients() {
		if(wsList==null || wsList.size()<=0)
			return new UavOnlineWebSocketServer[0];
		
		List<UavOnlineWebSocketServer> rtnList=new LinkedList<UavOnlineWebSocketServer>();
		UavOnlineWebSocketServer ws=null;
		for (int i = 0; i < wsList.size(); i++) {
			ws=wsList.get(i);
			if(recurMap.containsKey(ws.session.getId()))
				continue;
			
			rtnList.add(ws);
		}
		
		return (UavOnlineWebSocketServer[])rtnList.toArray(new UavOnlineWebSocketServer[]{});
	}
	
	public static void sendMessage(String msg) {
		UavOnlineWebSocketServer[] clients=getClients();
		if(clients==null || clients.length<=0)
			return;
		
		for (UavOnlineWebSocketServer client : clients) {
			try {
				if(client.session!=null && client.session.isOpen())
					client.session.getBasicRemote().sendText(msg);
			} catch (Exception e) {
				log.error("飞行监视数据推送失败！",e);
			}
		}
	}
	
	public boolean singleClientMessage(String msg){
		try {
			if(this.session!=null && this.session.isOpen())
				this.session.getBasicRemote().sendText(msg);
			
			return true;
		} catch (Exception e) {
			log.error("消息推送失败，sessionId="+this.session.getId(), e);
			return false;
		}
	}
	
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		log.debug("得到消息：" + message);
		//recurStart_begtime_endtime_speed开始重演
		//recurStop停止重演
		if(message==null || message.trim().length()==0)
			return;
		else
			message=message.trim();
		
		if(message.startsWith(CMD_RECUR_START)){
			//开始重演
			if(recurMap.containsKey(session.getId()))
				return;
			//message=========recurStart_begtime_endtime_speed
			String[] messages = message.split("_");
			String begTime = messages[1];
			String endTime = messages[2];
			String speed = messages[3];
			RecurHandler handler=new RecurHandler(wsMap.get(session.getId()),begTime,endTime,speed);
			recurMap.put(session.getId(), handler);
			handler.start();
		}else if(message.startsWith(CMD_RECUR_STOP)){
			//结束重演
			if(!recurMap.containsKey(session.getId()))
				return;
			
			RecurHandler handler=recurMap.get(session.getId());
			handler.stop();
			recurMap.remove(session.getId());
		}else if(message.startsWith(CMD_RECUR_PAUSE)){
			//暂停重放
			RecurHandler handler=recurMap.get(session.getId());
			handler.pause();
		}else if(message.startsWith(CMD_RECUR_CONTINUE)){
			//继续重放
			RecurHandler handler=recurMap.get(session.getId());
			handler.toContinue();
		}else if(message.startsWith(CMD_RECUR_REPLAY)){
			//再次重放
			RecurHandler handler=recurMap.get(session.getId());
			handler.reset();
		}
	}
	
	@OnError
	public void onError(Session session, Throwable error) {
		//log.error("发生错误", error);
	}
	
	public String getSessionId(){
		return this.session.getId();
//		return "sess000001";
	}
}