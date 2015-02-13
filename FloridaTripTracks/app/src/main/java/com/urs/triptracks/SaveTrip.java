/**	 TripTracks, Copyright 2014 Florida Transportation Authority
 *                                    Florida, USA
 *
 * 	 @author Hema Sahu <hema.sahu@urs.com>
 *
 *   This file is part of TripTracks.
 *
 */

package com.urs.triptracks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SaveTrip extends Activity {
	long tripid;
	HashMap <Integer, ToggleButton> purpButtons = new HashMap<Integer,ToggleButton>();
	String purpose = "";

	HashMap <Integer, String> purpDescriptions = new HashMap<Integer, String>();
	////////////////////////////////////////HS
	public final static int PREF_OTHER = 1;
	public final static int PREF_TRAVELED = 2;
    public final static int PREF_MEMBERS = 3;
    public final static int PREF_NONMEMBERS = 4;
    public final static int PREF_DELAYS = 5;
    public final static int PREF_PAYTOLL = 6;
    public final static int PREF_PAYTOLLAMT = 7;
    public final static int PREF_PAYPARKING = 8;
    public final static int PREF_PAYPARKINGAMT = 9;
    public final static int PREF_FARE = 10;

    final String [] TRAVELED = {"Car, truck, van or motorcycle", "City/public bus/shuttle routes, transfers, boarding/alighting locations", "School bus",
    		"Walk", "Bicycle", "Carpool", "Other"};
	/////////////////////////////////////////////

	// Set up the purpose buttons to be one-click only
	void preparePurposeButtons() {
		purpButtons.put(R.id.ToggleCommute, (ToggleButton)findViewById(R.id.ToggleCommute));
		purpButtons.put(R.id.ToggleSchool,  (ToggleButton)findViewById(R.id.ToggleSchool));
		purpButtons.put(R.id.ToggleWorkRel, (ToggleButton)findViewById(R.id.ToggleWorkRel));
		purpButtons.put(R.id.ToggleExercise,(ToggleButton)findViewById(R.id.ToggleExercise));
		purpButtons.put(R.id.ToggleSocial,  (ToggleButton)findViewById(R.id.ToggleSocial));
		purpButtons.put(R.id.ToggleShopping,(ToggleButton)findViewById(R.id.ToggleShopping));
		purpButtons.put(R.id.ToggleErrand,  (ToggleButton)findViewById(R.id.ToggleErrand));
		purpButtons.put(R.id.ToggleCollege,  (ToggleButton)findViewById(R.id.ToggleCollege));
		purpButtons.put(R.id.ToggleDaycare,  (ToggleButton)findViewById(R.id.ToggleDaycare));
		purpButtons.put(R.id.ToggleOther,   (ToggleButton)findViewById(R.id.ToggleOther));

        purpDescriptions.put(R.id.ToggleCommute,
			"<b>My home:</b> this trip was primarily to get between home and your main workplace.");
		purpDescriptions.put(R.id.ToggleSchool,
			"<b>School:</b> this trip was primarily to go to or from school.");
		purpDescriptions.put(R.id.ToggleWorkRel,
			"<b>My work (including volunteer work):</b> this trip was primarily to go to or from a business related meeting, function, or work-related errand for your job.");
		purpDescriptions.put(R.id.ToggleExercise,
			"<b>Recreation (personal sports, gym):</b> this trip was primarily for exercise, going to the gym, etc.");
		purpDescriptions.put(R.id.ToggleSocial,
			"<b>Meal/Dining Out:</b> this trip was primarily for going to or from a social activity, e.g. at a friend's house, the park, a restaurant, the movies.");
		purpDescriptions.put(R.id.ToggleShopping,
			"<b>Shopping/Errands:</b> this trip was primarily to purchase or bring home goods or groceries.");
		purpDescriptions.put(R.id.ToggleErrand,
			"<b>Friend's or relative's home:</b> this trip was primarily for social time.");
		purpDescriptions.put(R.id.ToggleCollege,
				"<b>College:</b> this trip was primarily to go to or from college.");
		purpDescriptions.put(R.id.ToggleDaycare,
				"<b>Daycare:</b> this trip was primarily to go to or from daycare.");
		purpDescriptions.put(R.id.ToggleOther,
			"<b>Other:</b> Please enter your other trip purpose below:");

		CheckListener cl = new CheckListener();
		for (Entry<Integer, ToggleButton> e: purpButtons.entrySet()) {
			e.getValue().setOnCheckedChangeListener(cl);
		}
	}

	//HS - save for every stop
	void saveEveryStop(){
        SharedPreferences settings = getSharedPreferences("PREFSEV", 0);
        Map <String, ?> prefs = settings.getAll();
        for (Entry <String, ?> p : prefs.entrySet()) {
            int key = Integer.parseInt(p.getKey());
            CharSequence value = (CharSequence) p.getValue();
            //String value = (String) p.getValue();

           switch (key) {
           case PREF_OTHER:
        	   ((EditText)findViewById(R.id.NotesField)).setText(value);
               break;
           case PREF_TRAVELED:
        	   if (value.equals(TRAVELED[0])) {
                   ((RadioButton) findViewById(R.id.RadioButtonTraveledBy1)).setChecked(true);
               } else if (value.equals(TRAVELED[1])) {
                   ((RadioButton) findViewById(R.id.RadioButtonTraveledBy2)).setChecked(true);
               } else if (value.equals(TRAVELED[2])) {
                   ((RadioButton) findViewById(R.id.RadioButtonTraveledBy3)).setChecked(true);
               } else if (value.equals(TRAVELED[3])) {
                   ((RadioButton) findViewById(R.id.RadioButtonTraveledBy4)).setChecked(true);
               } else if (value.equals(TRAVELED[4])) {
                   ((RadioButton) findViewById(R.id.RadioButtonTraveledBy5)).setChecked(true);
               } else if (value.equals(TRAVELED[5])) {
                   ((RadioButton) findViewById(R.id.RadioButtonTraveledBy6)).setChecked(true);
               } else if (value.equals(TRAVELED[6])) {
                   ((RadioButton) findViewById(R.id.RadioButtonTraveledBy7)).setChecked(true);
               }
               break;
           case PREF_MEMBERS:
        	   ((EditText)findViewById(R.id.EditTextHouseHold)).setText(value);
               break;
           case PREF_NONMEMBERS:
            	((EditText)findViewById(R.id.EditTextNoHouseHold)).setText(value);
                break;
           case PREF_DELAYS:
            	 if (value.equals("Yes")) {
                     ((RadioButton) findViewById(R.id.ButtonDelayYes)).setChecked(true);
                 } else if (value.equals("No")) {
                     ((RadioButton) findViewById(R.id.ButtonDelayNo)).setChecked(true);
                 }
            	break;
           case PREF_PAYTOLL:
            	if (value.equals("Yes")) {
                    ((RadioButton) findViewById(R.id.ButtonPayTollYes)).setChecked(true);
                } else if (value.equals("No")) {
                    ((RadioButton) findViewById(R.id.ButtonPayTollNo)).setChecked(true);
                }
            	break;
           case PREF_PAYTOLLAMT:
           		((EditText)findViewById(R.id.EditTextFarePayToll)).setText(value);
           		break;
           case PREF_PAYPARKING:
            	if (value.equals("Yes")) {
                    ((RadioButton) findViewById(R.id.ButtonPayParkingYes)).setChecked(true);
                } else if (value.equals("No")) {
                    ((RadioButton) findViewById(R.id.ButtonPayParkingNo)).setChecked(true);
                }
            	break;
           case PREF_PAYPARKINGAMT:
            	((EditText)findViewById(R.id.EditTextFarePayParking)).setText(value);
            	break;
           case PREF_FARE:
           		((EditText)findViewById(R.id.EditTextFare)).setText(value);
           		break;
           }

        }
        SharedPreferences.Editor editor = settings.edit();

        //save Other text field
        editor.putString(""+PREF_OTHER,((EditText)findViewById(R.id.NotesField)).getText().toString());

        //save traveled vehicle
        RadioGroup trav = (RadioGroup) findViewById(R.id.RadioGroupTraveledBy);
        if (trav.getCheckedRadioButtonId() == R.id.RadioButtonTraveledBy1) editor.putString(""+PREF_TRAVELED,TRAVELED[0]);
        if (trav.getCheckedRadioButtonId() == R.id.RadioButtonTraveledBy2) editor.putString(""+PREF_TRAVELED,TRAVELED[1]);
        if (trav.getCheckedRadioButtonId() == R.id.RadioButtonTraveledBy3) editor.putString(""+PREF_TRAVELED,TRAVELED[2]);
        if (trav.getCheckedRadioButtonId() == R.id.RadioButtonTraveledBy4) editor.putString(""+PREF_TRAVELED,TRAVELED[3]);
        if (trav.getCheckedRadioButtonId() == R.id.RadioButtonTraveledBy5) editor.putString(""+PREF_TRAVELED,TRAVELED[4]);
        if (trav.getCheckedRadioButtonId() == R.id.RadioButtonTraveledBy6) editor.putString(""+PREF_TRAVELED,TRAVELED[5]);
        if (trav.getCheckedRadioButtonId() == R.id.RadioButtonTraveledBy7) editor.putString(""+PREF_TRAVELED,TRAVELED[6]);

        //save household members
        editor.putString(""+PREF_MEMBERS,((EditText)findViewById(R.id.EditTextHouseHold)).getText().toString());

        //save non-household members
        editor.putString(""+PREF_NONMEMBERS,((EditText)findViewById(R.id.EditTextNoHouseHold)).getText().toString());

        //Any delay
        RadioGroup delay = (RadioGroup) findViewById(R.id.RadioGroupDelay);
        if (delay.getCheckedRadioButtonId() == R.id.ButtonDelayYes) editor.putString(""+PREF_DELAYS,"Yes");
        if (delay.getCheckedRadioButtonId() == R.id.ButtonDelayNo) editor.putString(""+PREF_DELAYS,"No");

        //pay toll?
        RadioGroup paytoll = (RadioGroup) findViewById(R.id.RadioGroupPayToll);
        if (paytoll.getCheckedRadioButtonId() == R.id.ButtonPayTollYes){
        	editor.putString(""+PREF_PAYTOLL,"Yes");
        	editor.putString(""+PREF_PAYTOLLAMT,((EditText)findViewById(R.id.EditTextFarePayToll)).getText().toString());
        }
        if (paytoll.getCheckedRadioButtonId() == R.id.ButtonPayTollNo) editor.putString(""+PREF_PAYTOLL,"No");

        //pay parking?
        RadioGroup paypark = (RadioGroup) findViewById(R.id.RadioGroupPayParking);
        if (paypark.getCheckedRadioButtonId() == R.id.ButtonPayParkingYes){
        	editor.putString(""+PREF_PAYPARKING,"Yes");
        	editor.putString(""+PREF_PAYPARKINGAMT,((EditText)findViewById(R.id.EditTextFarePayParking)).getText().toString());
        }
        if (paypark.getCheckedRadioButtonId() == R.id.ButtonPayParkingNo) editor.putString(""+PREF_PAYPARKING,"No");

        //how much fare?
        //editor.putString(""+PREF_FARE,((EditText)findViewById(R.id.EditTextFare)).getText().toString());
        if (trav.getCheckedRadioButtonId() == R.id.RadioButtonTraveledBy2)
        	editor.putString(""+PREF_FARE,((EditText)findViewById(R.id.EditTextFare)).getText().toString());
        //else
        	//editor.putFloat(""+PREF_FARE,0);
        	//editor.putString(""+PREF_FARE,"");

        //Don't forget to commit your edits!!!
        editor.commit();
		//Toast.makeText(getBaseContext(),"Every stop time questions saved.", Toast.LENGTH_SHORT).show();
	}

	// Called every time a purp togglebutton is changed:
	class CheckListener implements CompoundButton.OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton v, boolean isChecked) {
			// First, uncheck all purp buttons
			if (isChecked) {
				for (Entry<Integer, ToggleButton> e: purpButtons.entrySet()) {
					e.getValue().setChecked(false);
				}
				v.setChecked(true);
				purpose = v.getText().toString();
				//HS 1_29
				/*if (purpose.equals("Other")){
					//EditText pur=(EditText) findViewById(R.id.NotesField);
	            	purpose= ((EditText) findViewById(R.id.NotesField)).getText().toString();
	            	if (purpose.equals("")) {
						// Oh no!  No trip purpose!
						Toast.makeText(getBaseContext(), "You must type your \"Other\" trip purpose.", Toast.LENGTH_SHORT).show();
						return;
					}
				}*/
				/*if (purpose.equals("")) {
					// Oh no!  No trip purpose!
					Toast.makeText(getBaseContext(), "You must select your trip purpose.", Toast.LENGTH_SHORT).show();
					return;
				}*/

				((TextView) findViewById(R.id.TextPurpDescription)).setText(
				        Html.fromHtml(purpDescriptions.get(v.getId())));
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.save);
		//HS: disable the notes field for other comments until other button clicked
		((TextView) findViewById(R.id.NotesField)).setEnabled(false);
		finishRecording();

		// Set up trip purpose buttons
		//purpose = "";
		//purpose = "";
		preparePurposeButtons();

        // User prefs btn
        final Button prefsButton = (Button) findViewById(R.id.ButtonPrefs);
        final Intent pi = new Intent(this, UserInfoActivity.class);
        prefsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(pi);
            }
        });

        //HS 1_29, other toggle button fires other trip purpose textfield

        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        if (settings.getAll().size() >= 1) {
            prefsButton.setVisibility(View.GONE);
        }
        //HS 2_5_15
        else{
        	Toast.makeText(getBaseContext(), "User Information has not entered correctly, please enter!",	Toast.LENGTH_SHORT).show();
        }
        addListenerOnPurposeToggleButton();
        addListenerOnCityPublicBusShuttleRadioButton();
        addListenerOnPayTollRadioButton();
        addListenerOnPayParkingRadioButton();

        saveEveryStop();

		// Discard btn
		final Button btnDiscard = (Button) findViewById(R.id.ButtonDiscard);
		btnDiscard.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "Trip discarded.",	Toast.LENGTH_SHORT).show();

				cancelRecording();

				Intent i = new Intent(SaveTrip.this, MainInput.class);
				i.putExtra("keepme", true);
				startActivity(i);
				SaveTrip.this.finish();
			}
		});

		// Submit btn
		final Button btnSubmit = (Button) findViewById(R.id.ButtonSubmit);
		btnSubmit.setEnabled(false);

		// Don't pop up the soft keyboard until user clicks!
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	// submit btn is only activated after the service.finishedRecording() is completed.
	void activateSubmitButton() {
		final Button btnSubmit = (Button) findViewById(R.id.ButtonSubmit);
		final Intent xi = new Intent(this, ShowMap.class);
		btnSubmit.setEnabled(true);

		btnSubmit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				TripData trip = TripData.fetchTrip(SaveTrip.this, tripid);
				trip.populateDetails();

				// Make sure trip purpose has been selected
				//HS((EditText) findViewById(R.id.NotesField)).setEnabled(true);
				if (purpose.equals("")) {
					// Oh no!  No trip purpose!
					//Toast.makeText(getBaseContext(), "You must select a trip purpose before submitting! If you selected \"Other\", please type your purpose.", Toast.LENGTH_LONG).show();
					//return;
					purpose=((EditText) findViewById(R.id.NotesField)).getText().toString();
					if(purpose.equals("")){
						Toast.makeText(getBaseContext(), "You must select a trip purpose before submitting! If you selected \"Other\", please type your purpose.", Toast.LENGTH_LONG).show();
						return;
					}
				}


				//EditText notes = (EditText) findViewById(R.id.NotesField);
				String travelBy="";
				RadioGroup trav = (RadioGroup) findViewById(R.id.RadioGroupTraveledBy);
		        if (trav.getCheckedRadioButtonId() == R.id.RadioButtonTraveledBy1) travelBy=TRAVELED[0];
		        if (trav.getCheckedRadioButtonId() == R.id.RadioButtonTraveledBy2) travelBy=TRAVELED[1];
		        if (trav.getCheckedRadioButtonId() == R.id.RadioButtonTraveledBy3) travelBy=TRAVELED[2];
		        if (trav.getCheckedRadioButtonId() == R.id.RadioButtonTraveledBy4) travelBy=TRAVELED[3];
		        if (trav.getCheckedRadioButtonId() == R.id.RadioButtonTraveledBy5) travelBy=TRAVELED[4];
		        if (trav.getCheckedRadioButtonId() == R.id.RadioButtonTraveledBy6) travelBy=TRAVELED[5];
		        if (trav.getCheckedRadioButtonId() == R.id.RadioButtonTraveledBy7) travelBy=TRAVELED[6];

				// Make sure traveled by has been selected
				if (travelBy.equals("")) {
					Toast.makeText(getBaseContext(), "You must select a traveled by info before submitting!", Toast.LENGTH_SHORT).show();
					return;
				}

			    EditText members = (EditText) findViewById(R.id.EditTextHouseHold);
			    // Make sure household members field has been entered
				if (members.getText().toString().equals("")) {
					Toast.makeText(getBaseContext(), "You must enter household members traveled with you before submitting!", Toast.LENGTH_SHORT).show();
					return;
				}

			    EditText nonmembers = (EditText) findViewById(R.id.EditTextNoHouseHold);
			    // Make sure non-household members field has been entered
				if (nonmembers.getText().toString().equals("")) {
					Toast.makeText(getBaseContext(), "You must enter NOT household members traveled with you before submitting!", Toast.LENGTH_SHORT).show();
					return;
				}

			    int delays=-1;
			    RadioGroup delay = (RadioGroup) findViewById(R.id.RadioGroupDelay);
		        if (delay.getCheckedRadioButtonId() == R.id.ButtonDelayYes) delays=1;
		        if (delay.getCheckedRadioButtonId() == R.id.ButtonDelayNo) delays=0;
		        // Make sure delays info has been entered
				if (delays==-1) {
					Toast.makeText(getBaseContext(), "You must enter delays info before submitting!", Toast.LENGTH_SHORT).show();
					return;
				}

				int toll=-1;
				double tollAmtfinal=0.0;
				EditText tollAmt = (EditText) findViewById(R.id.EditTextFarePayToll);
				RadioGroup paytoll = (RadioGroup) findViewById(R.id.RadioGroupPayToll);
				if (paytoll.getCheckedRadioButtonId() == R.id.ButtonPayTollYes){
					toll=1;
					if (tollAmt.getText().toString().equals("")) {
						Toast.makeText(getBaseContext(), "You must enter your toll amount before submitting!", Toast.LENGTH_SHORT).show();
						return;
					}
					else
						tollAmtfinal=Double.parseDouble(tollAmt.getText().toString());
				}
				if (paytoll.getCheckedRadioButtonId() == R.id.ButtonPayTollNo){ toll=0; tollAmtfinal=0.0;}
				// Make sure toll info has been entered
				if (toll==-1) {
					Toast.makeText(getBaseContext(), "You must enter pay toll info before submitting!", Toast.LENGTH_SHORT).show();
					return;
				}

				int payForParking=-1;
				double payForParkingAmtfinal=0.0;
				EditText payForParkingAmt = (EditText) findViewById(R.id.EditTextFarePayParking);
				RadioGroup paypark = (RadioGroup) findViewById(R.id.RadioGroupPayParking);
				if (paypark.getCheckedRadioButtonId() == R.id.ButtonPayParkingYes){
					payForParking=1;
					if (payForParkingAmt.getText().toString().equals("")) {
						Toast.makeText(getBaseContext(), "You must enter your Parking amount before submitting!", Toast.LENGTH_SHORT).show();
						return;
					}
					else
						payForParkingAmtfinal=Double.parseDouble(payForParkingAmt.getText().toString());
				}
				if (paypark.getCheckedRadioButtonId() == R.id.ButtonPayParkingNo) {payForParking=0; payForParkingAmtfinal=0.0;}

				// Make sure parking info has been entered
				if (payForParking==-1) {
					Toast.makeText(getBaseContext(), "You must enter pay for parking info before submitting!", Toast.LENGTH_SHORT).show();
					return;
				}

				EditText fare = (EditText) findViewById(R.id.EditTextFare);
				// Make sure fare field has been entered
				/*if (fare.getText().toString().equals("")) {
					Toast.makeText(getBaseContext(), "You must enter your fare charges info before submitting or enter 0!", Toast.LENGTH_SHORT).show();
					return;
				}*/
				String fancyStartTime = DateFormat.getInstance().format(trip.startTime);

				// "3.5 miles in 26 minutes"
				SimpleDateFormat sdf = new SimpleDateFormat("m");
				sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				String minutes = sdf.format(trip.endTime - trip.startTime);
				String fancyEndInfo = String.format("%1.1f miles, %s minutes.  %s",
						(0.0006212f * trip.distance),
						minutes,
						//HS - no notes field - notes.getEditableText().toString());
						"");
				if (trav.getCheckedRadioButtonId() == R.id.RadioButtonTraveledBy2){
					if (fare.getText().toString().equals("")) {
						Toast.makeText(getBaseContext(), "You must enter your fare charges before submitting!", Toast.LENGTH_SHORT).show();
						return;
					}
					trip.updateTrip(
							purpose, travelBy, Integer.parseInt(members.getText().toString()), Integer.parseInt(nonmembers.getText().toString()),
							delays, toll, tollAmtfinal, payForParking, payForParkingAmtfinal,
							Double.parseDouble(fare.getText().toString()), fancyStartTime, fancyEndInfo);
				}
				else{
					trip.updateTrip(
							purpose, travelBy, Integer.parseInt(members.getText().toString()), Integer.parseInt(nonmembers.getText().toString()),
							delays, toll, tollAmtfinal, payForParking, payForParkingAmtfinal,
							0.0, fancyStartTime, fancyEndInfo);

				}

				// Save the trip details to the phone database. W00t!
				/*trip.updateTrip(
						purpose, travelBy, Integer.parseInt(members.getText().toString()), Integer.parseInt(nonmembers.getText().toString()),
						delays, toll, tollAmtfinal, payForParking, payForParkingAmtfinal,
						Double.parseDouble(fare.getText().toString()), fancyStartTime, fancyEndInfo);*/

				trip.updateTripStatus(TripData.STATUS_COMPLETE);
				resetService();

				// Force-drop the soft keyboard for performance
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

				// Now create the MainInput Activity so BACK btn works properly
				Intent i = new Intent(getApplicationContext(), MainInput.class);
				startActivity(i);

				// And, show the map!
                xi.putExtra("showtrip", trip.tripid);
                xi.putExtra("uploadTrip", true);
				startActivity(xi);
				SaveTrip.this.finish();
			}
		});

	}

	void cancelRecording() {
		Intent rService = new Intent(this, RecordingService.class);
		ServiceConnection sc = new ServiceConnection() {
			public void onServiceDisconnected(ComponentName name) {}
			public void onServiceConnected(ComponentName name, IBinder service) {
				IRecordService rs = (IRecordService) service;
				rs.cancelRecording();
				unbindService(this);
			}
		};
		// This should block until the onServiceConnected (above) completes.
		bindService(rService, sc, Context.BIND_AUTO_CREATE);
	}

	void resetService() {
		Intent rService = new Intent(this, RecordingService.class);
		ServiceConnection sc = new ServiceConnection() {
			public void onServiceDisconnected(ComponentName name) {}
			public void onServiceConnected(ComponentName name, IBinder service) {
				IRecordService rs = (IRecordService) service;
				rs.reset();
				unbindService(this);
			}
		};
		// This should block until the onServiceConnected (above) completes.
		bindService(rService, sc, Context.BIND_AUTO_CREATE);
	}

	void finishRecording() {
		Intent rService = new Intent(this, RecordingService.class);
		ServiceConnection sc = new ServiceConnection() {
			public void onServiceDisconnected(ComponentName name) {}
			public void onServiceConnected(ComponentName name, IBinder service) {
				IRecordService rs = (IRecordService) service;
				tripid = rs.finishRecording();
				SaveTrip.this.activateSubmitButton();
				unbindService(this);
			}
		};
		// This should block until the onServiceConnected (above) completes.
		bindService(rService, sc, Context.BIND_AUTO_CREATE);
	}

	public void addListenerOnPayTollRadioButton(){
    	RadioButton noButton = (RadioButton) findViewById(R.id.ButtonPayTollNo);
        RadioButton yesButton = (RadioButton) findViewById(R.id.ButtonPayTollYes);
        noButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                ((TextView) findViewById(R.id.TextViewFareDollarPayToll)).setEnabled(false);
                ((EditText)findViewById(R.id.EditTextFarePayToll)).setEnabled(false);
            }
          });
        yesButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((TextView) findViewById(R.id.TextViewFareDollarPayToll)).setEnabled(true);
                ((EditText)findViewById(R.id.EditTextFarePayToll)).setEnabled(true);
            }
          });
    }

	public void addListenerOnPayParkingRadioButton(){
    	RadioButton noButton = (RadioButton) findViewById(R.id.ButtonPayParkingNo);
        RadioButton yesButton = (RadioButton) findViewById(R.id.ButtonPayParkingYes);
        noButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                ((TextView) findViewById(R.id.TextViewFareDollarPayParking)).setEnabled(false);
                ((EditText)findViewById(R.id.EditTextFarePayParking)).setEnabled(false);
            }
          });
        yesButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((TextView) findViewById(R.id.TextViewFareDollarPayParking)).setEnabled(true);
                ((EditText)findViewById(R.id.EditTextFarePayParking)).setEnabled(true);
            }
          });
    }

	public void addListenerOnCityPublicBusShuttleRadioButton(){
    	RadioButton carTravelButton = (RadioButton) findViewById(R.id.RadioButtonTraveledBy1);
    	RadioButton cityTravelButton = (RadioButton) findViewById(R.id.RadioButtonTraveledBy2);
    	RadioButton schoolTravelButton = (RadioButton) findViewById(R.id.RadioButtonTraveledBy3);
    	RadioButton walkTravelButton = (RadioButton) findViewById(R.id.RadioButtonTraveledBy4);
    	RadioButton bikeTravelButton = (RadioButton) findViewById(R.id.RadioButtonTraveledBy5);
    	RadioButton carpoolTravelButton = (RadioButton) findViewById(R.id.RadioButtonTraveledBy6);
    	RadioButton otherTravelButton = (RadioButton) findViewById(R.id.RadioButtonTraveledBy7);

    	carTravelButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((TextView) findViewById(R.id.TextViewFare)).setEnabled(false);
            	((TextView) findViewById(R.id.TextViewFareDollar)).setEnabled(false);
                ((EditText)findViewById(R.id.EditTextFare)).setEnabled(false);
            }
        });

        cityTravelButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((TextView) findViewById(R.id.TextViewFare)).setEnabled(true);
            	((TextView) findViewById(R.id.TextViewFareDollar)).setEnabled(true);
                ((EditText)findViewById(R.id.EditTextFare)).setEnabled(true);
            }
        });

        schoolTravelButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((TextView) findViewById(R.id.TextViewFare)).setEnabled(false);
            	((TextView) findViewById(R.id.TextViewFareDollar)).setEnabled(false);
                ((EditText)findViewById(R.id.EditTextFare)).setEnabled(false);
            }
        });

        walkTravelButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((TextView) findViewById(R.id.TextViewFare)).setEnabled(false);
            	((TextView) findViewById(R.id.TextViewFareDollar)).setEnabled(false);
                ((EditText)findViewById(R.id.EditTextFare)).setEnabled(false);
            }
        });

        bikeTravelButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((TextView) findViewById(R.id.TextViewFare)).setEnabled(false);
            	((TextView) findViewById(R.id.TextViewFareDollar)).setEnabled(false);
                ((EditText)findViewById(R.id.EditTextFare)).setEnabled(false);
            }
        });

        carpoolTravelButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((TextView) findViewById(R.id.TextViewFare)).setEnabled(false);
            	((TextView) findViewById(R.id.TextViewFareDollar)).setEnabled(false);
                ((EditText)findViewById(R.id.EditTextFare)).setEnabled(false);
            }
        });

        otherTravelButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((TextView) findViewById(R.id.TextViewFare)).setEnabled(false);
            	((TextView) findViewById(R.id.TextViewFareDollar)).setEnabled(false);
                ((EditText)findViewById(R.id.EditTextFare)).setEnabled(false);
            }
        });
	}
	public void addListenerOnPurposeToggleButton(){
		ToggleButton commuteButton = (ToggleButton) findViewById(R.id.ToggleCommute);
		ToggleButton schoolButton = (ToggleButton) findViewById(R.id.ToggleSchool);
		ToggleButton workButton = (ToggleButton) findViewById(R.id.ToggleWorkRel);
		ToggleButton excerciseButton = (ToggleButton) findViewById(R.id.ToggleExercise);
		ToggleButton socialButton = (ToggleButton) findViewById(R.id.ToggleSocial);
		ToggleButton shoppingButton = (ToggleButton) findViewById(R.id.ToggleShopping);
		ToggleButton errandButton = (ToggleButton) findViewById(R.id.ToggleErrand);
		ToggleButton collegeButton = (ToggleButton) findViewById(R.id.ToggleCollege);
		ToggleButton daycareButton = (ToggleButton) findViewById(R.id.ToggleDaycare);
		ToggleButton otherButton = (ToggleButton) findViewById(R.id.ToggleOther);
		commuteButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((EditText) findViewById(R.id.NotesField)).setEnabled(false);
            }
        });
		schoolButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((EditText) findViewById(R.id.NotesField)).setEnabled(false);
            }
        });
		workButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((EditText) findViewById(R.id.NotesField)).setEnabled(false);
            }
        });
		excerciseButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((EditText) findViewById(R.id.NotesField)).setEnabled(false);
            }
        });
		socialButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((EditText) findViewById(R.id.NotesField)).setEnabled(false);
            }
        });
		shoppingButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((EditText) findViewById(R.id.NotesField)).setEnabled(false);
            }
        });
		errandButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((EditText) findViewById(R.id.NotesField)).setEnabled(false);
            }
        });
		collegeButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((EditText) findViewById(R.id.NotesField)).setEnabled(false);
            }
        });
		daycareButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((EditText) findViewById(R.id.NotesField)).setEnabled(false);
            }
        });
        otherButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((EditText) findViewById(R.id.NotesField)).setEnabled(true);
            	EditText pur=(EditText) findViewById(R.id.NotesField);
            	purpose=pur.getText().toString();
            }
        });


	}
}
