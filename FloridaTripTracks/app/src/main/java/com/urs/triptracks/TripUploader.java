/**	 TripTracks, Copyright 2014 Florida Transportation Authority
 *                                    Florida, USA
 *
 * 	 @author Hema Sahu <hema.sahu@urs.com>
 *
 *   This file is part of TripTracks.
 *
 */

package com.urs.triptracks;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.zip.GZIPOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
//HS updated the deprecated System
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

public class TripUploader extends AsyncTask <Long, Integer, Boolean> {
    Context mCtx;
    //DbAdapter mDb;
    DbAdapter mDb;

    public static final String TRIP_COORDS_TIME = "rec";
    public static final String TRIP_COORDS_LAT = "lat";
    public static final String TRIP_COORDS_LON = "lon";
    public static final String TRIP_COORDS_ALT = "alt";
    public static final String TRIP_COORDS_SPEED = "spd";
    public static final String TRIP_COORDS_HACCURACY = "hac";
    public static final String TRIP_COORDS_VACCURACY = "vac";

    public static final String USER_GENDER = "gender";
    public static final String USER_AGE = "age";
    public static final String USER_EMPLOYMENTSTATUS1 = "fulltime";
    public static final String USER_EMPLOYMENTSTATUS2 = "parttime";
    public static final String USER_EMPLOYMENTSTATUS3 = "empLess5Months";
    public static final String USER_EMPLOYMENTSTATUS4 = "unemployed";
    public static final String USER_EMPLOYMENTSTATUS5 = "retired";
    public static final String USER_EMPLOYMENTSTATUS6 = "workAtHome";
    public static final String USER_EMPLOYMENTSTATUS7 = "homemaker";
    public static final String USER_EMPLOYMENTSTATUS8 = "selfemployed";
    public static final String USER_DAYSWORKTRIP = "workdays";
    public static final String USER_STUDENT = "student";
    public static final String USER_STUDENTLEVEL = "studentlevel";
    public static final String USER_VALIDDRIVERLIC = "driverLicense";
    public static final String USER_TRANSITPASS = "transitpass";
    public static final String USER_DISABLEDPARKPASS = "disableparkpass";

    //Every stop questions
    /*public static final String EVERYSTOP_TRAVELED = "travelBy";
    public static final String EVERYSTOP_MEMBERS = "members";
    public static final String EVERYSTOP_NONMEMBERS = "nonmembers";
    public static final String EVERYSTOP_DELAYS = "delays";
    public static final String EVERYSTOP_PAYTOLL = "toll";
    public static final String EVERYSTOP_PAYTOLLAMT = "tollAmt";
    public static final String EVERYSTOP_PAYPARKING = "payForParking";
    public static final String EVERYSTOP_PAYPARKINGAMT = "payForParkingAmt";
    public static final String EVERYSTOP_FARE = "fare";*/

   /* public static final String USER_EMAIL = "email";
    public static final String USER_ZIP_HOME = "homeZIP";
    public static final String USER_ZIP_WORK = "workZIP";
    public static final String USER_ZIP_SCHOOL = "schoolZIP";
    public static final String USER_CYCLING_FREQUENCY = "cyclingFreq";*/

    public TripUploader(Context ctx) {
        super();
        this.mCtx = ctx;
        this.mDb = new DbAdapter(this.mCtx);
    }

