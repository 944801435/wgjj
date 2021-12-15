package com.uav.web.view.system.sysOptLog;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uav.base.common.PagerVO;
import com.uav.base.model.SysOptLog;
import com.uav.base.model.SysUser;

/**
 * 系统操作日志
 * @ClassName: SysOptLogService
 * @Description: 
 * @author 
 * @date 2018年8月27日 下午2:01:57
 */
@Service
@Transactional(rollbackFor={RuntimeException.class,Exception.class})
@SuppressWarnings("unchecked")
public class SysOptLogService {
	
	private final static String  ADMIN_DEPT_ID = "0000000000";

	@Autowired
	private SysOptLogDao sysOptLogDao;
	
	/**
	 * @throws Exception 
	 * 查询
	 * 描述
	 * @Title: findList 
	 * @author 
	 * @Modified By 钟志峰
	 * @Modified Date:  2018年8月27日 下午2:02:05                                    
	 * @Why & What is modified  <修改分页，减少冗余>    
	 * @param sysOptLog 查询参数
	 * @param curDeptId 部门ID
	 * @param curPage 当前页面
	 * @param pageSize 一页显示的条数
	 * @return    
	 * PagerVO 返回类型 
	 * @throws
	 */
	public PagerVO findList(SysOptLog sysOptLog,String curDeptId, int curPage,int pageSize) throws Exception{
		
		List<Object> params = new ArrayList<Object>();
		
		StringBuffer hql = new StringBuffer("select logSeq from SysOptLog o where 1 = 1 ");
		
		if(curDeptId != null && !ADMIN_DEPT_ID.equals(curDeptId)) {
			hql = new StringBuffer("select logSeq from SysOptLog o where o.deptId=?");
			params.add(curDeptId);
		}
		if(!StringUtils.isBlank(sysOptLog.getBeginTime())){
			hql.append(" and o.optTime>=?");
			params.add(sysOptLog.getBeginTime());
		}
		if(!StringUtils.isBlank(sysOptLog.getEndTime())){
			hql.append(" and o.optTime<=?");
			params.add(sysOptLog.getEndTime());
		}
		if(!StringUtils.isBlank(sysOptLog.getUserName())){
			hql.append(" and o.userId in (select s.userId from SysUser s where s.userName like ?) ");
			params.add("%"+sysOptLog.getUserName()+"%");
		}
		hql.append(" order by o.optTime desc");
		PagerVO vo = sysOptLogDao.findPaginated(hql.toString(), params.toArray(), curPage, pageSize);
		List<Object> datas = new ArrayList<Object>();
		for(Object o : vo.getItems()){
			Integer logSeq = (Integer) o;
			datas.add(getSysOptLogById(logSeq));
		}
		vo.setItems(datas);
		return vo;
	}
	
	public SysOptLog getSysOptLogById(Integer logSeq){
		return (SysOptLog) sysOptLogDao.findById(SysOptLog.class, logSeq);
	}
	
	/**
	 *  查询用户
	 * 描述
	 * @Title: findUserById 
	 * @author 
	 * @Modified By 钟志峰
	 * @param usrId 用户ID
	 * @return    
	 * SysUser 返回类型 
	 * @throws
	 */
	public SysUser findUserById(String usrId) {
		return (SysUser) sysOptLogDao.findById(SysUser.class, usrId);
	}
	
	/**
	 * 新增日志
	 * 描述
	 * @Title: addLog 
	 * @author 
	 * @Modified By 钟志峰
	 * @param sysOptLog  实体
	 * void 返回类型  
	 * @throws
	 */
	public void addLog(SysOptLog sysOptLog){
		sysOptLogDao.save(sysOptLog);
	}
	
}
