/**	 TripTracks, (c) 2014 Florida Transportation Authority
 * 					  Florida, USA
 *
 *   	 @author Hema Sahu <hema.sahu@urs.com>
 *
 */
package com.urs.triptracks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoActivity extends Activity {
    /*public final static int PREF_AGE = 1;
    public final static int PREF_ZIPHOME = 2;
    public final static int PREF_ZIPWORK = 3;
    public final static int PREF_ZIPSCHOOL = 4;
    public final static int PREF_EMAIL = 5;
    public final static int PREF_GENDER = 6;
    public final static int PREF_CYCLEFREQ = 7;*/
	public final static int PREF_GENDER = 1;
    public final static int PREF_AGE = 2;
    public final static int PREF_EMPLOYMENTSTATUS1 = 3;
    public final static int PREF_EMPLOYMENTSTATUS2 = 4;
    public final static int PREF_EMPLOYMENTSTATUS3 = 5;
    public final static int PREF_EMPLOYMENTSTATUS4 = 6;
    public final static int PREF_EMPLOYMENTSTATUS5 = 7;
    public final static int PREF_EMPLOYMENTSTATUS6 = 8;
    public final static int PREF_EMPLOYMENTSTATUS7 = 9;
    public final static int PREF_EMPLOYMENTSTATUS8 = 10;
    public final static int PREF_DAYSWORKTRIP = 11;
    public final static int PREF_STUDENT = 12;
    public final static int PREF_STUDENTLEVEL = 13;
    public final static int PREF_VALIDDRIVERLIC = 14;
    public final static int PREF_TRANSITPASS = 15;
    public final static int PREF_DISABLEDPARKPASS = 16;

    private final static int MENU_SAVE = 0;

    //final String[] freqDesc = {"Less than once a month", "Several times a month", "Several times per week", "Daily"};
    final String [] ES = {"Full-time 40 hours and more than 5 months of the year", "Part-time 20 hours and more than 5 months of the year", "Employed less than 5 months of the year",
    		"Unemployed all civilians 16 years old and over neither at work nor with a job but not at work, actively looking for work, and able to accept a job",
    		"Retired", "Work at home", "Homemaker", "Self employed"};
    final String [] WT = {"1 day", "2 days", "3 days", "4 days", "5 days", "6 days", "7 days"};
    final String [] STL = {"Preschool/Daycare", "Kindergarten", "Elementary school", "Middle school", "High school", "College/University"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprefs);

        // Don't pop up the soft keyboard until user clicks!
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        addListenerOnStudentRadioButton();
        addListenerOnAge0To15RadioButton();
        //addListenerOnEmploymentCheckboxes();

       /* SharedPreferences settings = getSharedPreferences("PREFS", 0);
        Map <String, ?> prefs = settings.getAll();
        for (Entry <String, ?> p : prefs.entrySet()) {
            int key = Integer.parseInt(p.getKey());
            CharSequence value = (CharSequence) p.getValue();
           // String value = (String) p.getValue();

           switch (key) {
           case PREF_GENDER:
               if (value.equals("M")) {
                   ((RadioButton) findViewById(R.id.ButtonMale)).setChecked(true);
               } else if (value.equals("F")) {
                   ((RadioButton) findViewById(R.id.ButtonFemale)).setChecked(true);
               }
        	   RadioGroup rbg = (RadioGroup) findViewById(R.id.RadioGroupGender);
               if (rbg.getCheckedRadioButtonId() == R.id.ButtonMale) ((RadioButton) findViewById(R.id.ButtonMale)).setChecked(true);
               if (rbg.getCheckedRadioButtonId() == R.id.ButtonFemale) editor.putString(""+PREF_GENDER,"F");
               break;
            case PREF_AGE:
            	if (value.equals("0-4")) {
                    ((RadioButton) findViewById(R.id.RadioButtonAge)).setChecked(true);
                } else if (value.equals("5-15")) {
                    ((RadioButton) findViewById(R.id.RadioButtonAge1)).setChecked(true);
                } else if (value.equals("16-21")) {
                    ((RadioButton) findViewById(R.id.RadioButtonAge2)).setChecked(true);
                } else if (value.equals("22-49")) {
                    ((RadioButton) findViewById(R.id.RadioButtonAge3)).setChecked(true);
                } else if (value.equals("50-64")) {
                    ((RadioButton) findViewById(R.id.RadioButtonAge4)).setChecked(true);
                } else if (value.equals("65 or older")) {
                    ((RadioButton) findViewById(R.id.RadioButtonAge5)).setChecked(true);
                }
                break;
            case PREF_EMPLOYMENTSTATUS1:
            	if (value.equals(ES[0])) {
                    ((CheckBox) findViewById(R.id.CheckBoxES1)).setChecked(true);
                }
                break;
            case PREF_EMPLOYMENTSTATUS2:
            	if (value.equals(ES[1])) {
                    ((CheckBox) findViewById(R.id.CheckBoxES2)).setChecked(true);
                }
            	break;
            case PREF_EMPLOYMENTSTATUS3:
            	if (value.equals(ES[2])) {
                    ((CheckBox) findViewById(R.id.CheckBoxES3)).setChecked(true);
                }
            	break;
            case PREF_EMPLOYMENTSTATUS4:
            	if (value.equals(ES[3])) {
                    ((CheckBox) findViewById(R.id.CheckBoxES4)).setChecked(true);
                }
            	break;
            case PREF_EMPLOYMENTSTATUS5:
            	if (value.equals(ES[4])) {
                    ((CheckBox) findViewById(R.id.CheckBoxES5)).setChecked(true);
                }
            	break;
            case PREF_EMPLOYMENTSTATUS6:
            	if (value.equals(ES[5])) {
                    ((CheckBox) findViewById(R.id.CheckBoxES6)).setChecked(true);
                }
            	break;
            case PREF_EMPLOYMENTSTATUS7:
            	if (value.equals(ES[6])) {
                    ((CheckBox) findViewById(R.id.CheckBoxES7)).setChecked(true);
                }
            	break;
            case PREF_EMPLOYMENTSTATUS8:
            	if (value.equals(ES[7])) {
                    ((CheckBox) findViewById(R.id.CheckBoxES8)).setChecked(true);
                }
            	break;
            case PREF_DAYSWORKTRIP:
            	if (value.equals(WT[0])) {
                    ((RadioButton) findViewById(R.id.RadioButton1day)).setChecked(true);
                } else if (value.equals(WT[1])) {
                    ((RadioButton) findViewById(R.id.RadioButton2day)).setChecked(true);
                } else if (value.equals(WT[2])) {
                    ((RadioButton) findViewById(R.id.RadioButton3day)).setChecked(true);
                } else if (value.equals(WT[3])) {
                    ((RadioButton) findViewById(R.id.RadioButton4day)).setChecked(true);
                } else if (value.equals(WT[4])) {
                    ((RadioButton) findViewById(R.id.RadioButton5day)).setChecked(true);
                } else if (value.equals(WT[5])) {
                    ((RadioButton) findViewById(R.id.RadioButton6day)).setChecked(true);
                } else if (value.equals(WT[6])) {
                    ((RadioButton) findViewById(R.id.RadioButton7day)).setChecked(true);
                }
                break;
            case PREF_STUDENT:
                if (value.equals("Yes")) {
                    ((RadioButton) findViewById(R.id.ButtonSYes)).setChecked(true);
                } else if (value.equals("No")) {
                    ((RadioButton) findViewById(R.id.ButtonSNo)).setChecked(true);
                    ((RadioButton) findViewById(R.id.RadioButtonDaycare)).setEnabled(false);
                	((RadioButton) findViewById(R.id.RadioButtonKindergarten)).setEnabled(false);
                    ((RadioButton) findViewById(R.id.RadioButtonElementary)).setEnabled(false);
                    ((RadioButton) findViewById(R.id.RadioButtonMiddle)).setEnabled(false);
                    ((RadioButton) findViewById(R.id.RadioButtonHigh)).setEnabled(false);
                    ((RadioButton) findViewById(R.id.RadioButtonCollege)).setEnabled(false);
                    ((TextView) findViewById(R.id.TextViewStudentLevel)).setEnabled(false);
                }
                break;
            case PREF_STUDENTLEVEL:
            	if (value.equals(STL[0])) {
                    ((RadioButton) findViewById(R.id.RadioButtonDaycare)).setChecked(true);
                } else if (value.equals(STL[1])) {
                    ((RadioButton) findViewById(R.id.RadioButtonKindergarten)).setChecked(true);
                } else if (value.equals(STL[2])) {
                    ((RadioButton) findViewById(R.id.RadioButtonElementary)).setChecked(true);
                } else if (value.equals(STL[3])) {
                    ((RadioButton) findViewById(R.id.RadioButtonMiddle)).setChecked(true);
                } else if (value.equals(STL[4])) {
                    ((RadioButton) findViewById(R.id.RadioButtonHigh)).setChecked(true);
                } else if (value.equals(STL[5])) {
                    ((RadioButton) findViewById(R.id.RadioButtonCollege)).setChecked(true);
                }
                break;
            case PREF_VALIDDRIVERLIC:
                if (value.equals("Yes")) {
                    ((RadioButton) findViewById(R.id.ButtonDrLicYes)).setChecked(true);
                } else if (value.equals("No")) {
                    ((RadioButton) findViewById(R.id.ButtonDrLicNo)).setChecked(true);
                }
                break;
            case PREF_TRANSITPASS:
                if (value.equals("Yes")) {
                    ((RadioButton) findViewById(R.id.ButtonTransitPassYes)).setChecked(true);
                } else if (value.equals("No")) {
                    ((RadioButton) findViewById(R.id.ButtonTransitPassNo)).setChecked(true);
                }
                break;
            case PREF_DISABLEDPARKPASS:
                if (value.equals("Yes")) {
                    ((RadioButton) findViewById(R.id.ButtonDisParkPassYes)).setChecked(true);
                } else if (value.equals("No")) {
                    ((RadioButton) findViewById(R.id.ButtonDisParkPassNo)).setChecked(true);
                }
                break;
            }
        }*/
    }

    @Override
    public void onDestroy() {
        savePreferences();
        super.onDestroy();
    }

    private void savePreferences() {
        // Save user preferences. We need an Editor object to
        // make changes. All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        SharedPreferences.Editor editor = settings.edit();

        /*editor.putString(""+PREF_AGE,((EditText)findViewById(R.id.TextAge)).getText().toString());
        editor.putString(""+PREF_ZIPHOME,((EditText)findViewById(R.id.TextZipHome)).getText().toString());
        editor.putString(""+PREF_ZIPWORK,((EditText)findViewById(R.id.TextZipWork)).getText().toString());
        editor.putString(""+PREF_ZIPSCHOOL,((EditText)findViewById(R.id.TextZipSchool)).getText().toString());
        editor.putString(""+PREF_EMAIL,((EditText)findViewById(R.id.TextEmail)).getText().toString());
        editor.putString(""+PREF_CYCLEFREQ,""+((SeekBar)findViewById(R.id.SeekCycleFreq)).getProgress());*/

        //Save Gender
        RadioGroup rbg = (RadioGroup) findViewById(R.id.RadioGroupGender);
        if (rbg.getCheckedRadioButtonId() == R.id.ButtonMale) editor.putString(""+PREF_GENDER,"M");
        if (rbg.getCheckedRadioButtonId() == R.id.ButtonFemale) editor.putString(""+PREF_GENDER,"F");
        //Gender is needed!
        if (rbg.getCheckedRadioButtonId() == -1) {
			Toast.makeText(getBaseContext(), "Gender is needed!", Toast.LENGTH_SHORT).show();
			return;
		}

        //Save Age
        RadioGroup chg = (RadioGroup) findViewById(R.id.RadioGroupAge);
        if (chg.getCheckedRadioButtonId() == R.id.RadioButtonAge){
        	editor.putString(""+PREF_AGE,"0-4");
        	//driver license is 0 (as this radio button will be disabled)
        	editor.putInt(""+PREF_VALIDDRIVERLIC,0);
        }
        if (chg.getCheckedRadioButtonId() == R.id.RadioButtonAge1){
        	editor.putString(""+PREF_AGE,"5-15");
        	//driver license is 0 (as this radio button will be disabled)
        	editor.putInt(""+PREF_VALIDDRIVERLIC,0);
        }
        if (chg.getCheckedRadioButtonId() == R.id.RadioButtonAge2) editor.putString(""+PREF_AGE,"16-21");
        if (chg.getCheckedRadioButtonId() == R.id.RadioButtonAge3) editor.putString(""+PREF_AGE,"22-49");
        if (chg.getCheckedRadioButtonId() == R.id.RadioButtonAge4) editor.putString(""+PREF_AGE,"50-64");
        if (chg.getCheckedRadioButtonId() == R.id.RadioButtonAge5) editor.putString(""+PREF_AGE,"65 or older");
        //Age is needed!
        if (chg.getCheckedRadioButtonId() == -1) {
			Toast.makeText(getBaseContext(), "Age is needed!", Toast.LENGTH_SHORT).show();
			return;
		}

        //Save Employment Status
        CheckBox chk1 = (CheckBox) findViewById(R.id.CheckBoxES1);
        CheckBox chk2 = (CheckBox) findViewById(R.id.CheckBoxES2);
        CheckBox chk3 = (CheckBox) findViewById(R.id.CheckBoxES3);
        CheckBox chk4 = (CheckBox) findViewById(R.id.CheckBoxES4);
        CheckBox chk5 = (CheckBox) findViewById(R.id.CheckBoxES5);
        CheckBox chk6 = (CheckBox) findViewById(R.id.CheckBoxES6);
        CheckBox chk7 = (CheckBox) findViewById(R.id.CheckBoxES7);
        CheckBox chk8 = (CheckBox) findViewById(R.id.CheckBoxES8);

        if (chk1.isChecked()) {editor.putInt(""+PREF_EMPLOYMENTSTATUS1, 1);} else {editor.putInt(""+PREF_EMPLOYMENTSTATUS1, 0);}
        if (chk2.isChecked()) {editor.putInt(""+PREF_EMPLOYMENTSTATUS2, 1);} else {editor.putInt(""+PREF_EMPLOYMENTSTATUS2, 0);}
        if (chk3.isChecked()) {editor.putInt(""+PREF_EMPLOYMENTSTATUS3, 1);} else {editor.putInt(""+PREF_EMPLOYMENTSTATUS3, 0);}
        if (chk4.isChecked()) {editor.putInt(""+PREF_EMPLOYMENTSTATUS4, 1);} else {editor.putInt(""+PREF_EMPLOYMENTSTATUS4, 0);}
        if (chk5.isChecked()) {editor.putInt(""+PREF_EMPLOYMENTSTATUS5, 1);} else {editor.putInt(""+PREF_EMPLOYMENTSTATUS5, 0);}
        if (chk6.isChecked()) {editor.putInt(""+PREF_EMPLOYMENTSTATUS6, 1);} else {editor.putInt(""+PREF_EMPLOYMENTSTATUS6, 0);}
        if (chk7.isChecked()) {editor.putInt(""+PREF_EMPLOYMENTSTATUS7, 1);} else {editor.putInt(""+PREF_EMPLOYMENTSTATUS7, 0);}
        if (chk8.isChecked()) {editor.putInt(""+PREF_EMPLOYMENTSTATUS8, 1);} else {editor.putInt(""+PREF_EMPLOYMENTSTATUS8, 0);}

        //Save days per week work
        RadioGroup wtg = (RadioGroup) findViewById(R.id.RadioGroupDays);
        if (wtg.getCheckedRadioButtonId() == R.id.RadioButton1day) editor.putInt(""+PREF_DAYSWORKTRIP,1);
        if (wtg.getCheckedRadioButtonId() == R.id.RadioButton2day) editor.putInt(""+PREF_DAYSWORKTRIP,2);
        if (wtg.getCheckedRadioButtonId() == R.id.RadioButton3day) editor.putInt(""+PREF_DAYSWORKTRIP,3);
        if (wtg.getCheckedRadioButtonId() == R.id.RadioButton4day) editor.putInt(""+PREF_DAYSWORKTRIP,4);
        if (wtg.getCheckedRadioButtonId() == R.id.RadioButton5day) editor.putInt(""+PREF_DAYSWORKTRIP,5);
        if (wtg.getCheckedRadioButtonId() == R.id.RadioButton6day) editor.putInt(""+PREF_DAYSWORKTRIP,6);
        if (wtg.getCheckedRadioButtonId() == R.id.RadioButton7day) editor.putInt(""+PREF_DAYSWORKTRIP,7);
        //by default days per week work is Zero
        if (wtg.getCheckedRadioButtonId() == -1) {
        	editor.putInt(""+PREF_DAYSWORKTRIP,0);
		}

        //Save Student or not
        RadioGroup stg = (RadioGroup) findViewById(R.id.RadioGroupStudent);
        if (stg.getCheckedRadioButtonId() == R.id.ButtonSYes) editor.putInt(""+PREF_STUDENT,1);
        if (stg.getCheckedRadioButtonId() == R.id.ButtonSNo){
        	editor.putInt(""+PREF_STUDENT,0);
        	//when selected as not a student, the entry is empty string
        	editor.putString(""+PREF_STUDENTLEVEL,"");
        }
       //Are you a student?
        if (stg.getCheckedRadioButtonId() == -1) {
			Toast.makeText(getBaseContext(), "Are you a student?", Toast.LENGTH_SHORT).show();
			return;
		}

        //Save Student level
        RadioGroup stlg = (RadioGroup) findViewById(R.id.RadioGroupStudentLevel);
        if (stlg.getCheckedRadioButtonId() == R.id.RadioButtonDaycare) editor.putString(""+PREF_STUDENTLEVEL,STL[0]);
        if (stlg.getCheckedRadioButtonId() == R.id.RadioButtonKindergarten) editor.putString(""+PREF_STUDENTLEVEL,STL[1]);
        if (stlg.getCheckedRadioButtonId() == R.id.RadioButtonElementary) editor.putString(""+PREF_STUDENTLEVEL,STL[2]);
        if (stlg.getCheckedRadioButtonId() == R.id.RadioButtonMiddle) editor.putString(""+PREF_STUDENTLEVEL,STL[3]);
        if (stlg.getCheckedRadioButtonId() == R.id.RadioButtonHigh) editor.putString(""+PREF_STUDENTLEVEL,STL[4]);
        if (stlg.getCheckedRadioButtonId() == R.id.RadioButtonCollege) editor.putString(""+PREF_STUDENTLEVEL,STL[5]);
        //If Student is "Yes", make sure student level checked
        if (stg.getCheckedRadioButtonId() == R.id.ButtonSYes){
        	if (stlg.getCheckedRadioButtonId() == -1) {
    			Toast.makeText(getBaseContext(), "You are a student, but student level has not checked!", Toast.LENGTH_SHORT).show();
    			return;
    		}
        }

        //Save Valid Personal Driver's license
        RadioGroup drlc = (RadioGroup) findViewById(R.id.RadioGroupDrLic);
        if (drlc.getCheckedRadioButtonId() == R.id.ButtonDrLicYes) editor.putInt(""+PREF_VALIDDRIVERLIC,1);
        if (drlc.getCheckedRadioButtonId() == R.id.ButtonDrLicNo) editor.putInt(""+PREF_VALIDDRIVERLIC,0);
      //valid personal driver license radio button has not checked while the age is >= 16
        if ((chg.getCheckedRadioButtonId() == R.id.RadioButtonAge2) || (chg.getCheckedRadioButtonId() == R.id.RadioButtonAge3) ||(chg.getCheckedRadioButtonId() == R.id.RadioButtonAge4)
        		||(chg.getCheckedRadioButtonId() == R.id.RadioButtonAge5)){
        	if (drlc.getCheckedRadioButtonId() == -1) {
    			Toast.makeText(getBaseContext(), "Valid personal driver's license has not checked!", Toast.LENGTH_SHORT).show();
    			return;
    		}
        }

        //Save Transit Pass
        RadioGroup tpass = (RadioGroup) findViewById(R.id.RadioGroupTransitPass);
        if (tpass.getCheckedRadioButtonId() == R.id.ButtonTransitPassYes) editor.putInt(""+PREF_TRANSITPASS,1);
        if (tpass.getCheckedRadioButtonId() == R.id.ButtonTransitPassNo) editor.putInt(""+PREF_TRANSITPASS,0);
        if (tpass.getCheckedRadioButtonId() == -1) {
			Toast.makeText(getBaseContext(), "Transit pass has not checked!", Toast.LENGTH_SHORT).show();
			return;
		}

        //Save disabled parking
        RadioGroup disp = (RadioGroup) findViewById(R.id.RadioGroupDisParkPass);
        if (disp.getCheckedRadioButtonId() == R.id.ButtonDisParkPassYes) editor.putInt(""+PREF_DISABLEDPARKPASS,1);
        if (disp.getCheckedRadioButtonId() == R.id.ButtonDisParkPassNo) editor.putInt(""+PREF_DISABLEDPARKPASS,0);
        if (disp.getCheckedRadioButtonId() == -1) {
			Toast.makeText(getBaseContext(), "Disabled parking pass has not checked!", Toast.LENGTH_SHORT).show();
			return;
		}

        // Don't forget to commit your edits!!!
        editor.commit();
		//Toast.makeText(getBaseContext(),"User preferences saved, now click on Start Trip!", Toast.LENGTH_SHORT).show();
		this.finish();
    }

    /* Creates the menu items */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menu.add(0, MENU_SAVE, 0, "Save").setIcon(android.R.drawable.ic_menu_save);
        return true;
    }

    /* Handles item selections */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* switch (item.getItemId()) {
        case MENU_SAVE:
            savePreferences();
            this.finish();
            return true;
        }*/
        return false;
    }

    public void saveUserPref(View v){
    	savePreferences();
       //HS this.finish(); this one is going to Start Trip! page without warning messages
    }

    public void addListenerOnStudentRadioButton(){
    	RadioButton noButton = (RadioButton) findViewById(R.id.ButtonSNo);
        RadioButton yesButton = (RadioButton) findViewById(R.id.ButtonSYes);
        noButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((RadioButton) findViewById(R.id.RadioButtonDaycare)).setEnabled(false);
            	((RadioButton) findViewById(R.id.RadioButtonKindergarten)).setEnabled(false);
                ((RadioButton) findViewById(R.id.RadioButtonElementary)).setEnabled(false);
                ((RadioButton) findViewById(R.id.RadioButtonMiddle)).setEnabled(false);
                ((RadioButton) findViewById(R.id.RadioButtonHigh)).setEnabled(false);
                ((RadioButton) findViewById(R.id.RadioButtonCollege)).setEnabled(false);
                ((TextView) findViewById(R.id.TextViewStudentLevel)).setEnabled(false);
            }
          });
        yesButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((RadioButton) findViewById(R.id.RadioButtonDaycare)).setEnabled(true);
            	((RadioButton) findViewById(R.id.RadioButtonKindergarten)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButtonElementary)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButtonMiddle)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButtonHigh)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButtonCollege)).setEnabled(true);
                ((TextView) findViewById(R.id.TextViewStudentLevel)).setEnabled(true);
            }
          });
    }

    public void addListenerOnAge0To15RadioButton(){
    	RadioButton zeroButton = (RadioButton) findViewById(R.id.RadioButtonAge);
        RadioButton fiveButton = (RadioButton) findViewById(R.id.RadioButtonAge1);
        RadioButton sixteenButton = (RadioButton) findViewById(R.id.RadioButtonAge2);
        RadioButton twenty2Button = (RadioButton) findViewById(R.id.RadioButtonAge3);
        RadioButton fiftyButton = (RadioButton) findViewById(R.id.RadioButtonAge4);
        RadioButton sixty5Button = (RadioButton) findViewById(R.id.RadioButtonAge5);
        zeroButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((TextView) findViewById(R.id.TextViewES)).setEnabled(false);
            	((CheckBox) findViewById(R.id.CheckBoxES1)).setEnabled(false);
            	((CheckBox) findViewById(R.id.CheckBoxES2)).setEnabled(false);
            	((CheckBox) findViewById(R.id.CheckBoxES3)).setEnabled(false);
            	((CheckBox) findViewById(R.id.CheckBoxES4)).setEnabled(false);
            	((CheckBox) findViewById(R.id.CheckBoxES5)).setEnabled(false);
            	((CheckBox) findViewById(R.id.CheckBoxES6)).setEnabled(false);
            	((CheckBox) findViewById(R.id.CheckBoxES7)).setEnabled(false);
            	((CheckBox) findViewById(R.id.CheckBoxES8)).setEnabled(false);

            	((TextView) findViewById(R.id.TextViewDays)).setEnabled(false);
            	((RadioButton) findViewById(R.id.RadioButton1day)).setEnabled(false);
                ((RadioButton) findViewById(R.id.RadioButton2day)).setEnabled(false);
                ((RadioButton) findViewById(R.id.RadioButton3day)).setEnabled(false);
                ((RadioButton) findViewById(R.id.RadioButton4day)).setEnabled(false);
                ((RadioButton) findViewById(R.id.RadioButton5day)).setEnabled(false);
                ((RadioButton) findViewById(R.id.RadioButton6day)).setEnabled(false);
                ((RadioButton) findViewById(R.id.RadioButton7day)).setEnabled(false);

                ((TextView) findViewById(R.id.TextViewDrLic)).setEnabled(false);
                ((RadioButton) findViewById(R.id.ButtonDrLicYes)).setEnabled(false);
                ((RadioButton) findViewById(R.id.ButtonDrLicNo)).setEnabled(false);

            }
          });
        fiveButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((TextView) findViewById(R.id.TextViewES)).setEnabled(false);
            	((CheckBox) findViewById(R.id.CheckBoxES1)).setEnabled(false);
            	((CheckBox) findViewById(R.id.CheckBoxES2)).setEnabled(false);
            	((CheckBox) findViewById(R.id.CheckBoxES3)).setEnabled(false);
            	((CheckBox) findViewById(R.id.CheckBoxES4)).setEnabled(false);
            	((CheckBox) findViewById(R.id.CheckBoxES5)).setEnabled(false);
            	((CheckBox) findViewById(R.id.CheckBoxES6)).setEnabled(false);
            	((CheckBox) findViewById(R.id.CheckBoxES7)).setEnabled(false);
            	((CheckBox) findViewById(R.id.CheckBoxES8)).setEnabled(false);

            	((TextView) findViewById(R.id.TextViewDays)).setEnabled(false);
            	((RadioButton) findViewById(R.id.RadioButton1day)).setEnabled(false);
                ((RadioButton) findViewById(R.id.RadioButton2day)).setEnabled(false);
                ((RadioButton) findViewById(R.id.RadioButton3day)).setEnabled(false);
                ((RadioButton) findViewById(R.id.RadioButton4day)).setEnabled(false);
                ((RadioButton) findViewById(R.id.RadioButton5day)).setEnabled(false);
                ((RadioButton) findViewById(R.id.RadioButton6day)).setEnabled(false);
                ((RadioButton) findViewById(R.id.RadioButton7day)).setEnabled(false);

                ((TextView) findViewById(R.id.TextViewDrLic)).setEnabled(false);
                ((RadioButton) findViewById(R.id.ButtonDrLicYes)).setEnabled(false);
                ((RadioButton) findViewById(R.id.ButtonDrLicNo)).setEnabled(false);
            }
          });
        sixteenButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((TextView) findViewById(R.id.TextViewES)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES1)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES2)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES3)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES4)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES5)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES6)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES7)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES8)).setEnabled(true);

            	((TextView) findViewById(R.id.TextViewDays)).setEnabled(true);
            	((RadioButton) findViewById(R.id.RadioButton1day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton2day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton3day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton4day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton5day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton6day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton7day)).setEnabled(true);

                ((TextView) findViewById(R.id.TextViewDrLic)).setEnabled(true);
                ((RadioButton) findViewById(R.id.ButtonDrLicYes)).setEnabled(true);
                ((RadioButton) findViewById(R.id.ButtonDrLicNo)).setEnabled(true);

            }
          });
        twenty2Button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((TextView) findViewById(R.id.TextViewES)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES1)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES2)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES3)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES4)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES5)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES6)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES7)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES8)).setEnabled(true);

            	((TextView) findViewById(R.id.TextViewDays)).setEnabled(true);
            	((RadioButton) findViewById(R.id.RadioButton1day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton2day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton3day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton4day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton5day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton6day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton7day)).setEnabled(true);

                ((TextView) findViewById(R.id.TextViewDrLic)).setEnabled(true);
                ((RadioButton) findViewById(R.id.ButtonDrLicYes)).setEnabled(true);
                ((RadioButton) findViewById(R.id.ButtonDrLicNo)).setEnabled(true);

            }
          });
        fiftyButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((TextView) findViewById(R.id.TextViewES)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES1)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES2)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES3)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES4)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES5)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES6)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES7)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES8)).setEnabled(true);

            	((TextView) findViewById(R.id.TextViewDays)).setEnabled(true);
            	((RadioButton) findViewById(R.id.RadioButton1day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton2day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton3day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton4day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton5day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton6day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton7day)).setEnabled(true);

                ((TextView) findViewById(R.id.TextViewDrLic)).setEnabled(true);
                ((RadioButton) findViewById(R.id.ButtonDrLicYes)).setEnabled(true);
                ((RadioButton) findViewById(R.id.ButtonDrLicNo)).setEnabled(true);

            }
          });
        sixty5Button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	((TextView) findViewById(R.id.TextViewES)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES1)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES2)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES3)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES4)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES5)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES6)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES7)).setEnabled(true);
            	((CheckBox) findViewById(R.id.CheckBoxES8)).setEnabled(true);

            	((TextView) findViewById(R.id.TextViewDays)).setEnabled(true);
            	((RadioButton) findViewById(R.id.RadioButton1day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton2day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton3day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton4day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton5day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton6day)).setEnabled(true);
                ((RadioButton) findViewById(R.id.RadioButton7day)).setEnabled(true);

                ((TextView) findViewById(R.id.TextViewDrLic)).setEnabled(true);
                ((RadioButton) findViewById(R.id.ButtonDrLicYes)).setEnabled(true);
                ((RadioButton) findViewById(R.id.ButtonDrLicNo)).setEnabled(true);
            }
          });
    }

}
