package com.swagger;

import java.util.List;

import org.json.JSONObject;

public class ResponseJsonCreationClass {

	/**
	 * Allows to create elements of SECURITY SCHEMA tag
	 * 
	 * @return returns JSON object which is anchored to the Main Tag
	 */
	public static JSONObject getSecuritySchema() {
		JSONObject jObjectclientCredentials = new JSONObject();
		JSONObject jObjectFlow = new JSONObject();
		JSONObject jObjectClientCredentials = new JSONObject();
		JSONObject jObjectSecuritySchemes = new JSONObject();
		JSONObject jsonScopeArray = new JSONObject();

		JSONObject jObjectOpenId = new JSONObject();

		jObjectclientCredentials.put("tokenUrl", "/identity/v2/token");
		jObjectclientCredentials.put("scopes", jsonScopeArray);
		jObjectFlow.put("clientCredentials", jObjectclientCredentials);
		jObjectClientCredentials.put("flows", jObjectFlow);
		jObjectClientCredentials.put("type", "oauth2");
		jObjectClientCredentials.put("description", "Client Credentials Grant Type via OAuth 2.0");
		jObjectOpenId.put("type", "openIdConnect");
		// Get the below url from FE
		jObjectOpenId.put("openIdConnectUrl", "****");
		jObjectSecuritySchemes.put("ClientCredentials", jObjectClientCredentials);
		jObjectSecuritySchemes.put("openId", jObjectOpenId);

		return jObjectSecuritySchemes;
	}

	/**
	 * Allows to create elements of SCHEMA tag
	 * 
	 * @return returns JSON object which is anchored to the Main Tag
	 */
	public static JSONObject getSchemas(List<List<String>> dataTable, String projectName) {
				
		JSONObject jObjectSchemas = new JSONObject();
		jObjectSchemas.put("Error", getError());
		jObjectSchemas.put("BaseError", getBaseError());
		jObjectSchemas.put("DefaultData", getDefaultData());
		jObjectSchemas.put("DefaultErrorData", getDefaultErrorData());
		jObjectSchemas.put(projectName + "Response", getDDIResponse(projectName));
		jObjectSchemas.put(projectName + "BaseResult", getDDIBaseResult(projectName));
		jObjectSchemas.put("BaseHeader", getBaseHeader());
		jObjectSchemas.put(projectName + "ResponseBody", getDDIResponseBody(projectName));
		jObjectSchemas.put(projectName, ExcelUtilityClass.getJSONResponseStringFromExcel(dataTable));

		return jObjectSchemas;

	}

	/**
	 * Allows to create elements of ERROR tag
	 * 
	 * @return returns JSON object which is anchored to the ERROR Tag
	 */
	private static JSONObject getError() {
		JSONObject jObjectInfo = new JSONObject();
		jObjectInfo.put("type", "string");
		jObjectInfo.put("description", "Additional details of the error");
		JSONObject jObjectMessage = new JSONObject();
		jObjectMessage.put("type", "string");
		jObjectMessage.put("description", "Summary of the error message");
		JSONObject jObjectCode = new JSONObject();
		jObjectCode.put("type", "string");
		jObjectCode.put("description", "Unique  reference number of the error");
		JSONObject jObjectProperties = new JSONObject();
		jObjectProperties.put("info", jObjectInfo);
		jObjectProperties.put("message", jObjectMessage);
		jObjectProperties.put("code", jObjectCode);
		JSONObject jObjectError = new JSONObject();
		jObjectError.put("properties", jObjectProperties);

		return jObjectError;
	}

	/**
	 * Allows to create elements of BASE ERROR tag
	 * 
	 * @return returns JSON object which is anchored to the BASE ERROR Tag
	 */
	private static JSONObject getBaseError() {
		JSONObject jObjectSchema = new JSONObject();
		jObjectSchema.put("$ref", "#/components/schemas/DefaultErrorData");

		return jObjectSchema;
	}

	/**
	 * Allows to create elements of ERROR tag
	 * 
	 * @return returns JSON object which is anchored to the ERROR Tag
	 */
	private static JSONObject getDefaultErrorData() {
		JSONObject jObjectOperationName = new JSONObject();
		jObjectOperationName.put("type", "string");
		jObjectOperationName.put("description", "Name of the operation performed");
		JSONObject jObjectTransactionId = new JSONObject();
		jObjectTransactionId.put("type", "string");
		jObjectTransactionId.put("description", "Unique id to identify the transaction");
		JSONObject jObjectDate = new JSONObject();
		jObjectDate.put("type", "string");
		jObjectDate.put("format", "date-time");
		jObjectDate.put("description", "Timestamp of operation in YYYY-MM-DDTHH:MM:SSZ format");
		JSONObject jObjectStatus = new JSONObject();
		jObjectStatus.put("type", "string");
		jObjectStatus.put("description", "Response status from API");
		JSONObject jObjectErrors = new JSONObject();
		jObjectErrors.put("type", "array");
		JSONObject jObjectErrorsItems = new JSONObject();
		jObjectErrorsItems.put("$ref", "#/components/schemas/Error");
		jObjectErrors.put("items", jObjectErrorsItems);
		JSONObject jObjectProperties = new JSONObject();
		jObjectProperties.put("info", jObjectOperationName);
		jObjectProperties.put("message", jObjectTransactionId);
		jObjectProperties.put("code", jObjectDate);
		jObjectProperties.put("status", jObjectStatus);
		jObjectProperties.put("errors", jObjectErrors);
		JSONObject jObjectError = new JSONObject();
		jObjectError.put("properties", jObjectProperties);

		return jObjectError;
	}

