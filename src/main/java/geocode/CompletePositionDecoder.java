package geocode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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
		return location;
	}
}
