package com.brilliance.base.listener;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

import com.brilliance.adsb.AdsbHandler;
import com.brilliance.base.common.Constants;
import com.brilliance.base.util.ConstantsUtil;
import com.brilliance.base.util.PropertiesUtil;
import com.brilliance.base.util.RedisClientUtil;
import com.brilliance.base.util.SeqUtil;
import com.brilliance.base.util.SpringContextUtil;
import com.brilliance.web.view.uavOnline.base.RedisUavConfListener;
import com.brilliance.web.view.uavOnline.base.RedisUavPlanListener;
import com.brilliance.web.view.uavOnline.base.RedisUavSpaceListener;
import com.brilliance.web.view.uavOnline.base.adsb.AdsbOnlineUtil;
import com.brilliance.web.view.uavOnline.base.adsb.AdsbXiaopiDeamon;
import com.brilliance.web.view.uavOnline.base.adsb.RedisAdsbFdListener;
import com.brilliance.web.view.uavOnline.base.adsb.RedisAdsbFlyLandListener;

public class SpringListener implements ApplicationListener<ApplicationContextEvent> {
	private static final Logger log = Logger.getLogger(SpringListener.class);

	// 开启订阅Fd redis开关 0：关闭，1：开启
	private static final String SUBSCRIBE_FD_REDIS = PropertiesUtil.getPropertyValue("SUBSCRIBE_FD_REDIS", "0");

	// 启动订阅处理态势数据线程数量
	private static final Integer START_UAV_FLY_DATA_THREAD_NUM = Integer
			.parseInt(PropertiesUtil.getPropertyValue("START_UAV_FLY_DATA_THREAD_NUM", "1"));
	// 启动订阅处理架次数据线程数量
	private static final Integer START_UAV_FLY_STS_THREAD_NUM = Integer
			.parseInt(PropertiesUtil.getPropertyValue("START_UAV_FLY_STS_THREAD_NUM", "1"));
	// 开启ADSB进程
	private static final String ADSB_SWITCH = PropertiesUtil.getPropertyValue("ADSB_SWITCH", "0");
	
	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {
		if (event.getApplicationContext().getParent() != null)
			return;
		
		if(event instanceof ContextRefreshedEvent){
			log.info("===========================spring加载完成");
			ConstantsUtil.readData();

			// 解决数据库并发问题
			SeqUtil.getNextSeq("biz_fly", 10);
			
			if ("1".equals(SUBSCRIBE_FD_REDIS)) {// 开启订阅
				// 订阅Redis系统配置数据
				boolean confSucc = RedisClientUtil.subscribeTopic(RedisClientUtil.REDIS_TOPIC_CONF, new RedisUavConfListener());
				if (confSucc) {
					log.info("订阅Redis系统配置数据成功！");
				} else {
					log.error("订阅Redis系统配置数据失败！");
				}

				// 订阅Redis空域数据
				boolean spaceSucc = RedisClientUtil.subscribeTopic(RedisClientUtil.REDIS_TOPIC_SPACE, new RedisUavSpaceListener());
				if (spaceSucc) {
					log.info("订阅Redis空域数据成功！");
				} else {
					log.error("订阅Redis空域数据失败！");
				}

				// 订阅Redis计划数据
				boolean planSucc = RedisClientUtil.subscribeTopic(RedisClientUtil.REDIS_TOPIC_PLAN, new RedisUavPlanListener());
				if (planSucc) {
					log.info("订阅Redis计划数据成功！");
				} else {
					log.error("订阅Redis计划数据失败！");
				}

				// 启动ADSB进程
				if ("1".equals(ADSB_SWITCH)) {

					// 订阅Redis ADSB数据
					boolean succ = RedisClientUtil.subscribeTopic(RedisClientUtil.REDIS_TOPIC_ADSB_FD, new RedisAdsbFdListener());
					if (succ) {
						log.info("订阅Redis ADSB数据成功！");
					} else {
						log.error("订阅Redis ADSB数据失败！");
					}
					// 订阅Redis Adsb降落数据
					succ = RedisClientUtil.subscribeTopic(RedisClientUtil.REDIS_TOPIC_ADSB_FLY_LAND, new RedisAdsbFlyLandListener());
					if (succ) {
						log.info("订阅Redis ADSB降落数据成功！");
					} else {
						log.error("订阅Redis ADSB降落数据失败！");
					}
					// 启动获取态势入库数据线程
					for (int i = 0; i < START_UAV_FLY_DATA_THREAD_NUM; i++) {
						RedisClientUtil.brpop(RedisClientUtil.REDIS_QUEUE_ADSB_FLY_DATA);
					}
					// 启动获取架次数据线程
					for (int i = 0; i < START_UAV_FLY_STS_THREAD_NUM; i++) {
						RedisClientUtil.brpop(RedisClientUtil.REDIS_QUEUE_ADSB_FLY_STS);
					}
					// 启动ADSB消批处理进程
					AdsbXiaopiDeamon.getInstance().start();
					// 启动ADSB数据推送进程
					AdsbOnlineUtil.startSenderDeamon();

					int loadInterval = Integer.parseInt(PropertiesUtil.getPropertyValue("ADSB_LOAD_INTERVAL", "3000"));
					int dealInterval = Integer.parseInt(PropertiesUtil.getPropertyValue("ADSB_DEAL_INTERVAL", "500"));
					String urlsStr = PropertiesUtil.getPropertyValue(Constants.CONFIG_FILE, "ADSB_URLS", "");
					if (urlsStr != null && urlsStr.trim().length() > 0) {
						String[] urls = urlsStr.trim().split(",");
						List<String> urlList = new LinkedList<String>();
						for (int i = 0; i < urls.length; i++) {
							urlList.add(urls[i]);
						}
						SessionFactory sessionFactory = (SessionFactory) SpringContextUtil.getBean("sessionFactory");
						new AdsbHandler().start(loadInterval, dealInterval, urlList, sessionFactory);
					}
				}
			}
		}else if(event instanceof ContextClosedEvent){
			log.info("应用关闭处理...");
			log.info("应用关闭成功！");
		}
	}
}