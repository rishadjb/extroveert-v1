package fractal.extroveert;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by osadmin on 08/01/16.
 */
public class EditProfile extends Activity implements OnClickListener{

    SharedPreferences sharedpref;

    LinearLayout layout_personal, layout_searchpref;
    TextView button_searchpref, button_personal;
    Animation LeftSwipeIn, LeftSwipeOut, RightSwipeIn, RightSwipeOut;

    String USERA;

    private EditText startTime_text, endTime_text, startPrice_text, endPrice_text, radius_text;

    //default field strings
    String default_location;

    private TimePickerDialog startTimePickerDialog;
    private TimePickerDialog endTimePickerDialog;

    private SimpleDateFormat dateFormatter;

    AutoCompleteTextView location_text, eventType_text;
    TextView update_button;

    String eventType[] = {"Concert", "Theater", "Sports", "Other"};


    String city_list;

    double myLat, myLon;

    String JSONcheck = "empty";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile);

        findViewsById();

        GetMyLocation getloc = new GetMyLocation(this);

        myLat = getloc.GetCurrentLat();
        myLon = getloc.GetCurrentLon();

        city_list = getCityList();

        String cityList[] = city_list.split(",,");

        ArrayAdapter<String> citylist_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cityList);
        ArrayAdapter<String> eventType_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, eventType);

        location_text.setThreshold(2);  //2 characters before options are shown
        location_text.setAdapter(citylist_adapter);

        //----------------------Action for when dropdown item is selected------------------------------------
        location_text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        //#----------------------Action for when dropdown item is selected------------------------------------

        eventType_text.setThreshold(0);  //2 characters before options are shown
        eventType_text.setAdapter(eventType_adapter);


        setDateTimeField();


        sharedpref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        USERA = sharedpref.getString("user", "DEFAULT");

        saveDefaultFields();


        layout_personal = (LinearLayout) findViewById(R.id.layout_personal);
        layout_searchpref = (LinearLayout) findViewById(R.id.layout_searchpref);

        button_searchpref = (TextView) findViewById(R.id.button_searchpref);
        button_personal = (TextView) findViewById(R.id.button_personal);

        RightSwipeIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
        RightSwipeOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
        LeftSwipeIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
        LeftSwipeOut = AnimationUtils.loadAnimation(this, R.anim.swipe_left_out);



        layout_searchpref.setVisibility(View.GONE);

        //------------------------------------- onClick action for People button ---------------------------------
        findViewById(R.id.button_searchpref).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (layout_searchpref.getVisibility() != View.VISIBLE) {
                    button_searchpref.setTextAppearance(getApplicationContext(), R.style.boldText);
                    button_personal.setTextAppearance(getApplicationContext(), R.style.normalText);

                    layout_searchpref.setVisibility(View.VISIBLE);
                    layout_personal.startAnimation(LeftSwipeOut);
                    layout_searchpref.startAnimation(LeftSwipeIn);
                    layout_personal.setVisibility(View.GONE);
                }

            }
        });
        //#------------------------------------- onClick action for People button ---------------------------------


        //------------------------------------- onClick action for Events button ---------------------------------
        findViewById(R.id.button_personal).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (layout_personal.getVisibility() != View.VISIBLE) {
                    button_personal.setTextAppearance(getApplicationContext(), R.style.boldText);
                    button_searchpref.setTextAppearance(getApplicationContext(), R.style.normalText);

                    layout_personal.setVisibility(View.VISIBLE);
                    layout_searchpref.startAnimation(RightSwipeOut);
                    layout_personal.startAnimation(RightSwipeIn);
                    layout_searchpref.setVisibility(View.GONE);
                }

            }
        });
        //#------------------------------------- onClick action for Events button ---------------------------------

        update_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), location_text.getText().toString(), Toast.LENGTH_SHORT).show();


                sharedpref.edit().putString("default_location", location_text.getText().toString()).apply();
                sharedpref.edit().putString("default_searchRadius", radius_text.getText().toString() ).apply();
                sharedpref.edit().putString("default_startRime", startTime_text.getText().toString() ).apply();
                sharedpref.edit().putString("default_endRime", endTime_text.getText().toString() ).apply();
                sharedpref.edit().putString("default_startPrice", startPrice_text.getText().toString() ).apply();
                sharedpref.edit().putString("default_endPrice", endPrice_text.getText().toString() ).apply();

            }
        });


    }

    private void findViewsById() {

        update_button = (TextView) findViewById(R.id.updateprofile_button);

        location_text = (AutoCompleteTextView) findViewById(R.id.location_text);
        eventType_text = (AutoCompleteTextView) findViewById(R.id.eventType_text);
        radius_text = (EditText) findViewById(R.id.radius_text);

        startTime_text = (EditText) findViewById(R.id.startTime_text);
        startTime_text.setInputType(InputType.TYPE_NULL);

        endTime_text = (EditText) findViewById(R.id.endTime_text);
        endTime_text.setInputType(InputType.TYPE_NULL);

        startPrice_text = (EditText) findViewById(R.id.startPrice_text);
        endPrice_text = (EditText) findViewById(R.id.endPrice_text);

    }

    private void saveDefaultFields(){
        saveOneDefaultField(location_text, "default_location");
        saveOneDefaultField(radius_text, "default_searchRadius");
        saveOneDefaultField(startTime_text, "default_startTime");
        saveOneDefaultField(endTime_text, "default_endTime");
        saveOneDefaultField(startPrice_text, "default_startPrice");
        saveOneDefaultField(endPrice_text, "default_endPrice");
        saveOneDefaultField(eventType_text, "default_eventType");
    }

    private void saveOneDefaultField(EditText field, String sharedfield){
        field.setText(sharedpref.getString(sharedfield, ""));
    }

    //Create Date and Time picker fields
    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();

        startTime_text.setOnClickListener(this);
        endTime_text.setOnClickListener(this);

        startTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                startTime_text.setText( selectedHour + ":" + selectedMinute);
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), false);//Yes 24 hour time


        endTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                endTime_text.setText( selectedHour + ":" + selectedMinute);
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), false);//Yes 24 hour time

        //Set title for the date and time picker popups
        startTimePickerDialog.setTitle("Select Start Time");
        endTimePickerDialog.setTitle("Select End Time");
    }

    //Onclick action for date and time fields
    @Override
    public void onClick(View view) {

        if (view == startTime_text) {
            startTimePickerDialog.show();
        }
        else if (view == endTime_text) {
            endTimePickerDialog.show();
        }
    }


    public String getCityList() {
        InteractDB myData = new InteractDB("http://99.235.237.216/extv/getCityList.php");

        myData.execute();
        while (JSONcheck == "empty") {
            try {
                JSONcheck = myData.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        try {
            JSONObject jsonObject = new JSONObject(JSONcheck);
            JSONArray jsonMainNode = jsonObject.getJSONArray("citylist");
            JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
            city_list = jsonChildNode.optString("city");
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
        return city_list;
    }
}
