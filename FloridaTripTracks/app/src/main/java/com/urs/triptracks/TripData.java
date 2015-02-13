/**  TripTracks, Copyright 2014 Florida Transportation Authority
 *                                    Florida, USA
 *
 *   @author Hema Sahu <hema.sahu@urs.com>
 *
 *   This file is part of TripTracks.
 *
 */

package com.urs.triptracks;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;

public class TripData {
	long tripid;
	double startTime = 0, tollAmt=0, payForParkingAmt=0, fare=0;
	double endTime = 0;
	int numpoints = 0;
	int lathigh, lgthigh, latlow, lgtlow, latestlat, latestlgt;
	int status, members, nonmembers, delays, toll, payForParking;
	float distance;
	String purp, travelBy, fancystart, info;
	//private ItemizedOverlayTrack gpspoints;
	private ArrayList<TripPoint> gpspoints = new ArrayList<TripPoint>();
	TripPoint startpoint, endpoint;
	double totalPauseTime = 0;
	double pauseStartedAt = 0;

	DbAdapter mDb;

    public static int STATUS_INCOMPLETE = 0;
    public static int STATUS_COMPLETE = 1;
    public static int STATUS_SENT = 2;

	public static TripData createTrip(Context c) {
		TripData t = new TripData(c.getApplicationContext(), 0);
		t.createTripInDatabase(c);
        t.initializeData();
		return t;
	}

	public static TripData fetchTrip(Context c, long tripid) {
		TripData t = new TripData(c.getApplicationContext(), tripid);
		t.populateDetails();
		return t;
	}

	public TripData (Context ctx, long tripid) {
		Context context = ctx.getApplicationContext();
		this.tripid = tripid;
		mDb = new DbAdapter(context);
	}

	void initializeData() {
		startTime = System.currentTimeMillis();
		endTime = System.currentTimeMillis();
        numpoints = members = nonmembers=0;
        latestlat = 800; latestlgt = 800;
        distance = 0;

        lathigh = (int) (-100 * 1E6);
		latlow = (int) (100 * 1E6);
		lgtlow = (int) (180 * 1E6);
		lgthigh = (int) (-180 * 1E6);
		purp = travelBy=fancystart = info = "";

		updateTrip();
	}

    // Get lat/long extremes, etc, from trip record
	void populateDetails() {

	    mDb.openReadOnly();

	    Cursor tripdetails = mDb.fetchTrip(tripid);
	    startTime = tripdetails.getDouble(tripdetails.getColumnIndex("start"));
	    lathigh = tripdetails.getInt(tripdetails.getColumnIndex("lathi"));
	    latlow =  tripdetails.getInt(tripdetails.getColumnIndex("latlo"));
	    lgthigh = tripdetails.getInt(tripdetails.getColumnIndex("lgthi"));
	    lgtlow =  tripdetails.getInt(tripdetails.getColumnIndex("lgtlo"));
	    status =  tripdetails.getInt(tripdetails.getColumnIndex("status"));
	    endTime = tripdetails.getDouble(tripdetails.getColumnIndex("endtime"));
        distance = tripdetails.getFloat(tripdetails.getColumnIndex("distance"));

        purp = tripdetails.getString(tripdetails.getColumnIndex("purp"));
        travelBy=tripdetails.getString(tripdetails.getColumnIndex("travelBy"));
        members =  tripdetails.getInt(tripdetails.getColumnIndex("members"));
	    nonmembers = tripdetails.getInt(tripdetails.getColumnIndex("nonmembers"));
	    delays =  tripdetails.getInt(tripdetails.getColumnIndex("delays"));
	    toll = tripdetails.getInt(tripdetails.getColumnIndex("toll"));
	    tollAmt = tripdetails.getDouble(tripdetails.getColumnIndex("tollAmt"));
	    payForParking = tripdetails.getInt(tripdetails.getColumnIndex("payForParking"));
	    payForParkingAmt = tripdetails.getDouble(tripdetails.getColumnIndex("payForParkingAmt"));
	    fare = tripdetails.getDouble(tripdetails.getColumnIndex("fare"));
        fancystart = tripdetails.getString(tripdetails.getColumnIndex("fancystart"));
        info = tripdetails.getString(tripdetails.getColumnIndex("fancyinfo"));

	    tripdetails.close();

		Cursor points = mDb.fetchAllCoordsForTrip(tripid);
		if (points != null) {
	        numpoints = points.getCount();
	        points.close();
		}

	    mDb.close();
	}

