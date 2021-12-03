package com.uav.web.view.system.sysUser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uav.base.common.Constants;
import com.uav.base.common.PagerVO;
import com.uav.base.model.SysDept;
import com.uav.base.model.SysUser;

/**
 * 用户管理
 * @ClassName: SysUserService
 * @Description: 
 * @author 
 * @date 2018年8月27日 下午2:10:39
 */
@Service
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
@SuppressWarnings("unchecked")
public class SysUserService {

	@Autowired
	private SysUserDao userDao;

	/**
	 * @throws Exception 查询用户列表
	 * 
	 * @Title: findList 
	 * @author 
	 * @Modified By 钟志峰
	 * @Modified Date:  2018年8月27日 下午2:10:46                                    
	 * @Why & What is modified  <修改分页，减少冗余>    
	 * @param sysUser 查询参数，实体
	 * @param curPage 当前页面
	 * @param pageSize 一页显示的条数
	 * @return    
	 * List<SysUser> 返回类型 
	 * @throws
	 */
	public PagerVO findList(SysUser sysUser, int curPage, int pageSize) throws Exception {
		// 判断usrId是不是0000000000
		StringBuffer hql = new StringBuffer("from SysUser o where o.userSts=? and o.userId<>? ");
		List<Object> params = new ArrayList<Object>();
		params.add(Constants.sys_default_yes);
		params.add(sysUser.getUserId());
		if (Constants.SYS_USER_ID.equals(sysUser.getUserId())) {
			// 当前用户是admin，可以查询出本机构下的用户也可以查出所有的机构的管理员
			hql.append(" and ((o.deptId=? and o.isAdmin=?) or (o.deptId<>? and o.isAdmin=?)) ");
			params.add(sysUser.getDeptId());
			params.add(Constants.sys_default_no);
			params.add(sysUser.getDeptId());
			params.add(Constants.sys_default_yes);
		} else {
			// 其他用户登录的话，则可以查出本机构下不是管理员的用户
			hql.append(" and (o.deptId=? and o.isAdmin=?)");
			params.add(sysUser.getDeptId());
			params.add(Constants.sys_default_no);
		}
		hql.append(" order by o.userId desc");
		return userDao.findPaginated(hql.toString(), params.toArray(), curPage, pageSize);
	}

	/**
	 * 
	 * 描述 查询用户根据用户名和密码,部门
	 * @Title: findUserByUserNamePwd 
	 * @author 
	 * @Modified By 钟志峰
	 * @param username 用户名称
	 * @param pwd 用户密码
	 * @param deptId 部门ID
	 * @return    
	 * SysUser 返回类型 
	 * @throws
	 */
	public SysUser findUserByUserNamePwd(String username, String pwd, String deptId) {
		String hql = "from SysUser o where o.userName=? and o.userPwd=? and o.userSts=? and o.deptId=?";
		SysUser authuser = (SysUser) userDao.findUnique(hql, new Object[] { username, pwd, Constants.sys_default_yes, deptId });
		return authuser;
	}

	/**
	 * 
	 * 描述 查询用户根据用户名和密码
	 * @Title: findUserByUserNamePwd 
	 * @author 
	 * @Modified By 钟志峰
	 * @param username 用户名称
	 * @param pwd 用户名密码
	 * @return    
	 * SysUser 返回类型 
	 * @throws
	 */
	public SysUser findUserByUserNamePwd(String username, String pwd) {
		String hql = "from SysUser o where o.userName=? and o.userPwd=? and o.userSts=?";
		SysUser authuser = (SysUser) userDao.findUnique(hql, new Object[] { username, pwd, Constants.sys_default_yes });
		return authuser;
	}

	/**
	 * 查询用户
	 * 描述
	 * @Title: findUserById 
	 * @author 
	 * @Modified By 钟志峰
	 * @param usrId 用户id
	 * @return    
	 * SysUser 返回类型 
	 * @throws
	 */
	public SysUser findUserById(String usrId) {
		return (SysUser) userDao.findById(SysUser.class, usrId);
	}

	/**
	 * 查询机构
	 * 描述
	 * @Title: findDeptById 
	 * @author 
	 * @Modified By 钟志峰
	 * @param deptId 机构ID
	 * @return    
	 * SysDept 返回类型 
	 * @throws
	 */
	public SysDept findDeptById(String deptId) {
		return (SysDept) userDao.findById(SysDept.class, deptId);
	}

