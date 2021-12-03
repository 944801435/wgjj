package com.uav.web.view.uavOnline.base.adsb;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.uav.base.util.SpringContextUtil;

import lombok.extern.slf4j.Slf4j;
/**
 * 处理飞行态势数据
 * 
 */
@Slf4j
public class RedisAdsbFlyDataListener {

	private static final String STR_DOT = ",";

	public static void deal(String message) {
		Session sess = null;
		Transaction tx = null;
		try {
			log.info("【adsb】接收到Redis飞行数据：" + message);

			SessionFactory sessionFactory = (SessionFactory) SpringContextUtil.getBean("sessionFactory");
			sess = sessionFactory.openSession();
			tx = sess.beginTransaction();
			/* 
			 * 格式：
			 	PN,LON,LAT,ALT,SPE,DIR,TI,MUC,TIME   //MUC：信号时间  TIME：FDP接收时间
			 * 780969,110.524864,20.766311,611.124,0.0,0.0,cbj5747,2018-11-07 11:22:09.483,2018-11-07 11:22:09
			 */
			String[] fields = message.split(STR_DOT, -1);
			if (fields.length < 8) {
				log.error("【adsb】接收到的飞行数据格式错误，message=" + message);
				return;
			}

			String pn = fields[0];
			if (pn.length() == 0) {
				log.error("【adsb】接收到的飞行数据格式错误，编码不能为空，message=" + message);
				return;
			}
			String lng = fields[1];// 经度
			if (lng.length() == 0) {
				log.error("【adsb】接收到的飞行数据格式错误，经度不能为空，message=" + message);
				return;
			}
			String lat = fields[2];// 纬度
			if (lat.length() == 0) {
				log.error("【adsb】接收到的飞行数据格式错误，纬度不能为空，message=" + message);
				return;
			}
			String alt = fields[3];// 
			if (alt.length() == 0) {
				log.error("【adsb】接收到的飞行数据格式错误，高度不能为空，message=" + message);
				return;
			}
			String spe = fields[4];// 速度
			if (spe.length() == 0) {
				log.error("【adsb】接收到的飞行数据格式错误，速度不能为空，message=" + message);
				return;
			}
			String dir = fields[5];// 航向
			if (dir.length() == 0) {
				log.error("【adsb】接收到的飞行数据格式错误，航向不能为空，message=" + message);
				return;
			}
			String ti = fields[6]; // 航班号
			if (ti.length() == 0) {
				log.error("【adsb】接收到的飞行数据格式错误，航班号不能为空，message=" + message);
				return;
			}
			String mcu = fields[7];// 信号时间
			if (StringUtils.isBlank(mcu)) {
				log.error("【adsb】接收到的飞行数据格式错误，MCU时间不能为空，message=" + message);
				return;
			}
			String time = fields[8];// FDP接收时间
			if (StringUtils.isBlank(mcu)) {
				log.error("【adsb】接收到的飞行数据格式错误，FDP接收时间不能为空，message=" + message);
				return;
			}
			Query query = sess
					.createSQLQuery("insert into t_adsbdata (ID,AT,SP,HT,ANG,LAT,LNG,TI,TIME) values (?,?,?,?,?,?,?,?,?)");
			int index = -1;
			query.setString(++index, pn);
			query.setString(++index, mcu);
			query.setDouble(++index, Float.parseFloat(spe));
			query.setDouble(++index, Float.parseFloat(alt));
			query.setDouble(++index, Integer.parseInt(dir));
			query.setDouble(++index, Double.parseDouble(lat));
			query.setDouble(++index, Double.parseDouble(lng));
			query.setString(++index, ti);
			query.setString(++index, time);
			query.executeUpdate();

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			log.error("处理态势数据出现异常！", e);
		} finally {
			try {
				if (sess != null) {
					sess.close();
				}
			} catch (Exception e2) {
			}
		}
	}

	public static void main(String[] args) {
//		String s = "109.7641650";
//		System.out.println(Float.parseFloat(s));
	}
}