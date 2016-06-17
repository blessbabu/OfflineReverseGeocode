/*
The MIT License (MIT)
[OSI Approved License]
The MIT License (MIT)

Copyright (c) 2014 Daniel Glasson

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package geocode;

import geocode.kdtree.KDNodeComparator;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import java.util.Comparator;

/**
 * Created by Daniel Glasson on 18/05/2014.
 * This class works with a placenames files from http://download.geonames.org/export/dump/
 */

public class GeoName extends KDNodeComparator<GeoName> {
    private String name;
    private String subRegion;
    private boolean majorPlace; // Major or minor place
    private double latitude;
    private double longitude;
    private double point[] = new double[3]; // The 3D coordinates of the point
    private String country;
    private String subRegionName;

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubRegion() {
		return subRegion;
	}

	public void setSubRegion(String subRegion) {
		this.subRegion = subRegion;
	}

	public boolean isMajorPlace() {
		return majorPlace;
	}

	public void setMajorPlace(boolean majorPlace) {
		this.majorPlace = majorPlace;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	GeoName(String data) {
        String[] names = data.split("\t");
        this.setName(names[1]);
        this.setMajorPlace(names[6].equals("P"));
        this.setLatitude(Double.parseDouble(names[4]));
        this.setLongitude(Double.parseDouble(names[5]));
        this.setSubRegion(names[10]);
        setPoint();
        this.setCountry(names[8]);
       
    }
    
    public String getSubRegionName() {
		return subRegionName;
	}

	public void setSubRegionName(String subRegionName) {
		this.subRegionName = subRegionName;
	}

	GeoName(Double latitude, Double longitude) {
        name = country = "Search";
        this.latitude = latitude;
        this.longitude = longitude;
        setPoint();
    }

    private void setPoint() {
        point[0] = cos(toRadians(latitude)) * cos(toRadians(longitude));
        point[1] = cos(toRadians(latitude)) * sin(toRadians(longitude));
        point[2] = sin(toRadians(latitude));
    }

    @Override
    public String toString() {
        return name + "," + subRegion + "(" + subRegionName + ")," + country;
    }

    @Override
    protected double squaredDistance(GeoName other) {
        double x = this.point[0] - other.point[0];
        double y = this.point[1] - other.point[1];
        double z = this.point[2] - other.point[2];
        return (x*x) + (y*y) + (z*z);
    }

    @Override
    protected double axisSquaredDistance(GeoName other, int axis) {
        double distance = point[axis] - other.point[axis];
        return distance * distance;
    }

    @Override
    protected Comparator<GeoName> getComparator(int axis) {
        return GeoNameComparator.values()[axis];
    }

    protected static enum GeoNameComparator implements Comparator<GeoName> {
        x {
            @Override
            public int compare(GeoName a, GeoName b) {
                return Double.compare(a.point[0], b.point[0]);
            }
        },
        y {
            @Override
            public int compare(GeoName a, GeoName b) {
                return Double.compare(a.point[1], b.point[1]);
            }
        },
        z {
            @Override
            public int compare(GeoName a, GeoName b) {
                return Double.compare(a.point[2], b.point[2]);
            }
        };
    }
}
