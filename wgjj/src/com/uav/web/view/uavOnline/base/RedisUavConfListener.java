package com.uav.web.view.uavOnline.base;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.uav.base.util.ConstantsUtil;
import com.uav.base.util.RedisClientUtil;

import redis.clients.jedis.JedisPubSub;

//接收订阅的系统配置变更数据
public class RedisUavConfListener extends JedisPubSub {
	private static final Logger log = Logger.getLogger(RedisUavConfListener.class);

	@Override
	public void onMessage(String channel, String message) {
		log.info("接收到Redis消息，channel=" + channel + "，message=" + message);
		// 加载最新的系统配置数据到系统缓存中
		Map<String, String> map = RedisClientUtil.hgetAll(RedisClientUtil.REDIS_CACHE_SYS_CONF);
		if (map != null) {
			for (Entry<String, String> entry : map.entrySet()) {
				ConstantsUtil.sysConfigMap.put(entry.getKey(), entry.getValue());
			}
		}
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