package com.uav.web.view.uavOnline.base.adsb;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.uav.base.common.Constants;
import com.uav.base.util.SeqUtil;
import com.uav.base.util.SpringContextUtil;

import lombok.extern.slf4j.Slf4j;
/**
 * 处理飞行架次数据
 * 
 * @Title: RedisUavFlyStsListener.java
 */
@Slf4j
@SuppressWarnings("unchecked")
public class RedisAdsbFlyStsListener {

	private static final String STR_DOT = ",";

	public static void deal(String message) {
		Session sess = null;
		Transaction tx = null;
		try {
			log.info("【adsb】接收到Redis架次数据：" + message);
			SessionFactory sessionFactory = (SessionFactory) SpringContextUtil.getBean("sessionFactory");
			sess = sessionFactory.openSession();
			tx = sess.beginTransaction();
			/* 
			 * 格式：
			 	有人机编号,经度,维度,起飞或降落状态,时间
			 */
			if (StringUtils.isBlank(message)) {
				log.error("【adsb】接收到Redis架次数据错误！");
				return;
			}
			String[] fields = message.split(STR_DOT, -1);
			if (fields.length < 5) {
				log.error("【adsb】接收到Redis架次数据格式错误：" + message);
				return;
			}
			String pn = fields[0];// 飞机编号
			if (pn.length() == 0) {
				log.error("【adsb】接收到Redis架次数据格式错误，SN不能为空，message=" + message);
				return;
			}
			String lng = fields[1];// 经度
			String lat = fields[2];// 纬度
			String tol = fields[3];// 起飞：3 降落：4
			if (StringUtils.isBlank(tol)) {
				log.error("【adsb】接收到Redis架次数据格式错误，起飞降落状态不能为空，message=" + message);
				return;
			}
			String time = fields[4];// 时间
			
			if (Constants.UAV_FLY_UP.equals(tol)) {
				// 起飞
				Query query = sess.createSQLQuery("insert into biz_fly_adsb (fly_seq,beg_time,beg_date,beg_loc,fly_sts,plane_no) values (?,?,?,?,?,?)");
				int index = -1;
				String flySeq=SeqUtil.getNextSeq("biz_fly_adsb", 10);
				query.setString(++index, flySeq);
				query.setString(++index, time);
				query.setString(++index, time.substring(0, 10));
				query.setString(++index, lng+","+lat);
				query.setString(++index, tol);
				query.setString(++index, pn);
				query.executeUpdate();
				log.debug("【adsb】飞行架次起飞" + flySeq + ":" + pn);
			} else if (Constants.UAV_FLY_DOWN.equals(tol)) {
				// 查询起飞状态的架次
				List<Object[]> flyList = sess.createQuery("select fly_seq,beg_time from biz_fly_adsb where plane_no=? and beg_time=? and fly_sts=?").setString(0, pn).setString(1, time)
						.setString(2, Constants.UAV_FLY_UP).list();
				for (Object[] objs : flyList) {
					String flySeq=objs[0]+"";
					String begTime=objs[1]+"";
					//修改架次表降落
					sess.createSQLQuery("update biz_fly_adsb set end_time=?,end_date=?,end_loc=? where fly_seq=?").setString(0, time)
						.setString(1, time.substring(0, 10)).setString(2, lng+","+lat).setString(3,flySeq).executeUpdate();
					// 修改航迹表中的架次id
					sess.createSQLQuery("update t_adsbdata set fly_seq=? where ti=? and time>=? and time<=?").setString(0, flySeq)
							.setString(1, pn).setString(2, begTime).setString(3, time).executeUpdate();
				}
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			log.error("【adsb】处理架次数据出现异常！", e);
		} finally {
			try {
				if(sess!=null){
					sess.close();
				}
			} catch (Exception e2) {
			}
		}
	}
}