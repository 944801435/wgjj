package com.uav.web.view.uavOnline.base.adsb;

import org.apache.log4j.Logger;

import redis.clients.jedis.JedisPubSub;

//接收订阅的飞行数据
public class RedisAdsbFdListener extends JedisPubSub {
	private static final Logger log = Logger.getLogger(RedisAdsbFdListener.class);
	
	@Override
	public void onMessage(String channel, String message) {
		log.debug("接收到Redis消息，channel="+channel+"，message="+message);
		AdsbOnlineUtil.pushData(message);
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
		//log.info("channel:" + channel + "is been subscribed:" + subscribedChannels);
	}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {

	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {

	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		//log.info("channel:" + channel + "is been unsubscribed:" + subscribedChannels);
	}
}