    private String getCoordsJSON(long tripId) throws JSONException {
    	JSONObject coord = new JSONObject();
        //HS-SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//use new SimpleDateFormat(String template, Locale locale) with for example Locale.US for ASCII dates

        mDb.openReadOnly();
        Cursor tripCoordsCursor = mDb.fetchAllCoordsForTrip(tripId);

        // Build the map between JSON fieldname and phone db fieldname:
        Map<String, Integer> fieldMap = new HashMap<String, Integer>();
        fieldMap.put(TRIP_COORDS_TIME,
        		tripCoordsCursor.getColumnIndex(DbAdapter.K_POINT_TIME));
        fieldMap.put(TRIP_COORDS_LAT,
        		tripCoordsCursor.getColumnIndex(DbAdapter.K_POINT_LAT));
        fieldMap.put(TRIP_COORDS_LON,
        		tripCoordsCursor.getColumnIndex(DbAdapter.K_POINT_LGT));
        fieldMap.put(TRIP_COORDS_ALT,
        		tripCoordsCursor.getColumnIndex(DbAdapter.K_POINT_ALT));
        fieldMap.put(TRIP_COORDS_SPEED,
        		tripCoordsCursor.getColumnIndex(DbAdapter.K_POINT_SPEED));
        fieldMap.put(TRIP_COORDS_HACCURACY,
        		tripCoordsCursor.getColumnIndex(DbAdapter.K_POINT_ACC));
        fieldMap.put(TRIP_COORDS_VACCURACY,
        		tripCoordsCursor.getColumnIndex(DbAdapter.K_POINT_ACC));

        // Build JSON objects for each coordinate:
        JSONArray tripCoords = new JSONArray();
        String str=new String();

        JSONArray key = new JSONArray();
        while (!tripCoordsCursor.isAfterLast()) {
            //
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            coord.put(TRIP_COORDS_TIME,
            		df.format(tripCoordsCursor.getDouble(fieldMap.get(TRIP_COORDS_TIME))));
            coord.put(TRIP_COORDS_LAT,
            		tripCoordsCursor.getDouble(fieldMap.get(TRIP_COORDS_LAT)) / 1E6);
            coord.put(TRIP_COORDS_LON,
            		tripCoordsCursor.getDouble(fieldMap.get(TRIP_COORDS_LON)) / 1E6);
            coord.put(TRIP_COORDS_ALT,
            		tripCoordsCursor.getInt(fieldMap.get(TRIP_COORDS_ALT)));
            coord.put(TRIP_COORDS_SPEED,
            		tripCoordsCursor.getDouble(fieldMap.get(TRIP_COORDS_SPEED)));
            coord.put(TRIP_COORDS_HACCURACY,
            		tripCoordsCursor.getDouble(fieldMap.get(TRIP_COORDS_HACCURACY)));
            coord.put(TRIP_COORDS_VACCURACY,
            		tripCoordsCursor.getDouble(fieldMap.get(TRIP_COORDS_VACCURACY)));

            //tripCoords.put(coord.getString("rec"), coord);
            //JSONObject jo=new JSONObject();
            /*key.put("coord");
           // jo.put("coord", coord);
            tripCoords.put(coord);
            tripCoordsCursor.moveToNext();
            Log.v("coordsoutputtosee1:", tripCoords.toString());
            tripCoords.put(coord);
            Log.v("coordsoutputtosee2:", tripCoords.toString());*/
            str=str.concat("{\"coord\":");
            str=str.concat(coord.toString());
            str= str.concat("}");
            tripCoordsCursor.moveToNext();
            if(!tripCoordsCursor.isAfterLast()){
            	str=str.concat(",");
            }
        }
        tripCoordsCursor.close();
        mDb.close();
        return str;
    }
    /* private JSONObject getCoordsJSON(long tripId) throws JSONException {
    	JSONObject coord = new JSONObject();
        //HS-SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//use new SimpleDateFormat(String template, Locale locale) with for example Locale.US for ASCII dates

        mDb.openReadOnly();
        Cursor tripCoordsCursor = mDb.fetchAllCoordsForTrip(tripId);

        // Build the map between JSON fieldname and phone db fieldname:
        Map<String, Integer> fieldMap = new HashMap<String, Integer>();
        fieldMap.put(TRIP_COORDS_TIME,
        		tripCoordsCursor.getColumnIndex(DbAdapter.K_POINT_TIME));
        fieldMap.put(TRIP_COORDS_LAT,
        		tripCoordsCursor.getColumnIndex(DbAdapter.K_POINT_LAT));
        fieldMap.put(TRIP_COORDS_LON,
        		tripCoordsCursor.getColumnIndex(DbAdapter.K_POINT_LGT));
        fieldMap.put(TRIP_COORDS_ALT,
        		tripCoordsCursor.getColumnIndex(DbAdapter.K_POINT_ALT));
        fieldMap.put(TRIP_COORDS_SPEED,
        		tripCoordsCursor.getColumnIndex(DbAdapter.K_POINT_SPEED));
        fieldMap.put(TRIP_COORDS_HACCURACY,
        		tripCoordsCursor.getColumnIndex(DbAdapter.K_POINT_ACC));
        fieldMap.put(TRIP_COORDS_VACCURACY,
        		tripCoordsCursor.getColumnIndex(DbAdapter.K_POINT_ACC));

        // Build JSON objects for each coordinate:
        JSONObject tripCoords = new JSONObject();
        while (!tripCoordsCursor.isAfterLast()) {
            //
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            coord.put(TRIP_COORDS_TIME,
            		df.format(tripCoordsCursor.getDouble(fieldMap.get(TRIP_COORDS_TIME))));
            coord.put(TRIP_COORDS_LAT,
            		tripCoordsCursor.getDouble(fieldMap.get(TRIP_COORDS_LAT)) / 1E6);
            coord.put(TRIP_COORDS_LON,
            		tripCoordsCursor.getDouble(fieldMap.get(TRIP_COORDS_LON)) / 1E6);
            coord.put(TRIP_COORDS_ALT,
            		tripCoordsCursor.getDouble(fieldMap.get(TRIP_COORDS_ALT)));
            coord.put(TRIP_COORDS_SPEED,
            		tripCoordsCursor.getDouble(fieldMap.get(TRIP_COORDS_SPEED)));
            coord.put(TRIP_COORDS_HACCURACY,
            		tripCoordsCursor.getDouble(fieldMap.get(TRIP_COORDS_HACCURACY)));
            coord.put(TRIP_COORDS_VACCURACY,
            		tripCoordsCursor.getDouble(fieldMap.get(TRIP_COORDS_VACCURACY)));

            //tripCoords.put(coord.getString("rec"), coord);
            tripCoords.put("coord", coord);
            tripCoordsCursor.moveToNext();
        }
        tripCoordsCursor.close();
        mDb.close();
        return tripCoords;
    }*/

