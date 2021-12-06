package com.uav.web.view.uavOnline.base.adsb;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.uav.base.common.Constants;
import com.uav.web.view.uavOnline.base.UavOnlineVo;
import com.uav.web.view.uavOnline.base.UavOnlineWebSocketServer;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import redis.clients.jedis.JedisPubSub;

//接收订阅的无人机降落数据
@Slf4j
public class RedisAdsbFlyLandListener extends JedisPubSub {

	private static final String STR_DOT = ",";

	@Override
	public void onMessage(String channel, String message) {
		log.info("【adsb】接收到Redis消息，channel=" + channel + "，message=" + message);
		// 修改推送到前端的报文格式
		/* 格式：
		 	无人机编码,降落时间
			test01,20210312121220218
		*/
		if (StringUtils.isBlank(message)) {
			return;
		}
		String[] fields = message.split(STR_DOT, -1);
		if (fields.length < 2) {
			log.error("【adsb】接收到的adsb降落数据格式错误，message=" + message);
			return;
		}
		String pn = fields[0];// 编码
		if (pn.length() == 0) {
			log.error("【adsb】接收到的adsb降落数据格式错误，无人机编码不能为空，message=" + message);
			return;
		}
		String time = fields[1];// 降落时间
		if (time.length() == 0) {
			log.error("【adsb】接收到的adsb降落数据格式错误，降落时间不能为空，message=" + message);
			return;
		}
		UavOnlineVo vo = new UavOnlineVo(pn, Constants.UAV_FLY_DOWN, time);
		List<UavOnlineVo> list=new ArrayList<UavOnlineVo>();
		list.add(vo);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("adsb", list);
		UavOnlineWebSocketServer.sendMessage(jsonObject.toString());
	}

	@Override
	public void unsubscribe() {
		super.unsubscribe();
	}

	@Override
	public void unsubscribe(String... channels) {
		super.unsubscribe(channels);
	}

	@Override
	public void subscribe(String... channels) {
		super.subscribe(channels);
	}

	@Override
	public void psubscribe(String... patterns) {
		super.psubscribe(patterns);
	}

	@Override
	public void punsubscribe() {
		super.punsubscribe();
	}

	@Override
	public void punsubscribe(String... patterns) {
		super.punsubscribe(patterns);
	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {

	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		// log.info("channel:" + channel + "is been subscribed:" +
		// subscribedChannels);
	}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {

	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {

	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		// log.info("channel:" + channel + "is been unsubscribed:" +
		// subscribedChannels);
	}
}