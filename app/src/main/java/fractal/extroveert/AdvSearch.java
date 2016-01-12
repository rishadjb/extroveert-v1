package fractal.extroveert;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by osadmin on 22/09/15.
 */
public class AdvSearch extends Activity implements OnClickListener {

    EditText lat;
    EditText lon;

    TextView search_button;

    private EditText startDate_text, endDate_text, startTime_text, endTime_text, startPrice_text, endPrice_text;

    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;
    private TimePickerDialog startTimePickerDialog;
    private TimePickerDialog endTimePickerDialog;

    private SimpleDateFormat dateFormatter;

    private String userID;

    LinearLayout layout_events, layout_users, slide_bar;
    TextView button_people, button_events;
    Animation LeftSwipeIn, LeftSwipeOut, RightSwipeIn, RightSwipeOut, slide_bar_left, slide_bar_right;


    AutoCompleteTextView location_text;
    MultiAutoCompleteTextView eventType_text;
    TextView radius_text;

    String eventType[] = {"Concert", "Theater", "Sports", "Other"};


    String city_list;

    double myLat, myLon;


    TextView username;
    TextView addremove_button;
    TextView block_button;


    LinearLayout userprofile_layout;

    AutoCompleteTextView usersearch_field;

    String USERA, USERB, USERA_enc, USERB_enc, ADD_ACTION, BLOCK_ACTION;

    String USERA_db, USERB_db, current_status;

    String JSONcheck = "empty";