    private JSONObject getUserJSON() throws JSONException {
        JSONObject user = new JSONObject();
        Map<String, Integer> fieldMap = new HashMap<String, Integer>();
        //HS-fieldMap.put(USER_AGE, new Integer(UserInfoActivity.PREF_AGE));//Use Integer.valueOf(UserInfoActivity.PREF_AGE) instead
        //fieldMap.put(USER_EMAIL, new Integer(UserInfoActivity.PREF_EMAIL));
        //Use Integer.valueOf(UserInfoActivity.PREF_AGE) instead
        fieldMap.put(USER_GENDER, Integer.valueOf(UserInfoActivity.PREF_GENDER));
        fieldMap.put(USER_AGE, Integer.valueOf(UserInfoActivity.PREF_AGE));
        /*fieldMap.put(USER_EMPLOYMENTSTATUS1, Integer.valueOf(UserInfoActivity.PREF_EMPLOYMENTSTATUS1));
        fieldMap.put(USER_EMPLOYMENTSTATUS2, Integer.valueOf(UserInfoActivity.PREF_EMPLOYMENTSTATUS2));
        fieldMap.put(USER_EMPLOYMENTSTATUS3, Integer.valueOf(UserInfoActivity.PREF_EMPLOYMENTSTATUS3));
        fieldMap.put(USER_EMPLOYMENTSTATUS4, Integer.valueOf(UserInfoActivity.PREF_EMPLOYMENTSTATUS4));
        fieldMap.put(USER_EMPLOYMENTSTATUS5, Integer.valueOf(UserInfoActivity.PREF_EMPLOYMENTSTATUS5));
        fieldMap.put(USER_EMPLOYMENTSTATUS6, Integer.valueOf(UserInfoActivity.PREF_EMPLOYMENTSTATUS6));
        fieldMap.put(USER_EMPLOYMENTSTATUS7, Integer.valueOf(UserInfoActivity.PREF_EMPLOYMENTSTATUS7));
        fieldMap.put(USER_EMPLOYMENTSTATUS8, Integer.valueOf(UserInfoActivity.PREF_EMPLOYMENTSTATUS8));
        fieldMap.put(USER_DAYSWORKTRIP, Integer.valueOf(UserInfoActivity.PREF_DAYSWORKTRIP));
        fieldMap.put(USER_STUDENT, Integer.valueOf(UserInfoActivity.PREF_STUDENT));*/
        fieldMap.put(USER_STUDENTLEVEL, Integer.valueOf(UserInfoActivity.PREF_STUDENTLEVEL));
        //fieldMap.put(USER_VALIDDRIVERLIC, Integer.valueOf(UserInfoActivity.PREF_VALIDDRIVERLIC));
       // fieldMap.put(USER_TRANSITPASS, Integer.valueOf(UserInfoActivity.PREF_TRANSITPASS));
        //fieldMap.put(USER_DISABLEDPARKPASS, Integer.valueOf(UserInfoActivity.PREF_DISABLEDPARKPASS));

       /* fieldMap.put(USER_ZIP_HOME, new Integer(UserInfoActivity.PREF_ZIPHOME));
        fieldMap.put(USER_ZIP_WORK, new Integer(UserInfoActivity.PREF_ZIPWORK));
        fieldMap.put(USER_ZIP_SCHOOL, new Integer(UserInfoActivity.PREF_ZIPSCHOOL));*/

        SharedPreferences settings = this.mCtx.getSharedPreferences("PREFS", 0);
        for (Entry<String, Integer> entry : fieldMap.entrySet()) {
               user.put(entry.getKey(), settings.getString(entry.getValue().toString(), null));
        }
        user.put(USER_EMPLOYMENTSTATUS1, settings.getInt(""+UserInfoActivity.PREF_EMPLOYMENTSTATUS1, 0));
        user.put(USER_EMPLOYMENTSTATUS2, settings.getInt(""+UserInfoActivity.PREF_EMPLOYMENTSTATUS2, 0));
        user.put(USER_EMPLOYMENTSTATUS3, settings.getInt(""+UserInfoActivity.PREF_EMPLOYMENTSTATUS3, 0));
        user.put(USER_EMPLOYMENTSTATUS4, settings.getInt(""+UserInfoActivity.PREF_EMPLOYMENTSTATUS4, 0));
        user.put(USER_EMPLOYMENTSTATUS5, settings.getInt(""+UserInfoActivity.PREF_EMPLOYMENTSTATUS5, 0));
        user.put(USER_EMPLOYMENTSTATUS6, settings.getInt(""+UserInfoActivity.PREF_EMPLOYMENTSTATUS6, 0));
        user.put(USER_EMPLOYMENTSTATUS7, settings.getInt(""+UserInfoActivity.PREF_EMPLOYMENTSTATUS7, 0));
        user.put(USER_EMPLOYMENTSTATUS8, settings.getInt(""+UserInfoActivity.PREF_EMPLOYMENTSTATUS8, 0));
        user.put(USER_DAYSWORKTRIP, settings.getInt(""+UserInfoActivity.PREF_DAYSWORKTRIP, 0));
        user.put(USER_STUDENT, settings.getInt(""+UserInfoActivity.PREF_STUDENT, 0));
       // user.put(USER_STUDENTLEVEL, settings.getInt(""+UserInfoActivity.PREF_STUDENTLEVEL, 0));
        user.put(USER_VALIDDRIVERLIC, settings.getInt(""+UserInfoActivity.PREF_VALIDDRIVERLIC, 0));
        user.put(USER_TRANSITPASS, settings.getInt(""+UserInfoActivity.PREF_TRANSITPASS, 0));
        user.put(USER_DISABLEDPARKPASS, settings.getInt(""+UserInfoActivity.PREF_DISABLEDPARKPASS, 0));
        user.put("device", getDeviceId());

        //user.put(USER_CYCLING_FREQUENCY, Integer.parseInt(settings.getString(""+UserInfoActivity.PREF_CYCLEFREQ, "0"))/100);
        return user;
    }

