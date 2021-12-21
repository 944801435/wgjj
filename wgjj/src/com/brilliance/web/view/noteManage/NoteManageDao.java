package com.brilliance.web.view.noteManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.brilliance.base.common.BaseDAO;
import com.brilliance.base.common.PagerVO;
import com.brilliance.base.model.internetModel.NoteFiles;
import com.brilliance.base.model.internetModel.NotePlanFlight;
import com.brilliance.base.model.internetModel.NotePlanInfo;
import com.brilliance.base.model.internetModel.NoteReport;

/**
 * 照会信息管理
 * @ClassName: NoteManageDao
 * @Description: 
 * @author 
 * @date 
 */
@Repository
@SuppressWarnings("rawtypes")
public class NoteManageDao extends BaseDAO{

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
	private void appendConditions(StringBuilder sb, NotePlanInfo planInfo) {
		/*if(!StringUtils.isBlank(planInfo.getBeginTime())){
			sb.append(" and npi.createTime>=:beginTime");
		}
		if(!StringUtils.isBlank(planInfo.getEndTime())){
			sb.append(" and npi.createTime<=:endTime");
		}*/
		if (!StringUtils.isBlank(planInfo.getDocumentNum())) {
			sb.append(" and npi.note_no=:noteNo");  //文书编号
		}
		if (!StringUtils.isBlank(planInfo.getNationality())) {
			sb.append(" and npi.nationality=:nationality");  //国籍
		}
		if (!StringUtils.isBlank(planInfo.getModel())) {
			sb.append(" and npi.model=:model");   //机型
		}
		if (!StringUtils.isBlank(planInfo.getCallNumber())) {
			sb.append(" and npi.call_number=:callNumber");
		}
		if (!StringUtils.isBlank(planInfo.getEntryName())) {
			sb.append(" and npf.entry_name=:entryName");
		}
		if (!StringUtils.isBlank(planInfo.getExitName())) {
			sb.append(" and npf.exit_name=:exitName");
		}
		if (planInfo.getStatus()!=null&&planInfo.getStatus()!=0) {
			sb.append(" and npi.status=:status");
		}
	}
	private void setParameters(Query query, NotePlanInfo planInfo) {
		/*if(!StringUtils.isBlank(planInfo.getBeginTime())){
			query.setParameter("beginTime", planInfo.getBeginTime());
		}
		if(!StringUtils.isBlank(planInfo.getEndTime())){
			query.setParameter("endTime", planInfo.getEndTime());
		}*/
		if (!StringUtils.isBlank(planInfo.getDocumentNum())) {
			query.setParameter("documentNum",planInfo.getDocumentNum());  //文书编号
		}
		if (!StringUtils.isBlank(planInfo.getNationality())) {
			query.setParameter("nationality",planInfo.getNationality());  //国籍
		}
		if (!StringUtils.isBlank(planInfo.getModel())) {
			query.setParameter("model",planInfo.getModel());  //机型
		}
		if (!StringUtils.isBlank(planInfo.getCallNumber())) {
			query.setParameter("callNumber",planInfo.getCallNumber());
		}
		if (!StringUtils.isBlank(planInfo.getEntryName())) {
			query.setParameter("entryName",planInfo.getEntryName());
		}
		if (!StringUtils.isBlank(planInfo.getExitName())) {
			query.setParameter("exitName",planInfo.getExitName());
		}
		if (planInfo.getStatus()!=null&&planInfo.getStatus()!=0) {
			query.setParameter("status",planInfo.getStatus());
		}
	}
	public PagerVO findNoteInfoList(NotePlanInfo planInfo, Integer curPage, int pagesize) {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(DISTINCT npi.`note_id`) as noteCount FROM note_plan_info npi LEFT JOIN note_plan_flight npf ON npi.`note_id`=npf.`note_id` WHERE  npi.del_status!=0 ");
		appendConditions(sb,planInfo);
		sb.append(" order by npi.create_time desc");
		String sql =sb.toString();
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
		setParameters(query,planInfo);
		int rows = Integer.valueOf(query.uniqueResult().toString());
		StringBuilder sb1 = new StringBuilder();
		sb1.append("SELECT DISTINCT npi.`note_id`,npi.`document_num`,npi.`letter_unit`,npi.`person_name`,"
				+ "npi.`tel_no`,npi.`mission`,npi.`nationality`,npi.`reg_no`,npi.`call_number`,npi.`model`,"
				+ "npi.`person_number`,npi.`operator`,npi.`other`,npi.`air_number`,npi.`flight_time`,npi.`creator`,"
				+ "npi.`create_time`,npi.`note_no`,npi.`flight_plan`,npi.`status`  FROM note_plan_info npi "
				+ "LEFT JOIN note_plan_flight npf ON npi.`note_id`=npf.`note_id` WHERE npi.del_status!=0 ");
		appendConditions(sb1,planInfo);
		sb1.append(" order by npi.create_time desc");
		String sql1 =sb1.toString();
		Query query1 = getSessionFactory().getCurrentSession().createSQLQuery(sql1);
		setParameters(query1,planInfo);
		if(pagesize>0){
			query1.setFirstResult((curPage - 1) * pagesize);
			query1.setMaxResults(pagesize);
		}
		List<Object[]> accounts = query1.list();
		for (Object[] account : accounts) {
			Map<String, String> m = new HashMap<String, String>();
			m.put("noteId", account[0].toString());
			m.put("documentNum", account[1]==null?"":account[1].toString());
			m.put("letterUnit", account[2]==null?"":account[2].toString());
			m.put("personName", account[3]==null?"":account[3].toString());
			m.put("telNo", account[4]==null?"":account[4].toString());
			m.put("mission", account[5]==null?"":account[5].toString());
			m.put("nationality", account[6]==null?"":account[6].toString());
			m.put("regNo", account[7]==null?"":account[7].toString());
			m.put("callNumber", account[8]==null?"":account[8].toString());
			m.put("model", account[9]==null?"":account[9].toString());
			m.put("personNumber", account[10]==null?"":account[10].toString());
			m.put("operator", account[11]==null?"":account[11].toString());
			m.put("other", account[12]==null?"":account[12].toString());
			m.put("airNumber", account[13]==null?"":account[13].toString());
			m.put("flightTime", account[14]==null?"":account[14].toString());
			m.put("noteNo", account[17]==null?"":account[17].toString());
			m.put("flightPlan", account[18]==null?"":account[18].toString());
			m.put("status", account[19]==null?"":account[19].toString());
			data.add(m);
		}
		PagerVO<Map<String, String>> pv = new PagerVO();
		pv.setItems(data);
		pv.setCounts(rows);
		return pv;
	}
	public void addPlanFlightInfo(NotePlanFlight obj) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.save(obj);
			transaction.commit();
			session.flush();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
			logger.error(e.getMessage());
		}finally{
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	}
	public void saveNoteInfo(NotePlanInfo obj,List<NoteFiles> list) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.save(obj);
			for(int i=0;i<list.size();i++){
				NoteFiles noteFiles = list.get(i);
				noteFiles.setNoteId(obj.getNoteId());
				session.save(noteFiles);
			}
			transaction.commit();
			session.flush();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
			logger.error(e.getMessage());
		}finally{
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	}
	public List<NoteFiles> findFilesByNoteId(Integer noteIds) {
		Session session = getSessionFactory().openSession();
		List<NoteFiles> noteFiles = null;
		try {
			SQLQuery sqlQuery = session.createSQLQuery("SELECT * FROM note_files nf WHERE nf.`note_id`=:noteId ORDER BY nf.`id` DESC").addEntity(NoteFiles.class);
			sqlQuery.setParameter("noteId", noteIds);
			noteFiles = sqlQuery.list();
			return noteFiles;
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return null;
	}
	public List<NotePlanFlight> findFlightByNoteId(String noteId) {
		Session session = getSessionFactory().openSession();
		List<NotePlanFlight> notePlanFlights = null;
		try {
			SQLQuery sqlQuery = session.createSQLQuery("SELECT * FROM note_plan_flight nf WHERE nf.`note_id`=:noteId ORDER BY nf.`id` DESC").addEntity(NotePlanFlight.class);
			sqlQuery.setParameter("noteId", noteId);
			notePlanFlights = sqlQuery.list();
			return notePlanFlights;
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return null;
	}
	public void editNoteInfo(NotePlanInfo obj, List<NoteFiles> list) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = getSessionFactory().openSession();
			transaction = session.beginTransaction();
			if (obj!=null&&obj.getNoteId()!=0) {
				String sql ="UPDATE note_plan_info SET letter_unit =:letterUnit,  person_name =:personName,"
						+ "tel_no =:telNo,  mission =:mission,  nationality =:nationality,  reg_no =:regNo,"
						+ "call_number =:callNumber,  model =:model,  person_number =:personNumber,"
						+ "operator =:operator,other =:other,  air_number =:airNumber, note_no =:noteNo "
						+ "WHERE note_id =:noteId";
				SQLQuery query = session.createSQLQuery(sql);
				query.setParameter("letterUnit", obj.getLetterUnit());
				query.setParameter("personName", obj.getPersonName());
				query.setParameter("telNo", obj.getTelNo());
				query.setParameter("mission", obj.getMission());
				query.setParameter("nationality", obj.getNationality());
				query.setParameter("regNo", obj.getRegNo());
				query.setParameter("callNumber", obj.getCallNumber());
				query.setParameter("model", obj.getModel());
				query.setParameter("personNumber", obj.getPersonNumber());
				query.setParameter("operator", obj.getOperator());
				query.setParameter("other", obj.getOther());
				query.setParameter("airNumber", obj.getAirNumber());
				query.setParameter("noteNo", obj.getNoteNo());
				query.setParameter("noteId", obj.getNoteId());
				query.executeUpdate();
				//需要在日志中体现出来
				logger.info("照会信息修改!");
			}
			for(int i=0;i<list.size();i++){
				NoteFiles noteFiles = list.get(i);
				noteFiles.setNoteId(obj.getNoteId());
				session.save(noteFiles);
			}
			transaction.commit();
			session.flush();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
			logger.error(e.getMessage());
		}finally{
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
		
	}
	public void editNoteFile(NoteFiles obj) {
		// TODO Auto-generated method stub
		
	}

}
