package geocode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CompletePositionDecoder {

	public static void main(String args[]) throws FileNotFoundException,
			IOException {
		ClassLoader classLoader = CompletePositionDecoder.class.getClassLoader();
		String fileName = "cities15000.txt";
		File file = new File(classLoader.getResource(fileName).getFile());
		ReverseGeoCode reverseGeoCode = new ReverseGeoCode(new FileInputStream(file), true);
		GeoName location = reverseGeoCode.nearestPlace(9.986151, 76.2879012);
		RegionMapper regionMapper = new RegionMapper("admin1CodesASCII.txt", '\t',0, 2);
		regionMapper.generateMap();
		location.setSubRegionName( regionMapper.getValue(location.country + "."
				+ location.subRegion));
		System.out.println("Nearest is "+ location);
		;
	}
}