    //HS Extra purposes - every time a stop question
   /* private JSONObject getEveryStopJSON() throws JSONException {
        JSONObject everystop = new JSONObject();
        Map<String, Integer> fieldMap = new HashMap<String, Integer>();

        fieldMap.put(EVERYSTOP_TRAVELED, Integer.valueOf(SaveTrip.PREF_TRAVELED));
        fieldMap.put(EVERYSTOP_MEMBERS, Integer.valueOf(SaveTrip.PREF_MEMBERS));
        fieldMap.put(EVERYSTOP_NONMEMBERS, Integer.valueOf(SaveTrip.PREF_NONMEMBERS));
        fieldMap.put(EVERYSTOP_DELAYS, Integer.valueOf(SaveTrip.PREF_DELAYS));
        fieldMap.put(EVERYSTOP_PAYTOLL, Integer.valueOf(SaveTrip.PREF_PAYTOLL));
        fieldMap.put(EVERYSTOP_PAYTOLLAMT, Integer.valueOf(SaveTrip.PREF_PAYTOLLAMT));
        fieldMap.put(EVERYSTOP_PAYPARKING, Integer.valueOf(SaveTrip.PREF_PAYPARKING));
        fieldMap.put(EVERYSTOP_PAYPARKINGAMT, Integer.valueOf(SaveTrip.PREF_PAYPARKINGAMT));
        fieldMap.put(EVERYSTOP_FARE, Integer.valueOf(SaveTrip.PREF_FARE));

        SharedPreferences settings = this.mCtx.getSharedPreferences("PREFSEV", 0);
        for (Entry<String, Integer> entry : fieldMap.entrySet()) {
        	everystop.put(entry.getKey(), settings.getString(entry.getValue().toString(), null));
        }
        return everystop;
    }*/

