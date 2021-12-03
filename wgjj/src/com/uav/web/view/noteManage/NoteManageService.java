package com.uav.web.view.noteManage;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uav.base.common.Constants;
import com.uav.base.common.PagerVO;
import com.uav.base.model.SysOptLog;
import com.uav.base.model.SysUser;
import com.uav.base.model.internetModel.NoteCivilMessage;
import com.uav.base.model.internetModel.NotePlanInfo;

/**
 * 照会文书管理
 * @ClassName: NoteManageService
 * @Description: 
 * @author 
 * @date 
 */
@Service
@Transactional(rollbackFor={RuntimeException.class,Exception.class})
@SuppressWarnings("unchecked")
public class NoteManageService {
	
	@Autowired
	private NoteManageDao noteManageDao;
	/**
	 * @throws Exception 
	 * 查询
	 * 描述
	 * @Title: findList 
	 * @author 
	 * @param planInfo 查询参数
	 * @param curPage 当前页面
	 * @param pageSize 一页显示的条数
	 * @return    
	 * PagerVO 返回类型 
	 * @throws
	 */
	public PagerVO findList(NotePlanInfo planInfo, Integer curPage, int pageSize) throws Exception{
		
		List<Object> params = new ArrayList<Object>();
		
		StringBuffer hql = new StringBuffer("select DISTINCT note.noteId from NotePlanInfo note , NoteCivilMessage ncm WHERE 1=1 ");
		
		if(!StringUtils.isBlank(planInfo.getBeginTime())){
			hql.append(" and note.createTime>=?");
			params.add(planInfo.getBeginTime());
		}
		if(!StringUtils.isBlank(planInfo.getEndTime())){
			hql.append(" and note.createTime<=?");
			params.add(planInfo.getEndTime());
		}
		if(!StringUtils.isBlank(planInfo.getPermitNumber())){
			hql.append(" and note.noteId=ncm.noteId and ncm.permitNumber = ? ");
			params.add(planInfo.getPermitNumber());
		}
		if(!StringUtils.isBlank(planInfo.getNoteNo())){
			hql.append(" and note.noteNo like ? ");
			params.add("%"+planInfo.getNoteNo()+"%");
		}
		hql.append(" order by note.createTime desc");
		PagerVO vo = noteManageDao.findPaginated(hql.toString(), params.toArray(), curPage, pageSize);
		List<Object> datas = new ArrayList<Object>();
		for(Object o : vo.getDatas()){
			Integer noteId = (Integer) o;
			datas.add(getNoteManageById(noteId));
		}
		vo.setDatas(datas);
		return vo;
	}
	/**
	 * 获取信息
	 * */
	public NotePlanInfo getNoteManageById(Integer noteId){
		return (NotePlanInfo) noteManageDao.findById(NotePlanInfo.class, noteId);
	}
	public NoteCivilMessage findCivilMessageById(int noteId) {
		return (NoteCivilMessage) noteManageDao.findById(NoteCivilMessage.class, noteId);
	}
	/**
	 * 
	 * 描述 查询民航最新的回复信息
	 * @Title: findNoteCivilMessageByCreateTime 
	 * @author 
	 * @return    
	 * NoteCivilMessage 返回类型 
	 * @throws
	 */
	public NoteCivilMessage findNoteCivilMessageByCreateTime(Integer noteId) {
		String sql = "from NoteCivilMessage nc where nc.createTime=(select MAX(createTime) from NoteCivilMessage) AND nc.noteId=? ";
//		String sql = "SELECT nc.* FROM note_civil_message nc WHERE nc.create_time=(SELECT MAX(create_time) FROM note_civil_message) AND nc.note_id=? ";
		NoteCivilMessage civilMessage = (NoteCivilMessage) noteManageDao.findUnique(sql,  new Object[] {noteId});
		return civilMessage;
	}
}
