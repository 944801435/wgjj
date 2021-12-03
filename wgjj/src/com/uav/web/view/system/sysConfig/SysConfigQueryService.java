package com.uav.web.view.system.sysConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uav.base.common.PagerVO;
import com.uav.base.model.SysConfig;

/**
 * 系统配置参数
 * @ClassName: SysConfigQueryService
 * @Description: 
 * @author 
 * @date 2018年8月27日 下午1:52:41
 */
@Service
@Transactional(rollbackFor={RuntimeException.class,Exception.class})
@SuppressWarnings("unchecked")
public class SysConfigQueryService {

	@Autowired
	private SysConfigQueryDao sysConfigQueryDao;
	
	/**
	 * @throws Exception 
	 * 
	 * 描述
	 * @Title: findList 
	 * @author 
	 * @Modified By 钟志峰
	 * @Modified Date:  2018年8月27日 下午1:52:55                                    
	 * @Why & What is modified  <修改分页，减少代码冗余>    
	 * @param curPage
	 * @param pageSize
	 * @return    
	 * List<SysConfig> 返回类型 
	 * @throws
	 */
	public PagerVO findList(int curPage, int pageSize) throws Exception{
		StringBuffer hql = new StringBuffer("from SysConfig o order by o.cfgId asc");
		return sysConfigQueryDao.findPaginated(hql.toString(), new Object[]{}, curPage, pageSize);
	}
	
	/**
	 * 
	 * 描述 根据ID 查询
	 * @Title:  findById 
	 * @author 
	 * @Modified By 钟志峰
	 * @param cfgId
	 * @return    
	 * SysConfig 返回类型 
	 * @throws
	 */
	public SysConfig findById(String cfgId) {
		return (SysConfig)sysConfigQueryDao.findById(SysConfig.class, cfgId);
	}
	
	/**
	 * 
	 * 描述 修改
	 * @Title: edit 
	 * @author 
	 * @Modified By 钟志峰
	 * @param sysConfig    
	 * void 返回类型 
	 * @throws
	 */
	public void edit(SysConfig sysConfig) {
		sysConfigQueryDao.update(sysConfig);
	}
	
}