	void createTripInDatabase(Context c) {
		mDb.open();
		tripid = mDb.createTrip();
		mDb.close();
	}

	void dropTrip() {
	    mDb.open();
		mDb.deleteAllCoordsForTrip(tripid);
		mDb.deleteTrip(tripid);
		mDb.close();
	}

	public ArrayList<TripPoint> getPoints() {
		// If already built, don't build again!
		if (gpspoints != null && gpspoints.size() > 0) {
			return gpspoints;
		}

		// Otherwise, we need to query DB and build points from scratch.
		gpspoints = new ArrayList<TripPoint>();

		try {
			mDb.openReadOnly();

			Cursor points = mDb.fetchAllCoordsForTrip(tripid);
			int COL_LAT = points.getColumnIndex("lat");
			int COL_LGT = points.getColumnIndex("lgt");
			int COL_TIME = points.getColumnIndex("time");
			int COL_ACC = points.getColumnIndex(DbAdapter.K_POINT_ACC);

			numpoints = points.getCount();

			points.moveToLast();
			this.endpoint = new TripPoint(points.getInt(COL_LAT),
					points.getInt(COL_LGT), points.getDouble(COL_TIME));

			points.moveToFirst();
			this.startpoint = new TripPoint(points.getInt(COL_LAT),
					points.getInt(COL_LGT), points.getDouble(COL_TIME));

			while (!points.isAfterLast()) {
				int lat = points.getInt(COL_LAT);
				int lgt = points.getInt(COL_LGT);
				double time = points.getDouble(COL_TIME);
				float acc = (float) points.getDouble(COL_ACC);
				TripPoint pt = new TripPoint(lat, lgt, time, acc);
				gpspoints.add(pt);
				// addPointToSavedMap(lat, lgt, time, acc);
				points.moveToNext();
			}
			points.close();
			mDb.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// gpspoints.repopulate();

		return gpspoints;
	}

	/*private void addPointToSavedMap(int lat, int lgt, double currentTime, float acc) {
		TripPoint pt = new TripPoint(lat, lgt, currentTime, acc);

		OverlayItem opoint = new OverlayItem(pt, null, null);
		gpspoints.addOverlay(opoint);
	}*/

	boolean addPointNow(Location loc, double currentTime, float dst) {
		int lat = (int) (loc.getLatitude() * 1E6);
		int lgt = (int) (loc.getLongitude() * 1E6);

		// Skip duplicates
		if (latestlat == lat && latestlgt == lgt)
			return true;

		float accuracy = loc.getAccuracy();
		int altitude = (int) loc.getAltitude();
		float speed = loc.getSpeed();

		TripPoint pt = new TripPoint(lat, lgt, currentTime, accuracy,
				altitude, speed);

        numpoints++;
        endTime = currentTime - this.totalPauseTime;
        distance = dst;

		latlow = Math.min(latlow, lat);
		lathigh = Math.max(lathigh, lat);
		lgtlow = Math.min(lgtlow, lgt);
		lgthigh = Math.max(lgthigh, lgt);

		latestlat = lat;
		latestlgt = lgt;

        mDb.open();
        boolean rtn = mDb.addCoordToTrip(tripid, pt);
        rtn = rtn && mDb.updateTrip(tripid, "", "", 0, 0,
        		0, 0, 0.0, 0, 0.0, 0.0, startTime, "", "",
                lathigh, latlow, lgthigh, lgtlow, distance);
        mDb.close();

        return rtn;
	}

	public boolean updateTripStatus(int tripStatus) {
		boolean rtn;
		mDb.open();
		rtn = mDb.updateTripStatus(tripid, tripStatus);
		mDb.close();
		return rtn;
	}

	public boolean getStatus(int tripStatus) {
		boolean rtn;
		mDb.open();
		rtn = mDb.updateTripStatus(tripid, tripStatus);
		mDb.close();
		return rtn;
	}

	public void updateTrip() { updateTrip("","",0,0,0,0,0.0,0,0.0,0.0,"",""); }
	public void updateTrip(String purpose, String travelBy, int members, int nonmembers, int delays, int toll, double tollAmt, int payForParking, double payForParkingAmt, double fare,
			String fancyStart, String fancyInfo) {
		// Save the trip details to the phone database. W00t!
		mDb.open();
		mDb.updateTrip(tripid, purpose,	travelBy, members, nonmembers, delays, toll, tollAmt, payForParking, payForParkingAmt, fare, startTime, fancyStart, fancyInfo,
				lathigh, latlow, lgthigh, lgtlow, distance);
		mDb.close();
	}
}