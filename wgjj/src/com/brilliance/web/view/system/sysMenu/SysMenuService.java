package com.brilliance.web.view.system.sysMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brilliance.base.common.PagerVO;
import com.brilliance.base.model.SysMenu;
import com.brilliance.base.model.SysPms;

/**
 * 菜单管理
 * @ClassName: SysMenuService
 * @Description:
 * @author
 * @date 2018年8月27日 下午1:34:11
 */
@Service
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
@SuppressWarnings("unchecked")
public class SysMenuService {

	@Autowired
	private SysMenuDao menuDao;

	/**
	 * 获取所有的菜单列表
	 * @return          菜单列表
	 */
	public List<SysMenu> findMenuList() {
		List<SysMenu> list = menuDao.findList("from SysMenu m where m.enableFlag='Y' order by m.sx");
		return reloadMenu(list);
	}

	/**
	 * 根据角色获取对应的菜单列表
	 * @return          菜单列表
	 */
	public List<SysMenu> findMenuListByRoleId(String roleId) {
		List<SysMenu> list = menuDao.findList(
				"from SysMenu m where m.enableFlag='Y' and m.menuId in (select um.menuId from SysPms um where um.validSts='1' and um.pmsId in (select up.pmsId from SysRolePms up where up.roleId=?)) order by m.sx",
				new Object[] { roleId });
		// 读取父集菜单
		List<Integer> parentIds = new ArrayList<Integer>();
		for (SysMenu menu : list) {
			parentIds.add(menu.getParentId());
		}

		if (parentIds.size() > 0) {
			Map<String, Object> params = new HashMap<String, Object>(16);
			params.put("menuId", parentIds);
			list.addAll(menuDao.findList("from SysMenu o where o.enableFlag='Y' and o.menuId in (:menuId) order by o.sx", params));
		}
		return reloadMenu(list);
	}

	private static List<SysMenu> reloadMenu(List<SysMenu> list) {
		List<SysMenu> newList = new ArrayList<SysMenu>();
		for (SysMenu item : list) {
			if (item.getParentId().intValue() == -1) {
				newList.add(item);
			}
		}
		list.removeAll(newList);
		// 加载子集菜单
		if (list.size() > 0) {
			reloadMenuChild(newList, list);
		}
		return newList;
	}

	private static void reloadMenuChild(List<SysMenu> newList, List<SysMenu> list) {
		for (SysMenu menu : newList) {
			List<SysMenu> childs = new ArrayList<SysMenu>();
			for (SysMenu child : list) {
				if (child.getParentId().intValue() == menu.getMenuId().intValue()) {
					childs.add(child);
				}
			}
			menu.setChildMenus(childs);
			list.removeAll(childs);
			if (list.size() > 0 && childs.size() > 0) {
				reloadMenuChild(childs, list);
			}
		}
	}

	/**
	 * 根据id获取子集菜单
	 * 描述
	 * @Title: findChildById
	 * @author
	 * @Modified By 钟志峰
	 * @param menuId
	 * @return
	 * List<SysMenu> 返回类型
	 * @throws
	 */
	public List<SysMenu> findChildById(String menuId) {
		String hql = "from SysMenu o where o.enableFlag=? and o.parentId=?";
		List<SysMenu> list = menuDao.findList(hql, new Object[] { "Y", Integer.parseInt(menuId) });
		return list;
	}

	public List<String> findListByRoleId(String roleId) {
		List<String> list = menuDao.findList("select o.pmsId from SysRolePms o where o.roleId=?", new Object[] { roleId });
		return list;
	}

