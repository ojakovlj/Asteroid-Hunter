import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONException;


public class DataParser {
	Asteroid a1, a2, a3, a4;
	
	public DataParser() throws IOException, JSONException{
		a1 = new Asteroid("asteroid1.json");
		a2 = new Asteroid("asteroid2.json");
		a3 = new Asteroid("asteroid3.json");
		a4 = new Asteroid("asteroid4.json");


	}
	
	public ImgTextEntity getData(int n){
		if(n==1)
			return a1.getData();
		if(n==2)
			return a2.getData();
		if(n==3)
			return a3.getData();
		if(n==4)
			return a4.getData();
		else 
			return null;

	}
}
