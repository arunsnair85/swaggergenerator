package com.swagger;

import org.json.JSONArray;
import org.json.JSONObject;

public class ConstantJsonCreationClasss {

	/**
     * Allows to create the constant Parent tags and its corresponding child tags
     * @param parent and child tags that remains constant
     * @return JSON Object to be added to the main parent tag in the JSON
     */
	public static JSONObject createJsonForConstants(String openapi, String title, String description, String version,
			String contactName, String contactEmail, String serverURL, JSONObject jObjPaths, JSONObject jObjComponents) {
		JSONObject jObjMain = new JSONObject();
		JSONObject jObjInfo = new JSONObject();
		JSONObject jObjContactInfo = new JSONObject();
		JSONObject jObjServerUrl = new JSONObject();
		JSONArray serverArray = new JSONArray();
		JSONObject jObjectTagMain = new JSONObject();
		JSONArray tagMainArray = new JSONArray();
		
		jObjMain.put("paths", jObjPaths);
		jObjMain.put("components", jObjComponents);
		jObjMain.put("openapi", openapi);
		jObjInfo.put("title", title);
		jObjInfo.put("description", description);
		jObjInfo.put("version", version);
		jObjContactInfo.put("name", contactName);
		jObjContactInfo.put("email", contactEmail);
		jObjInfo.put("contact", jObjContactInfo);
		jObjServerUrl.put("url", serverURL);
		serverArray.put(jObjServerUrl);
		jObjectTagMain.put("name", "Utility");
		jObjectTagMain.put("description", "Utility Apis");
		tagMainArray.put(jObjectTagMain);
		jObjMain.put("tags", tagMainArray);
		jObjMain.put("info", jObjInfo);
		jObjMain.put("servers", serverArray);

		return jObjMain;

	}

}
