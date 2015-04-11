package com.sincress.entitymap.DataParser;

import com.sincress.entitymap.Entities.ImgTextEntity;
import java.io.IOException;


public class DataParser {
	AsteroidData a1, a2, a3, a4;
	
	public DataParser() throws IOException {
		a1 = new AsteroidData("asteroid1.txt");
		a2 = new AsteroidData("asteroid2.txt");
		a3 = new AsteroidData("asteroid3.txt");
		a4 = new AsteroidData("asteroid4.txt");

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
