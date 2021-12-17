package com.brilliance.web.view.flyPlan;

import java.io.FileNotFoundException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brilliance.base.common.Constants;
import com.brilliance.base.model.FlyPlan;
import com.brilliance.base.model.FlyPlanFlight;
import com.brilliance.base.model.SysFile;
import com.brilliance.base.model.UnitDict;
import com.brilliance.base.util.DateUtil;
import com.brilliance.base.util.wordtopdf.HX;
import com.brilliance.base.util.wordtopdf.WordParam;
import com.brilliance.base.util.wordtopdf.WordToPDFUtil;
import com.brilliance.web.view.system.sysFile.SysFileService;
import com.brilliance.web.view.unitDict.UnitDictService;

/**
 * 创建审批表和许可函文件工具类
 * 
 * @Title: CreateFileUtil.java
 * @author mq  
 * @date 2021年11月15日 下午4:09:08
 */
@Service
@Transactional(rollbackFor = Exception.class)
@SuppressWarnings({ "deprecation" })
public class CreateFileUtil {
	
	@Autowired
	private UnitDictService unitDictService;
	@Autowired
	private SysFileService sysFileService;

	private static final String SHENPI_WORD_FILE_NAME = "外国军机临时进出我国领空审批表.docx";
	private static final String XUKE_WORD_FILE_NAME = "外国军机临时进出我国领空许可.docx";
	private static final String XUKE_PDF_FILE_NAME = "外国军机临时进出我国领空许可.pdf";
	private static String WORD_CONTENT_TYPE = "";
	private static String PDF_CONTENT_TYPE = "";
	static {
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		WORD_CONTENT_TYPE = fileNameMap.getContentTypeFor(SHENPI_WORD_FILE_NAME);
		WORD_CONTENT_TYPE = fileNameMap.getContentTypeFor(XUKE_PDF_FILE_NAME);
	}

	/**
	 * 创建审批文件
	 * 
	 * @Title: createAppFile
	 * @author mq
	 * @date 2021年11月15日 下午4:29:13
	 * @param plan
	 * @return
	 * @throws IllegalAccessException 
	 * @throws FileNotFoundException 
	 */
	public String createAppFile(FlyPlan plan, List<FlyPlanFlight> flightList, String[] flightBodys) throws FileNotFoundException, IllegalAccessException {
		List<List<HX>> hxsList = getHxList(flightList, flightBodys);
		WordParam wordParam = getWordParam(plan, flightList);
		return WordToPDFUtil.createWordByShenpi(wordParam, hxsList, SHENPI_WORD_FILE_NAME, WORD_CONTENT_TYPE, plan.getAppOptUser());
	}

	/**
	 * 创建许可函WORD
	 * 
	 * @Title: createRptFile
	 * @author mq
	 * @date 2021年11月15日 下午5:36:40
	 * @param plan
	 * @param flightBodys
	 * @return
	 * @throws IllegalAccessException 
	 * @throws FileNotFoundException 
	 */
	public String createRptWordFile(FlyPlan plan, List<FlyPlanFlight> flightList, String[] flightBodys) throws FileNotFoundException, IllegalAccessException {
		List<List<HX>> hxsList = getHxList(flightList, flightBodys);
		WordParam wordParam = getWordParam(plan, flightList);
		List<UnitDict> dictList = unitDictService.getUsedList();
		List<String> unitNameList = dictList.stream().map(item -> item.getUnitName()).distinct().collect(Collectors.toList());
		wordParam.setTzdw(String.join("\n", unitNameList));
		return WordToPDFUtil.createWordByXuke(wordParam, hxsList, XUKE_WORD_FILE_NAME, WORD_CONTENT_TYPE, plan.getAppOptUser());
	}

	/**
	 * 创建许可函PDF
	 * 
	 * @Title: createRptPdfFile
	 * @author mq
	 * @date 2021年11月15日 下午5:52:05
	 * @param fileId
	 * @param userId
	 * @return
	 */
	public String createRptPdfFile(String fileId, String userId) {
		SysFile file = sysFileService.findById(fileId);
		return WordToPDFUtil.wordToPdf(file.getFileBytes(), XUKE_PDF_FILE_NAME, PDF_CONTENT_TYPE, userId);
	}

