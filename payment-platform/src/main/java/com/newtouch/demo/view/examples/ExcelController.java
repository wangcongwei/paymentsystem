package com.newtouch.demo.view.examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.newtouch.common.exception.ApplicationException;
import com.newtouch.common.model.PageData;
import com.newtouch.common.view.ReqModel;
import com.newtouch.common.view.RespModel;

/**
 * 一个excel，读取导出的例子
 * @author dongfeng.zhang
 * @version 1.0
 * @date 2015/6/1
 */

@RequestMapping(value = "/examples/excel")
@Controller
public class ExcelController {
	private static Logger logger = LoggerFactory.getLogger(ExcelController.class);
	/** 文件目录 */
	private String excelDir = "d:/";

	/**
	 * excel 文件数据读取返回
	 * 
	 * @param reqModel
	 * @param pageIndex
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
	public RespModel queryExcelData(@RequestBody ReqModel reqModel) {
		String filename = (String) reqModel.getQueryParams().get("excelFileName");
		List<Map<String, String>> content = new ArrayList<Map<String, String>>();
		Pageable pageRequest = reqModel.getPageable();
		Long total = 0L;
		Map<String, String> title = new HashMap<String, String>();
		try {
			InputStream inp = new FileInputStream(excelDir + File.separator + filename);
			HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(inp));
			ExcelExtractor extractor = new ExcelExtractor(wb);

			extractor.setFormulasNotResults(true);
			extractor.setIncludeSheetNames(false);
			String text = extractor.getText();
			extractor.close();

			String[] lines = text.split("\n");// 行解析

			String[] titleCells = lines[0].split("\t");// 列解析
			for (int index = 0; index < titleCells.length; index++) {
				title.put("cell" + index, titleCells[index]);
			}

			total = 0L + lines.length;
			int begin = pageRequest.getPageSize() * pageRequest.getPageNumber();
			int end = pageRequest.getPageSize() * (1 + pageRequest.getPageNumber());
			for (int i = begin; i < total - 1 && i < end; i++) {
				String row = lines[i + 1];
				String[] cells = row.split("\t");// 列解析
				Map<String, String> rowMap = new HashMap<String, String>();
				for (int index = 0; index < cells.length; index++) {
					rowMap.put("cell" + index, cells[index]);
				}
				content.add(rowMap);
			}
		} catch (FileNotFoundException fe) {
			logger.error("文件找不到", fe);
			throw new ApplicationException("Request.FileNotFound");
		} catch (Exception e) {
			logger.error("文件解析错误", e);
			throw new ApplicationException("Request.FilePaserError");
		}
		Map<String, Object> v = new HashMap<String, Object>();
		v.put("title", title);
		v.put("page", new PageData(content, pageRequest, total));
		return new RespModel(v);
	}

	/**
	 * 数据导出为excel文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public ModelAndView viewExcel(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		// 初始化测试数据
		List<String> list = new ArrayList<String>();
		Map<String, Object> model = new HashMap<String, Object>();
		list.add("test1");
		list.add("test2");
		model.put("data", list);
		String filename = "Excel文件下载aaaa.xls";
		filename = URLEncoder.encode(filename, "UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + filename);
		// 构建excel模型
		AbstractExcelView viewExcel = new AbstractExcelView() {
			@Override
			protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook,
					HttpServletRequest request, HttpServletResponse response) throws Exception {
				// CreationHelper createHelper = workbook.getCreationHelper();
				Sheet sheet = workbook.createSheet("new sheet");
				Row row0 = sheet.createRow(0);
				row0.createCell(0).setCellValue("什么什么的模版");
				row0.createCell(1);
				row0.createCell(2);

				sheet.addMergedRegion(new CellRangeAddress(0, // first row (0-based)
						0, // last row (0-based)
						0, // first column (0-based)
						2 // last column (0-based)
				));
				Row row1 = sheet.createRow(1);
				row1.createCell(0).setCellValue("保单号1");
				row1.createCell(1).setCellValue("保单号2");
				row1.createCell(2).setCellValue("保单号3");

				// List<String> data = (List<String>) model.get("data");
				/*
				 * for (int i = 1; i < data.size(); i++) { // Create a row and put some cells in it. Rows are 0 based.
				 * Row row = sheet.createRow(i); // Create a cell and put a value in it. Cell cell = row.createCell(0);
				 * cell.setCellValue(data.get(0)); }
				 */
			}
		};

		// 返回excel文件
		return new ModelAndView(viewExcel, model);
	}
}
