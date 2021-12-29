package com.brilliance.base.common;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Component
public class BaseDAO<T extends Serializable, ID extends Serializable> extends HibernateDaoSupport {

	private static final Logger log = Logger.getLogger(BaseDAO.class);
	
	/** The Constant ENABLE_FLAG. */
	public static final String ENABLE_FLAG = "Y";

	/** The Constant DISABLE_FLAG. */
	public static final String DISABLE_FLAG = "N";

	/**
	 * Sets the session factory0.
	 * 
	 * @param sessionFactory
	 *            the new session factory0
	 * @author harry
	 */
	@Resource(name = "sessionFactory")
	public void setSessionFactory0(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	public void delete(Object obj) {
		this.getHibernateTemplate().delete(obj);
	}

	public void deleteAll(Collection<Object> col) {
		Iterator<Object> iterator = col.iterator();
		while (iterator.hasNext()) {
			this.getHibernateTemplate().delete(iterator.next());
		}
	}

	public Serializable save(Object obj) {
		return this.getHibernateTemplate().save(obj);
	}
	public void saveOrUpdate(Object obj) {
		this.getHibernateTemplate().saveOrUpdate(obj);
	}
	public void merge(Object obj) {
		this.getHibernateTemplate().merge(obj);
	}
	public void executeSql(final String sql, final Object[] params) {
		getHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session sess) throws HibernateException, SQLException {
				SQLQuery q = sess.createSQLQuery(sql);
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						q.setParameter(i, params[i]);
					}
				}
				return q.executeUpdate();
			}

		});

	}

	public void executeHql(final String hql, final Object[] params) {
		getHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session sess) throws HibernateException, SQLException {
				Query q = sess.createQuery(hql);
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						q.setParameter(i, params[i]);
					}
				}
				return q.executeUpdate();
			}

		});

	}
	
	public void executeHql(final String hql, final Map<String, Object> params) {
		getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session sess) throws HibernateException, SQLException {
				Query q = sess.createQuery(hql);
				if (params != null) {
					for (String key : params.keySet()) {
						Object value = params.get(key);
						if (value instanceof Collection) {
							q.setParameterList(key, (Collection<?>) value);
						} else if (value instanceof Object[]) {
							q.setParameterList(key, (Object[]) value);
						} else {
							q.setParameter(key, value);
						}
					}
				}
				return q.executeUpdate();
			}
		});
	}

	public void update(Object obj) {
		this.getHibernateTemplate().update(obj);
	}

	public Object findById(Class<Object> entityClass, Serializable id) {
		return this.getHibernateTemplate().get(entityClass, id);
	}

	public Integer findCount(final String hql, final Object[] params) {
		int count = (int)getHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session sess) throws HibernateException, SQLException {
				Query q = sess.createQuery(hql);
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						q.setParameter(i, params[i]);
					}
				}
				Object object = q.uniqueResult();
				int total = 0;
				if (object != null) {
					total = Integer.parseInt(object.toString());
				}
				return total;
			}

		});

		return count;

	}

	public Integer findCount(final String hql, final Map<String, Object> params) {
		int count = (int)getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session sess) throws HibernateException, SQLException {
				Query q = sess.createQuery(hql);
				if (params != null) {
					for (String key : params.keySet()) {
						Object value = params.get(key);
						if (value instanceof Collection) {
							q.setParameterList(key, (Collection<?>) value);
						} else if (value instanceof Object[]) {
							q.setParameterList(key, (Object[]) value);
						} else {
							q.setParameter(key, value);
						}
					}
				}
				return Integer.parseInt(q.uniqueResult() + "");
			}
		});
		return count;
	}

	public Integer findCount_Sql(final String sql, final Object[] params) {
		int count = (int)getHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session sess) throws HibernateException, SQLException {
				SQLQuery q = sess.createSQLQuery(sql);
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						q.setParameter(i, params[i]);
					}
				}
				return Integer.parseInt(q.uniqueResult() + "");
			}

		});

		return count;

	}
	
	public Integer findCount_Sql(final String sql, final Map<String,Object> params) {
		int count = (int)getHibernateTemplate().execute(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session sess) throws HibernateException, SQLException {
				SQLQuery q = sess.createSQLQuery(sql);
				if (params != null) {
					for (String key : params.keySet()) {
						Object value = params.get(key);
						if (value instanceof Collection) {
							q.setParameterList(key, (Collection<?>) value);
						} else if (value instanceof Object[]) {
							q.setParameterList(key, (Object[]) value);
						} else {
							q.setParameter(key, value);
						}
					}
				}
				return Integer.parseInt(q.uniqueResult() + "");
			}
			
		});
		
		return count;
		
	}

	public List<?> findList(final String hql) {
		return getHibernateTemplate().execute(new HibernateCallback<List<?>>() {

			@Override
			public List<?> doInHibernate(Session sess) throws HibernateException, SQLException {
				Query q = sess.createQuery(hql);
				q.setCacheable(true);
				return q.list();
			}

		});
	}

	public List<?> findList(final String hql, final Object[] params) {
		return getHibernateTemplate().execute(new HibernateCallback<List<?>>() {

			@Override
			public List<?> doInHibernate(Session sess) throws HibernateException, SQLException {
				Query q = sess.createQuery(hql);
				q.setCacheable(true);
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						q.setParameter(i, params[i]);
					}
				}
				return q.list();
			}

		});

	}
	
	public List<?> findList(final StringBuffer hql, final List<Object> params) {
		return getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			
			@Override
			public List<?> doInHibernate(Session sess) throws HibernateException, SQLException {
				Query q = sess.createQuery(hql.toString());
				q.setCacheable(true);
				if (params != null) {
					for (int i = 0; i < params.size(); i++) {
						q.setParameter(i, params.get(i));
					}
				}
				return q.list();
			}
			
		});
		
	}

	public List<?> findList(final String hql, final Map<String, Object> params) {
		return getHibernateTemplate().execute(new HibernateCallback<List<?>>() {

			@Override
			public List<?> doInHibernate(Session sess) throws HibernateException, SQLException {
				Query q = sess.createQuery(hql);
				if (params != null) {
					for (String key : params.keySet()) {
						if (params.get(key) instanceof Collection) {
							q.setParameterList(key, (Collection) params.get(key));
						} else if (params.get(key) instanceof Object[]) {
							q.setParameterList(key, (Object[]) params.get(key));
						} else {
							q.setParameter(key, params.get(key));
						}
					}
				}
				return q.list();
			}

		});

	}

	public List<?> findList_Sql(final String sql, final Object[] params) {
		return getHibernateTemplate().execute(new HibernateCallback<List<?>>() {

			@Override
			public List<?> doInHibernate(Session sess) throws HibernateException, SQLException {
				SQLQuery q = sess.createSQLQuery(sql);
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						q.setParameter(i, params[i]);
					}
				}
				return q.list();
			}

		});

	}

	public List<?> findList_Sql(final String sql, final Map<String, Object> params) {
		return getHibernateTemplate().execute(new HibernateCallback<List<?>>() {

			@Override
			public List<?> doInHibernate(Session sess) throws HibernateException, SQLException {
				SQLQuery q = sess.createSQLQuery(sql);
				if (params != null) {
					for (String key : params.keySet()) {
						if (params.get(key) instanceof Collection) {
							q.setParameterList(key, (Collection) params.get(key));
						} else if (params.get(key) instanceof Object[]) {
							q.setParameterList(key, (Object[]) params.get(key));
						} else {
							q.setParameter(key, params.get(key));
						}
					}
				}
				return q.list();
			}

		});

	}

	public List<?> findListByPage(final String hql, final Object[] params, final int curPage, final int pageSize) {
		return getHibernateTemplate().execute(new HibernateCallback<List<?>>() {

			@Override
			public List<?> doInHibernate(Session sess) throws HibernateException, SQLException {
				Query q = sess.createQuery(hql);
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						q.setParameter(i, params[i]);
					}
				}
				q.setFirstResult((curPage - 1) * pageSize);
				q.setMaxResults(pageSize);
				return q.list();
			}

		});

	}

	public List<?> findListByPage(final String hql, final Map<String, Object> params, final int curPage, final int pageSize) {
		return getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			@Override
			public List<?> doInHibernate(Session sess) throws HibernateException, SQLException {
				Query q = sess.createQuery(hql);
				if (params != null) {
					for (String key : params.keySet()) {
						Object value = params.get(key);
						if (value instanceof Collection) {
							q.setParameterList(key, (Collection<?>) value);
						} else if (value instanceof Object[]) {
							q.setParameterList(key, (Object[]) value);
						} else {
							q.setParameter(key, value);
						}
					}
				}
				q.setFirstResult((curPage - 1) * pageSize);
				q.setMaxResults(pageSize);
				return q.list();
			}
		});
	}

	/**
	 * 查询结果会以List<Map<String,Object>>返回
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findList_Sql_Map(final String sql, final Object[] params) {
		return getHibernateTemplate().execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session sess) throws HibernateException, SQLException {
				SQLQuery q = sess.createSQLQuery(sql);
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						q.setParameter(i, params[i]);
					}
				}
				q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return q.list();
			}
		});
	}

	/**
	 * 查询结果会以List<Map<String,Object>>返回
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findList_Sql_Map(final String sql, final Map<String, Object> params) {
		return getHibernateTemplate().execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session sess) throws HibernateException, SQLException {
				SQLQuery q = sess.createSQLQuery(sql);
				if (params != null) {
					for (String key : params.keySet()) {
						Object value = params.get(key);
						if (value instanceof Collection) {
							q.setParameterList(key, (Collection<?>) value);
						} else if (value instanceof Object[]) {
							q.setParameterList(key, (Object[]) value);
						} else {
							q.setParameter(key, value);
						}
					}
				}
				q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return q.list();
			}
		});
	}

	/**
	 * 查询结果会以List<Map<String,Object>>返回
	 * 
	 * @param sql
	 * @param params
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> findListByPage_Sql_Map(final String sql, final Object[] params, final int curPage, final int pageSize) {
		return getHibernateTemplate().execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session sess) throws HibernateException, SQLException {
				SQLQuery q = sess.createSQLQuery(sql);
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						q.setParameter(i, params[i]);
					}
				}
				q.setFirstResult((curPage - 1) * pageSize);
				q.setMaxResults(pageSize);
				q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return q.list();
			}
		});
	}

	/**
	 * 查询结果会以List<Map<String,Object>>返回
	 * 
	 * @param sql
	 * @param params
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> findListByPage_Sql_Map(final String sql, final Map<String, Object> params, final int curPage, final int pageSize) {
		return getHibernateTemplate().execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session sess) throws HibernateException, SQLException {
				SQLQuery q = sess.createSQLQuery(sql);
				if (params != null) {
					for (String key : params.keySet()) {
						Object value = params.get(key);
						if (value instanceof Collection) {
							q.setParameterList(key, (Collection<?>) value);
						} else if (value instanceof Object[]) {
							q.setParameterList(key, (Object[]) value);
						} else {
							q.setParameter(key, value);
						}
					}
				}
				q.setFirstResult((curPage - 1) * pageSize);
				q.setMaxResults(pageSize);
				q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return q.list();
			}
		});
	}

	public boolean updateObject(final String hql, final List<Object> params) {
		int count = (Integer) getHibernateTemplate().execute(new HibernateCallback<Object>() {

			@Override
			public Object doInHibernate(Session sess) throws HibernateException, SQLException {
				Query q = sess.createQuery(hql.toString());
				for (int i = 0; i < params.size(); i++) {
					q.setParameter(i, params.get(i));
				}
				return q.executeUpdate();
			}
		});
		return count > 0;
	}

	public boolean updateObject(final String hql, final Map<String, Object> map) {
		int count = (Integer) getHibernateTemplate().execute(new HibernateCallback<Object>() {

			@Override
			public Object doInHibernate(Session sess) throws HibernateException, SQLException {
				Query q = sess.createQuery(hql.toString());
				for (String key : map.keySet()) {
					q.setParameter(key, map.get(key));
				}
				return q.executeUpdate();
			}
		});
		return count > 0;
	}

	public Object findUnique(final String hql, final Object[] params) {

		return getHibernateTemplate().execute(new HibernateCallback<Object>() {

			@Override
			public Object doInHibernate(Session sess) throws HibernateException, SQLException {
				Query q = sess.createQuery(hql);
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						q.setParameter(i, params[i]);
					}
				}
				return q.uniqueResult();
			}
		});

	}

	public Object findUnique_Sql(final String sql, final Object[] params) {
		return getHibernateTemplate().execute(new HibernateCallback<Object>() {

			@Override
			public Object doInHibernate(Session sess) throws HibernateException, SQLException {
				SQLQuery q = sess.createSQLQuery(sql);
				for (int i = 0; i < params.length; i++) {
					q.setParameter(i, params[i]);
				}
				return q.uniqueResult();
			}
		});

	}
	
	/**
	 * 根据HQL语句进行分页查询，查询语句中包含若干个?参数
	 * @param queryHql HQL查询语句
	 * @param params HQL查询语句中用于替换?的参数值
	 * @param offset 从第几行开始查询
	 * @param pagesize 最多返回多少行数据
	 * @return
	 */
	public PagerVO findPaginated(String queryHql, Object[] params, int offset,int pagesize) throws Exception{
		log.debug("根据HQL语句进行分页查询，查询语句中包含若干个参数");
		// 首先查询总记录数
		String countHql = getCountHql(queryHql);
		Query query = getSessionFactory().getCurrentSession().createQuery(countHql);
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
		int total = ((Long) query.uniqueResult()).intValue();
		
		// 查询当前页的数据
		query = getSessionFactory().getCurrentSession().createQuery(queryHql);
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
		if(pagesize>0){
			query.setFirstResult((offset - 1) * pagesize);
			query.setMaxResults(pagesize);
		}
		List<Object> datas = query.list();
		PagerVO pv = new PagerVO();
		pv.setItems(datas);
		pv.setCounts(total);
		
		return pv;
	}
	
	public PagerVO findPaginated(StringBuffer queryHql, List<Object> params, int offset,int pagesize) throws Exception{
		log.debug("根据HQL语句进行分页查询，查询语句中包含若干个参数");
		// 首先查询总记录数
		String countHql = getCountHql(queryHql.toString());
		Query query = getSessionFactory().getCurrentSession().createQuery(countHql);
		if (params != null) {
			for (int i = 0; i < params.size(); i++) {
				query.setParameter(i, params.get(i));
			}
		}
		int total = ((Long) query.uniqueResult()).intValue();
		
		// 查询当前页的数据
		query = getSessionFactory().getCurrentSession().createQuery(queryHql.toString());
		if (params != null) {
			for (int i = 0; i < params.size(); i++) {
				query.setParameter(i, params.get(i));
			}
		}
		if(pagesize>0){
			query.setFirstResult((offset - 1) * pagesize);
			query.setMaxResults(pagesize);
		}
		List<Object> datas = query.list();
		PagerVO pv = new PagerVO();
		pv.setItems(datas);
		pv.setCounts(total);
		
		return pv;
	}
	
	public PagerVO findPaginated(String queryHql, Map<String,Object> params, int offset,int pagesize) throws Exception{
		log.debug("根据HQL语句进行分页查询，查询语句中包含若干个参数");
		// 首先查询总记录数
		String countHql = getCountHql(queryHql);
		Query query = getSessionFactory().getCurrentSession().createQuery(countHql);
		if (params != null) {
			for (String key : params.keySet()) {
				Object value = params.get(key);
				if (value instanceof Collection) {
					query.setParameterList(key, (Collection<?>) value);
				} else if (value instanceof Object[]) {
					query.setParameterList(key, (Object[]) value);
				} else {
					query.setParameter(key, value);
				}
			}
		}
		int total = ((Long) query.uniqueResult()).intValue();
		
		// 查询当前页的数据
		query = getSessionFactory().getCurrentSession().createQuery(queryHql);
		if (params != null) {
			for (String key : params.keySet()) {
				Object value = params.get(key);
				if (value instanceof Collection) {
					query.setParameterList(key, (Collection<?>) value);
				} else if (value instanceof Object[]) {
					query.setParameterList(key, (Object[]) value);
				} else {
					query.setParameter(key, value);
				}
			}
		}
		if(pagesize>0){
			query.setFirstResult((offset - 1) * pagesize);
			query.setMaxResults(pagesize);
		}
		List<Object> datas = query.list();
		PagerVO pv = new PagerVO();
		pv.setItems(datas);
		pv.setCounts(total);
		
		return pv;
	}
	
	/**
	 * 
	 * 描述 创建查询总记录数的HQL
	 * @Title: getCountHql 
	 * @author 钟志峰
	 * @Modified By 钟志峰
	 * @param @param queryHql 原始HQL
	 * @param @return
	 * @param @throws Exception    
	 * @return String 返回类型 
	 * @throws
	 */
	protected String getCountHql(String queryHql) throws Exception{
		int index = queryHql.toLowerCase().indexOf("from");
		if (index == -1) {
			throw new RuntimeException("非法的查询语句【" + queryHql + "】");
		}
		return "select count(*) " + queryHql.substring(index);
	}

}
