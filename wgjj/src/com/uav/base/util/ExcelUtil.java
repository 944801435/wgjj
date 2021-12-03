package com.uav.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	public static boolean isExcel2003(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls)$");
	}

	public static boolean isExcel2007(String filePath) {
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}

	public static List<List<Object>> readExcel(File file) throws Exception {
		if (!file.exists()) {
			throw new Exception("找不到文件");
		}
		List<List<Object>> list = new LinkedList<List<Object>>();
		Workbook xwb = null;
		try {
			if (isExcel2007(file.getPath())) {
				xwb = new XSSFWorkbook(new FileInputStream(file));
			} else {
				xwb = new HSSFWorkbook(new FileInputStream(file));
			}
			// 读取第一张表格内容
			Sheet sheet = xwb.getSheetAt(0);
			Row row = null;
			Cell cell = null;
			for (int i = (sheet.getFirstRowNum()); i <= (sheet.getPhysicalNumberOfRows() - 1); i++) {
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				List<Object> linked = new LinkedList<Object>();
				for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
					Object value = null;
					cell = row.getCell(j);
					if (cell == null) {
						linked.add(null);
						continue;
					}

					switch (cell.getCellTypeEnum()) {
					case STRING:
						// String类型返回String数据
						value = cell.getStringCellValue();
						break;
					case NUMERIC:
						// cell.getCellStyle().getDataFormatString()
						value = cell.getNumericCellValue();
						break;
					case BOOLEAN:
						// 布尔类型
						value = cell.getBooleanCellValue();
						break;
					case BLANK:
						// 空单元格
						break;
					default:
						value = cell.toString();
					}
					if (value != null && !value.equals("")) {
						// 单元格不为空，则加入列表
						linked.add(value);
					}
				}
				if (linked.size() != 0) {
					list.add(linked);
				}
			}
		} catch (Exception e) {
			if (xwb != null) {
				xwb.close();
			}
		}
		return list;

	}

	/**
	 * 参数格式 { key:sheet名字 value:{[表头1,表头2,表头3],[数据1,数据2,数据3]} }
	 * @throws IOException 
	 */
	public static void init(Map<String, List<Object[]>> map, HttpServletResponse response, String fileName) throws IOException {
		XSSFWorkbook wb = new XSSFWorkbook();
		try {
			Map<String, CellStyle> styles = createStyles(wb);
			for (String key : map.keySet()) {
				XSSFSheet sheet = wb.createSheet(key);
				for (int i = 0; i < map.get(key).size(); i++) {
					Object[] objs = map.get(key).get(i);
					XSSFRow row = sheet.createRow(i);
					row.setHeightInPoints(20);
					for (int j = 0; j < objs.length; j++) {
						XSSFCell cell = row.createCell(j);
						cell.setCellValue(objs[j] == null ? "" : objs[j].toString());
						if (i == 0) {
							cell.setCellStyle(styles.get("header"));
						} else {
							cell.setCellStyle(styles.get("data"));
						}
						int length = cell.getStringCellValue().length();
						int byteLength = cell.getStringCellValue().getBytes().length;
						byteLength = byteLength == length ? (byteLength - 3) * 2 : byteLength;
						byteLength = byteLength < 128 ? byteLength : 128;
						if (sheet.getColumnWidth(j) < byteLength * 256) {
							sheet.setColumnWidth(j, byteLength * 256);
						}
					}
				}
			}
			write(response, fileName, wb);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (wb != null) {
				wb.close();
			}
		}
	}

	public static void main(String[] args) {
		String s = "2020-05-25 11:39:26";
		System.out.println(s.getBytes().length);
		System.out.println(s.length());
		System.out.println("116°35′31″,39°47′15″".getBytes().length);
		System.out.println("°".getBytes().length);
		System.out.println("′".getBytes().length);
		System.out.println("″".getBytes().length);
	}

	/**
	 * 创建表格样式
	 * 
	 * @param wb 工作薄对象
	 * @return 样式列表
	 */
	private static Map<String, CellStyle> createStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();

		CellStyle style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		Font headerFont = wb.createFont();
		headerFont.setFontName("Arial");
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setBold(true);
		style.setFont(headerFont);
		styles.put("header", style);

		style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setWrapText(true);
		Font dataFont = wb.createFont();
		dataFont.setFontName("Arial");
		dataFont.setFontHeightInPoints((short) 11);
		style.setFont(dataFont);
		styles.put("data", style);

		return styles;
	}

	/**
	 * 输出到客户端
	 * 
	 * @param fileName 输出文件名
	 */
	private static void write(HttpServletResponse response, String fileName, XSSFWorkbook wb) throws IOException {
		response.reset();
		response.setContentType("application/octet-stream; charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(fileName));
		wb.write(response.getOutputStream());
	}
}
