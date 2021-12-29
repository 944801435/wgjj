package com.brilliance.web.view.civilaviation;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brilliance.base.common.PagerVO;
import com.brilliance.base.model.internetModel.NoteCivilReply;
import com.brilliance.base.model.internetModel.NoteFiles;
import com.brilliance.base.model.internetModel.NotePlanFlight;
import com.brilliance.base.model.internetModel.NotePlanInfo;
import com.brilliance.base.util.FileUtil;
import com.brilliance.base.util.JsonUtil;
import com.brilliance.base.util.PropertiesUtil;

import javax.json.JsonObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 照会文书管理
 * @ClassName: NoteManageService
 * @Description: 
 * @author 
 * @date 
 */
@Service
@Transactional(rollbackFor={RuntimeException.class,Exception.class})
@Slf4j
@SuppressWarnings("unchecked")
public class CivilAviationService {
	//本地文件夹操作位置
	String rootPath = PropertiesUtil.getPropertyValue("file.upload.path","");
	@Autowired
	private CivilAviationDAO civilAviationDao;
	/**‰
	 * @throws Exception 
	 * 查询
	 * 描述
	 * @Title: findList 
	 * @author 
	 * @param pagerVO 查询入参
	 * @return
	 * PagerVO 返回类型 
	 * @throws
	 */
	public PagerVO findList(PagerVO<NotePlanInfo> pagerVO) throws Exception{
		
		List<Object> params = new ArrayList<Object>();
		
		StringBuffer hql = new StringBuffer("select  note.noteId from NotePlanInfo note  WHERE note.delStatus=1 and note.status in (2,3,4,5) ");
		if(pagerVO.getParams()!=null){
			if (!StringUtils.isBlank((String)pagerVO.getParams().get("documentNum"))) {
				hql.append(" and note.documentNum = ? ");
				params.add(pagerVO.getParams().get("documentNum"));
			}
			if (!StringUtils.isBlank((String)pagerVO.getParams().get("noteNo"))) {
				hql.append(" and note.noteNo like ? ");
				params.add("%" + pagerVO.getParams().get("noteNo") + "%");
			}
			if (StringUtils.isNotBlank((String)pagerVO.getParams().get("nationality"))) {
				hql.append(" and note.nationality like ? ");
				params.add("%" + pagerVO.getParams().get("nationality") + "%");
			}
			if(StringUtils.isNotBlank((String)pagerVO.getParams().get("flightTime"))){
				hql.append(" and str_to_date(note.flightTime, '%Y-%m-%d' )  =  str_to_date(?,'%Y-%m-%d') ");
				params.add(  pagerVO.getParams().get("flightTime") );

			}
			if (pagerVO.getParams().get("status")!=null&&(Integer)pagerVO.getParams().get("status")!=0) {
				hql.append(" and note.status= ? ");
				params.add(pagerVO.getParams().get("status"));
			}
		}

		hql.append(" order by note.createTime desc");
		pagerVO = civilAviationDao.findPaginated(hql.toString(), params.toArray(), pagerVO.getPageNo(), pagerVO.getPageSize());
		List<NotePlanInfo> datas = new ArrayList<NotePlanInfo>();
		for(Object o : pagerVO.getItems()){
			Integer noteId = (Integer) o;
			datas.add(findPlanInfoById(noteId));
		}
		pagerVO.setItems(datas);
		return pagerVO;
	}
	public PagerVO findListBySql(PagerVO<NotePlanInfo> pagerVO) throws Exception{

		List<Object> params = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer("" +
				" SELECT note.note_id from note_plan_info note left join ( " +
				" SELECT  " +
				"  rep.id,  " +
				"  rep.note_id,  " +
				"  rep.create_time as replyCreateTime  " +
				" FROM  " +
				"  note_civil_reply rep  " +
				" WHERE  " +
				"  1=1   " +
				" group by rep.note_id  " +
				" ORDER BY  " +
				"  timestamp(rep.create_time) DESC) t " +
				" ON t.note_id=note.note_id  " +
				" where   " +
				"  note.del_status = 1  " +
				"  AND note.status IN (2, 3, 4, 5) " +
				"");
		if(pagerVO.getParams()!=null){
			if (!StringUtils.isBlank((String)pagerVO.getParams().get("documentNum"))) {
				sql.append(" and note.document_num = ? ");
				params.add(pagerVO.getParams().get("documentNum"));
			}
			if (!StringUtils.isBlank((String)pagerVO.getParams().get("noteNo"))) {
				sql.append(" and note.note_no like ? ");
				params.add("%" + pagerVO.getParams().get("noteNo") + "%");
			}
			if (StringUtils.isNotBlank((String)pagerVO.getParams().get("nationality"))) {
				sql.append(" and note.nationality like ? ");
				params.add("%" + pagerVO.getParams().get("nationality") + "%");
			}
			if(StringUtils.isNotBlank((String)pagerVO.getParams().get("replyCreateTime"))){
				sql.append(" and str_to_date(t.replyCreateTime, '%Y-%m-%d' )  =  str_to_date(?,'%Y-%m-%d') ");
				params.add(  pagerVO.getParams().get("replyCreateTime") );

			}
			if (pagerVO.getParams().get("status")!=null&&(Integer)pagerVO.getParams().get("status")!=0) {
				sql.append(" and note.status= ? ");
				params.add(pagerVO.getParams().get("status"));
			}
		}

		sql.append(" order by note.create_time desc");
		List<Map<String, Object>> resultList = civilAviationDao.findListByPage_Sql_Map(sql.toString(), params.toArray(), pagerVO.getPageNo(), pagerVO.getPageSize());
		List<NotePlanInfo> datas = new ArrayList<NotePlanInfo>();
		for(Map<String,Object> o : resultList){
			Integer noteId = (Integer) o.get("note_id");
			datas.add(findPlanInfoById(noteId));
		}
		pagerVO.setItems(datas);
		return pagerVO;
	}
	/**
	 * 获取信息
	 * */
	public NotePlanInfo findPlanInfoById(Integer noteId){
		NotePlanInfo notePlanInfo=(NotePlanInfo) civilAviationDao.findById(NotePlanInfo.class, noteId);
		NoteCivilReply noteCivilReply=this.findNewCivilReplyByNoteId(noteId);
		if(noteCivilReply!=null){
			notePlanInfo.setReplyCreateTime(noteCivilReply.getCreateTime());
			notePlanInfo.setPermitNumber(noteCivilReply.getPermitNumber());
		}
		return notePlanInfo;
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
	public NoteCivilReply findCivilReplyByNoteId(Integer noteId) {
		String hql = "from NoteCivilReply cr where cr.createTime=(select MAX(createTime) from NoteCivilReply where noteId=?) ";
		NoteCivilReply civilMessage = (NoteCivilReply) civilAviationDao.findUnique(hql,  new Object[] {noteId});
		return civilMessage;
	}
	public NoteCivilReply findNewCivilReplyByNoteId(Integer noteId) {
		String sql = "" +
				"SELECT  " +
				"  *  " +
				"FROM  " +
				"  (  " +
				"    SELECT  " +
				"      rep.*  " +
				"    FROM  " +
				"      note_civil_reply rep  " +
				"    WHERE  " +
				"      1 = 1  " +
				"    GROUP BY  " +
				"      rep.note_id  " +
				"    ORDER BY  " +
				"      TIMESTAMP (rep.create_time) DESC  " +
				"  ) t  " +
				"WHERE  " +
				"  t.note_id = ?";
		Object[] civilMessage =  (Object[])civilAviationDao.findUnique_Sql(sql,  new Object[] {noteId});
		NoteCivilReply noteCivilReply=null;
		if(civilMessage!=null){
			noteCivilReply=parseCivilReplyByObjectArray(civilMessage);
		}
		return noteCivilReply;
	}
	NoteCivilReply parseCivilReplyByObjectArray(Object[] resultArray){
		NoteCivilReply civilMessage=NoteCivilReply.builder()
				.id((Integer)resultArray[0])
				.noteId((Integer)resultArray[1])
				.permitNumber((String)resultArray[2])
				.planTime((Date)resultArray[3])
				.upAirport((String)resultArray[4])
				.downAirport((String)resultArray[5])
				.planRoute((String)resultArray[6])
				.bakRoute((String)resultArray[7])
				.fileName((String)resultArray[8])
				.fileUrl((String)resultArray[9])
				.createTime((String)resultArray[10])
				.build();
		return civilMessage;
	}
	/**
	 * 查找飞机飞行计划信息
	 * @param noteId
	 * @return
	 */
	public List<NotePlanFlight> findPlanFlightByNoteId(Integer noteId) {
		String hql = "from NotePlanFlight pf where  pf.noteId=? ";
		List<NotePlanFlight> planFlights = (List<NotePlanFlight>) civilAviationDao.findList(hql,  new Object[] {noteId});
		return planFlights;
	}

	/**
	 * 查找照会文书列表
	 * @param noteId
	 * @return
	 */
	public List<NoteFiles> findNoteFileListByNoteId(Integer noteId){
		String hql = "from NoteFiles pf where  pf.noteId=? ";
		List<NoteFiles> noteFiles=civilAviationDao.findList(hql,new Object[] {noteId});
		return noteFiles;
	}
	/**
	 * 查找民航信息VO
	 * @param noteId
	 * @return
	 */
	public CivilAviationVO findCivilAviationVO(Integer noteId) {
		CivilAviationVO civilAviationVO= CivilAviationVO.builder()
				.noteCivilReply(this.findCivilReplyByNoteId(noteId))
				.planInfo(this.findPlanInfoById(noteId))
				.planFlights(this.findPlanFlightByNoteId(noteId))
				.noteFiles(this.findNoteFileListByNoteId(noteId))
				.build();
		return civilAviationVO;
	}

	/**
	 * 保存回复
	 * @param noteCivilReply
	 * @return
	 */
	public boolean saveCivilReply(NoteCivilReply noteCivilReply){
		boolean flag=false;
		try{
			civilAviationDao.save(noteCivilReply);
			flag=true;
		}catch (Exception e){
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 逻辑删除，将delStatus状态设置为"1"，代表已经删除
	 * @param noteId
	 * @return
	 */
	public boolean deletePlanInfoById(Integer noteId){
		boolean flag=false;
		NotePlanInfo notePlanInfo=this.findPlanInfoById(noteId);
		if(notePlanInfo!=null) {
			notePlanInfo.setDelStatus(0);
			civilAviationDao.update(notePlanInfo);
			flag=true;
		}
		return flag;
	}

	/**
	 * 根据此次民航导出生成一个文件夹
	 * @param fileDirId
	 * @return
	 */
	public String getExportFilePath(String fileDirId){
		String exportFilePath=rootPath+File.separator+"civilaviation"+ File.separator+fileDirId;
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
	public void createCivilFiles(Integer noteId,String exportFilePath) {
		String civilFilePath=exportFilePath+File.separator+noteId+"";
		File civilFileDir=new File(civilFilePath);
		if(!civilFileDir.exists()){
			civilFileDir.mkdir();
		}
		CivilAviationVO civilAviationVO=this.findCivilAviationVO(noteId);
		if(civilAviationVO!=null&&civilAviationVO.getNoteCivilReply()!=null){
			JSONObject planInfoJson= JSONUtil.parseObj(civilAviationVO.getPlanInfo());
			JSONObject civilReply= JSONUtil.parseObj(civilAviationVO.getNoteCivilReply());
			planInfoJson.putOpt("civilReply",civilReply);
			if(CollectionUtil.isNotEmpty(civilAviationVO.getPlanFlights())){
				JSONArray planFilghts=JSONUtil.parseArray(civilAviationVO.getPlanFlights());
				planInfoJson.putOpt("flightDetails",planFilghts);
			}
			try {
				String jsonContent = planInfoJson.toString();
				String jsonFileName = civilFilePath + File.separator + noteId + ".json";
				File jsonFile = new File(jsonFileName);
				if (!jsonFile.exists()) {
					jsonFile.createNewFile();
				}
				//.json文件输出
				FileOutputStream jsonFileOut = new FileOutputStream(jsonFile);
				jsonFileOut.write(jsonContent.getBytes());
				jsonFileOut.close();
				//拷贝回复附件
				if (StringUtils.isNotBlank(civilAviationVO.getNoteCivilReply().getFileUrl())) {
					String replyFileUrl = civilAviationVO.getNoteCivilReply().getFileUrl();
					String replyFileName = civilAviationVO.getNoteCivilReply().getFileName();
					if(StringUtils.isNotBlank(replyFileName)){
						Pattern filePattern = Pattern.compile("[\\\\/:*?\"<>|《》（）\n]");
						replyFileName=filePattern.matcher(replyFileName).replaceAll("");
						File replyFile = new File(rootPath , replyFileUrl);
						log.info("==> replyFile exists:"+replyFile.getAbsoluteFile().exists());
						log.info("==> replyFile exists:"+replyFile.exists());
						if (replyFile.getAbsoluteFile().exists()) {
							FileOutputStream replyOut = new FileOutputStream(new File(civilFilePath + File.separator + replyFileName));
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

	/**
	 * zip压缩文件夹
	 * @param exportFilePath
	 * @return
	 */
	public String zipCivilFiles(String exportFilePath){
		String zipFilePath="";
		File exportFileDir=new File(exportFilePath);
		zipFilePath=rootPath+File.separator+"civilaviation"+File.separator+exportFileDir.getName()+".zip";
		File zipFile=ZipUtil.zip(exportFilePath,zipFilePath);
		zipFilePath="civilaviation"+File.separator+exportFileDir.getName()+".zip";
		return zipFilePath;
	}

	/**
	 * 导出民航信息zip文件压缩包
	 * @param noteId
	 * @return
	 */
	public String exportCivilZip(Integer noteId){
		String exportZipPath="";
		String fileDirId=IdUtil.fastSimpleUUID();
		String exportFilePath=getExportFilePath(fileDirId);
		createCivilFiles(noteId,exportFilePath);
		exportZipPath=zipCivilFiles(exportFilePath);
		return exportZipPath;
	}

	/**
	 * 批量导出民航信息zip压缩包
	 * @param noteIds
	 * @return
	 */
	public String exportCivilZip(Integer[] noteIds){
		String exportZipPath="";
		String fileDirId=IdUtil.fastSimpleUUID();
		//创建年月文件夹
		Calendar date = Calendar.getInstance();
		DateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
		fileDirId = DateUtil.format(date.getTime(), dateFormat);
		String exportFilePath = getExportFilePath(fileDirId);
		for(Integer noteId:noteIds) {
			createCivilFiles(noteId, exportFilePath);
		}
		exportZipPath=zipCivilFiles(exportFilePath);
		return exportZipPath;
	}
}
