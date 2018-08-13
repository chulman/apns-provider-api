package Utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonUtils {
	
	public static String parse(String message) {
		JSONObject aps = new JSONObject();
		JSONObject alert = new JSONObject();
		
		alert.put("alert", message);
		
		aps.put("aps",alert);
		
		return aps.toJSONString();
	}
	
	public static String getJsonData(String path, String fileName) {

		JSONParser parser = new JSONParser();
		JSONObject jObj = new JSONObject();
		Object obj = null;
		try {
			obj = parser.parse(new FileReader(path+fileName));
			jObj = (JSONObject) obj;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jObj.toString();
	}

}
