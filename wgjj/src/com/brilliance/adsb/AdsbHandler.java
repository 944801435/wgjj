package com.brilliance.adsb;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import com.brilliance.base.util.RedisClientUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AdsbHandler {
	private static final Logger log = Logger.getLogger(AdsbHandler.class);

	private boolean doLoad = false;
	private boolean doDeal = false;
	private List<String> urls = null;
	private ExecutorService threadPool = null;
	private HttpClientConnectionManager connManager = null;
	private RequestConfig requestConfig = null;
	private int loadInterval = 3000;
	private int dealInterval = 500;
	Queue<List<FlightTarget>> queue = null;// 先进先出队列，数据获取线程压入数据，处理线程消费数据

	public AdsbHandler() {
		doLoad = false;
		doDeal = false;
		queue = new ArrayDeque<List<FlightTarget>>();
	}

	public synchronized void start(int loadInterval, int dealInterval, List<String> urls, SessionFactory sessionFactory) {
		this.loadInterval = loadInterval;
		this.dealInterval = dealInterval;
		this.urls = urls;
		this.threadPool = Executors.newFixedThreadPool(20);

		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		connManager.setMaxTotal(20);// 整个连接池的最大连接数
		connManager.setDefaultMaxPerRoute(10);// 每个route默认的连接数
		this.connManager = connManager;

		this.requestConfig = RequestConfig.custom().setConnectionRequestTimeout(2000).setConnectTimeout(2000).setSocketTimeout(2000).build();

		// 启动ADSB数据加载进程
		startLoadDeamon();

		// 启动ADSB数据处理进程
		startDealDeamon();
	}

	public synchronized void stop() {
		doLoad = false;
		doDeal = false;
		threadPool = null;
		connManager = null;
		requestConfig = null;
		queue = null;
	}

	// 启动ADSB数据获取进程
	public synchronized void startLoadDeamon() {
		doLoad = true;
		new Thread(new Runnable() {
			public void run() {
				while (doLoad) {
					log.debug("doLoad...");

					try {
						for (final String url : urls) {
							threadPool.execute(new Runnable() {
								@Override
								public void run() {
									CloseableHttpClient client = null;
									try {
										client = HttpClients.custom().setConnectionManager(connManager).setConnectionManagerShared(true)
												.setDefaultRequestConfig(requestConfig).build();
										HttpGet httpGet = new HttpGet(url);
										String content = EntityUtils.toString(client.execute(httpGet).getEntity());
										ObjectMapper mapper = new ObjectMapper();
										// 忽略未找到的属性
										mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
										FlightTargetWrapper wrapper = mapper.readValue(content, FlightTargetWrapper.class);
										if (wrapper != null && wrapper.getDs() != null) {
											log.info(url + " = " + wrapper.getDs().size());

											if (wrapper.getDs().size() > 0)
												queue.add(wrapper.getDs());
										} else {
											log.info(url + " = 0");
										}
									} catch (Exception e) {
										log.error("ADSB数据获取失败，url=" + url, e);
									} finally {
										try {
											if (client != null)
												client.close();
										} catch (Exception e) {
										}
									}
								}
							});
						}
					} catch (Exception e) {
						log.error("ADSB数据获取失败！", e);
					}

					try {
						Thread.sleep(loadInterval);
					} catch (Exception e) {
					}
				}
			}
		}).start();

		log.info("ADSB数据获取进程启动成功！");
	}

	// 启动ADSB数据处理进程
	public synchronized void startDealDeamon() {
		doDeal = true;
		new Thread(new Runnable() {
			public void run() {
				while (doDeal) {
					try {
						log.debug("doDeal...");

						List<FlightTarget> list = queue.poll();
						if (list != null && list.size() > 0) {
							// 推送到FDP，频道ly.uav.adsb，格式PN,LON,LAT,ALT,SPE,DIR,TI,LTIME
							double minJd = 122.1623973;
							double minWd = 40.9118352;
							double maxJd = 124.798736;
							double maxWd = 42.6478791;
							for (FlightTarget tar : list) {
								if (tar.getLng() >= minJd && tar.getLng() <= maxJd && tar.getLat() >= minWd && tar.getLat() <= maxWd) {
									StringBuilder sb = new StringBuilder();
									sb.append(tar.getId());
									sb.append(",").append(tar.getLng());
									sb.append(",").append(tar.getLat());
									sb.append(",").append(Math.round(tar.getHt()));
									sb.append(",").append(Math.round(tar.getSp()));
									sb.append(",").append(Math.round(tar.getAng()));
									sb.append(",").append(tar.getTi().trim());
									sb.append(",").append(tar.getAt());
									RedisClientUtil.lpush(RedisClientUtil.REDIS_TOPIC_ADSB_MD, sb.toString());
								}
							}
						}
					} catch (Exception e) {
						log.error("ADSB数据处理失败！", e);
					}

					try {
						Thread.sleep(dealInterval);
					} catch (Exception e) {
					}
				}
			}
		}).start();

		log.info("ADSB数据处理进程启动成功！");
	}
}