package com.swagger;

import org.json.JSONArray;
import org.json.JSONObject;

public class StatusCodeJsonCreationClass {

	/**
	 * Allows to create elements of STATUS CODE tag
	 * 
	 * @return returns JSON object which is anchored to the STATUS CODE Tag
	 */
	public static JSONObject getStatusCodes(String projectName) {
		String[] statusCodeArray = { "200", "400", "401", "500" };
		JSONObject jObjectStatusCode = new JSONObject();

		for (int i = 0; i < statusCodeArray.length; i++) {

			JSONObject jObjectDescription = new JSONObject();
			JSONObject jObjectSchema = new JSONObject();
			JSONObject jObjectSchemaMain = new JSONObject();
			JSONObject jObjectApplicationType = new JSONObject();
			JSONArray jArrayOneOf = new JSONArray();
			JSONObject jObjectOneOf = new JSONObject();

			if (statusCodeArray[i] == "200") {
				jObjectSchema.put("$ref", "#/components/schemas/" + projectName + "Response");
				jArrayOneOf.put(jObjectSchema);
				jObjectOneOf.put("oneOf", jArrayOneOf);
				jObjectOneOf.put("type", "object");
				jObjectSchemaMain.put("schema", jObjectOneOf);
				jObjectApplicationType.put("application/json", jObjectSchemaMain);
				jObjectDescription.put("content", jObjectApplicationType);
				jObjectDescription.put("description", "successful response");
				jObjectStatusCode.put("200", jObjectDescription);

			} else if (statusCodeArray[i] == "400") {
				jObjectSchema.put("$ref", "#/components/schemas/BaseError");
				jObjectSchemaMain.put("schema", jObjectSchema);
				jObjectApplicationType.put("application/json", jObjectSchemaMain);
				jObjectDescription.put("content", jObjectApplicationType);
				jObjectDescription.put("description", "Bad Request");
				jObjectStatusCode.put("400", jObjectDescription);

			} else if (statusCodeArray[i] == "401") {
				jObjectSchema.put("$ref", "#/components/schemas/BaseError");
				jObjectSchemaMain.put("schema", jObjectSchema);
				jObjectApplicationType.put("application/json", jObjectSchemaMain);
				jObjectDescription.put("content", jObjectApplicationType);
				jObjectDescription.put("description", "Unauthorised access");
				jObjectStatusCode.put("401", jObjectDescription);

			} else {
				jObjectSchema.put("$ref", "#/components/schemas/BaseError");
				jObjectSchemaMain.put("schema", jObjectSchema);
				jObjectApplicationType.put("application/json", jObjectSchemaMain);
				jObjectDescription.put("content", jObjectApplicationType);
				jObjectDescription.put("description", "Internal server error");
				jObjectStatusCode.put("500", jObjectDescription);
			}
		}
		return jObjectStatusCode;
	}

}