	/**
	 * Allows to create elements of ERROR tag
	 * 
	 * @return returns JSON object which is anchored to the ERROR Tag
	 */
	private static JSONObject getDefaultData() {
		JSONObject jObjectOperationName = new JSONObject();
		jObjectOperationName.put("type", "string");
		jObjectOperationName.put("description", "Name of the operation performed");
		JSONObject jObjectTransactionId = new JSONObject();
		jObjectTransactionId.put("type", "string");
		jObjectTransactionId.put("description", "Unique id to identify the transaction");
		JSONObject jObjectDate = new JSONObject();
		jObjectDate.put("type", "string");
		jObjectDate.put("format", "date-time");
		jObjectDate.put("description", "Timestamp of operation in YYYY-MM-DDTHH:MM:SSZ format");
		JSONObject jObjectStatus = new JSONObject();
		jObjectStatus.put("type", "string");
		jObjectStatus.put("description", "Response status from API");
		JSONObject jObjectProperties = new JSONObject();
		jObjectProperties.put("info", jObjectOperationName);
		jObjectProperties.put("message", jObjectTransactionId);
		jObjectProperties.put("code", jObjectDate);
		jObjectProperties.put("status", jObjectStatus);
		JSONObject jObjectError = new JSONObject();
		jObjectError.put("properties", jObjectProperties);

		return jObjectError;
	}

	/**
	 * Allows to create elements of 
	 * 
	 * @return 
	 */
	private static JSONObject getDDIResponse(String projectName) {
		JSONObject jObjStatus = new JSONObject();
		JSONObject jObjResult = new JSONObject();
		JSONObject jObjProperties = new JSONObject();
		JSONObject jObjDDIResponse = new JSONObject();
		jObjStatus.put("type", "string");
		jObjStatus.put("description", "status of the operation");
		jObjResult.put("$ref", "#/components/schemas/" + projectName + "BaseResult");
		jObjResult.put("$status", jObjStatus);
		jObjProperties.put("result", jObjResult);
		jObjDDIResponse.put("properties", jObjProperties);
		jObjDDIResponse.put("$ref", "#/components/schemas/DefaultData");

		return jObjDDIResponse;
	}

	/**
	 * Allows to create elements of DDI BASE RESULT tag
	 * 
	 * @return returns JSON object which is anchored to the DDI BASE RESULT Tag
	 */
	private static JSONObject getDDIBaseResult(String projectName) {
		JSONObject jObjItems = new JSONObject();
		JSONObject jObjProperties = new JSONObject();
		JSONObject jObjDDIBaseResult = new JSONObject();
		jObjItems.put("$ref", "#/components/schemas/Links");

		JSONObject jObjItemsData = new JSONObject();
		JSONObject jObjData = new JSONObject();
		jObjItemsData.put("$ref", "#/components/schemas/" + projectName + "ResponseBody");
		jObjData.put("type", "array");
		jObjData.put("items", jObjItemsData);
		jObjProperties.put("data", jObjData);

		JSONObject jObjMetaData = new JSONObject();
		jObjMetaData.put("$ref", "#/components/schemas/BaseHeader");
		jObjProperties.put("metadata", jObjMetaData);

		jObjDDIBaseResult.put("properties", jObjProperties);

		return jObjDDIBaseResult;

	}

	/**
	 * Allows to create elements of DDI BASE HEADER tag
	 * 
	 * @return returns JSON object which is anchored to the DDI BASE HEADER Tag
	 */
	private static JSONObject getBaseHeader() {
		JSONObject jObjParent = new JSONObject();
		JSONObject jObjLanguage = new JSONObject();
		JSONObject jObjProperties = new JSONObject();
		jObjLanguage.put("type", "string");
		jObjProperties.put("language", jObjLanguage);
		jObjProperties.put("dealerId", jObjLanguage);
		jObjProperties.put("country", jObjLanguage);

		JSONObject jObjTotalRecords = new JSONObject();
		jObjTotalRecords.put("type", "number");
		jObjTotalRecords.put("description", "Total number of records");
		jObjProperties.put("totalRecords", jObjTotalRecords);
		jObjParent.put("properties", jObjProperties);

		return jObjParent;
	}

	/**
	 * Allows to create elements of DDI RESPONSE BODY tag
	 * 
	 * @return returns JSON object which is anchored to the DDI RESPONSE BODY Tag
	 */
	private static JSONObject getDDIResponseBody(String projectName) {
		JSONObject jObjBody = new JSONObject();
		JSONObject jObjProperties = new JSONObject();
		JSONObject jObjDDIResponseBody = new JSONObject();
		jObjBody.put("$ref", "#/components/schemas/" + projectName);
		jObjProperties.put("body", jObjBody);
		jObjDDIResponseBody.put("properties", jObjProperties);

		return jObjDDIResponseBody;
	}

}
