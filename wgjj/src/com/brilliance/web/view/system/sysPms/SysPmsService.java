package com.brilliance.web.view.system.sysPms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brilliance.base.common.Constants;
import com.brilliance.base.common.PagerVO;
import com.brilliance.base.model.SysPms;
import com.brilliance.base.model.SysRole;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SysPmsService {

	@Autowired
	private SysPmsDao sysPmsDao;

	public PagerVO findList(SysPms sysPms, int curPage, int pageSize) throws Exception {
		StringBuffer hql = new StringBuffer("from SysPms o where o.validSts='1'");
		List<Object> params = new ArrayList<Object>();
        hql.append(" and o.menuId = ?");
        params.add(sysPms.getMenuId());
		hql.append(" order by pmsName desc");
		return sysPmsDao.findPaginated(hql.toString(), params.toArray(), curPage, pageSize);
	}
	public List<SysPms> findList() {
		return sysPmsDao.findList("from SysPms where validSts='1' order by pmsName");
	}
	public SysPms findPmsById(String roleId){
		return (SysPms) sysPmsDao.findById(SysPms.class, roleId);
	}
	/**
     * 添加操作权限
	 * @param sysPms
     * @return
     */
	public String save(SysPms sysPms) {
		int count = sysPmsDao.findCount("select count(*) from SysPms o where o.pmsName=? and o.validSts='1'",
				new Object[] { sysPms.getPmsName()});
		if (count > 0) {
			return "该操作权限名称已经存在！";
		}
		sysPms.setValidSts("1");
		sysPms.setIsSys("0");
		sysPmsDao.save(sysPms);
		return "";
	}

    /**
     * 修改操作权限
	 * @param sysPms
     * @return
     */
	public String update(SysPms sysPms) {
		int count = sysPmsDao.findCount("select count(*) from SysPms o where o.pmsName=? and o.pmsId!=? ",
				new Object[] { sysPms.getPmsName(), sysPms.getPmsId() });
		if (count > 0) {
			return "该操作权限名称已经存在！";
		}
		sysPmsDao.executeHql("update SysPms o set o.pmsName=? , o.menuId=? where o.isSys = '0' and o.pmsId=?",
				new Object[] { sysPms.getPmsName(), sysPms.getMenuId(), sysPms.getPmsId() });
		return "";
	}

    /**
     * 删除操作权限
	 * @param pmsIds
     */
	public void delete(String[] pmsIds) {
		for (String id : pmsIds) {
			sysPmsDao.executeHql("update SysPms set validSts=? where isSys = '0' and pmsId=?", new Object[] { Constants.sys_default_no, id });
		}
	}
}
