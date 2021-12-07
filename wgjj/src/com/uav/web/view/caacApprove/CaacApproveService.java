package com.uav.web.view.caacApprove;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uav.base.common.BaseDAO;
import com.uav.base.common.Constants;
import com.uav.base.common.PagerVO;
import com.uav.base.model.CaacApprove;
import com.uav.base.model.CaacFlight;
import com.uav.base.model.CaacFlightWay;
import com.uav.base.model.Note;
import com.uav.base.util.FileUtil;
import com.uav.base.util.FileZipUtil;
import com.uav.base.util.flight.FlightException;
import com.uav.base.util.flight.FlightUtil;
import com.uav.base.util.flight.FlightWayVo;
import com.uav.web.view.note.NoteService;

@Service
@Transactional(rollbackFor = Exception.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CaacApproveService {

	@Autowired
	private BaseDAO baseDAO;
	@Autowired
	private NoteService noteService;
	@Autowired
	private FlightUtil flightUtil;

	public PagerVO findList(Note obj, Integer curPage, Integer pageSize) throws Exception {
		StringBuilder hql = new StringBuilder("from CaacApprove o where 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if (obj != null) {
			if (StringUtils.isNotBlank(obj.getNoteSeq())) {
				hql.append(" and o.noteSeq like ? ");
				params.add("%" + obj.getNoteSeq() + "%");
			}
			if (StringUtils.isNotBlank(obj.getNationality())) {
				hql.append(" and o.note.nationality like ? ");
				params.add("%" + obj.getNationality() + "%");
			}
			if (StringUtils.isNotBlank(obj.getModel())) {
				hql.append(" and o.note.model like ? ");
				params.add("%" + obj.getModel() + "%");
			}
			if (StringUtils.isNotBlank(obj.getAcid())) {
				hql.append(" and o.note.acid like ? ");
				params.add("%" + obj.getAcid() + "%");
			}
			if (StringUtils.isNotBlank(obj.getLetterUnit())) {
				hql.append(" and o.note.letterUnit like ? ");
				params.add("%" + obj.getLetterUnit() + "%");
			}
			if (StringUtils.isNotBlank(obj.getLetterUnit())) {
				hql.append(" and o.note.licenseNo like ? ");
				params.add("%" + obj.getLicenseNo() + "%");
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
		hql.append(" order by o.crtTime desc, o.appVer desc");
		PagerVO vo = baseDAO.findPaginated(hql.toString(), params.toArray(), curPage, pageSize);
		return vo;
	}

	public CaacApprove findById(String caSeq) {
		return (CaacApprove) baseDAO.findById(CaacApprove.class, caSeq);
	}

	public List<CaacFlight> findFlightListByCaSeq(String caSeq) {
		String hql = "from CaacFlight o where o.caSeq = ? order by o.planDate asc";
		List<CaacFlight> list = baseDAO.findList(hql, new Object[] { caSeq });
		return list;
	}

	public List<CaacFlightWay> findWayListByCafSeq(String cafSeq) {
		String hql = "from CaacFlightWay o where o.cafSeq = ? order by o.cafwSeq asc";
		List<CaacFlightWay> list = baseDAO.findList(hql, new Object[] { cafSeq });
		return list;
	}

	public void save(CaacApprove caac, MultipartFile uploadFile) throws Exception {
		// 解压压缩文件file
		List<File> fileList = new ArrayList<File>();
		File caacZipFile = null;
		try {
			fileList = FileZipUtil.unZip(uploadFile.getBytes());
			if (fileList == null || fileList.size() < 2) {
				throw new FlightException("解压文件出现问题或压缩包内内容不全！");
			}
			// 获得结构化文件
			List<File> caacJsonFileList = new ArrayList<File>();
			// 获得民航意见扫描件
			List<File> caacFileList = new ArrayList<File>();
			for (File file : fileList) {
				String fileName = file.getName();
				if (fileName.contains(".json")) {
					// 结构化文件
					caacJsonFileList.add(file);
				} else {
					// 扫描件
					caacFileList.add(file);
				}
			}
			// 缺少任何一类文件，返回错误信息
			if (caacJsonFileList.size() == 0) {
				throw new FlightException("压缩文件内不存在以“json”为后缀的文件！");
			} else if (caacJsonFileList.size() != 1) {
				throw new FlightException("压缩文件内存在多个以“json”为后缀的文件，该文件只能存在一个！");
			}
			if (caacFileList.size() == 0) {
				throw new FlightException("压缩文件内未找到除了以“json”为后缀以外的其他文件！");
			}
			// 校验结构化文件内容，如果有字段不对，返回错误信息
			File caacJsonFile = caacJsonFileList.get(0);
			String caacJsonStr = FileUtil.readFile(caacJsonFile);
			if (StringUtils.isBlank(caacJsonStr)) {
				throw new FlightException(caacJsonFile.getName() + "的文件内容为空！");
			}
			JSONObject caacJson = JSONObject.parseObject(caacJsonStr);
			if (!caacJson.containsKey("documentNum")) {
				throw new FlightException(caacJsonFile.getName() + "的文件内容有误，未找到“文书编号【documentNum】”字段！");
			} else if (StringUtils.isBlank(caacJson.getString("documentNum"))) {
				throw new FlightException(caacJsonFile.getName() + "的文件内容有误，“文书编号【documentNum】”字段不能为空！");
			}
			if (!caacJson.containsKey("dateSked")) {
				throw new FlightException(caacJsonFile.getName() + "的文件内容有误，未找到“日期类型【dateSked】”字段！");
			} else if (StringUtils.isBlank(caacJson.getString("dateSked"))) {
				throw new FlightException(caacJsonFile.getName() + "的文件内容有误，“日期类型【dateSked】”字段不能为空！");
			}
			if (!caacJson.containsKey("permissionNumber")) {
				throw new FlightException(caacJsonFile.getName() + "的文件内容有误，未找到“许可号【permissionNumber】”字段！");
			} else if (StringUtils.isBlank(caacJson.getString("permissionNumber"))) {
				throw new FlightException(caacJsonFile.getName() + "的文件内容有误，“许可号【permissionNumber】”字段不能为空！");
			}
			if (!caacJson.containsKey("flightDetails")) {
				throw new FlightException(caacJsonFile.getName() + "的文件内容有误，未找到“计划航线【flightDetails】”字段！");
			} else {
				JSONArray flightDetails = caacJson.getJSONArray("flightDetails");
				if (flightDetails.size() == 0) {
					throw new FlightException(caacJsonFile.getName() + "的文件内容有误，“计划航线【flightDetails】”字段不能为空！");
				} else {
					for (int i = 0; i < flightDetails.size(); i++) {
						JSONObject flightDetail = flightDetails.getJSONObject(i);
						if (!flightDetail.containsKey("flightDate")) {
							throw new FlightException(caacJsonFile.getName() + "的文件内容有误，“未找到计划航线【flightDetails】”内单条航线的日期【flightDate】字段！");
						} else if (StringUtils.isBlank(flightDetail.getString("flightDate"))) {
							throw new FlightException(caacJsonFile.getName() + "的文件内容有误，“计划航线【flightDetails】”内单条航线的日期【flightDate】字段不能为空！");
						}
						if (!flightDetail.containsKey("route")) {
							throw new FlightException(caacJsonFile.getName() + "的文件内容有误，“未找到计划航线【flightDetails】”内单条航线的航路【route】字段！");
						} else if (StringUtils.isBlank(flightDetail.getString("route"))) {
							throw new FlightException(caacJsonFile.getName() + "的文件内容有误，“计划航线【flightDetails】”内单条航线的航路【route】字段不能为空！");
						}
						if (!flightDetail.containsKey("upAirport")) {
							throw new FlightException(caacJsonFile.getName() + "的文件内容有误，“未找到计划航线【flightDetails】”内单条航线的起飞机场【upAirport】字段！");
						} else if (StringUtils.isBlank(flightDetail.getString("upAirport"))) {
							throw new FlightException(caacJsonFile.getName() + "的文件内容有误，“计划航线【flightDetails】”内单条航线的起飞机场【upAirport】字段不能为空！");
						}
						if (!flightDetail.containsKey("downAirport")) {
							throw new FlightException(caacJsonFile.getName() + "的文件内容有误，“未找到计划航线【flightDetails】”内单条航线的降落机场【downAirport】字段！");
						} else if (StringUtils.isBlank(flightDetail.getString("downAirport"))) {
							throw new FlightException(caacJsonFile.getName() + "的文件内容有误，“计划航线【flightDetails】”内单条航线的降落机场【downAirport】字段不能为空！");
						}
					}
				}
			}
			// 校验照会文书是否已经存在
			Note note = noteService.findById(caacJson.getString("documentNum"));
			if (note == null) {
				throw new FlightException("未找到响应的外交照会数据！");
			}

			// 解析结构化文件内容，将结构化文件解析内容填入CaacApprove、CaacFlight、CaacFlightWay表中
			caac.setNoteSeq(caacJson.getString("documentNum"));
			caac.setAppSts(Constants.sys_default_no);
			// 获得最大的民航意见版本
			Integer maxAppVer = (Integer) baseDAO.findUnique("select max(appVer) from CaacApprove where noteSeq=?",
					new Object[] { caac.getNoteSeq() });
			if (maxAppVer == null) {
				caac.setAppVer(1);
			} else {
				caac.setAppVer(maxAppVer + 1);
			}
			caac.setDateSked(caacJson.getString("dateSked"));
			caac.setLicenseNo(caacJson.getString("permissionNumber"));

			baseDAO.save(caac);

			note.setLicenseNo(caac.getLicenseNo());
//			note.setCanDelSts(Constants.sys_default_no);
			baseDAO.update(note);

			JSONArray flightDetails = caacJson.getJSONArray("flightDetails");
			for (int i = 0; i < flightDetails.size(); i++) {
				JSONObject flightDetail = flightDetails.getJSONObject(i);
				String flightDate = flightDetail.getString("flightDate");
				CaacFlight flight = new CaacFlight(caac.getCaSeq(), flightDate, "", flightDetail.getString("route"),
						flightDetail.getString("upAirport"), flightDetail.getString("downAirport"));
				baseDAO.save(flight);
				// 调用解析航线方法，得到航线上各个点的信息，如果出现解析不了的航线，返回错误信息
//				List<FlightWayVo> wayVoList = flightUtil.getFlightWayList(flight.getUpAirport(), flight.getDownAirport(), flight.getFlightBody());
//				for (FlightWayVo wayVo : wayVoList) {
//					CaacFlightWay way = new CaacFlightWay(note.getNoteSeq(), flight.getCafSeq(), wayVo.getIdent(), wayVo.getLonx(), wayVo.getLaty(),
//							wayVo.getAlt());
//					baseDAO.save(way);
//				}
//				// 调用生成航线示意图方法，其中包含重要目标数据、用空活动计划数据，生成各个航线示意图
//				String flightFileId = flightUtil.createFlightFile(wayVoList);
//				if(flightFileId!=null){
//					flight.setFlightFileId(flightFileId);
//				}
//				flight.setInPointIdent(wayVoList.get(0).getIdent());
//				flight.setInPointIdent(wayVoList.get(wayVoList.size() - 1).getIdent());
//				baseDAO.update(flight);
			}
			// 对民航意见扫描件用名字进行排序
			Collections.sort(caacFileList, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			List<String> caacFileIdList = new ArrayList<String>();
			// 将民航意见扫描件上传到数据库
			for (File noteFile : caacFileList) {
				String fileId = FileUtil.uploadFileToDb(noteFile, caac.getCrtUser());
				caacFileIdList.add(fileId);
			}
			// 将民航意见扫描件的id保存到caFileId字段中
			caac.setCaFileId(String.join(",", caacFileIdList));
			// 将民航意见扫描件添加到压缩文件zip中
			caacZipFile = FileZipUtil.zipFile("民航意见扫描件.zip", caacFileList);
			// 将民航意见扫描件压缩文件zip上传到数据库
			String caacZipFileId = FileUtil.uploadFileToDb(caacZipFile, caac.getCrtUser());
			// 将民航意见扫描件压缩文件zip的id保存到caZipFileId字段中
			caac.setCaacZipFileId(caacZipFileId);
			// 将压缩文件file上传到数据库
			String uploadFileId = FileUtil.uploadFileToDb(uploadFile, caac.getCrtUser());
			caac.setUploadFileId(uploadFileId);
			baseDAO.update(caac);
		} finally {
			if (fileList.size() > 0) {
				for (File file : fileList) {
					file.delete();
				}
				File file = fileList.get(0);
				File parentFile = file.getParentFile();
				parentFile.delete();
			}
			if (caacZipFile != null) {
				if (caacZipFile.exists()) {
					caacZipFile.delete();
					File parentFile = caacZipFile.getParentFile();
					parentFile.delete();
				}
			}
		}
	}
}
