package com.brilliance.web.view.civilaviation;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.brilliance.base.common.BaseDAO;
import com.brilliance.base.common.PagerVO;
import com.brilliance.base.model.internetModel.NoteReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 照会信息管理
 * @ClassName: NoteManageDao
 * @Description: 
 * @author 
 * @date 
 */
@Repository
@SuppressWarnings("rawtypes")
public class CivilAviationDAO extends BaseDAO{

	public PagerVO findReportList(NoteReport noteReport, Integer curPage, int pagesize) {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();		
		StringBuilder sb = new StringBuilder();
		sb.append("select count(1) from note_report nr LEFT JOIN note_plan_info npi ON nr.`note_id`=npi.`note_id` "
				+ " LEFT JOIN note_civil_message ncm ON nr.`note_id`=ncm.`note_id` WHERE ncm.`create_time`=(SELECT MAX(create_time)  FROM note_civil_message) ");
		if(!StringUtils.isBlank(noteReport.getBeginTime())){
			sb.append(" and nr.createTime>=:beginTime");
		}
		if(!StringUtils.isBlank(noteReport.getEndTime())){
			sb.append(" and nr.createTime<=:endTime");
		}
		sb.append(" order by nr.create_time desc");
		String sql =sb.toString();
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
		if(!StringUtils.isBlank(noteReport.getBeginTime())){
			query.setParameter("beginTime", noteReport.getBeginTime());
		}
		if(!StringUtils.isBlank(noteReport.getEndTime())){
			query.setParameter("endTime", noteReport.getEndTime());
		}
		int rows = Integer.valueOf(query.uniqueResult().toString());
		StringBuilder sb1 = new StringBuilder();
		sb1.append("select nr.`id`,nr.`note_id`,nr.`document_num`,nr.`file_name`,nr.`file_path`,nr.`status`,nr.`create_time` "
				+ " ,npi.`call_number`,npi.`nationality`,npi.`note_no`,ncm.`permit_number` from note_report nr LEFT JOIN note_plan_info npi ON nr.`note_id`=npi.`note_id` LEFT JOIN "
				+ " note_civil_message ncm ON nr.`note_id`=ncm.`note_id` WHERE ncm.`create_time`=(SELECT MAX(create_time)  FROM note_civil_message) ");
		if(!StringUtils.isBlank(noteReport.getBeginTime())){
			sb1.append(" and nr.createTime>=:beginTime");
		}
		if(!StringUtils.isBlank(noteReport.getEndTime())){
			sb1.append(" and nr.createTime<=:endTime");
		}
		sb1.append(" order by nr.create_time desc");
		String sql1 =sb1.toString();
		Query query1 = getSessionFactory().getCurrentSession().createSQLQuery(sql1);
		if(!StringUtils.isBlank(noteReport.getBeginTime())){
			query1.setParameter("beginTime", noteReport.getBeginTime());
		}
		if(!StringUtils.isBlank(noteReport.getEndTime())){
			query1.setParameter("endTime", noteReport.getEndTime());
		}
		if(pagesize>0){
			query1.setFirstResult((curPage - 1) * pagesize);
			query1.setMaxResults(pagesize);
		}
		List<Object[]> accounts = query1.list();
		for (Object[] account : accounts) {
			Map<String, String> m = new HashMap<String, String>();
			m.put("id", account[0].toString());
			m.put("noteId", account[1].toString());
			m.put("documentNum", account[2]==null?"":account[2].toString());
			m.put("fileName", account[3]==null?"":account[3].toString());
			m.put("filePath", account[4]==null?"":account[4].toString());
			m.put("status", account[5]==null?"":account[5].toString());
			m.put("createTime", account[6]==null?"":account[6].toString());
			m.put("callNumber", account[7]==null?"":account[7].toString());
			m.put("nationality", account[8]==null?"":account[8].toString());
			m.put("noteNo", account[9]==null?"":account[9].toString());
			m.put("permitNumber", account[10]==null?"":account[10].toString());
			data.add(m);
		}
		PagerVO<Map<String, String>> pv = new PagerVO();
		pv.setItems(data);
		pv.setCounts(rows);
		return pv;
	}

}
