package com.brilliance.web.view.unitDict;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brilliance.base.common.Constants;
import com.brilliance.base.common.PagerVO;
import com.brilliance.base.model.UnitDict;

@Service
@Transactional(rollbackFor = Exception.class)
@SuppressWarnings({ "unchecked" })
public class UnitDictService {

	@Autowired
	private UnitDictDao unitDictDao;

	public PagerVO findList(UnitDict unitDict, int curPage, int pageSize) throws Exception {
		StringBuffer hql = new StringBuffer("from UnitDict o ");
		List<Object> params = new ArrayList<Object>();
		hql.append("order by dictId desc");
		return unitDictDao.findPaginated(hql.toString(), params.toArray(), curPage, pageSize);
	}

	public List<UnitDict> findList() {
		StringBuffer hql = new StringBuffer("from UnitDict order by dictId desc");
		return unitDictDao.findList(hql.toString());
	}

	public UnitDict findUnitDictById(String dictId) {
		return (UnitDict) unitDictDao.findById(UnitDict.class, dictId);
	}

	/**
	 * 添加违规信息
	 * @param unitDict
	 * @return
	 */
	public String save(UnitDict unitDict) {
		unitDict.setValidSts("1");
		unitDictDao.save(unitDict);
		return "";
	}

	/**
	 * 修改违规信息
	 * @param unitDict
	 * @return
	 */
	public String update(UnitDict unitDict) {
		unitDictDao.executeHql("update UnitDict o set o.unitName=? , o.validSts=? where o.dictId=?",
				new Object[] { unitDict.getUnitName(), unitDict.getValidSts(), unitDict.getDictId() });
		return "";
	}

	/**
	 * 删除违规信息
	 * @param dictIds
	 */
	public void delete(String[] dictIds) {
		for (String id : dictIds) {
			unitDictDao.executeHql("delete from UnitDict where dictId=?", new Object[] { id });
		}
	}

	/**
	 * 查询使用中的通知单位
	 * 
	 * @Title: getUsedList
	 * @author mq
	 * @date 2021年11月15日 下午5:32:49
	 * @return
	 */
	public List<UnitDict> getUsedList() {
		return unitDictDao.findList("from UnitDict where validSts=?", new Object[] { Constants.sys_default_yes });
	}
}
