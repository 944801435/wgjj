package com.brilliance.web.view.noteManage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.brilliance.base.common.Constants;
import com.brilliance.base.common.PagerVO;
import com.brilliance.base.model.internetModel.NoteCivilReply;
import com.brilliance.base.model.internetModel.NoteFiles;
import com.brilliance.base.model.internetModel.NotePlanFlight;
import com.brilliance.base.model.internetModel.NotePlanInfo;
import com.brilliance.base.model.internetModel.NoteReport;
import com.brilliance.base.util.DateUtil;
import com.brilliance.base.util.FanyiV3Util;
import com.brilliance.base.util.FileUtil;
import com.brilliance.base.util.OcrV3Util;
import com.brilliance.base.util.PropertiesUtil;
import com.brilliance.web.view.civilaviation.CivilAviationService;
import com.brilliance.web.view.civilaviation.CivilAviationVO;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import sun.util.logging.resources.logging;

/**
 * 照会文书管理
 * @ClassName: NoteManageService
 * @Description: 
 * @author 
 * @date 
 */
@Service
@Transactional(rollbackFor={RuntimeException.class,Exception.class})
@SuppressWarnings("unchecked")
public class NoteManageService {
	protected static final Logger log = Logger.getLogger(NoteManageService.class);
	@Autowired
	private NoteManageDao noteManageDao;
	@Autowired
	private CivilAviationService civilAviationService;
	//本地文件夹操作位置
		String rootPath = PropertiesUtil.getPropertyValue("file.upload.path.windows","");
	/**
	 * @throws Exception 
	 * 查询
	 * 描述
	 * @Title: findList 
	 * @author 
	 * @param planInfo 查询参数
	 * @param curPage 当前页面
	 * @param pageSize 一页显示的条数
	 * @return    
	 * PagerVO 返回类型 
	 * @throws
	 */
	public PagerVO findList(NotePlanInfo planInfo, Integer curPage, int pageSize) throws Exception{
		
		List<Object> params = new ArrayList<Object>();
		
		StringBuffer hql = new StringBuffer("select DISTINCT note.noteId from NotePlanInfo note , NoteCivilReply ncm WHERE 1=1 ");
		
		if(!StringUtils.isBlank(planInfo.getBeginTime())){
			hql.append(" and note.createTime>=?");
			params.add(planInfo.getBeginTime());
		}
		if(!StringUtils.isBlank(planInfo.getEndTime())){
			hql.append(" and note.createTime<=?");
			params.add(planInfo.getEndTime());
		}
		if(!StringUtils.isBlank(planInfo.getPermitNumber())){
			hql.append(" and note.noteId=ncm.noteId and ncm.permitNumber = ? ");
			params.add(planInfo.getPermitNumber());
		}
		if(!StringUtils.isBlank(planInfo.getNoteNo())){
			hql.append(" and note.noteNo like ? ");
			params.add("%"+planInfo.getNoteNo()+"%");
		}
		hql.append(" order by note.createTime desc");
		PagerVO vo = noteManageDao.findPaginated(hql.toString(), params.toArray(), curPage, pageSize);
		List<Object> datas = new ArrayList<Object>();
		for(Object o : vo.getItems()){
			Integer noteId = (Integer) o;
			datas.add(getNoteManageById(noteId));
		}
		vo.setItems(datas);
		return vo;
	}
	/**
	 * 获取信息
	 * */
	public NotePlanInfo getNoteManageById(Integer noteId){
		return (NotePlanInfo) noteManageDao.findById(NotePlanInfo.class, noteId);
	}
	public NoteCivilReply findCivilMessageById(int noteId) {
		return (NoteCivilReply) noteManageDao.findById(NoteCivilReply.class, noteId);
	}
	/**
	 * 
	 * 描述 查询民航最新的回复信息
	 * @Title: findNoteCivilMessageByCreateTime 
	 * @author 
	 * @return    
	 * NoteCivilMessage 返回类型 
	 * @throws
	 */
	public NoteCivilReply findNoteCivilMessageByCreateTime(Integer noteId) {
		String sql = "from NoteCivilReply nc where nc.createTime=(select MAX(createTime) from NoteCivilReply) AND nc.noteId=? ";
//		String sql = "SELECT nc.* FROM note_civil_message nc WHERE nc.create_time=(SELECT MAX(create_time) FROM note_civil_message) AND nc.note_id=? ";
		NoteCivilReply civilMessage = (NoteCivilReply) noteManageDao.findUnique(sql,  new Object[] {noteId});
		return civilMessage;
	}
	public PagerVO findReportList(NoteReport noteReport, Integer curPage, int pagesize) {
		return noteManageDao.findReportList(noteReport,curPage,pagesize);
	}
	public PagerVO findNoteInfoList(NotePlanInfo planInfo, Integer curPage, int pagesize) {
		return noteManageDao.findNoteInfoList(planInfo,curPage,pagesize);
	}
	public void delete(Integer[] noteIds) {
		for (Integer noteId : noteIds) {
			noteManageDao.executeHql("update NotePlanInfo set delStatus=0 where noteId=? ", new Object[] {noteId });
		}
	}
	public String addNoteInfo(NotePlanInfo obj, MultipartFile[] file) {
		List<NoteFiles> list = new ArrayList<>();
		String deposeFilesDir = Constants.readValue("noteFilesDir")+getYYYYMM()+"\\"+getOrderID()+"\\";
		if(file!=null){
		for (MultipartFile multipartFile : file) {
			try {
				NoteFiles noteFiles = new NoteFiles();
				String url = FileUtil.uploadFile(multipartFile,deposeFilesDir);
				String fileName = multipartFile.getOriginalFilename();
				long fileSize = multipartFile.getSize();
				noteFiles.setFilePath(url);
				noteFiles.setFileNameCn(fileName);
				noteFiles.setFileSize(Integer.parseInt(String.valueOf(fileSize))/1024/1024);//单位M
				noteFiles.setCreateTime(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
				list.add(noteFiles);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		obj.setDocumentNum(getDocumentNo());
		obj.setStatus(1);
		obj.setDelStatus(1);
		noteManageDao.saveNoteInfo(obj,list);
		return "success";
		}else{
			return "failed";
		}
	}
	public String addPlanFlightInfo(NotePlanFlight obj) {
		if(obj!=null){
			noteManageDao.addPlanFlightInfo(obj);
			return "success";
		}
		return "failed";
		
	}
	public List<NoteFiles> findFilesByNoteId(Integer noteIds) {
		return noteManageDao.findFilesByNoteId(noteIds);
	}
	public List<NotePlanFlight> findFlightByNoteId(String noteId) {
		return noteManageDao.findFlightByNoteId(noteId);
	}
	public static String getOrderID()
	  {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMdd");
	    String date = sDateFormat.format(cal.getTime());
	    return date;
	  }
	public static String getYYYYMM()
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMM");
		String date = sDateFormat.format(cal.getTime());
		return date;
	}
	public static String getDocumentNo()
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMdd");
		String date = sDateFormat.format(cal.getTime());
		int str = (int) (Math.random() * 10000);
		String ss = date + String.valueOf(str);
		return ss;
	}
	public void delPlanFlightInfo(Integer id) {
		noteManageDao.executeHql("delete from NotePlanFlight where id=?", new Object[] { id });
		
	}
	public NotePlanInfo findById(Integer noteId) {
		return (NotePlanInfo) noteManageDao.findById(NotePlanInfo.class, noteId);
	}
	public String editNoteInfo(NotePlanInfo obj, MultipartFile[] file) {
		List<NoteFiles> list = new ArrayList<>();
		String deposeFilesDir = Constants.readValue("noteFilesDir")+getYYYYMM()+"/"+getOrderID()+"/";
		if(file!=null){
		for (MultipartFile multipartFile : file) {
			try {
				NoteFiles noteFiles = new NoteFiles();
				String url = FileUtil.uploadFile(multipartFile,deposeFilesDir);
				String fileName = multipartFile.getOriginalFilename();
				long fileSize = multipartFile.getSize();
				noteFiles.setFilePath(url);
				noteFiles.setFileNameCn(fileName);
				noteFiles.setFileSize(Integer.parseInt(String.valueOf(fileSize))/1024/1024);//单位M
				noteFiles.setCreateTime(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
				list.add(noteFiles);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		noteManageDao.editNoteInfo(obj,list);
		return "success";
		}else{
			return "failed";
		}
	}
	public void apply(Integer[] noteIds) {
		for (Integer noteId : noteIds) {
			noteManageDao.executeHql("update NotePlanInfo set status=0 where noteId=? ", new Object[] {noteId });
		}
	}
	public String ocrDistinguish(NoteFiles obj) {
		if(obj!=null&&obj.getFilePath()!=null){
			try {
				String ocrResult = OcrV3Util.ocrDistinguish(obj.getFilePath());
				obj.setOcrText(ocrResult);
				noteManageDao.executeHql("update NoteFiles set ocrText=? where id=? ", new Object[] {obj.getOcrText(),obj.getId()});
			} catch (IOException e) {
				log.error("照会文件识别失败!", e);
				e.printStackTrace();
			}
			return "success";
		}else{
			return "failed";
		}
	}
	public String translationNoteInfo(NoteFiles obj) {
		if(obj!=null&&obj.getFilePath()!=null){
			try {
				String transResult = FanyiV3Util.translationNoteInfo(obj.getOcrText());
				obj.setTranslationText(transResult);
				noteManageDao.executeHql("update NoteFiles set translationText=? where id=? ", new Object[] {obj.getTranslationText(),obj.getId()});
			} catch (IOException e) {
				log.error("照会文件翻译失败!", e);
				e.printStackTrace();
			}
			return "success";
		}else{
			return "failed";
		}
	}
	/**
	 * 批量导出照会信息zip压缩包
	 * @param noteIds
	 * @return
	 */
	public String exportNoteZip(Integer[] noteIds) {
		String exportZipPath="";
		String fileDirId=IdUtil.fastSimpleUUID();
		String exportFilePath = getExportFilePath(fileDirId);
		for(Integer noteId:noteIds) {
			createNoteFiles(noteId, exportFilePath);
		}
		exportZipPath=zipCivilFiles(exportFilePath);
		return exportZipPath;
	}
	/**
	 * zip压缩文件夹
	 * @param exportFilePath
	 * @return
	 */
	public String zipCivilFiles(String exportFilePath){
		String zipFilePath="";
		File exportFileDir=new File(exportFilePath);
		zipFilePath=rootPath+File.separator+"note"+File.separator+exportFileDir.getName()+".zip";
		File zipFile=ZipUtil.zip(exportFilePath,zipFilePath);
		zipFilePath="note"+File.separator+exportFileDir.getName()+".zip";
		return zipFilePath;
	}
	/**
	 * 根据此次导出生成一个文件夹
	 * @param fileDirId
	 * @return
	 */
	public String getExportFilePath(String fileDirId){
		String exportFilePath=rootPath+File.separator+"note"+ File.separator+fileDirId;
		File fileDir=new File(exportFilePath);
		if(!fileDir.exists()){
			fileDir.mkdirs();
		}
		return exportFilePath;
	}
	/**
	 * 根据导出目录文件夹和民航信息id，生成民航信息导出内容
	 * @param noteId
	 * @param exportFilePath
	 */
	public void createNoteFiles(Integer noteId,String exportFilePath) {
		String NoteFilePath=exportFilePath+File.separator+noteId+"";
		File NoteFileDir=new File(NoteFilePath);
		if(!NoteFileDir.exists()){
			NoteFileDir.mkdir();
		}
		CivilAviationVO civilAviationVO=civilAviationService.findCivilAviationVO(noteId);
		if(civilAviationVO!=null&&civilAviationVO.getPlanInfo()!=null){
			JSONObject planInfoJson= JSONUtil.parseObj(civilAviationVO.getPlanInfo());
			if(CollectionUtil.isNotEmpty(civilAviationVO.getPlanFlights())){
				JSONArray planFilghts=JSONUtil.parseArray(civilAviationVO.getPlanFlights());
				planInfoJson.putOpt("flightDetails",planFilghts);
			}
			try {
				String jsonContent = planInfoJson.toString();
				String jsonFileName = NoteFilePath + File.separator + noteId + ".json";
				File jsonFile = new File(jsonFileName);
				if (!jsonFile.exists()) {
					jsonFile.createNewFile();
				}
				//.json文件输出
				FileOutputStream jsonFileOut = new FileOutputStream(jsonFile);
				jsonFileOut.write(jsonContent.getBytes());
				jsonFileOut.close();
				//拷贝回复附件
				List<NoteFiles> list = civilAviationVO.getNoteFiles();
				for(int i=0;i<list.size();i++){
					NoteFiles noteFiles = list.get(i);
				if (StringUtils.isNotBlank(noteFiles.getFilePath())&&StringUtils.isNotBlank(noteFiles.getFileNameCn())) {
					String filePath = noteFiles.getFilePath();
					String fileName = noteFiles.getFileNameCn();
					Pattern filePattern = Pattern.compile("[\\\\/:*?\"<>|《》（）\n]");
					fileName=filePattern.matcher(fileName).replaceAll("");
					File replyFile = new File(filePath);
					if (replyFile.exists()) {
						FileOutputStream replyOut = new FileOutputStream(new File(NoteFilePath + File.separator + fileName));
						FileUtils.copyFile(replyFile, replyOut);
						replyOut.close();
					}
				}
				}
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}
}