	private List<List<HX>> getHxList(List<FlyPlanFlight> flightList, String[] flightBodys) {
		List<List<HX>> hxsList = new ArrayList<List<HX>>();
		for (int i = 0; i < flightList.size(); i++) {
			FlyPlanFlight flight = flightList.get(i);
			String flightBody = flight.getFlightBody();
			if (flightBodys != null && flightBodys.length == flightList.size()) {
				flightBody = flightBodys[i];
			}
			List<HX> hxList = new ArrayList<HX>();
			String value = flight.getPlanDate().substring(8) + "日：" + flight.getUpAirport() + "机场";
			HX hx = new HX(Constants.WORD_HX_TYPE1, value);
			hxList.add(hx);
			hx = new HX(Constants.WORD_HX_TYPE3, "");
			hxList.add(hx);
			hx = new HX(Constants.WORD_HX_TYPE1, "—" + flight.getInPointIdent() + "入境—");
			hxList.add(hx);
			hx = new HX(Constants.WORD_HX_TYPE2, flightBody);
			hxList.add(hx);
			hx = new HX(Constants.WORD_HX_TYPE1, "—" + flight.getOutPointIdent() + "出境—");
			hxList.add(hx);
			hx = new HX(Constants.WORD_HX_TYPE1, flight.getDownAirport() + "机场");
			hxList.add(hx);
			hx = new HX(Constants.WORD_HX_TYPE4, "");
			hxList.add(hx);
			hxsList.add(hxList);
		}
		return hxsList;
	}

	private WordParam getWordParam(FlyPlan plan, List<FlyPlanFlight> flightList) {
		Date date = DateUtil.getNowDate();
		String pc = date.getYear() + " " + plan.getRegNo() + "号";
		String zhh = plan.getNationality() + "/" + plan.getNoteSeq();
		// 飞行日期
		List<String> dayList = flightList.stream().map(item -> item.getPlanDate()).distinct().collect(Collectors.toList());
		String fxrq = formatFxrq(dayList);
		return new WordParam(pc, plan.getLetterUnit(), plan.getPersonName(), plan.getTelNo(), zhh, plan.getModel(), "1", fxrq, plan.getMission());
	}

	private String formatFxrq(List<String> dayList) {
		// 按年份月份分解日期
		Map<String, List<String>> yearDayMap = new LinkedHashMap<String, List<String>>();
		for (String day : dayList) {
			String yyyy = day.substring(0, 4);
			String mm = day.substring(5, 7);
			String yyyyMM = yyyy + "年" + mm + "月";
			if (!yearDayMap.containsKey(yyyyMM)) {
				yearDayMap.put(yyyyMM, new ArrayList<String>());
			}
			yearDayMap.get(yyyyMM).add(day.substring(8) + "日");
		}
		List<String> fxrqList = new ArrayList<String>();
		for (Entry<String, List<String>> entry : yearDayMap.entrySet()) {
			fxrqList.add(entry.getKey() + String.join("、", entry.getValue()));
		}
		return String.join("，", fxrqList);
	}

	public static void main(String[] args) {
		String[] dayList = new String[] { "2021-11-10", "2021-11-19", "2021-12-10", };
		Map<String, List<String>> yearDayMap = new LinkedHashMap<String, List<String>>();
		for (String day : dayList) {
			String yyyy = day.substring(0, 4);
			String mm = day.substring(5, 7);
			String yyyyMM = yyyy + "年" + mm + "月";
			if (!yearDayMap.containsKey(yyyyMM)) {
				yearDayMap.put(yyyyMM, new ArrayList<String>());
			}
			yearDayMap.get(yyyyMM).add(day.substring(8) + "日");
		}
		System.out.println(yearDayMap);
	}
}