    private Vector<String> getTripData(long tripId) {
        Vector<String> tripData = new Vector<String>();
        mDb.openReadOnly();
        Cursor tripCursor = mDb.fetchTrip(tripId);

        //String note = tripCursor.getString(tripCursor
              //  .getColumnIndex(DbAdapter.K_TRIP_NOTE));
        String purpose = tripCursor.getString(tripCursor
                .getColumnIndex(DbAdapter.K_TRIP_PURP));
        Double startTime = tripCursor.getDouble(tripCursor
                .getColumnIndex(DbAdapter.K_TRIP_START));
        Double endTime = tripCursor.getDouble(tripCursor
                .getColumnIndex(DbAdapter.K_TRIP_END));
        String travelBy = tripCursor.getString(tripCursor
                .getColumnIndex(DbAdapter.K_TRIP_EVERYSTOP_TRAVELED));
        Integer members = tripCursor.getInt(tripCursor
                .getColumnIndex(DbAdapter.K_TRIP_EVERYSTOP_MEMBERS));
        Integer nonmembers = tripCursor.getInt(tripCursor
                .getColumnIndex(DbAdapter.K_TRIP_EVERYSTOP_NONMEMBERS));
        Integer delays = tripCursor.getInt(tripCursor
                .getColumnIndex(DbAdapter.K_TRIP_EVERYSTOP_DELAYS));
        Integer toll = tripCursor.getInt(tripCursor
                .getColumnIndex(DbAdapter.K_TRIP_EVERYSTOP_PAYTOLL));
        Double tollAmt = tripCursor.getDouble(tripCursor
                .getColumnIndex(DbAdapter.K_TRIP_EVERYSTOP_PAYTOLLAMT));
        Integer payForParking = tripCursor.getInt(tripCursor
                .getColumnIndex(DbAdapter.K_TRIP_EVERYSTOP_PAYPARKING));
        Double payForParkingAmt = tripCursor.getDouble(tripCursor
                .getColumnIndex(DbAdapter.K_TRIP_EVERYSTOP_PAYPARKINGAMT));
        Double fare = tripCursor.getDouble(tripCursor
                .getColumnIndex(DbAdapter.K_TRIP_EVERYSTOP_FARE));

        tripCursor.close();
        mDb.close();

        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//To get local formatting use getDateInstance(), getDateTimeInstance(), or getTimeInstance(), or use new SimpleDateFormat(String template, Locale locale) with for example
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale locale);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        //tripData.add(note);
        tripData.add(purpose);
        tripData.add(df.format(startTime));
        tripData.add(df.format(endTime));

        tripData.add(travelBy);
        tripData.add(members.toString());
        tripData.add(nonmembers.toString());
        tripData.add(delays.toString());
        tripData.add(toll.toString());
        tripData.add(tollAmt.toString());
        tripData.add(payForParking.toString());
        tripData.add(payForParkingAmt.toString());
        tripData.add(fare.toString());

