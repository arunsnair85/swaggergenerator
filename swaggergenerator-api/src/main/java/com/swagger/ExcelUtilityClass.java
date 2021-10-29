package com.swagger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelUtilityClass {

	/**
	 * Reads the data sheet excel file
	 * 
	 * @param sheet from which data needs to be extracted.
	 * @return cell value as List<List<String>>
	 */
	
	private static final Logger LOGGER =LoggerFactory.getLogger(ExcelUtilityClass.class);
	
	public static List<List<String>> getSheetDataList(Sheet sheet) {
		List<List<String>> ret = new ArrayList<List<String>>();

		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();

		if (lastRowNum > 0) {
			for (int i = firstRowNum; i < lastRowNum + 1; i++) {
				Row row = sheet.getRow(i);

				int firstCellNum = row.getFirstCellNum();
				int lastCellNum = row.getLastCellNum();

				List<String> rowDataList = new ArrayList<String>();

				for (int j = firstCellNum; j < lastCellNum; j++) {
					Cell cell = row.getCell(j);
					//LOGGER.info("lastCellNum "+lastCellNum);
					//LOGGER.info(" current string  "+cell.getStringCellValue());
					LOGGER.info(" current cell "+j);
					CellType cellType = cell.getCellType();

					if (cellType == CellType.NUMERIC) {
						double numberValue = cell.getNumericCellValue();
						LOGGER.info(" current BOOLEAN  "+cell.getNumericCellValue());
						String stringCellValue = BigDecimal.valueOf(numberValue).toPlainString();

						rowDataList.add(stringCellValue);

					} else if (cellType == CellType.STRING) {
						LOGGER.info(" current string  "+cell.getStringCellValue());
						String cellValue = cell.getStringCellValue();
						rowDataList.add(cellValue);
					} else if (cellType == CellType.BOOLEAN) {
						LOGGER.info(" current BOOLEAN  "+cell.getBooleanCellValue());
						boolean numberValue = cell.getBooleanCellValue();

						String stringCellValue = String.valueOf(numberValue);

						rowDataList.add(stringCellValue);

					} else if (cellType == CellType.BLANK) {
						rowDataList.add("");
					}
				}
				ret.add(rowDataList);
			}
		}
		return ret;
	}

	/**
	 * Allows to write data extracted to a JSON file
	 * 
	 * @param JSON data that is extracted and name of file to which data is saved.
	 */
	public static void writeStringToFile(String data, String fileName) {
		try {
			String currentWorkingFolder = System.getProperty("user.dir");
			String filePathSeperator = System.getProperty("file.separator");
			String filePath = currentWorkingFolder + filePathSeperator + fileName;

			File file = new File(filePath);
			FileWriter fw = new FileWriter(file);
			BufferedWriter buffWriter = new BufferedWriter(fw);

			buffWriter.write(data);
			buffWriter.flush();
			buffWriter.close();

			LOGGER.info(filePath + " has been created.");

		} catch (IOException ex) {
			LOGGER.error("Exception", ex);
		}
	}

	/**
	 * Creates the JSON structure for Parameter tag
	 * 
	 * @param sheet from which data needs to be extracted.
	 * @return returns JSON array to be added to the parameter tag
	 */
	public static JSONArray getJSONStringFromExcel(List<List<String>> dataTable) {
		JSONArray jsonArray = new JSONArray();
		List<String> tags = new ArrayList<String>();

		tags.add("Invoices");
		if (dataTable != null) {
			int rowCount = dataTable.size();

			if (rowCount > 1) {

				jsonArray = new JSONArray();

				List<String> headerRow = dataTable.get(0);
				int columnCount = headerRow.size();

				for (int i = 1; i < rowCount; i++) {
					List<String> dataRow = dataTable.get(i);
					JSONObject rowJsonObject = new JSONObject();
					JSONObject jObjSchema = new JSONObject();

					for (int j = 0; j < columnCount; j++) {
						String columnName = headerRow.get(j);
						String columnValue = dataRow.get(j);

						if (columnName.equalsIgnoreCase("type")) {
							jObjSchema.put(columnName, columnValue);
							continue;
						}
						if (columnName.equalsIgnoreCase("format")) {
							if (!(columnValue.equalsIgnoreCase(""))) {
								jObjSchema.put(columnName, columnValue);
							}
						} else if (columnValue.equalsIgnoreCase("true")) {
							rowJsonObject.put(columnName, true);
						} else {
							rowJsonObject.put(columnName, columnValue);
						}
					}
					rowJsonObject.put("schema", jObjSchema);
					rowJsonObject.put("in", "query");
					jsonArray.put(rowJsonObject);
				}
			}
		}
		return jsonArray;
	}

	/**
	 * Creates the JSON structure for DDI(Parts, Vehicle) tag
	 * 
	 * @param sheet from which data needs to be extracted.
	 * @return returns JSON array to be added to the parameter tag
	 */
	public static JSONObject getJSONResponseStringFromExcel(List<List<String>> dataTable) {

		if (dataTable != null) {
			int rowCount = dataTable.size();

			JSONObject jObjDDI = new JSONObject();
			JSONObject jObjField = new JSONObject();
			JSONObject jObjSubField = new JSONObject();
			String prop = null;
			String parentValue = "";
			String columnName = null;
			String columnValue = null;
			if (rowCount > 1) {
				List<String> headerRow = dataTable.get(0);
				int columnCount = headerRow.size();

				for (int i = 1; i < rowCount; i++) {
					List<String> dataRow = dataTable.get(i);
					JSONObject rowJsonObject = new JSONObject();
					for (int j = 0; j < columnCount; j++) {
						columnName = headerRow.get(j);
						columnValue = dataRow.get(j);
						if (j == 0) {
							prop = columnValue;
						} else if (!(columnValue.equalsIgnoreCase("")) && (j != (columnCount - 1))) {
							rowJsonObject.put(columnName, columnValue);
						}
					}
					if (columnName.equalsIgnoreCase("parent") && !(columnValue.isEmpty())
							&& (!(parentValue.equalsIgnoreCase(columnValue))
									&& (!(parentValue.equalsIgnoreCase(""))))) {
						jObjSubField = new JSONObject();
						jObjSubField.put(prop, rowJsonObject);
						parentValue = columnValue;
						jObjField = getJSON(jObjSubField, jObjField, parentValue);

					} else if (columnName.equalsIgnoreCase("parent") && !(columnValue.equalsIgnoreCase(""))) {
						jObjSubField.put(prop, rowJsonObject);
						parentValue = columnValue;
						jObjField = getJSON(jObjSubField, jObjField, parentValue);
					} else {
						jObjField.put(prop, rowJsonObject);
					}
				}
				jObjDDI.put("properties", jObjField);
				jObjDDI.put("type", "object");
			}
			return jObjDDI;
		}
		return null;
	}

	/**
	 * Creates nested JSON's based on the parent and child fields
	 * 
	 * @param JSON Object of the parent and child tag.
	 * @return returns JSON object which is anchored to the Response Tag
	 */
	private static JSONObject getJSON(JSONObject jObjSubField, JSONObject jObjField, String parentValue) {
		JSONObject jObjProperty = new JSONObject();
		jObjProperty.put("properties", jObjSubField);
		jObjProperty.put("type", "object");
		jObjField.put(parentValue, jObjProperty);

		return jObjField;
	}
}
