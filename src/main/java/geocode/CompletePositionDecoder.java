package geocode;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompletePositionDecoder {

	ReverseGeoCode reverseGeoCode;
	RegionMapper regionMapper;

	String countryDataFile = "cities15000.txt";
	String adminDataFile = "admin1CodesASCII.txt";

	private CompletePositionDecoder() throws FileNotFoundException, IOException {
		init();
	}

	private void init() throws FileNotFoundException, IOException {
		ClassLoader classLoader = CompletePositionDecoder.class
				.getClassLoader();
		File file = new File(classLoader.getResource(countryDataFile).getFile());
		reverseGeoCode = new ReverseGeoCode(new FileInputStream(file), true);
		regionMapper = new RegionMapper(adminDataFile, '\t', 0, 2);
		regionMapper.generateMap();
	}

	public CompletePositionDecoder(String countryDataFile, String adminDataFile)
			throws FileNotFoundException, IOException {
		this.countryDataFile = countryDataFile;
		this.adminDataFile = adminDataFile;
		init();
	}

	static CompletePositionDecoder INSTANCE;

	static CompletePositionDecoder getInstance() throws FileNotFoundException,
			IOException {
		if (INSTANCE == null) {
			INSTANCE = new CompletePositionDecoder();
		}
		return INSTANCE;
	}

	static CompletePositionDecoder getInstance(String countryDataFile,
			String adminDataFile) throws FileNotFoundException, IOException {
		if (INSTANCE == null) {
			INSTANCE = new CompletePositionDecoder(countryDataFile,
					adminDataFile);
		}
		return INSTANCE;
	}

	public GeoName getNearestLocation(Double lat, Double lon) {
		GeoName location = reverseGeoCode.nearestPlace(lat, lon);
		String region=regionMapper.getValue(location.getCountry()+"."+location.getSubRegion());
		location.setSubRegionName(region);
		return location;
	}
	
	public GeoName getNearestLocation(Point2D point) {
		double lat = point.getX();
		double lon = point.getY();
		GeoName location = reverseGeoCode.nearestPlace(lat, lon);
		String region=regionMapper.getValue(location.getCountry()+"."+location.getSubRegion()); 
		location.setSubRegionName(region);
		return location ;
	}

	public GeoName getNearestLocation(List<Point2D.Double> polygon) {
		Point2D.Double lat=(java.awt.geom.Point2D.Double) calculateCentroid(polygon);
		GeoName location = reverseGeoCode.nearestPlace(lat.getX(), lat.getY());
		String region=regionMapper.getValue(location.getCountry()+"."+location.getSubRegion()); 
		location.setSubRegionName(region);
		return location;
	}

	
	public Point2D calculateCentroid(List<Point2D.Double> polygon) {
	    double x = 0.;
	    double y = 0.;
	    int pointCount = polygon.size();
	    for (int i = 0;i < pointCount;i++){
	        final Point2D point = polygon.get(i);
	        x += point.getX();
	        y += point.getY();
	    }
	   
	    x = x/pointCount;
	    y = y/pointCount;
	    return new Point2D.Double(x, y);
	}
	public static void main(String args[]) throws FileNotFoundException, IOException{
		List<Point2D.Double> points =new ArrayList<>();
		points.add(new Point2D.Double(20.064307,85.624118));
		points.add(new Point2D.Double(20.43391,85.624118 ));
		points.add(new Point2D.Double(20.43391,86.082706));
		points.add(new Point2D.Double(20.064307,86.082706));
		System.out.println(CompletePositionDecoder.getInstance().getNearestLocation(points));
		System.out.println(CompletePositionDecoder.getInstance().getNearestLocation(20.2491085,85.853412));
		
	}

}
