package geocode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Controller {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		LocationFinder findLocation=new LocationFinder();
		String FileName="/home/weavers/git/OfflineReverseGeocode/gnip.log";
		BufferedReader br=new BufferedReader(new FileReader(FileName));
		while(true){
			String data=br.readLine();
			if(data==null){
				break;
			}
			System.out.println(findLocation.findLocation(data));
			
		}
	}

}
