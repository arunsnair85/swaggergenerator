package com.swagger;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.gson.Gson;

@RestController
public class SwaggerGeneratorController {

	// Save the uploaded file to this folder
	@Value("{file.path}")
	private static String UPLOADED_FOLDER;
	/**
	 * Allows to upload the excel sheet from which data must be extracted to create
	 * the JSON file
	 * 
	 * @param excel sheet from which data needs to be extracted
	 * @return Response URL from which the data sheet excel can be download
	 */
	@PostMapping("/upload")
	public ResponseEntity uploadToLocalFileSystem(@RequestParam("file") MultipartFile file) {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Path path = Paths.get(UPLOADED_FOLDER + fileName);
//		try {
//			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(fileName)
				.toUriString();
		return ResponseEntity.ok(fileDownloadUri);
	}

	/**
	 * Allows to download the excel data sheet template
	 * 
	 * @param excel sheet from which data needs to be extracted
	 * @return Downloads the excel sheet to local directory
	 */
	@GetMapping("/download/{fileName:.+}")
	public ResponseEntity downloadFileFromLocal(@PathVariable String fileName) {
		Path path = Paths.get(UPLOADED_FOLDER + fileName);
		Resource resource = null;
		try {
			resource = new UrlResource(path.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping(path = "/generateswagger")
	public static String creteJSONFileFromExcel(@RequestParam("file") MultipartFile file,
			@RequestParam("details") String details) {
		try {
			Gson gson = new Gson();
			DataList dataList = gson.fromJson(details, DataList.class);
			InputStream fInputStream = file.getInputStream();
			Workbook excelWorkBook = new XSSFWorkbook(fInputStream);
			int totalSheetNumber = excelWorkBook.getNumberOfSheets();
			JSONObject jObjMain = new JSONObject();
			JSONObject jObjRequestType = new JSONObject();
			JSONObject jObjApiType = new JSONObject();
			JSONObject jObjPaths = new JSONObject();
			JSONObject jObjComponents = new JSONObject();

			for (int i = 0; i < totalSheetNumber; i++) {
				Sheet sheet = excelWorkBook.getSheetAt(i);
				String sheetName = sheet.getSheetName();

				if (sheetName.equalsIgnoreCase("Request")) {
					List<List<String>> sheetDataTable = ExcelUtilityClass.getSheetDataList(sheet);
					List<String> tagArray = new ArrayList<String>();
					tagArray.add("Invoice"); // ---> Read this from FE
					jObjRequestType.put("tags", tagArray);
					// --- Read the below field from FE-----
					jObjRequestType.put("description", dataList.description);
					jObjRequestType.put("summary", dataList.apiSummary);
					jObjRequestType.put("operationId", "get" + dataList.apiPath.substring(1) + "Invoices");
					jObjRequestType.put("deprecated", false);
					jObjRequestType.put("parameters", ExcelUtilityClass.getJSONStringFromExcel(sheetDataTable));
					jObjRequestType.put("responses",
							StatusCodeJsonCreationClass.getStatusCodes(dataList.apiPath.substring(1)));
					jObjApiType.put(dataList.requestMethod, jObjRequestType); // Read this from FE
					jObjPaths.put(dataList.apiPath, jObjApiType); // Read this from FE
				}
				// Components JSON creation starts here
				else if (sheetName.equalsIgnoreCase("Response")) {
					List<List<String>> sheetDataTable = ExcelUtilityClass.getSheetDataList(sheet);
					jObjComponents.put("securitySchemes", ResponseJsonCreationClass.getSecuritySchema());
					jObjComponents.put("schemas",
							ResponseJsonCreationClass.getSchemas(sheetDataTable, dataList.apiPath.substring(1)));
				}
			}

			// Method to retrieve the JSON object for constants
			jObjMain = ConstantJsonCreationClasss.createJsonForConstants("3.0.0", dataList.title, dataList.description,
					dataList.apiVersion, dataList.contactName, dataList.contactEmail, dataList.serverUrl, jObjPaths,
					jObjComponents);
			// Tear down
			String jsonString = jObjMain.toString();
			excelWorkBook.close();
			fInputStream.close();

			return jsonString;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}