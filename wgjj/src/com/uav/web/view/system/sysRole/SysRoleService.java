package com.uav.web.view.system.sysRole;

import java.util.*;

import com.uav.base.common.Constants;
import com.uav.base.common.PagerVO;
import com.uav.base.model.SysRole;
import com.uav.base.model.SysRolePms;
import com.uav.base.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SysRoleService {

	@Autowired
	private SysRoleDao sysRoleDao;

	public PagerVO findList(SysRole sysRole, int curPage, int pageSize) throws Exception {
		StringBuffer hql = new StringBuffer("from SysRole o where 1=1");
		List<Object> params = new ArrayList<Object>();
		hql.append("order by roleName desc");
		return sysRoleDao.findPaginated(hql.toString(), params.toArray(), curPage, pageSize);
	}
	public List<SysRole> findList(){
		StringBuffer hql = new StringBuffer("from SysRole order by roleName");
		return sysRoleDao.findList(hql.toString());
	}
	public SysRole findRoleById(String roleId){
		return (SysRole) sysRoleDao.findById(SysRole.class, roleId);
	}

	/**
	 * 添加角色
	 * @param sysRole
	 * @return
	 */
	public String save(SysRole sysRole, String[] pmsId) {
		int count = sysRoleDao.findCount("select count(*) from SysRole o where o.roleName=?",
				new Object[] { sysRole.getRoleName()});
		if (count > 0) {
			return "该角色名称已经存在！";
		}
		sysRoleDao.save(sysRole);
		if (pmsId != null) {
			// 筛选出是权限Id的列表
			Map<String, Object> params = new HashMap<String, Object>(16);
			params.put("pmsId", pmsId);
			List<String> pmsIds = sysRoleDao.findList("select pmsId from SysPms where pmsId in (:pmsId) and validSts='1'", params);
			for (String item : pmsIds) {
				SysRolePms rolePms = new SysRolePms();
				rolePms.setRoleId(sysRole.getRoleId());
				rolePms.setPmsId(item);
				sysRoleDao.save(rolePms);
			}
		}
		return "";
	}

	/**
	 * 修改角色
	 * @param sysRole
	 * @return
	 */
	public String update(SysRole sysRole, String[] pmsId) {
		int count = sysRoleDao.findCount("select count(*) from SysRole o where o.roleName=? and o.roleId!=?",
				new Object[] { sysRole.getRoleName(), sysRole.getRoleId() });
		if (count > 0) {
			return "该角色名称已经存在！";
		}
		sysRoleDao.executeHql("update SysRole o set o.roleName=? where o.roleId=?",
				new Object[] { sysRole.getRoleName(), sysRole.getRoleId() });
		if (pmsId != null) {
			// 筛选出是权限Id的列表
			Map<String, Object> params = new HashMap<String, Object>(16);
			params.put("pmsId", pmsId);
			List<String> newPmsIdList = sysRoleDao.findList("select pmsId from SysPms where pmsId in (:pmsId) and validSts='1'", params);

			// 获取该用户的权限
			List<String> rolePmsList = sysRoleDao.findList("select pmsId from SysRolePms where roleId=?", new Object[] { sysRole.getRoleId() });

			// 看现在授权的权限是否和原有的权限一样，
			if (!arrayIsEquals(newPmsIdList.toArray(new String[] {}), rolePmsList.toArray(new String[] {}))) {
				// 移除对应的权限
				sysRoleDao.executeHql("delete from SysRolePms um where um.roleId=?", new Object[] { sysRole.getRoleId() });
				for (String item : newPmsIdList) {
					SysRolePms rolePms = new SysRolePms();
					rolePms.setRoleId(sysRole.getRoleId());
					rolePms.setPmsId(item);
					sysRoleDao.save(rolePms);
				}
			}
		}
		return "";
	}
	// 判断两数组是否相同
	private static boolean arrayIsEquals(String[] a, String[] b) {
		Arrays.sort(a);
		Arrays.sort(b);
		return Arrays.equals(a, b);
	}
	/**
	 * 删除角色
	 * @param roleIds
	 */
	public void delete(String[] roleIds) {
		for (String id : roleIds) {
			sysRoleDao.executeHql("delete from SysRolePms where roleId=?", new Object[] { id });
			sysRoleDao.executeHql("delete from SysRole where roleId=?", new Object[] { id });
		}
	}
}
