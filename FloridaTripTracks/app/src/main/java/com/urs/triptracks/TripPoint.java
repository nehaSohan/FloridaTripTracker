/**	 TripTracks, Copyright 2014 Florida Transportation Authority
 *                                    Florida, USA
 *
 * 	 @author Hema Sahu <hema.sahu@urs.com>
 *
 */

package com.urs.triptracks;

class TripPoint {
	public float accuracy;
	public int altitude;
	public float speed;
	public double time;
	int latitude;
	int longitude;

    public TripPoint(int lat, int lgt, double currentTime) {
    	this.latitude = lat;
		this.longitude = lgt;
		this.time = currentTime;
    }

    public TripPoint(int lat, int lgt, double currentTime, float accuracy) {
    	this.latitude = lat;
		this.longitude = lgt;
		this.time = currentTime;
		this.accuracy = accuracy;
    }

	public TripPoint(int lat, int lgt, double currentTime, float accuracy, int altitude, float speed) {
		this.latitude = lat;
		this.longitude = lgt;
		this.time = currentTime;
		this.accuracy = accuracy;
		this.altitude = altitude;
		this.speed = speed;
	}
}
