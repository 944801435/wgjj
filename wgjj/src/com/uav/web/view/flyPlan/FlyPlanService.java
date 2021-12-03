package com.uav.web.view.flyPlan;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uav.base.common.BaseDAO;
import com.uav.base.common.Constants;
import com.uav.base.common.PagerVO;
import com.uav.base.model.FlyPlan;
import com.uav.base.model.FlyPlanFlight;
import com.uav.base.model.FlyPlanFlightWay;
import com.uav.base.model.Note;
import com.uav.base.model.NoteFlight;
import com.uav.base.model.NoteFlightWay;
import com.uav.web.view.note.NoteService;

@Service
@Transactional(rollbackFor = Exception.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FlyPlanService {

	@Autowired
	private BaseDAO baseDAO;
	@Autowired
	private NoteService noteService;
	@Autowired
	private CreateFileUtil createFileUtil;

	public PagerVO findList(FlyPlan obj, Integer curPage, Integer pageSize) throws Exception {
		StringBuilder hql = new StringBuilder("from FlyPlan o where 1=1 ");
		List<Object> params = new ArrayList<Object>();
		hql.append(" and o.crtDept = ? ");
		params.add(obj.getCrtDept());// 本机构的
		hql.append(" and o.hisSts = ? ");
		params.add(Constants.sys_default_no);// 不是手工录入的
		if (obj != null) {
			if (StringUtils.isNotBlank(obj.getNoteSeq())) {
				hql.append(" and o.noteSeq like ? ");
				params.add("%" + obj.getNoteSeq() + "%");
			}
			if (StringUtils.isNotBlank(obj.getNationality())) {
				hql.append(" and o.nationality like ? ");
				params.add("%" + obj.getNationality() + "%");
			}
			if (StringUtils.isNotBlank(obj.getModel())) {
				hql.append(" and o.model like ? ");
				params.add("%" + obj.getModel() + "%");
			}
			if (StringUtils.isNotBlank(obj.getAcid())) {
				hql.append(" and o.acid like ? ");
				params.add("%" + obj.getAcid() + "%");
			}
			if (StringUtils.isNotBlank(obj.getLetterUnit())) {
				hql.append(" and o.letterUnit like ? ");
				params.add("%" + obj.getLetterUnit() + "%");
			}
			if (StringUtils.isNotBlank(obj.getBegTime())) {
				hql.append(" and o.crtTime >= ? ");
				params.add(obj.getBegTime());
			}
			if (StringUtils.isNotBlank(obj.getEndTime())) {
				hql.append(" and o.crtTime <= ? ");
				params.add(obj.getEndTime());
			}
		}
		hql.append(" order by o.crtTime desc");
		PagerVO vo = baseDAO.findPaginated(hql.toString(), params.toArray(), curPage, pageSize);
		return vo;
	}

	// 生成计划
	public void save(FlyPlan flyPlan, String noteSeq) {
		Note note = noteService.findById(noteSeq);
		flyPlan.setAcid(note.getAcid());
		flyPlan.setLetterUnit(note.getLetterUnit());
		flyPlan.setLicenseNo(note.getLicenseNo());
		flyPlan.setMission(note.getMission());
		flyPlan.setModel(note.getModel());
		flyPlan.setNationality(note.getNationality());
		flyPlan.setNoteSeq(noteSeq);
		flyPlan.setOperator(note.getOperator());
		flyPlan.setOther(note.getOther());
		flyPlan.setPersonName(note.getPersonName());
		flyPlan.setPersonNumber(note.getPersonNumber());
		flyPlan.setPlanSts(Constants.BIZ_SPACE_REQ_CREATE);
		flyPlan.setRegNo(note.getRegNo());
		flyPlan.setSsrCode(note.getSsrCode());
		flyPlan.setTelNo(note.getTelNo());
		baseDAO.save(flyPlan);
		// 查询外交照会航线
		List<NoteFlight> noteFlightList = noteService.findFlightListByNoteSeq(noteSeq);
		for (NoteFlight noteFlight : noteFlightList) {
			FlyPlanFlight planFlight = new FlyPlanFlight(flyPlan.getPlanSeq(), noteFlight.getFlightFileId(),
					noteFlight.getRadioFrequency(), noteFlight.getPlanDate(), noteFlight.getOtherDate(),
					noteFlight.getFlightBody(), noteFlight.getUpAirport(), noteFlight.getDownAirport(), noteFlight.getInPointIdent(), noteFlight.getOutPointIdent());
			baseDAO.save(planFlight);
			List<NoteFlightWay> noteWayList = noteService.findWayListByNfSeq(noteFlight.getNfSeq());
			for (NoteFlightWay noteWay : noteWayList) {
				FlyPlanFlightWay planWay = new FlyPlanFlightWay(planFlight.getFpfSeq(), noteWay.getIdent(),
						noteWay.getLonx(), noteWay.getLaty(), noteWay.getAlt());
				baseDAO.save(planWay);
			}
		}
	}

	// 生成审批表
	public void commit(FlyPlan plan, String[] flightBodys) throws FileNotFoundException, IllegalAccessException {
		List<FlyPlanFlight> flightList = findFlightListByPlanSeq(plan.getPlanSeq());
		// 调用审批表生成工具
		String appFileId = createFileUtil.createAppFile(plan, flightList, flightBodys);
		plan.setAppFileId(appFileId);
		// 调用许可函生成工具
		String rptFileId = createFileUtil.createRptWordFile(plan, flightList, flightBodys);
		plan.setRptFileId(rptFileId);
		baseDAO.update(plan);
		// TODO 生成审批数据包
		
	}

	public FlyPlan findById(String planSeq) {
		return (FlyPlan) baseDAO.findById(FlyPlan.class, planSeq);
	}

	public List<FlyPlanFlight> findFlightListByPlanSeq(String planSeq) {
		String hql = "from FlyPlanFlight o where o.planSeq = ? order by o.planDate asc";
		List<FlyPlanFlight> list = baseDAO.findList(hql, new Object[] { planSeq });
		return list;
	}

	public List<FlyPlanFlightWay> findWayListByNfSeq(String fpfSeq) {
		String hql = "from FlyPlanFlightWay o where o.fpfSeq = ? order by o.fpfwSeq asc";
		List<FlyPlanFlightWay> list = baseDAO.findList(hql, new Object[] { fpfSeq });
		return list;
	}
}
