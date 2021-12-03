package com.uav.base.util.wordtopdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;
import com.spire.doc.Table;
import com.spire.doc.TableCell;
import com.spire.doc.TableRow;
import com.spire.doc.collections.ParagraphCollection;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.documents.UnderlineStyle;
import com.spire.doc.fields.TextRange;
import com.uav.base.common.Constants;
import com.uav.base.util.FileUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class WordToPDFUtil {
	@Autowired
	private ResourceLoader resourceLoader;
	private static File xukeFile;
	private static File shenpiFile;

	@PostConstruct
	public void init() {
		try {
			Resource temp1 = resourceLoader.getResource("classpath:xuke.docx");
			Resource temp2 = resourceLoader.getResource("classpath:shenpi.docx");
			xukeFile = temp1.getFile();
			shenpiFile = temp2.getFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成领空许可word文档
	 * @param wordParam
	 * @param list
	 * @param fileName
	 * @param contentType
	 * @param userId
	 * @return
	 * @throws IllegalAccessException 
	 * @throws FileNotFoundException 
	 */
	public static String createWordByXuke(WordParam wordParam, List<List<HX>> list, String fileName, String contentType, String userId)
			throws FileNotFoundException, IllegalAccessException {
		return docReplace(wordParam, list, fileName, contentType, userId, xukeFile);
	}

	/**
	 * 生成审批表word文档
	 * @param wordParam
	 * @param list
	 * @param fileName
	 * @param contentType
	 * @param userId
	 * @return
	 * @throws IllegalAccessException 
	 * @throws FileNotFoundException 
	 */
	public static String createWordByShenpi(WordParam wordParam, List<List<HX>> list, String fileName, String contentType, String userId)
			throws FileNotFoundException, IllegalAccessException {
		return docReplace(wordParam, list, fileName, contentType, userId, shenpiFile);
	}

	public static String docReplace(WordParam wordParam, List<List<HX>> list, String fileName, String contentType, String userId, File inPath)
			throws FileNotFoundException, IllegalAccessException {
		Map<String, Object> map = objectToMap(wordParam);
		String str = "";
		Document document = new Document();
		document.loadFromStream(new FileInputStream(inPath), FileFormat.Docx);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// 获取Section
		Section section = document.getSections().get(0);
		Table table = section.getTables().get(0);
		for (int i = 0; i < table.getRows().getCount(); i++) {
			// 获取表格行
			TableRow row = table.getRows().get(i);
			// 遍历每行中的每个单元格
			for (int j = 0; j < row.getCells().getCount(); j++) {
				// 获取单元格
				TableCell cell = row.getCells().get(j);
				ParagraphCollection paragraphs = cell.getParagraphs();
				for (int p = 0; p < paragraphs.getCount(); p++) {
					Paragraph paragraph = paragraphs.get(p);
					String text = paragraph.getText();
					if (text.equals("${fxhx}")) {
						paragraph.setText("");
						for (List<HX> hxs : list) {
							for (HX hx : hxs) {
								switch (hx.getType()) {
								case Constants.WORD_HX_TYPE1: {
									paragraph.appendText(hx.getValue());
									break;
								}
								case Constants.WORD_HX_TYPE2: {
									TextRange textRange = paragraph.appendText(hx.getValue());
									textRange.getCharacterFormat().setUnderlineStyle(UnderlineStyle.Dash);
									break;
								}
								case Constants.WORD_HX_TYPE3: {
									paragraph.appendText("↑");
									break;
								}
								case Constants.WORD_HX_TYPE4: {
									paragraph.appendText("↓");
									break;
								}
								}
//                                if (Constants.WORD_HX_TYPE1.equals(hx.getType())) {
//                                    paragraph.appendText(hx.getValue());
//                                }else if (Constants.WORD_HX_TYPE2.equals(hx.getType())) {
//                                    TextRange textRange = paragraph.appendText(hx.getValue());
//                                    textRange.getCharacterFormat().setUnderlineStyle(UnderlineStyle.Dash);
//                                }else if (Constants.WORD_HX_TYPE3.equals(hx.getType())) {
//                                    paragraph.appendText("↑");
//                                }
//                                else if (Constants.WORD_HX_TYPE4.equals(hx.getType())) {
//                                    paragraph.appendText("↓");
//                                }
							}
							paragraph.appendText("\r");// 每条航线结束后换行
						}
					}
				}
			}
		}
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue().toString();
			// 替换全部文本
			document.replace("${" + key + "}", value, false, false);
		}
//        document.saveToFile("D://test/xuke101.docx", FileFormat.Docx);
		try {
			document.saveToStream(bos, FileFormat.Docx);
			byte[] bs = bos.toByteArray();
			str = FileUtil.uploadToDb(bs.length, bs, fileName, contentType, userId);
		} finally {
			try {
				if (document != null)
					document.close();
				if (bos != null)
					bos.close();
			} catch (Exception e) {
			}
		}
		return str;
	}

	/**
	 * word转pdf格式
	 * @param in
	 * @param fileName
	 * @param contentType
	 * @param userId
	 * @return
	 */
	public static String wordToPdf(InputStream in, String fileName, String contentType, String userId) {
		String str = "";
		Document document = null;
		ByteArrayOutputStream bos = null;
		try {
			document = new Document();
			document.loadFromStream(in, FileFormat.Doc);
			bos = new ByteArrayOutputStream();
			document.saveToStream(bos, FileFormat.PDF);
			byte[] bs = bos.toByteArray();
			str = FileUtil.uploadToDb(bs.length, bs, fileName, contentType, userId);
		} finally {
			try {
				if (in != null)
					in.close();
				if (document != null)
					document.close();
				if (bos != null)
					bos.close();
			} catch (Exception e) {
			}
		}
		return str;
	}

	/**
	 * word转pdf格式
	 * 
	 * @Title: wordToPdf
	 * @author mq
	 * @date 2021年11月15日 下午5:43:54
	 * @param bs
	 * @param fileName
	 * @param contentType
	 * @param userId
	 * @return
	 */
	public static String wordToPdf(byte[] bs, String fileName, String contentType, String userId) {
		String str = "";
		Document document = null;
		ByteArrayOutputStream bos = null;
		InputStream in = null;
		try {
			in = FileUtil.byte2Input(bs);
			document = new Document();
			document.loadFromStream(in, FileFormat.Doc);
			bos = new ByteArrayOutputStream();
			document.saveToStream(bos, FileFormat.PDF);
			str = FileUtil.uploadToDb(bs.length, bs, fileName, contentType, userId);
		} finally {
			try {
				if (in != null)
					in.close();
				if (bos != null)
					bos.close();
				if (document != null)
					document.close();
			} catch (Exception e) {
			}
		}
		return str;
	}

	public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
		Map<String, Object> map = new HashMap<String, Object>();
		Class<?> clazz = obj.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			String fieldName = field.getName();
			Object value = field.get(obj).toString();
			map.put(fieldName, value);
		}
		return map;
	}

	@SuppressWarnings("resource")
	public void CreatWordByModel(String tmpFile, Map<String, String> contentMap, List<Map<String, String>> list, String exportFile) throws Exception {

		InputStream in = null;
		in = new FileInputStream(new File(tmpFile));

		XWPFDocument document = new XWPFDocument(in);
		Iterator<XWPFTable> it = document.getTablesIterator();
		// 表格内容替换添加
		while (it.hasNext()) {
			XWPFTable table = it.next();
			int rcount = table.getNumberOfRows();
			for (int i = 0; i < rcount; i++) {
				XWPFTableRow row = table.getRow(i);
				List<XWPFTableCell> cells = row.getTableCells();
				for (XWPFTableCell cell : cells) {
					if (cell.getText().equals("${fxhx}")) {
						cell.removeParagraph(0);
						XWPFParagraph pIO = cell.addParagraph();
						pIO.setIndentationLeft(567);
						pIO.setIndentationHanging(567);
						for (Map<String, String> m : list) {
							if (m.get("type").equals("1")) {
								XWPFRun rIO = pIO.createRun();
								rIO.setText((String) m.get("value"));
							} else if (m.get("type").equals("2")) {
								XWPFRun rIO = pIO.createRun();
								rIO.setUnderline(UnderlinePatterns.DASH);
								rIO.setText((String) m.get("value"));
							} else if (m.get("type").equals("3")) {
								XWPFRun rIO = pIO.createRun();
								rIO.setText("↑");
							} else if (m.get("type").equals("4")) {
								XWPFRun rIO = pIO.createRun();
								rIO.setText("↓");
							}
						}
					}
				}
			}
		}
		// 导出到文件
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			document.write((OutputStream) byteArrayOutputStream);
			OutputStream outputStream = new FileOutputStream(exportFile);
			outputStream.write(byteArrayOutputStream.toByteArray());
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 替换全部文本
	 * @param textMap 替换信息
	 * @param paragraphs 段落
	 */
	public void replaceAllTexts(Map<String, String> textMap, List<XWPFParagraph> paragraphs) {
		Set<Map.Entry<String, String>> textEntrySet = textMap.entrySet();
		for (Map.Entry<String, String> entry : textEntrySet) {
			String key = entry.getKey();
			String value = entry.getValue();
			// 替换全部文本
			replaceAllTexts(key, value, paragraphs);
		}
	}

	/**
	 * 替换全部文本
	 * @param key 替换键
	 * @param value 替换值
	 * @param paragraphs 段落
	 */
	public void replaceAllTexts(String key, String value, List<XWPFParagraph> paragraphs) {
		for (XWPFParagraph paragraph : paragraphs) {
			// 待替换文本
			String text = paragraph.getText();
			if (StringUtils.isNotEmpty(text) && text.indexOf(key) != -1) {
				List<XWPFRun> runs = paragraph.getRuns();
				// 只保留第一个Run
//                for (int i = (runs.size() - 1); i > 0; i--) {
//                    paragraph.removeRun(i);
//                }
//                paragraph.removeRun(i);
				if (text.equals(key)) {
					text = text.replace(key, value);
				}
//                text.replace("${" + key + "}", value);
//                text = text.replace(key, value);
				runs.get(0).setText(text);
			}
		}
	}

	public List<XWPFParagraph> getAllParagraphs(XWPFDocument document) {
		List<XWPFParagraph> paragraphs = new ArrayList<XWPFParagraph>();
		// 列表外段落
		paragraphs.addAll(document.getParagraphs());
		// 列表内段落
		List<XWPFTable> tables = document.getTables();
		for (XWPFTable table : tables) {
			List<XWPFTableRow> rows = table.getRows();
			for (XWPFTableRow row : rows) {
				List<XWPFTableCell> cells = row.getTableCells();
				for (XWPFTableCell cell : cells) {
					paragraphs.addAll(cell.getParagraphs());
				}
			}
		}
		return paragraphs;
	}

	public static void test() throws FileNotFoundException, IllegalAccessException {
		WordParam param = new WordParam();
		param.setPc("2020年402号");
		param.setDw("外交部欧洲司");
		param.setXm("李云彤");
		param.setDh("18810880808");
		param.setZhh("波兰/2990");
		param.setJx("GLF5/（军）");
		param.setJs("1");
		param.setFxrq("2021年8月11日、13日");
		param.setRwmd("波兰军机来华接运重病国民回国");
		param.setTzdw("外交部欧洲司\n民用航空局\n空参航管局\n空军7分队");
		param.setCbdw("外交部欧洲司");
		param.setLxr("李某某");
		param.setLxdh("18888888888");
		param.setCbyj("外方申请的飞行航线符合批准的外国军机临时进出我国领空航线，已对外开放，如无不妥，建议回复外交部我们没有不同意见。");

		List<List<HX>> lists = new ArrayList<>();
		List<HX> list = new ArrayList<>();
		HX listMap1 = new HX();
		listMap1.setType("1");
		listMap1.setValue("11日：波兰华沙肖邦机场");
		HX listMap2 = new HX();
		listMap2.setType("3");
		HX listMap3 = new HX();
		listMap3.setType("1");
		listMap3.setValue("—TELOK 入境—");
		HX listMap4 = new HX();
		listMap4.setType("2");
		listMap4.setValue("A345、A588、W107、A326、G597—AGAV0");
		HX listMap5 = new HX();
		listMap5.setType("1");
		listMap5.setValue("出境—韩国首尔仁川机场");
		HX listMap6 = new HX();
		listMap6.setType("4");
		list.add(listMap1);
		list.add(listMap2);
		list.add(listMap3);
		list.add(listMap4);
		list.add(listMap5);
		list.add(listMap6);
		lists.add(list);
		lists.add(list);
		lists.add(list);
		lists.add(list);
		lists.add(list);
		lists.add(list);
		String str = docReplace(param, lists, "", "", "", xukeFile);
		System.out.println(str);
	}
}
