package com.uav.base.util;

import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.uav.base.common.BaseDAO;

@SuppressWarnings("rawtypes")
public class SeqUtil {
	private static final Log log = LogFactory.getLog(SeqUtil.class);
	private static SeqUtil sequencer = new SeqUtil();
	private Map<String, SequenceBank> sequences = new Hashtable<String, SequenceBank>();
	private static final String tableName = "SYS_SEQUENCE";
	private static final String idColumn = "SEQ_NAME";
	private static final String valColumn = "SEQ_VALUE";
	private static final int DEFAULT_LEN = 10;
	private static final String STR_ZERO = "0000000000";

	private SeqUtil() {

	}

	public static String getNextSeq(String seqName) {
		return getNextSeq(seqName, DEFAULT_LEN);
	}

	public static String getNextSeq(String seqName, int rtnLen) {
		String nextSeq = getInstance().getNextSeqId(seqName).toString();
		int len = nextSeq.length();
		if (len >= rtnLen) {
			return nextSeq.substring(0, rtnLen);
		} else {
			return STR_ZERO.substring(0, rtnLen - len) + nextSeq;
		}
	}

	public Long getNextSeqId(String seqName) {
		SequenceBank bank = (SequenceBank) sequences.get(seqName);
		if (bank == null) {
			bank = new SequenceBank(seqName, this);
			sequences.put(seqName, bank);
		}
		return bank.getNextSeqId();
	}

	public static SeqUtil getInstance() {
		return sequencer;
	}

	class SequenceBank {
		private static final long bankSize = 100;
//		private static final long startSeqId = 0;
//		private static final int minWaitNanos = 500000; // 1/2 ms
//		private static final int maxWaitNanos = 10000000; // 1 ms
//		private static final int maxTries = 5;
		private static final String SQL_SEL = "SELECT " + SeqUtil.valColumn + " FROM " + SeqUtil.tableName + " WHERE " + SeqUtil.idColumn + "=?";
		private static final String SQL_INS = "INSERT INTO " + SeqUtil.tableName + " (" + SeqUtil.idColumn + "," + SeqUtil.valColumn + ") VALUES (?,?)";
		private static final String SQL_UPD = "UPDATE " + SeqUtil.tableName + " SET " + SeqUtil.valColumn + "= ? WHERE "+ SeqUtil.idColumn + "=?";

		private long curSeqId;
		private String seqName;

		public SequenceBank(String seqName, SeqUtil parentUtil) {
			this.seqName = seqName;
			curSeqId = 0;
		}

		public synchronized Long getNextSeqId() {
			if(curSeqId % bankSize ==0){
				fillBank();
			}
			if (curSeqId >= 0) {
				Long retSeqId = new Long(curSeqId);
				curSeqId++;
				return retSeqId;
			} else {
				return null;
			}
			
		}

		protected synchronized void fillBank() {
//			 ApplicationContext context = new
//			 ClassPathXmlApplicationContext("applicationContext.xml");
//			 BaseDAO baseDao=(BaseDAO)context.getBean("baseDAO");
			BaseDAO baseDao = (BaseDAO) SpringContextUtil.getBean("baseDAO");
			try {
				Object seqVal = findUnique_Sql(SQL_SEL, new Object[] { this.seqName },baseDao);
				if(seqVal==null){
					executeSql(SQL_INS, new Object[] { this.seqName,bankSize },baseDao);
					curSeqId=0;
				}else{
					curSeqId=Long.parseLong(seqVal + "");
					executeSql(SQL_UPD, new Object[]{curSeqId+bankSize,this.seqName},baseDao);
				}
			} catch (Exception e) {
				log.error("Fill bank failed!", e);
				curSeqId=-1;
			}
		}
	}

	
	public void executeSql(final String sql, final Object[] params,BaseDAO baseDao) {
		Session session = null;
		try {
			session = baseDao.getSessionFactory().openSession();
			session.beginTransaction();//开始事务
			SQLQuery q = session.createSQLQuery(sql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					q.setParameter(i, params[i]);
				}
			}
			q.executeUpdate();
			session.getTransaction().commit();//事务提交
			
		} catch (HibernateException e) {
			session.getTransaction().rollback();//回滚事务
		}finally {
			if (session != null) {
				session.close(); //关闭session
			}
		}
	}
	
	public Object findUnique_Sql(final String sql, final Object[] params,BaseDAO baseDao) {

		Session session = null;
		try {
			session = baseDao.getSessionFactory().openSession();//打开session
			SQLQuery q = session.createSQLQuery(sql);
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i, params[i]);
			}
			return q.uniqueResult();
		} finally {
			if (session != null) {
				session.close(); //关闭session
			}
		}

	}
	
	public static void main(String[] args) {
		for(int i=0;i<110;i++){
			System.out.println(SeqUtil.getNextSeq("qiaocy"));
		}
	}
}
