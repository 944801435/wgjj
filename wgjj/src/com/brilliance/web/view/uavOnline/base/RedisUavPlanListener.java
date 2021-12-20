package com.brilliance.web.view.uavOnline.base;

import org.apache.log4j.Logger;

import redis.clients.jedis.JedisPubSub;

//接收订阅的计划变更数据
public class RedisUavPlanListener extends JedisPubSub {
	private static final Logger log = Logger.getLogger(RedisUavPlanListener.class);
	
	@Override
	public void onMessage(String channel, String message) {
		log.info("接收到Redis消息，channel="+channel+"，message="+message);
		UavOnlineWebSocketServer.sendMessage("plan");
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