package com.uav.web.view.civilreport;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONObjectIter;
import cn.hutool.json.JSONUtil;
import com.uav.base.common.BaseDAO;
import com.uav.base.common.PagerVO;
import com.uav.base.model.CaacApprove;
import com.uav.base.model.internetModel.*;
import com.uav.base.util.FileUtil;
import com.uav.base.util.PropertiesUtil;
import com.uav.web.view.civilaviation.CivilAviationDAO;
import com.uav.web.view.civilaviation.CivilAviationService;
import com.uav.web.view.civilaviation.CivilAviationVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.json.JsonObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 审批许可管理
 * @ClassName: NoteManageService
 * @Description: 
 * @author 
 * @date 
 */
@Service
@Slf4j
@Transactional(rollbackFor={RuntimeException.class,Exception.class})
@SuppressWarnings("unchecked")
public class CivilReportService {
	//本地文件夹操作位置
	String ROOT_FILE_PATH = PropertiesUtil.getPropertyValue("file.upload.path","");
	//审批许可文件处理路径
	String CIVIL_REPORT_PATH="civilReport";

	static File jsonFile=null;
	static File pdfFile=null;


	@Autowired
	private CivilAviationService civilAviationService;
	@Autowired
	private CivilAviationDAO civilAviationDao;
	@Autowired
	private BaseDAO baseDAO;
	/**
	 * 审批许可列表查询
	 * @param pagerVO
	 * @param entityParam
	 * @return
	 * @throws Exception
	 */
	public PagerVO findList(PagerVO<CivilReportVO> pagerVO,CivilReportDTO entityParam) throws Exception{
		
		List<Object> params = new ArrayList<Object>();
		
		StringBuffer hql = new StringBuffer("select DISTINCT note.noteId from NotePlanInfo note  WHERE note.delStatus=0 ");
		if(entityParam!=null){
			if (!StringUtils.isBlank(entityParam.getDocumentNum())) {
				hql.append(" and note.documentNum = ? ");
				params.add(entityParam.getDocumentNum());
			}
			if (!StringUtils.isBlank(entityParam.getNoteNo())) {
				hql.append(" and note.noteNo like ? ");
				params.add("%" + entityParam.getNoteNo() + "%");
			}
			if (entityParam.getStatus()!=null&&entityParam.getStatus()!=0) {
				hql.append(" and note.status= ? ");
				params.add(entityParam.getStatus());
			}
		}

		hql.append(" order by note.createTime desc");
		PagerVO<NotePlanInfo> notePlanInfoPage = civilAviationDao.findPaginated(hql.toString(), params.toArray(), pagerVO.getPageNo(), pagerVO.getPageSize());
		List<CivilReportVO> civilReportVOS = new ArrayList<CivilReportVO>();
		CivilReportVO civilReportVO;
		for(Object o : notePlanInfoPage.getItems()){
			Integer noteId = (Integer) o;
			civilReportVO= CivilReportVO.builder()
					.planInfo(civilAviationService.findPlanInfoById(noteId))
					.noteReport(this.findNoteReportByNoteId(noteId))
					.build();
			civilReportVOS.add(civilReportVO);
		}
		pagerVO.setItems(civilReportVOS);
		return pagerVO;
	}
	NoteReport findNoteReportByNoteId(Integer noteId){
		NoteReport noteReport=(NoteReport)baseDAO.findUnique("from NoteReport nr where nr.noteId=?",new Object[]{noteId});
		return noteReport;
	}

	/**
	 * 根据文书编号查找飞机信息
	 * @param ducmtNum
	 * @return
	 */
	NotePlanInfo findPlanInfoByDocmtNum(String ducmtNum){
		NotePlanInfo planInfo=(NotePlanInfo)baseDAO.findUnique("from NotePlanInfo pl where pl.documentNum=?",new Object[]{ducmtNum});
		return planInfo;
	}