	/**
	 * 
	 * 描述 添加新增用户
	 * @Title:  save 
	 * @author 
	 * @Modified By 钟志峰
	 * @param sysUser 用户
	 * @param pmsId 权限
	 * @return    
	 * String 返回类型 
	 * @throws
	 */
	public String save(SysUser sysUser) {
		int count = userDao.findCount("select count(*) from SysUser o where o.userName=? and o.userSts=?",
				new Object[] { sysUser.getUserName(), Constants.sys_default_yes });
		if (count > 0) {
			return "该用户名称已经存在！";
		}
		userDao.save(sysUser);
		return "";
	}

	/**
	 * 
	 * 描述 修改用户
	 * @Title: update 
	 * @author 
	 * @Modified By 钟志峰
	 * @param sysUser 
	 * @param pmsId
	 * @return    
	 * String 返回类型 
	 * @throws
	 */
	public String update(SysUser sysUser) {
		int count = userDao.findCount("select count(*) from SysUser o where o.userName=? and o.userSts=? and o.userId!=?",
				new Object[] { sysUser.getUserName(), Constants.sys_default_yes, sysUser.getUserId() });
		if (count > 0) {
			return "该用户名称已经存在！";
		}
		if (StringUtils.isBlank(sysUser.getUserPwd())) {
			userDao.executeHql("update SysUser o set o.userName=?,o.userDesc=?,o.phone=?,o.roleId=? where o.userId=?",
					new Object[] { sysUser.getUserName(), sysUser.getUserDesc(), sysUser.getPhone(), sysUser.getRoleId(), sysUser.getUserId() });
		} else {
			userDao.executeHql("update SysUser o set o.userName=?,o.userDesc=?,o.phone=?,o.userPwd=?,o.roleId=? where o.userId=?",
					new Object[] { sysUser.getUserName(), sysUser.getUserDesc(), sysUser.getPhone(), sysUser.getUserPwd(), sysUser.getRoleId(),
							sysUser.getUserId() });
		}
		return "";
	}

	/**
	 * 修改密码
	 * 描述
	 * @Title: modifyPwd 
	 * @author 
	 * @Modified By 钟志峰
	 * @param usrId
	 * @param userPwd    
	 * void 返回类型 
	 * @throws
	 */
	public void modifyPwd(String usrId, String userPwd) {
		userDao.executeHql("update SysUser set userPwd=? where userId=?", new Object[] { userPwd, usrId });
	}

	/**
	 * 删除用户
	 * 描述
	 * @Title: delete 
	 * @author 
	 * @Modified By 钟志峰
	 * @param userIds    
	 * void 返回类型 
	 * @throws
	 */
	public void delete(String[] userIds) {
		for (String userId : userIds) {
			userDao.executeHql("update SysUser set userSts=? where userId=?", new Object[] { Constants.sys_default_no, userId });
		}
	}

	/**
	 * 查询用户列表
	 * 描述
	 * @Title: findDeptList 
	 * @author 
	 * @Modified By 钟志峰
	 * @return    
	 * List<SysDept> 返回类型 
	 * @throws
	 */
	public List<SysDept> findDeptList() {
		StringBuffer hql = new StringBuffer("from SysDept o where o.deptSts=?");
		List<Object> params = new ArrayList<Object>();
		params.add(Constants.USE_STS_ON);
		return userDao.findList(hql.toString(), params.toArray());
	}

	/**
	 * 获取用户常用菜单
	 */
	public Integer[] findUserMenuId(String userId) {
		List<Object> list = userDao.findList_Sql("SELECT MENU_ID FROM SYS_USER_MENU WHERE USER_ID=?", new Object[] { userId });
		return list.toArray(new Integer[list.size()]);
	}

	/**
	 * 保存用户常用菜单
	 */
	public void saveUserMenuId(String userId, String[] menuId) {
		// 移除用户下所有的常用菜单
		userDao.executeSql("DELETE FROM SYS_USER_MENU WHERE USER_ID=?", new Object[] { userId });
		// 插入常用菜单
		StringBuffer hql = new StringBuffer("INSERT INTO SYS_USER_MENU (MENU_ID,USER_ID) VALUES");

		if (menuId.length > 0 && !"".equals(menuId[0])) {
			for (int i = 0; i < menuId.length; i++) {
				if (i > 0) {
					hql.append(",");
				}
				hql.append("(" + menuId[i] + ",'" + userId + "')");
			}
			userDao.executeSql(hql.toString(), null);
		}
	}

}