    String user_list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advsearch);

        //get user ID from SharedPreferences
        userID = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("user", "DEFAULT");


        findViewsById();


        GetMyLocation getloc = new GetMyLocation(this);

        myLat = getloc.GetCurrentLat();
        myLon = getloc.GetCurrentLon();

        //lat.setText(Double.toString(myLat));
        //lon.setText(Double.toString(myLon));



        //----------------------------- SET THE DROPDOWN FOR CITY LIST --------------------------------------
        city_list = getCityList();
        String cityList[] = city_list.split(",,");

        ArrayAdapter<String> citylist_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cityList);

        location_text.setThreshold(2);  //2 characters before options are shown
        location_text.setAdapter(citylist_adapter);
        //----------------------------- SET THE DROPDOWN FOR CITY LIST --------------------------------------
        //----------------------Action for when CITIES dropdown item is selected-----------------------------
        location_text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        //#----------------------Action for when dropdown item is selected------------------------------------


        //----------------------------- SET THE DROPDOWN FOR EVENT TYPE --------------------------------------
        String eventType[] = {"Concert", "Theater", "Sports", "Other"};
        ArrayAdapter<String> eventType_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, eventType);
        eventType_text.setThreshold(0);  //2 characters before options are shown
        eventType_text.setAdapter(eventType_adapter);
        eventType_text.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        //#----------------------------- SET THE DROPDOWN FOR EVENT TYPE --------------------------------------


        search_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EventGrid.class);
                i.putExtra("cityName", location_text.getText().toString());
                i.putExtra("searchRadius", radius_text.getText().toString());

                i.putExtra("startDate", startDate_text.getText().toString());
                i.putExtra("endDate", endDate_text.getText().toString());
                i.putExtra("startTime", startTime_text.getText().toString());
                i.putExtra("endTime", endTime_text.getText().toString());
                i.putExtra("startPrice", startPrice_text.getText().toString());
                i.putExtra("endPrice", endPrice_text.getText().toString());

                i.putExtra("eventType", eventType_text.getText().toString());
                startActivity(i);


            }
        });

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);


        setDateTimeField();

        layout_events = (LinearLayout) findViewById(R.id.layout_events);
        layout_users = (LinearLayout) findViewById(R.id.layout_users);
        slide_bar = (LinearLayout) findViewById(R.id.slide_bar);

        button_people = (TextView) findViewById(R.id.button_people);
        button_events = (TextView) findViewById(R.id.button_events);

        RightSwipeIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
        RightSwipeOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
        LeftSwipeIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
        LeftSwipeOut = AnimationUtils.loadAnimation(this, R.anim.swipe_left_out);

        slide_bar_left = AnimationUtils.loadAnimation(this, R.anim.slide_bar_left);
        slide_bar_right = AnimationUtils.loadAnimation(this, R.anim.slide_bar_right);


        layout_users.setVisibility(View.GONE);

        //------------------------------------- onClick action for People button ---------------------------------
        findViewById(R.id.button_people).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (layout_users.getVisibility() != View.VISIBLE) {
                    button_people.setTextAppearance(getApplicationContext(), R.style.boldText);
                    button_events.setTextAppearance(getApplicationContext(), R.style.normalText);

                    layout_users.setVisibility(View.VISIBLE);
                    layout_events.startAnimation(LeftSwipeOut);
                    //slide_bar.startAnimation(slide_bar_left);
                    layout_users.startAnimation(LeftSwipeIn);
                    layout_events.setVisibility(View.GONE);
                }

            }
        });
        //#------------------------------------- onClick action for People button ---------------------------------


        //------------------------------------- onClick action for Events button ---------------------------------
        findViewById(R.id.button_events).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (layout_events.getVisibility() != View.VISIBLE) {
                    button_events.setTextAppearance(getApplicationContext(), R.style.boldText);
                    button_people.setTextAppearance(getApplicationContext(), R.style.normalText);

                    layout_events.setVisibility(View.VISIBLE);
                    //slide_bar.startAnimation(slide_bar_right);
                    layout_users.startAnimation(RightSwipeOut);
                    layout_events.startAnimation(RightSwipeIn);
                    layout_users.setVisibility(View.GONE);
                }

            }
        });
        //#------------------------------------- onClick action for Events button ---------------------------------


        //===========================================================================================================
        //------------------------------------------ USERS search panel----------------------------------------------
        //===========================================================================================================


        USERA = userID;

        try {
            USERA_enc = URLEncoder.encode(USERA, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Click on the View Friends button to open Friends screen
        (findViewById(R.id.viewfriends)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MyFriends.class));
            }
        });


        //----------------------Autocompletetextview for userlist----------------------------------
        usersearch_field = (AutoCompleteTextView) findViewById(R.id.usersearch_field);

        user_list = getUserList();
        String userList[] = user_list.split(",,");

        ArrayAdapter<String> userlist_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, userList);

        usersearch_field.setThreshold(2);  //2 characters before options are shown
        usersearch_field.setAdapter(userlist_adapter);

        //----------------------Action for when dropdown item is selected------------------------------------
        usersearch_field.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                USERB = usersearch_field.getText().toString();

                //encode the username for the sql query
                try {
                    USERB_enc = URLEncoder.encode(USERB, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                GetUserProfile();
                Toast.makeText(getApplicationContext(), usersearch_field.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });
        //#----------------------Action for when dropdown item is selected------------------------------------
        //#----------------------Autocompletetextview for userlist----------------------------------

        userprofile_layout = (LinearLayout) findViewById(R.id.userprofile_layout);
        userprofile_layout.setVisibility(View.GONE);

        //===========================================================================================================
        //------------------------------------------ USERS search panel----------------------------------------------
        //===========================================================================================================


        //===========================================================================================================
        //------------------------------------------ BOTTOM NAV BAR SETUP--------------------------------------------
        //===========================================================================================================
        String className = this.getClass().getSimpleName();
        (findViewById(R.id.button_home)).setOnClickListener(new MyClickListener(this, className, EventGrid.class));
        (findViewById(R.id.button_search)).setOnClickListener(new MyClickListener(this, className, AdvSearch.class));
        (findViewById(R.id.button_profile)).setOnClickListener(new MyClickListener(this, className, MyProfile.class));
        (findViewById(R.id.button_exit)).setOnClickListener(new MyClickListener(this, className, Startup.class));
        //===========================================================================================================
        //------------------------------------------ BOTTOM NAV BAR SETUP--------------------------------------------
        //===========================================================================================================


    }

    private void findViewsById() {
        search_button = (TextView) findViewById(R.id.search_button);
        location_text = (AutoCompleteTextView) findViewById(R.id.location_text);
        eventType_text = (MultiAutoCompleteTextView) findViewById(R.id.eventType_text);
        radius_text = (TextView) findViewById(R.id.radius_text);


        startDate_text = (EditText) findViewById(R.id.startDate_text);
        startDate_text.setInputType(InputType.TYPE_NULL);
        //startDate_text.requestFocus();

        endDate_text = (EditText) findViewById(R.id.endDate_text);
        endDate_text.setInputType(InputType.TYPE_NULL);

        startTime_text = (EditText) findViewById(R.id.startTime_text);
        startTime_text.setInputType(InputType.TYPE_NULL);

        endTime_text = (EditText) findViewById(R.id.endTime_text);
        endTime_text.setInputType(InputType.TYPE_NULL);

        startPrice_text = (EditText) findViewById(R.id.startPrice_text);
        endPrice_text = (EditText) findViewById(R.id.endPrice_text);

    }

    //Create Date and Time picker fields
    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();

        startDate_text.setOnClickListener(this);
        endDate_text.setOnClickListener(this);

        startTime_text.setOnClickListener(this);
        endTime_text.setOnClickListener(this);

        startDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                startDate_text.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        endDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                endDate_text.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


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
        startDatePickerDialog.setTitle("Select Start Date");
        endDatePickerDialog.setTitle("Select End Date");
        startTimePickerDialog.setTitle("Select Start Time");
        endTimePickerDialog.setTitle("Select End Time");
    }

    //Onclick action for date and time fields
    @Override
    public void onClick(View view) {

        if (view == startDate_text) {
            startDatePickerDialog.show();
        } else if (view == endDate_text) {
            endDatePickerDialog.show();
        }

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

    public void GetUserProfile() {

        userprofile_layout.setVisibility(View.VISIBLE);

        check_friend_status();

        username = (TextView) findViewById(R.id.username);
        username.setText(USERB);

        addremove_button = (TextView) findViewById(R.id.addremove_button);
        block_button = (TextView) findViewById(R.id.block_button);

        //new person
        if (current_status.equals("")) {
            addremove_button.setText("ADD");
            block_button.setText("BLOCK");
        }
        //friends
        else if (current_status.equals("friend")) {
            addremove_button.setText("REMOVE");
            block_button.setText("BLOCK");
        }
        //unfriended
        else if (current_status.equals("unfriend")) {
            addremove_button.setText("ADD");
            block_button.setText("BLOCK");
        }
        //blocked
        else if (current_status.equals("blocked")) {
            addremove_button.setText("ADD");

            if (USERB.equals(USERA_db)) {
                block_button.setText("BLOCK");
            } else block_button.setText("UNBLOCK");

        }
        //unblocked
        else if (current_status.equals("unblocked")) {
            addremove_button.setText("ADD");
            block_button.setText("BLOCK");
        }


        //USERB = username.getText().toString();
        ADD_ACTION = addremove_button.getText().toString();
        BLOCK_ACTION = block_button.getText().toString();

        //-------------------------onclick event for add button-----------------------------------
        addremove_button.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    if (USERB.equals(USERA_db) && current_status.equals("blocked")) {
                                                        Toast.makeText(getApplicationContext(), "You have been blocked by " + USERB, Toast.LENGTH_LONG).show();
                                                    } else {
                                                        //if you want to add user
                                                        if (ADD_ACTION.equals("ADD")) {
                                                            addremove_button.setText("REMOVE");
                                                            ADD_ACTION = "REMOVE";
                                                            block_button.setText("BLOCK");
                                                            BLOCK_ACTION = "BLOCK";
                                                            update_friend("add");
                                                        }
                                                        //if you want to remove user
                                                        else if (ADD_ACTION.equals("REMOVE")) {
                                                            addremove_button.setText("ADD");
                                                            ADD_ACTION = "ADD";
                                                            update_friend("remove");
                                                        }
                                                    }
                                                }
                                            }
        );
        //#-------------------------onclick event for add button-----------------------------------


        //-------------------------onclick event for block button-----------------------------------
        block_button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                if (USERB.equals(USERA_db) && current_status.equals("blocked")) {
                                                    Toast.makeText(getApplicationContext(), "You have been blocked by " + USERB, Toast.LENGTH_LONG).show();
                                                } else {
                                                    //if you want to block user
                                                    if (BLOCK_ACTION.equals("BLOCK")) {
                                                        block_button.setText("UNBLOCK");
                                                        BLOCK_ACTION = "UNBLOCK";
                                                        addremove_button.setText("ADD");
                                                        ADD_ACTION = "ADD";
                                                        update_friend("block");
                                                    }
                                                    //if you want to unblock user
                                                    else if (BLOCK_ACTION.equals("UNBLOCK")) {
                                                        block_button.setText("BLOCK");
                                                        BLOCK_ACTION = "BLOCK";
                                                        update_friend("unblock");
                                                    }
                                                }

                                            }
                                        }
        );
        //#-------------------------onclick event for block button-----------------------------------


    }


    /*
    Method that is called for updating database when user likes or unlikes an event
    */
    public void update_friend(String ACTION) {

        InteractDB myData = new InteractDB("http://99.235.237.216/extv/AddRemoveFriend.php?USERA=" + USERA_enc + "&USERB=" + USERB_enc + "&ACTION=" + ACTION);
        myData.execute();
    }


    public void check_friend_status() {

        JSONcheck = "empty";


        InteractDB myData = new InteractDB("http://99.235.237.216/extv/AddRemoveFriend.php?USERA=" + USERA_enc + "&USERB=" + USERB_enc + "&ACTION=");

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
            JSONArray jsonMainNode = jsonObject.getJSONArray("friendstatus");
            JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
            USERA_db = jsonChildNode.optString("USERAdb");
            USERB_db = jsonChildNode.optString("USERBdb");
            current_status = jsonChildNode.optString("status");
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    public String getUserList() {

        JSONcheck = "empty";

        InteractDB myData = new InteractDB("http://www.extroveert.com/extv/GetUserList.php");

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
            JSONArray jsonMainNode = jsonObject.getJSONArray("userlist");
            JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
            user_list = jsonChildNode.optString("users");
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        //myData = null;


        return user_list;
    }


}