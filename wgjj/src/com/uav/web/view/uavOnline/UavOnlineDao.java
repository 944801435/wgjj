package com.uav.web.view.uavOnline;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.uav.base.common.BaseDAO;

@SuppressWarnings("rawtypes")
@Repository
public class UavOnlineDao extends BaseDAO{
	public List<?> findTopList(final String hql, final Object[] params, final int count) {
		return getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			@Override
			public List<?> doInHibernate(Session sess) throws HibernateException, SQLException {
				Query q = sess.createQuery(hql);
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						q.setParameter(i, params[i]);
					}
				}
				q.setFirstResult(0);
				q.setMaxResults(count);
				return q.list();
			}
		});
	}
}