        return tripData;
    }

    public String getDeviceId() {
        //HS-String androidId = System.getString(this.mCtx.getContentResolver(),
        //        System.ANDROID_ID);
    	String androidId = Secure.getString(this.mCtx.getContentResolver(),
    	                Secure.ANDROID_ID);
        String androidBase = "androidDeviceId-";

        if (androidId == null) { // This happens when running in the Emulator
            final String emulatorId = "android-RunningAsTestingDeleteMe";
            return emulatorId;
        }
        String deviceId = androidBase.concat(androidId);
        Log.v("HemaAndroidId:", deviceId);
        return deviceId;
    }

   /* private List<NameValuePair> getPostData(long tripId) throws JSONException {
        JSONObject coord = getCoordsJSON(tripId);
        JSONObject user = getUserJSON();

        //JSONObject everystop=getEveryStopJSON();
        String deviceId = getDeviceId();
        Vector<String> tripData = getTripData(tripId);
        //String notes = tripData.get(0);
        String purpose = tripData.get(0);
        String startTime = tripData.get(1);
        String stopTime = tripData.get(2);
        String travelBy = tripData.get(3);
        String members = tripData.get(4);
        String nonmembers = tripData.get(5);
        String delays = tripData.get(6);
        String toll = tripData.get(7);
        String tollAmt = tripData.get(8);
        String payForParking = tripData.get(9);
        String payForParkingAmt = tripData.get(10);
        String fare = tripData.get(11);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("\"UserId:\"", "\"hema6\""));
        nameValuePairs.add(new BasicNameValuePair("coord:", coord.toString()));
        nameValuePairs.add(new BasicNameValuePair("user:", user.toString()));
        //HS
        nameValuePairs.add(new BasicNameValuePair("travelBy:", travelBy));
        nameValuePairs.add(new BasicNameValuePair("members:", members));
        nameValuePairs.add(new BasicNameValuePair("nonmembers:", nonmembers));
        nameValuePairs.add(new BasicNameValuePair("delays:", delays));
        nameValuePairs.add(new BasicNameValuePair("toll:", toll));
        nameValuePairs.add(new BasicNameValuePair("tollAmt:", tollAmt));
        nameValuePairs.add(new BasicNameValuePair("payForParking:", payForParking));
        nameValuePairs.add(new BasicNameValuePair("payForParkingAmt:", payForParkingAmt));
        nameValuePairs.add(new BasicNameValuePair("fare:", fare));
        nameValuePairs.add(new BasicNameValuePair("device:", deviceId));
        nameValuePairs.add(new BasicNameValuePair("purpose:", purpose));
        nameValuePairs.add(new BasicNameValuePair("startTime:", startTime));
        nameValuePairs.add(new BasicNameValuePair("stopTime:", stopTime));
        nameValuePairs.add(new BasicNameValuePair("version:", "2"));

        return nameValuePairs;
    }*/

    private String getPostData(long tripId) throws JSONException {
        //JSONObject coords = getCoordsJSON(tripId);
    	String coords = getCoordsJSON(tripId);
    	 Log.v("coordsoutput:", coords);
    	//JSONObject user = getUserJSON();
    	String user = getUserJSON().toString();
    	Log.v("useroutput:", user);
        //JSONObject everystop=getEveryStopJSON();
        //String deviceId = getDeviceId();
        Vector<String> tripData = getTripData(tripId);
        //String notes = tripData.get(0);
        String purpose = tripData.get(0);
        String startTime = tripData.get(1);
        String stopTime = tripData.get(2);
        String travelBy = tripData.get(3);
        int members = Integer.parseInt(tripData.get(4));
        int nonmembers = Integer.parseInt(tripData.get(5));
        int delays = Integer.parseInt(tripData.get(6));
        int toll = Integer.parseInt(tripData.get(7));
        double tollAmt = Double.parseDouble(tripData.get(8));
        int payForParking = Integer.parseInt(tripData.get(9));
        double payForParkingAmt = Double.parseDouble(tripData.get(10));
        double fare = Double.parseDouble(tripData.get(11));

        /*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("coords", coords.toString()));
        nameValuePairs.add(new BasicNameValuePair("user", user.toString()));
        //HS
        nameValuePairs.add(new BasicNameValuePair("travelBy", travelBy));
        nameValuePairs.add(new BasicNameValuePair("members", members));
        nameValuePairs.add(new BasicNameValuePair("nonmembers", nonmembers));
        nameValuePairs.add(new BasicNameValuePair("delays", delays));
        nameValuePairs.add(new BasicNameValuePair("toll", toll));
        nameValuePairs.add(new BasicNameValuePair("tollAmt", tollAmt));
        nameValuePairs.add(new BasicNameValuePair("payForParking", payForParking));
        nameValuePairs.add(new BasicNameValuePair("payForParkingAmt", payForParkingAmt));
        nameValuePairs.add(new BasicNameValuePair("fare", fare));
        nameValuePairs.add(new BasicNameValuePair("device", deviceId));
        nameValuePairs.add(new BasicNameValuePair("purpose", purpose));
        nameValuePairs.add(new BasicNameValuePair("startTime", startTime));
        nameValuePairs.add(new BasicNameValuePair("stopTime", stopTime));
        nameValuePairs.add(new BasicNameValuePair("version", "2"));*/

        //return nameValuePairs;
        String codedPostData = "{\"coords\":[" + coords
        						+ "],\"user\":"	+ user
        						+ ",\"travelBy\":\"" + travelBy
        						+ "\",\"members\":" + members
        						+ ",\"nonmembers\":" + nonmembers
        						+ ",\"delays\":" + delays
        						+ ",\"toll\":" + toll
        						+ ",\"tollAmt\":" + tollAmt
        						+ ",\"payForParking\":" + payForParking
        						+ ",\"payForParkingAmt\":" + payForParkingAmt
        						+ ",\"fare\":" + fare
        						//+ ", device:" + deviceId
        						+ ",\"purpose\":\"" + purpose
        						+ "\",\"startTime\":\"" + startTime
        						+ "\",\"stopTime\":\"" + stopTime
        						+ "\",\"version\":" + 2+"}";

        //String codedPostData = "\"UserId:\"" + "\"hema5\"";
        //String codedPostData= new JSONObject().put("UserId", "hemax").toString();//worked
        //String codedPostData= new JSONObject().put("UserId", "hemax").toString();
       // codedPostData= new JSONObject(codedPostData).toString();
        //codedPostData.

        //JSONObject travelBy=new JSONObject();

       /* JSONObject jason=new JSONObject();
        jason.put("coords", coords);
        jason.put("user", user);
        jason.put("travelBy", travelBy);
        jason.put("members", members);
        jason.put("nonMembers", nonmembers);
        jason.put("delays", delays);
        jason.put("toll", toll);
        jason.put("tollAmt", tollAmt);
        jason.put("payForParking", payForParking);
        jason.put("payForParkingAmt", payForParkingAmt);
        jason.put("fare", fare);
        //jason.put("device", deviceId);
        jason.put("purpose", purpose);
        jason.put("startTime", startTime);
        jason.put("stopTime", stopTime);
        jason.put("version", 2);*/


		//return jason.toString();
        return codedPostData;
    }

    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

  /*  boolean uploadOneTrip(long currentTripId) {
        boolean result = false;

        List<NameValuePair> nameValuePairs;
        try {
            nameValuePairs = getPostData(currentTripId);
        } catch (JSONException e) {
            e.printStackTrace();
            return result;
        }
        Log.v("PostData", nameValuePairs.toString());

        HttpClient client = new DefaultHttpClient();
        //final String postUrl = "http://bikedatabase.sfcta.org/post/";
        //final String postUrl = "https://fdotrts.ursokr.com/TripTracker_WCF_Rest_Service_ursokr/TripTracker.svc/SaveTrip/post/";
        //final String postUrl = "https://fdotrts.ursokr.com/TripTracker_WCF_Rest_Service_ursokr/TripTracker.svc/OpenConnTest";
        final String postUrl = "https://fdotrts.ursokr.com/TripTracker_WCF_Rest_Service_ursokr/TripTracker.svc/JsonTest";
        //String mark = "{\"UserId\":\"hema\"}";
        HttpPost postRequest = new HttpPost(postUrl);
        postRequest.setHeader("content-type", "application/json");
       // String mark = "{\"UserId\":\"hema\"}";

        try {
            postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(postRequest);
            //String responseString = convertStreamToString(response.getEntity().getContent());
            String responseString = getASCIIContentFromEntity(response.getEntity());
           // Log.v("markstring", mark);
            Log.v("httpResponse", responseString);
            JSONObject responseData = new JSONObject(responseString);
            if (responseData.getString("status").equals("success")) {
                mDb.open();
                mDb.updateTripStatus(currentTripId, TripData.STATUS_SENT);
                mDb.close();
                result = true;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Hema_log_tag", "Error parsing data "+e.toString());
            Log.e("Hema_log_tag", "Failed data was:\n" + result);

            return false;
        }
        return result;
    }*/

    protected static String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
		InputStream in = entity.getContent();
		StringBuffer out = new StringBuffer();
		int n = 1;
		while (n>0) {
			byte[] b = new byte[4096];
			n =  in.read(b);
			if (n>0) out.append(new String(b, 0, n));
		}
		return out.toString();
	}

    boolean uploadOneTrip(long currentTripId) {
        boolean result = false;
		String postBodyData;
        try {
        	postBodyData = getPostData(currentTripId);
        } catch (JSONException e) {
            e.printStackTrace();
            return result;
        }
        Log.v("codedPostData", postBodyData);
        HttpClient client = new DefaultHttpClient();
        //final String postUrl = "https://fdotrts.ursokr.com/TripTracker_WCF_Rest_Service_ursokr/TripTracker.svc/SaveTrip";
        final String postUrl1 = "https://fdotrts.ursokr.com/TripTracker_WCF_Rest_Service_ursokr/TripTracker.svc/OpenConnTest";
        HttpGet postRequest1 = new HttpGet(postUrl1);
        Log.v("PostURLOPENCONN:", postUrl1);
        Log.v("PostURLOPENCONN:", postRequest1.toString());
        postRequest1.setHeader("Accept", "application/json");
        postRequest1.setHeader("Content-type", "application/json; charset=utf-8");

        try {
        	StringEntity str1=new StringEntity(postBodyData, HTTP.UTF_8);
        	//postRequest1.setEntity(str1);
            HttpResponse response1 = client.execute(postRequest1);
            HttpEntity entity1 = response1.getEntity();
            String responseString1 = getASCIIContentFromEntity(entity1);

            Log.v("httpResponseHema1", responseString1);
            JSONObject responseData1 = new JSONObject(responseString1);
            Log.v("responseDataHema1:", responseData1.toString());

            /*if (responseData.getString("Result").equals("Trip data was saved")) {
                mDb.open();
                mDb.updateTripStatus(currentTripId, TripData.STATUS_SENT);
                mDb.close();
                result = true;
            }*/
            //if (responseData1.getString("Result").equals("Web service contacted and responded")) {
            if (responseData1.getString("Result").startsWith(" Success connecting to the database via the webservice")) {
            	final String postUrl2 = "https://fdotrts.ursokr.com/TripTracker_WCF_Rest_Service_ursokr/TripTracker.svc/SaveTrip";
            	HttpPost postRequest2 = new HttpPost(postUrl2);
                Log.v("PostURL2:", postUrl2);
                Log.v("PostRequest2:", postRequest2.toString());
                postRequest2.setHeader("Accept", "application/json");
                postRequest2.setHeader("Content-type", "application/json; charset=utf-8");

                try {
                	Log.v("postBodyData:", postBodyData);
                	StringEntity str2=new StringEntity(postBodyData, HTTP.UTF_8);
                	postRequest2.setEntity(str2);
                    HttpResponse response2 = client.execute(postRequest2);
                    HttpEntity entity2 = response2.getEntity();
                    String responseString2 = getASCIIContentFromEntity(entity2);

                    Log.v("httpResponseHema2", responseString2);
                    JSONObject responseData2 = new JSONObject(responseString2);
                    Log.v("responseDataHema2:", responseData2.toString());

                    if (responseData2.getString("Result").equals("Trip data was saved")) {
                        mDb.open();
                        mDb.updateTripStatus(currentTripId, TripData.STATUS_SENT);
                        mDb.close();
                        result = true;
                    }

                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Hema_log_tag2nd", "Error parsing data "+e.toString());
                    Log.e("Hema_log_tag2nd", "Failed data was:\n" + result);
                    return false;
                }
                return result;
            }
            else{
            	Toast.makeText(mCtx.getApplicationContext(),"Couldn't connect to the webservice.", Toast.LENGTH_LONG).show();
            }

        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Hema_log_tag1st", "1st step Error parsing data "+e.toString());
            Log.e("Hema_log_tag1st", "1st step Failed data was:\n" + result);
            return false;
        }
        return result;
    }

    /*
      boolean uploadOneTrip(long currentTripId) {
        boolean result = false;
		String postBodyData;
        try {
        	postBodyData = getPostData(currentTripId);
        } catch (JSONException e) {
            e.printStackTrace();
            return result;
        }
        Log.v("codedPostData", postBodyData);
        HttpClient client = new DefaultHttpClient();
        final String postUrl = "https://fdotrts.ursokr.com/TripTracker_WCF_Rest_Service_ursokr/TripTracker.svc/SaveTrip";
        HttpPost postRequest = new HttpPost(postUrl);
        Log.v("PostURLOPENCONN:", postUrl);
        Log.v("PostURLOPENCONN:", postRequest.toString());
        postRequest.setHeader("Accept", "application/json");
        postRequest.setHeader("Content-type", "application/json; charset=utf-8");

        try {
        	Log.v("postBodyData:", postBodyData);
        	StringEntity str=new StringEntity(postBodyData, HTTP.UTF_8);
        	postRequest.setEntity(str);
            HttpResponse response = client.execute(postRequest);
            HttpEntity entity = response.getEntity();
            String responseString = getASCIIContentFromEntity(entity);

            Log.v("httpResponseHema", responseString);
            JSONObject responseData = new JSONObject(responseString);
            Log.v("responseDataHema:", responseData.toString());

            if (responseData.getString("Result").equals("Trip data was saved")) {
                mDb.open();
                mDb.updateTripStatus(currentTripId, TripData.STATUS_SENT);
                mDb.close();
                result = true;
            }

        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Hema_log_tag", "Error parsing data "+e.toString());
            Log.e("Hema_log_tag", "Failed data was:\n" + result);
            return false;
        }
        return result;
    }*/



    public static byte[] compress(String string) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream(string.length());
		GZIPOutputStream gos = new GZIPOutputStream(os);
		gos.write(string.getBytes());
		gos.close();
		byte[] compressed = os.toByteArray();
		os.close();
		return compressed;
	}

    @Override
    protected Boolean doInBackground(Long... tripid) {
        // First, send the trip user asked for:
        Boolean result = uploadOneTrip(tripid[0]);

        // Then, automatically try and send previously-completed trips
        // that were not sent successfully.
        Vector <Long> unsentTrips = new Vector <Long>();

        mDb.openReadOnly();
        Cursor cur = mDb.fetchUnsentTrips();
        if (cur != null && cur.getCount()>0) {
            //pd.setMessage("Sent. You have previously unsent trips; submitting those now.");
            while (!cur.isAfterLast()) {
                //HS-unsentTrips.add(new Long(cur.getLong(0)));Use Long.valueOf(cur.getLong(0)) instead
                unsentTrips.add(Long.valueOf(cur.getLong(0)));
                cur.moveToNext();
            }
            cur.close();
        }
        mDb.close();

        for (Long trip: unsentTrips) {
            result &= uploadOneTrip(trip);
        }
        return result;
    }

    @Override
    protected void onPreExecute() {
        //Toast.makeText(mCtx.getApplicationContext(),"Submitting trip.  Thanks for using TripTracks!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        try {
            if (result) {
               Toast.makeText(mCtx.getApplicationContext(),"Trip uploaded successfully, now you can close your App.", Toast.LENGTH_LONG).show();//Toast.setDuration(0)
               //showCloseDialog();
            } else {
                Toast.makeText(mCtx.getApplicationContext(),"TripTracks couldn't upload the trip, and will retry when your next trip is completed.", Toast.LENGTH_LONG).show();//Toast.setDuration(1)
                Toast.makeText(mCtx.getApplicationContext(),"TripTracks couldn't upload the trip, and will retry when your next trip is completed.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            // Just don't toast if the view has gone out of context
        }
    }

   /* private void showCloseDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please enter your personal details so we can learn a bit about you.\n\nThen, try to use Florida Trip Tracks every time you go on a trip for 7 days. Your trip routes will be sent to the Florida Department Of Transportation for a Travel Survey!\n\nThanks,\nThe FDOT Trip Tracks Team")
               .setCancelable(false).setTitle("Welcome to Florida Trip Tracks App!")
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(final DialogInterface dialog, final int id) {
                       startActivity(new Intent(MainInput.this, UserInfoActivity.class));
                   }
               });

        final AlertDialog alert = builder.create();
        alert.show();
    }*/
}
