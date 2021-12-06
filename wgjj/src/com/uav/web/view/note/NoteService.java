package com.uav.web.view.note;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uav.base.common.BaseDAO;
import com.uav.base.common.PagerVO;
import com.uav.base.model.Note;
import com.uav.base.model.NoteFlight;
import com.uav.base.model.NoteFlightWay;
import com.uav.base.util.FileUtil;
import com.uav.base.util.FileZipUtil;
import com.uav.base.util.flight.FlightException;
import com.uav.base.util.flight.FlightUtil;
import com.uav.base.util.flight.FlightWayVo;

@Service
@Transactional(rollbackFor = Exception.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NoteService {

	@Autowired
	private BaseDAO baseDAO;
	@Autowired
	private FlightUtil flightUtil;

	public PagerVO findList(Note obj, Integer curPage, Integer pageSize) throws Exception {
		StringBuilder hql = new StringBuilder("from Note o where 1=1 ");
		List<Object> params = new ArrayList<Object>();
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

	public Note findById(String noteSeq) {
		return (Note) baseDAO.findById(Note.class, noteSeq);
	}

	public List<NoteFlight> findFlightListByNoteSeq(String noteSeq) {
		String hql = "from NoteFlight o where o.noteSeq = ? order by o.planDate asc";
		List<NoteFlight> list = baseDAO.findList(hql, new Object[] { noteSeq });
		return list;
	}

	public List<NoteFlightWay> findWayListByNfSeq(String nfSeq) {
		String hql = "from NoteFlightWay o where o.nfSeq = ? order by o.nfwSeq asc";
		List<NoteFlightWay> list = baseDAO.findList(hql, new Object[] { nfSeq });
		return list;
	}

	public void save(Note note, MultipartFile uploadFile) throws FlightException, Exception {
		// 解压压缩文件file
		List<File> fileList = new ArrayList<File>();
		File noteZipFile = null;
		try {
			fileList = FileZipUtil.unZip(uploadFile.getBytes());
			if (fileList == null || fileList.size() < 2) {
				throw new FlightException("解压文件出现问题或压缩包内内容不全！");
			}
			// 获得结构化文件
			List<File> noteJsonFileList = new ArrayList<File>();
			// 获得照会文件扫描件
			List<File> noteFileList = new ArrayList<File>();
			for (File file : fileList) {
				String fileName = file.getName();
				if (fileName.contains(".json")) {
					// 结构化文件
					noteJsonFileList.add(file);
				} else {
					// 扫描件
					noteFileList.add(file);
				}
			}
			// 缺少任何一类文件，返回错误信息
			if (noteJsonFileList.size() == 0) {
				throw new FlightException("压缩文件内不存在以“json”为后缀的文件！");
			} else if (noteJsonFileList.size() != 1) {
				throw new FlightException("压缩文件内存在多个以“json”为后缀的文件，该文件只能存在一个！");
			}
			if (noteFileList.size() == 0) {
				throw new FlightException("压缩文件内未找到除了以“json”为后缀以外的其他文件！");
			}
			// 校验结构化文件内容，如果有字段不对，返回错误信息
			File noteJsonFile = noteJsonFileList.get(0);
			String noteJsonStr = FileUtil.readFile(noteJsonFile);
			if (StringUtils.isBlank(noteJsonStr)) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容为空！");
			}
			JSONObject noteJson = JSONObject.parseObject(noteJsonStr);
			if (!noteJson.containsKey("documentNum")) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，未找到“文书编号【documentNum】”字段！");
			} else if (StringUtils.isBlank(noteJson.getString("documentNum"))) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，“文书编号【documentNum】”字段不能为空！");
			}
			if (!noteJson.containsKey("typeOfAircraft")) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，未找到“机型【typeOfAircraft】”字段！");
			} else if (StringUtils.isBlank(noteJson.getString("typeOfAircraft"))) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，“机型【typeOfAircraft】”字段不能为空！");
			}
			if (!noteJson.containsKey("registerNumber")) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，未找到“注册号【registerNumber】”字段！");
			} else if (StringUtils.isBlank(noteJson.getString("registerNumber"))) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，“注册号【registerNumber】”字段不能为空！");
			}
			if (!noteJson.containsKey("callSign")) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，未找到“呼号【callSign】”字段！");
			} else if (StringUtils.isBlank(noteJson.getString("callSign"))) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，“呼号【callSign】”字段不能为空！");
			}
			if (!noteJson.containsKey("crew")) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，未找到“机组人数【crew】”字段！");
			} else if (noteJson.getInteger("crew") == null) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，“机组人数【crew】”字段不能为空！");
			}
			if (!noteJson.containsKey("nationality")) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，未找到“国家【nationality】”字段！");
			} else if (StringUtils.isBlank(noteJson.getString("nationality"))) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，“国家【nationality】”字段不能为空！");
			}
			if (!noteJson.containsKey("operator")) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，未找到“营运方【operator】”字段！");
			} else if (StringUtils.isBlank(noteJson.getString("operator"))) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，“营运方【operator】”字段不能为空！");
			}
			if (!noteJson.containsKey("missionPurpose")) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，未找到“任务目的【missionPurpose】”字段！");
			} else if (StringUtils.isBlank(noteJson.getString("missionPurpose"))) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，“任务目的【missionPurpose】”字段不能为空！");
			}
			if (!noteJson.containsKey("flightDetails")) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，未找到“计划航线【flightDetails】”字段！");
			} else {
				JSONArray flightDetails = noteJson.getJSONArray("flightDetails");
				if (flightDetails.size() == 0) {
					throw new FlightException(noteJsonFile.getName() + "的文件内容有误，“计划航线【flightDetails】”字段不能为空！");
				} else {
					for (int i = 0; i < flightDetails.size(); i++) {
						JSONObject flightDetail = flightDetails.getJSONObject(i);
						if (!flightDetail.containsKey("flightDate")) {
							throw new FlightException(noteJsonFile.getName() + "的文件内容有误，“未找到计划航线【flightDetails】”内单条航线的日期【flightDate】字段！");
						} else if (StringUtils.isBlank(flightDetail.getString("flightDate"))) {
							throw new FlightException(noteJsonFile.getName() + "的文件内容有误，“计划航线【flightDetails】”内单条航线的日期【flightDate】字段不能为空！");
						}
						if (!flightDetail.containsKey("route")) {
							throw new FlightException(noteJsonFile.getName() + "的文件内容有误，“未找到计划航线【flightDetails】”内单条航线的航路【route】字段！");
						} else if (StringUtils.isBlank(flightDetail.getString("route").trim())) {
							throw new FlightException(noteJsonFile.getName() + "的文件内容有误，“计划航线【flightDetails】”内单条航线的航路【route】字段不能为空！");
						}
						if (!flightDetail.containsKey("upAirport")) {
							throw new FlightException(noteJsonFile.getName() + "的文件内容有误，“未找到计划航线【flightDetails】”内单条航线的起飞机场【upAirport】字段！");
						} else if (StringUtils.isBlank(flightDetail.getString("upAirport"))) {
							throw new FlightException(noteJsonFile.getName() + "的文件内容有误，“计划航线【flightDetails】”内单条航线的起飞机场【upAirport】字段不能为空！");
						}
						if (!flightDetail.containsKey("downAirport")) {
							throw new FlightException(noteJsonFile.getName() + "的文件内容有误，“未找到计划航线【flightDetails】”内单条航线的降落机场【downAirport】字段！");
						} else if (StringUtils.isBlank(flightDetail.getString("downAirport"))) {
							throw new FlightException(noteJsonFile.getName() + "的文件内容有误，“计划航线【flightDetails】”内单条航线的降落机场【downAirport】字段不能为空！");
						}
					}
				}
			}
			if (!noteJson.containsKey("letterUnit")) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，未找到“来电来函单位【letterUnit】”字段！");
			} else if (StringUtils.isBlank("letterUnit")) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，“来电来函单位【letterUnit】”字段不能为空！");
			}
			if (!noteJson.containsKey("name")) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，未找到“姓名【name】”字段！");
			} else if (StringUtils.isBlank("name")) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，“姓名【name】”字段不能为空！");
			}
			if (!noteJson.containsKey("telNo")) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，未找到“电话【telNo】”字段！");
			} else if (StringUtils.isBlank("telNo")) {
				throw new FlightException(noteJsonFile.getName() + "的文件内容有误，“电话【telNo】”字段不能为空！");
			}
			// 校验照会文书是否已经存在
			Note temp = findById(noteJson.getString("documentNum"));
			if (temp != null) {
				throw new FlightException("该照会文书内容已经导入，请勿重复导入！");
			}
			// 解析结构化文件内容，将结构化文件解析内容填入Note、NoteFlight、NoteFlightWay表中
			note.setAcid(noteJson.getString("callSign"));
			note.setLetterUnit(noteJson.getString("letterUnit"));
			note.setMission(noteJson.getString("missionPurpose"));
			note.setModel(noteJson.getString("typeOfAircraft"));
			note.setNationality(noteJson.getString("nationality"));
			note.setNoteSeq(noteJson.getString("documentNum"));
			note.setOperator(noteJson.getString("operator"));
			note.setOther(noteJson.containsKey("other") ? noteJson.getString("other") : "");
			note.setPersonName(noteJson.getString("name"));
			note.setPersonNumber(noteJson.getInteger("crew"));
			note.setRegNo(noteJson.getString("registerNumber"));
			note.setTelNo(noteJson.getString("telNo"));

			baseDAO.save(note);

			JSONArray flightDetails = noteJson.getJSONArray("flightDetails");
			for (int i = 0; i < flightDetails.size(); i++) {
				JSONObject flightDetail = flightDetails.getJSONObject(i);
				NoteFlight flight = new NoteFlight(note.getNoteSeq(), null, flightDetail.getString("flightDate"), "", flightDetail.getString("route").trim(),
						flightDetail.getString("upAirport"), flightDetail.getString("downAirport"));
				baseDAO.save(flight);
				// 调用解析航线方法，得到航线上各个点的信息，如果出现解析不了的航线，返回错误信息
//				List<FlightWayVo> wayVoList = flightUtil.getFlightWayList(flight.getUpAirport(), flight.getDownAirport(), flight.getFlightBody());
//				for (FlightWayVo wayVo : wayVoList) {
//					NoteFlightWay way = new NoteFlightWay(note.getNoteSeq(), flight.getNfSeq(), wayVo.getIdent(), wayVo.getLonx(), wayVo.getLaty(), wayVo.getAlt());
//					baseDAO.save(way);
//				}
//				// 调用生成航线示意图方法，其中包含重要目标数据、用空活动计划数据，生成各个航线示意图
//				String flightFileId = flightUtil.createFlightFile(wayVoList);
//				if (flightFileId != null) {
//					flight.setFlightFileId(flightFileId);
//				}
//				flight.setInPointIdent(wayVoList.get(0).getIdent());
//				flight.setInPointIdent(wayVoList.get(wayVoList.size() - 1).getIdent());
//				baseDAO.update(flight);
			}
			// 对照会文件扫描件用名字进行排序
			Collections.sort(noteFileList, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			List<String> noteFileIdList = new ArrayList<String>();
			// 将照会文件扫描件上传到数据库
			for (File noteFile : noteFileList) {
				String fileId = FileUtil.uploadFileToDb(noteFile, note.getCrtUser());
				noteFileIdList.add(fileId);
			}
			// 将照会文件扫描件的id保存到noteFileId字段中
			note.setNoteFileId(String.join(",", noteFileIdList));
			// 将照会文书扫描件添加到压缩文件zip中
			noteZipFile = FileZipUtil.zipFile("外交照会扫描件.zip", noteFileList);
			// 将照会文书扫描件压缩文件zip上传到数据库
			String noteZipFileId = FileUtil.uploadFileToDb(noteZipFile, note.getCrtUser());
			// 将照会文书扫描件压缩文件zip的id保存到noteZipFileId字段中
			note.setNoteZipFileId(noteZipFileId);
			// 将压缩文件file上传到数据库
			String uploadFileId = FileUtil.uploadFileToDb(uploadFile, note.getCrtUser());
			note.setUploadFileId(uploadFileId);
			baseDAO.update(note);
		} finally {
			if (fileList.size() > 0) {
				for (File file : fileList) {
					file.delete();
				}
				File file = fileList.get(0);
				File parentFile = file.getParentFile();
				parentFile.delete();
			}
			if (noteZipFile != null) {
				if (noteZipFile.exists()) {
					noteZipFile.delete();
					File parentFile = noteZipFile.getParentFile();
					parentFile.delete();
				}
			}
		}
	}

	public void del(String noteSeq) {
		// 删除所有文件
		Note note = findById(noteSeq);
		List<String> delFileIdList = new ArrayList<String>();
		// 用户上传的zip文件
		if (StringUtils.isNotBlank(note.getUploadFileId())) {
			delFileIdList.add(note.getUploadFileId());
		}
		// 照会文书扫描件
		if (StringUtils.isNotBlank(note.getNoteFileId())) {
			delFileIdList.addAll(Arrays.asList(note.getNoteFileId().split(",")));
		}
		// 照会文书扫描件zip文件
		if (StringUtils.isNotBlank(note.getNoteZipFileId())) {
			delFileIdList.add(note.getNoteZipFileId());
		}
		// 航线示意图
		List<String> wayFileIdList = baseDAO.findList("select flightFileId from NoteFlight where noteSeq=?", new Object[] { noteSeq });
		if (wayFileIdList.size() > 0) {
			delFileIdList.addAll(wayFileIdList);
		}
		// 删除本条外交照会相关文件
		if (delFileIdList.size() > 0) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("fileIds", delFileIdList);
			baseDAO.executeHql("delete from SysFile where fileId in (:fileIds)", params);
		}
		// 删除航点
		baseDAO.executeHql("delete from NoteFlightWay where noteSeq=?", new Object[] { noteSeq });
		// 删除航线
		baseDAO.executeHql("delete from NoteFlight where noteSeq=?", new Object[] { noteSeq });
		// 删除照会
		baseDAO.executeHql("delete from Note where noteSeq=?", new Object[] { noteSeq });
	}

	// 查询未生成计划并且有民航意见的照会列表
	public List<Note> findNoteNoPlanList() {
		List<Note> list = baseDAO.findList("from Note where noteSeq in (select noteSeq from CaacApprove) and noteSeq not in (select noteSeq from FlyPlan)");
		return list;
	}

}
