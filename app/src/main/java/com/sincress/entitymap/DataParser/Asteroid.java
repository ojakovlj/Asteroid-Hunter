import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.*;

public class Asteroid {
	
	BufferedReader reader;
	Integer counter;
	JSONObject obj;
	
	public Asteroid(String dat) throws IOException, JSONException{

		counter=1;
		String line;
		reader = new BufferedReader(new FileReader("C:\\Users\\y\\Documents\\FER\\" + dat));
		StringBuilder sb = new StringBuilder();
		
		while((line = reader.readLine()) != null){
			sb.append(line);
		}
		
		String json = sb.toString();
		obj = new JSONObject(json);
		
		

	}
	
		public ImgTextEntity getData(){
			JSONObject objectBox;
			if(obj.has(counter.toString())){
				objectBox = obj.getJSONObject(counter.toString());
				JSONObject pointObj = objectBox.getJSONObject("coordinates");
				Integer x = pointObj.getInt("x");
				Integer y = pointObj.getInt("y");
				Point p = new Point (x,y);
				counter++;
	
				return new ImgTextEntity(object.get("name"), object.get("image"), 
						object.get("text"), p);
			}
			return null;
		}
	}