	/**
	 * 处理上传的Zip压缩文件
	 * @param file
	 * @throws IOException
	 */
	public void uploadZipFile(MultipartFile file) throws IOException {
		log.info("上传Zip文件开始执行：Zip文件Path={}",file.getOriginalFilename());
		//创建年月文件夹
		Calendar date = Calendar.getInstance();
		File dateDirs = new File(date.get(Calendar.YEAR)
				+ File.separator + (date.get(Calendar.MONTH)+1));
		String uploadPath = ROOT_FILE_PATH+File.separator+CIVIL_REPORT_PATH+File.separator+dateDirs;
		String zipFilePath=FileUtil.uploadFile(file,uploadPath);
		File afterRenameZip=new File(zipFilePath);
		String zipFileName=afterRenameZip.getName();
		String newDir= uploadPath+File.separator+zipFileName.substring(0,zipFileName.lastIndexOf("."));
		File newDirPath=new File(newDir);
		if(!newDirPath.exists()){
			newDirPath.mkdir();
		}
		File unzipPath=ZipUtil.unzip(afterRenameZip,newDirPath);
		//处理解压后的文件
		File[] listFiles=unzipPath.listFiles();

		for(File fdir:listFiles){
			//批量导入处理
			if(fdir.isDirectory()){
				File[] lastFiles=fdir.listFiles();
				for(File f:lastFiles) {
					doCivilReportFile(f);
				}
			//单个处理
			}else{
				doCivilReportFile(fdir);
			}
		}


	}

	/**
	 * 处理解压后的文件
	 * @param civilFile
	 */
	public void doCivilReportFile(File civilFile){
		if (civilFile.getName().endsWith(".json")) {
			jsonFile = civilFile;

		}else if (civilFile.getName().endsWith(".pdf")) {
			pdfFile = civilFile;
		}
		NotePlanInfo notePlanInfo=null;
		if(jsonFile!=null){
			notePlanInfo=doJsonPlanInfo(jsonFile);
		}
		if(notePlanInfo!=null&&pdfFile!=null){
			doPdfCivilReportFile(pdfFile,notePlanInfo.getNoteId());
		}
	}

	/**
	 * 处理压缩包里的Json文件
	 * @param jsonFile
	 * @return
	 */
	public NotePlanInfo doJsonPlanInfo(File jsonFile){
		log.info("处理JSON文件开始执行：JsonFile PATH={}",jsonFile.getName());
		NotePlanInfo notePlanInfo=null;
		try {
			String planInfoString=FileUtils.readFileToString(jsonFile);
			JSONObject planInfoJson= new JSONObject(planInfoString);
			notePlanInfo=JSONUtil.toBean(planInfoJson,NotePlanInfo.class);
			String documentNum=notePlanInfo.getDocumentNum();
			NotePlanInfo planInfoDB=this.findPlanInfoByDocmtNum(documentNum);
			if(planInfoDB!=null){
				Integer noteId=planInfoDB.getNoteId();
				notePlanInfo.setNoteId(noteId);
				notePlanInfo.setStatus(3);
				baseDAO.merge(notePlanInfo);
			}else{
				Integer noteId=(Integer)baseDAO.save(notePlanInfo);
				notePlanInfo.setNoteId(noteId);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return notePlanInfo;
	}

	/**
	 * 处理压缩包里的PDF文件
	 * @param pdfFilePath
	 * @param noteId
	 */
	public void doPdfCivilReportFile(File pdfFilePath,Integer noteId){
		log.info("处理PDF文件开始执行：，PDFFile PATH={},NoteId={}",pdfFilePath.getName(),noteId);
		String pdfFileName=pdfFilePath.getName();
		String filePath=pdfFilePath.getAbsolutePath().substring(ROOT_FILE_PATH.length());
		NoteReport noteReport=(NoteReport)baseDAO.findUnique("from NoteReport where noteId=?",new Object[]{noteId});
		if(noteReport!=null){
			noteReport.setFileName(pdfFileName);
			noteReport.setFileName(filePath);
			baseDAO.merge(noteReport);
		}else{
			noteReport=new NoteReport();
			noteReport.setFileName(pdfFileName);
			noteReport.setCreateTime(DateUtil.date().toStringDefaultTimeZone());
			noteReport.setFilePath(filePath);
			noteReport.setNoteId(noteId);
			baseDAO.merge(noteReport);
		}

	}
}
