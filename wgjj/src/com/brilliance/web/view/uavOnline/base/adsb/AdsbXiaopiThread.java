package com.brilliance.web.view.uavOnline.base.adsb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.brilliance.base.common.Constants;
import com.brilliance.base.util.ConstantsUtil;
import com.brilliance.base.util.DateUtil;
import com.brilliance.base.util.PropertiesUtil;
import com.brilliance.base.util.RedisClientUtil;
import com.brilliance.base.util.SpringContextUtil;

public class AdsbXiaopiThread extends Thread {
	private static Logger log = Logger.getLogger(AdsbXiaopiThread.class);
	private static final String UAV_CACHE_FILE_LTIME = "ltime";
	private static final String UAV_CACHE_FILE_LNG = "lng";
	private static final String UAV_CACHE_FILE_LAT = "lat";
	private static String dftInstId = Long.toString(new Date().getTime());
	private boolean flag = false;

	protected AdsbXiaopiThread() {
		this.flag = true;
	}

	public void close() {
		flag = false;
	}

	synchronized public void run() {
		while (flag) {
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
			}

			Session sess = null;
			Transaction tx = null;
			PreparedStatement pstmt1 = null;
			PreparedStatement pstmtUpd3 = null;
			PreparedStatement pstmtUpd4 = null;
			ResultSet rs = null;
			try {
				SessionFactory sessionFactory = (SessionFactory) SpringContextUtil.getBean("sessionFactory");
				sess = sessionFactory.openSession();
				@SuppressWarnings("deprecation")
				Connection conn = sess.connection();
				tx = sess.beginTransaction();

				pstmt1 = conn.prepareStatement("select FLY_SEQ,PLANE_NO,BEG_TIME from biz_fly_adsb where FLY_STS=?");
				pstmtUpd3 = conn.prepareStatement("update biz_fly_adsb set FLY_STS=?,END_TIME=?,END_DATE=?,END_LOC=? where FLY_SEQ=?");
				pstmtUpd4 = conn.prepareStatement("update t_adsbdata set FLY_SEQ=? where ID=? and TIME>=? and TIME<=?");

				// 查询起飞状态的架次
				List<String[]> flyList = new LinkedList<String[]>();
				String flySeq = null;
				String sn = null;
				String begTime = null;
				pstmt1.setString(1, Constants.UAV_FLY_UP);
				rs = pstmt1.executeQuery();
				while (rs.next()) {
					flySeq = rs.getString(1);
					sn = rs.getString(2);
					begTime = rs.getString(3);
					flyList.add(new String[] { flySeq, sn, begTime });
				}
				rs.close();
				rs = null;

				if (flyList.size() <= 0)
					continue;

				// 获取系统配置参数-消批时间
				int xpTimeout = 0;
				String xpTimeoutStr = ConstantsUtil.sysConfigMap.get(Constants.SYS_CONF_UAV_ONLINE_TIMEOUT);
				if (xpTimeoutStr == null || xpTimeoutStr.trim().length() == 0) {
					xpTimeout = 30;
				} else {
					try {
						xpTimeout = Integer.parseInt(xpTimeoutStr.trim());
						if (xpTimeout <= 0)
							xpTimeout = 30;
					} catch (Exception e) {
						xpTimeout = 30;
					}
				}

				// 获取系统配置参数-消批处理抢占锁有效时间
				int lockTimeout = 0;
				String lockTimeoutStr = ConstantsUtil.sysConfigMap.get(Constants.SYS_CONF_XIAOPI_LOCK_TIMEOUT);
				if (lockTimeoutStr == null || lockTimeoutStr.trim().length() == 0) {
					lockTimeout = 210;
				} else {
					try {
						lockTimeout = Integer.parseInt(lockTimeoutStr.trim());
						if (lockTimeout <= 0)
							lockTimeout = 210;
					} catch (Exception e) {
						lockTimeout = 210;
					}
				}

				// 获取当前服务实例ID
				String instId = PropertiesUtil.getPropertyValue("INST_ID", dftInstId);

				// 随机获取架次信息
				Random r = new Random();
				int idx = 0;
				String[] strs = null;
				boolean doXiaopi = false;
				String key = null;
				String value = null;
				Date nowTime = null;
				String nowTimeStr = null;
				String nowTimeCompStr = null;
				List<String> list = null;
				String ltimeStr = null;
				String lngStr = null;
				String latStr = null;
				while (flyList.size() > 0) {
					idx = r.nextInt(flyList.size());
					strs = flyList.get(idx);
					flyList.remove(idx);

					flySeq = strs[0];
					sn = strs[1];
					begTime = strs[2];
					doXiaopi = false;

					// 到Redis缓存查询无人机最后信号时间，判断是否需要消批
					list = RedisClientUtil.getAdsbMsg(sn,
							new String[] { UAV_CACHE_FILE_LTIME, UAV_CACHE_FILE_LNG, UAV_CACHE_FILE_LAT});
					if(list == null){
						continue;
					}else{
						ltimeStr = list.get(0);
						lngStr = list.get(1);
						latStr = list.get(2);
					}
					if (ltimeStr == null) {
						// 缓存中不存在该架无人机信息，做消批处理
						doXiaopi = true;
					} else {
						// 当前时间减去最后信号时间，如果间隔大于消批时间，则消批处理
						if (((new Date().getTime() - Long.parseLong(ltimeStr)) / 1000) >= xpTimeout)
							doXiaopi = true;
					}
					if (!doXiaopi)
						continue;

					// 尝试到Redis缓存获取消批处理抢占锁，key=fly.adsb.飞行架次流水号
					key = RedisClientUtil.REDIS_THREAD_PREFIX_ADSB + flySeq;
					value = RedisClientUtil.getKeyValue(key);
					if (value != null && !value.equals(instId))
						continue;
					RedisClientUtil.setKeyValueTimeout(key, instId, lockTimeout);
					try {
						Thread.sleep(20);
					} catch (Exception e) {
					}
					value = RedisClientUtil.getKeyValue(key);
					if (!instId.equals(value))
						continue;
					
					// 抢占成功，执行消批处理
					nowTime = DateUtil.getNowTime();
					nowTimeStr = DateUtil.formatDate(nowTime, DateUtil.PATTERN_FULLTIME);
					nowTimeCompStr = DateUtil.formatDate(nowTime, DateUtil.PATTERN_TIME_WITH_MILSEC_COMPRESS);
					
					//删除Redis无人机信息缓存
					RedisClientUtil.delAdsbMsg(sn);
					
					// 修改架次状态为降落，记录降落时间等
					// pstmtUpd3=conn.prepareStatement("update biz_fly set
					// FLY_STS=?,END_TIME=?,END_DATE=?,END_LOC=? where
					// FLY_SEQ=?");
					pstmtUpd3.setString(1, Constants.UAV_FLY_DOWN);
					pstmtUpd3.setString(2, nowTimeStr);
					pstmtUpd3.setString(3, nowTimeStr.substring(0, 10));
					if (ltimeStr != null) {
						pstmtUpd3.setString(4, lngStr + "," + latStr);
					} else {
						pstmtUpd3.setString(4, "");
					}
					pstmtUpd3.setString(5, flySeq);
					pstmtUpd3.executeUpdate();

					// t_flydata航迹时间在架次时间范围内，修改航迹的飞行架次字段
					// pstmtUpd4=conn.prepareStatement("update t_flydata set
					// FLY_SEQ=? where SN=? and TIME>=? and TIME<=?");
					pstmtUpd4.setString(1, flySeq);
					pstmtUpd4.setString(2, sn);
					pstmtUpd4.setString(3, begTime);
					pstmtUpd4.setString(4, nowTimeStr);
					pstmtUpd4.executeUpdate();

					// 推送adsb.fly.land
					RedisClientUtil.publish(RedisClientUtil.REDIS_TOPIC_ADSB_FLY_LAND, sn + "," + nowTimeCompStr);
				}

				tx.commit();
			} catch (Exception e) {
				try {
					if (tx != null)
						tx.rollback();
				} catch (Exception e2) {
				}
				log.error("【adsb】消批处理失败!", e);
			} finally {
				try {
					if (rs != null)
						rs.close();
				} catch (Exception e2) {
				}
				try {
					if (pstmt1 != null)
						pstmt1.close();
				} catch (Exception e2) {
				}
				try {
					if (pstmtUpd3 != null)
						pstmtUpd3.close();
				} catch (Exception e2) {
				}
				try {
					if (pstmtUpd4 != null)
						pstmtUpd4.close();
				} catch (Exception e2) {
				}
				try {
					if (sess != null)
						sess.close();
				} catch (Exception e2) {
				}
			}
		}
	}
}