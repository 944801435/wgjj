package com.brilliance.web.view.violation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brilliance.base.common.Constants;
import com.brilliance.base.common.PagerVO;
import com.brilliance.base.model.Violation;

import java.util.*;


@Service
@Transactional(rollbackFor = Exception.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ViolationService {

	@Autowired
	private ViolationDao violationDao;

	public PagerVO findList(Violation violation, int curPage, int pageSize) throws Exception {
		StringBuffer hql = new StringBuffer("from Violation o ");
		List<Object> params = new ArrayList<Object>();
		hql.append("order by vioId desc");
		return violationDao.findPaginated(hql.toString(), params.toArray(), curPage, pageSize);
	}
	public List<Violation> findList(){
		StringBuffer hql = new StringBuffer("from Violation order by vioId desc");
		return violationDao.findList(hql.toString());
	}
	public Violation findViolationById(String vioId){
		return (Violation) violationDao.findById(Violation.class, vioId);
	}

	/**
	 * 添加违规信息
	 * @param violation
	 * @return
	 */
	public String save(Violation violation) {
		violation.setValidSts("1");
		violationDao.save(violation);
		return "";
	}

	/**
	 * 修改违规信息
	 * @param violation
	 * @return
	 */
	public String update(Violation violation) {
		violationDao.executeHql("update Violation o set o.model=? , o.acid=? , o.info=? , o.createTime=? , o.nationality=? , o.validSts=? where o.vioId=?",
				new Object[] { violation.getModel(),violation.getAcid(),violation.getInfo(),violation.getCreateTime(),violation.getNationality(), violation.getValidSts(),violation.getVioId() });
		return "";
	}
	/**
	 * 删除违规信息
	 * @param vioIds
	 */
	public void delete(String[] vioIds) {
		for (String id : vioIds) {
			violationDao.executeHql("delete from Violation where vioId=?", new Object[] { id });
		}
	}
}
