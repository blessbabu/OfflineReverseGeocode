package geocode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RegionMapper {
	
	Map<String,String> values=new HashMap<String, String>();
	String fileName;
	char delimeter;
	int Keyposition;
	int valuePosition;
	
	public RegionMapper(String fileName,char delimiter,int keyPosition,int valuePosition){
		this.fileName=fileName;
		this.delimeter=delimiter;
		this.Keyposition=keyPosition;
		this.valuePosition=valuePosition;
		
	}
	
	
	public Boolean generateMap() throws IOException{
		String regexSpilt=String.valueOf(delimeter);
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		BufferedReader br = new BufferedReader(new FileReader(file));
		while(true){
			
				String geoData=br.readLine();
				if(geoData == null){
					break;
				}
				String[] geoDetails=geoData.split("" + regexSpilt);
				values.put(geoDetails[Keyposition], geoDetails[valuePosition]);
				
			
		}
		return true;
		
	}
	
	public String getValue(String key){
		return values.get(key);
	}
	public static void main(String args[]) throws Exception{
		URL url=new URL("http://download.geonames.org/export/dump/admin1CodesASCII.txt");
		String file="http://download.geonames.org/export/dump/admin1CodesASCII.txt";
		RegionMapper map=new RegionMapper(file, 't', 0, 1);
		if(map.generateMap()){
		System.out.println(map.getValue("DZ.31"));
		}	
	}

}
