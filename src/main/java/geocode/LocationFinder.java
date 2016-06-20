package geocode;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class LocationFinder {
	String gnipData;
	Logger logger=Logger.getLogger(LocationFinder.class.getName());
	GeoName geoLocation=null;
	JSONObject geoDetails=new JSONObject();
	public JSONObject findLocation(String gnipDataInput) {
		try{
			
			
				JSONObject jsonData = new JSONObject(gnipDataInput);
				 if (jsonData.has("geo")
							&& jsonData.get("geo") != null
							&& !jsonData.get("geo").toString().equals("null")){
						JSONObject geo = (JSONObject) jsonData.get("geo");
						if(geo != null){
						JSONObject geoObject=geo.getJSONObject("coordinates");
						double latitude=(double) geoObject.get("latitude");
						double longitude=(double) geoObject.get("longitude");
						geoLocation=CompletePositionDecoder.getInstance().getNearestLocation(longitude,latitude );
						geoDetails.put("countrycode",geoLocation.getCountry());
						geoDetails.put("region", geoLocation.getSubRegionName());
						geoDetails.put("name", geoLocation.getName());
						return geoDetails;
						}

					 
				 }
				if (jsonData.has("location")
						&& jsonData.get("location") != null
						&& !jsonData.get("location").toString().equals("null")) {
					JSONObject location = (JSONObject) jsonData.get("location");
					if (location != null) {
					List<Point2D.Double>polygon=new ArrayList<>();
					String displayName=(String) location.get("displayName");
					String []regions=displayName.split(",");
					String displayRegion=regions[0];
					JSONObject locationObject=(JSONObject) location.getJSONObject("geo").getJSONObject("coordinates").getJSONArray("holes").get(0);
					JSONArray locateCordinates=(JSONArray) locationObject.getJSONArray("coordinates");
					for(int i=0;i<locateCordinates.length();i++){
						JSONObject cordinates=(JSONObject) locateCordinates.get(i);
						Point2D.Double points=new Point2D.Double(java.lang.Double.valueOf(cordinates.get("longitude").toString()), java.lang.Double.valueOf(cordinates.get("latitude").toString()));
						polygon.add(points);
	
						}
					geoLocation=CompletePositionDecoder.getInstance().getNearestLocation(polygon);
					if(displayRegion!=geoLocation.getSubRegionName()){
						logger.warn("Not Accurate Place");
					}
					geoDetails.put("countrycode",geoLocation.getCountry());
					geoDetails.put("region", geoLocation.getSubRegionName());
					geoDetails.put("name", geoLocation.getName());
					return geoDetails;
					

					}
				}
				if(jsonData.has("gnip")
						&& jsonData.get("gnip") != null
						&& !jsonData.get("gnip").toString().equals("null")&& jsonData.getJSONObject("gnip").getJSONArray("profileLocations").length()!=0){
					JSONObject gnip = (JSONObject) jsonData.get("gnip");
//					System.out.println(gnip);
					if(gnip!=null){
						JSONObject gnipProfileLocation=(JSONObject) gnip.getJSONArray("profileLocations").get(0);
						JSONObject gnipData=gnipProfileLocation.getJSONObject("address");
						JSONObject gnipCordinates=gnipProfileLocation.getJSONObject("geo").getJSONObject("coordinates");
						double latitude=(double) gnipCordinates.get("latitude");
						double longitude=(double) gnipCordinates.get("longitude");
						String region= gnipData.get("region").toString();
						String subRegion= gnipData.get("subRegion").toString();
						String countryCode= gnipData.get("countryCode").toString();
						String locality=gnipData.get("locality").toString();
						String country=gnipData.get("country").toString();
						geoLocation=CompletePositionDecoder.getInstance().getNearestLocation(longitude,latitude );
						geoDetails.put("countrycode",geoLocation.getCountry());
						geoDetails.put("region", geoLocation.getSubRegionName());
						geoDetails.put("name", geoLocation.getName());
						
					}
					
				}
				
				return geoDetails;
		}catch(Exception e){
			e.printStackTrace();
			return geoDetails;
		}
	}
		 

	}

	


