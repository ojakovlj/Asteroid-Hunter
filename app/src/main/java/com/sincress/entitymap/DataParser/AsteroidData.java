package com.sincress.entitymap.DataParser;


import android.graphics.Point;

import com.sincress.entitymap.Entities.ImgTextEntity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AsteroidData {
	
	HashMap<Integer, ArrayList<String>> map;
	//ArrayList<String> data ;
	BufferedReader reader;
	int counter;
	
	public AsteroidData(String dat) throws IOException{
		counter=1;
		String line;
		reader = new BufferedReader(new FileReader("drawable/" + dat));
		map = new HashMap<>();
		
		//data[0] == title
		//data[1] == image
		//data[2] == position
		//data[3] == text
		
		ArrayList<String> data = new ArrayList<String>();
		int table = 1;
		
		while((line = reader.readLine()) != null){
			
			if(line.startsWith("@") || line.startsWith("&") || line.startsWith("!")){
				line=line.substring(1);
				data.add(line);
			}
			else if(line.startsWith("#")){
				System.out.println("Data   "+data);
					map.put(table, new ArrayList<String>(data));
					data = new ArrayList<String>();
					table++;
			}
			else{
				String l;
				if(data.size()==3){
					l = line;
					data.add(l);
				}
				else{
					l = data.get(3);
					l.concat(" ");
					l.concat(line);
					data.add(3, l);
				}
			}
			}
		}
	
		public ImgTextEntity getData(){
			
			if(counter > 3)
				return null;
			
			ArrayList<String> data = map.get(counter);
		
			counter++;
			String[] s= data.get(3).split(" ");
			
			Point p = new Point (Integer.parseInt(s[0]), Integer.parseInt(s[1]));
			return new ImgTextEntity(data.get(1), data.get(0), data.get(3), p);
			
		}
	}