	// 根据角色的权限列出所有权限树
	public List<MenuTreeNode> findAllChild(List<String> pmsIds) {
		String hql = "from SysMenu o where o.enableFlag=? and o.menuId in (select um.menuId from SysPms um where um.validSts='1') order by o.sx";
		List<Object> params = new ArrayList<Object>();
		params.add("Y");
		List<SysMenu> list = menuDao.findList(hql, params.toArray());
		// 获取所有的权限 按菜单分组
		List<SysPms> pmsList = menuDao.findList("from SysPms sp where sp.validSts='1'");

		Map<Integer, List<SysPms>> pmsMap = new HashMap<Integer, List<SysPms>>(16);
		for (SysPms pms : pmsList) {
			if (pmsMap.containsKey(pms.getMenuId())) {
				pmsMap.get(pms.getMenuId()).add(pms);
			} else {
				List<SysPms> itemList = new ArrayList<SysPms>();
				itemList.add(pms);
				pmsMap.put(pms.getMenuId(), itemList);
			}
		}

		List<MenuTreeNode> treeList = new ArrayList<MenuTreeNode>();
		Map<String, List<MenuTreeNode>> map = new HashMap<String, List<MenuTreeNode>>(16);
		for (int i = 0; i < list.size(); i++) {
			SysMenu menu = list.get(i);
			MenuTreeNode menuTree = new MenuTreeNode();
			menuTree.setId(menu.getMenuId() + "");
			menuTree.setName(menu.getMenuName());
			menuTree.setParentId(menu.getParentId() + "");

			pmsList = pmsMap.get(menu.getMenuId());
			List<MenuTreeNode> childrenList = new ArrayList<MenuTreeNode>();
			for (SysPms pms : pmsList) {
				MenuTreeNode pmsChild = new MenuTreeNode();
				pmsChild.setId(pms.getPmsId());
				pmsChild.setName(pms.getPmsName());
				pmsChild.setParentId(menu.getMenuId() + "");
				pmsChild.setIsParent(false);
				if (pmsIds.contains(pmsChild.getId())) {
					pmsChild.setChecked(true);
				}
				childrenList.add(pmsChild);
			}
			menuTree.setIsParent(childrenList.size() > 0);
			menuTree.setChildren(childrenList);
			// 获取父菜单
			MenuTreeNode parentNode = findParent(menu.getParentId());

			if (map.containsKey(menuTree.getParentId())) {
				map.get(menuTree.getParentId()).add(menuTree);
			} else {
				treeList.add(parentNode);
				List<MenuTreeNode> childList = new ArrayList<MenuTreeNode>();
				childList.add(menuTree);
				map.put(menuTree.getParentId(), childList);
			}
		}
		for (MenuTreeNode item : treeList) {
			item.setChildren(map.get(item.getId()));
		}
		treeSort(treeList);
		return treeList;
	}

	private void treeSort(List<MenuTreeNode> treeNodeList) {
		Collections.sort(treeNodeList, new Comparator<MenuTreeNode>() {
			@Override
			public int compare(MenuTreeNode o1, MenuTreeNode o2) {
				return o1.getSx() - o2.getSx();
			}
		});
	}

	/**
	 *
	 * 描述
	 * @Title: findParent
	 * @author
	 * @Modified By 钟志峰
	 * @param parentId
	 * @return
	 * MenuTreeNode 返回类型
	 * @throws
	 */
	public MenuTreeNode findParent(Integer parentId) {
		SysMenu menu = (SysMenu) menuDao.findById(SysMenu.class, parentId);
		MenuTreeNode menuTree = new MenuTreeNode();
		menuTree.setId(menu.getMenuId() + "");
		menuTree.setName(menu.getMenuName());
		menuTree.setParentId(menu.getParentId() + "");
		menuTree.setIsParent(true);
		menuTree.setSx(menu.getSx());
		return menuTree;
	}

	/**
	 * 获取父菜单及子菜单列表
	 * 1.查询父菜单，2、根据父菜单id查询子菜单
	 * @return
	 */
	public JSONArray findMenuByPC() {
		String hql = "from SysMenu o where o.parentId=? order by o.sx";
		List<Object> p = new ArrayList<Object>();
		p.add(-1);
		List<SysMenu> sysMenuList = menuDao.findList(hql,p.toArray());
		JSONArray array = new JSONArray();
		sysMenuList.forEach(menu -> {
			JSONObject object = new JSONObject();
			object.put("id",menu.getMenuId());
			object.put("name",menu.getMenuName());
			List<Object> params = new ArrayList<Object>();
			params.add(menu.getMenuId());
			List<SysMenu> menuList = menuDao.findList(hql,params.toArray());
			JSONArray treeList = new JSONArray();
			menuList.forEach(sysMenu -> {
				JSONObject treeNode = new JSONObject();
				treeNode.put("id",sysMenu.getMenuId());
				treeNode.put("name",sysMenu.getMenuName());
				treeList.add(treeNode);
			});
			object.put("children",treeList);
			array.add(object);
		});
		return array;
	}
	public List<String> findPmsList() {
		List<String> list = menuDao.findList("select pmsId from SysPms sp where sp.validSts='1'");
		return list;
	}
	public List<SysMenu> findList(){
		StringBuffer hql = new StringBuffer("from SysMenu sp where sp.parentId !=-1 order by sp.menuId");
		return menuDao.findList(hql.toString());
	}
	public PagerVO findList(SysMenu sysMenu, int curPage, int pageSize) throws Exception {
		StringBuffer hql = new StringBuffer("from SysMenu sp where sp.parentId !=-1");
		List<Object> params = new ArrayList<Object>();
		hql.append("order by menuId asc");
		return menuDao.findPaginated(hql.toString(), params.toArray(), curPage, pageSize);
	}
}
