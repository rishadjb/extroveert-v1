package fractal.extroveert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;



/**
 * Created by root on 02/09/15.
 */
public class MyProfile extends Activity {
    GridView grid;
    String JSONcheck = "empty";

    String[] sku, eventname, buyurl, date, time, address, venue, city, state, catA, catB, pricerange;
    Integer[] likeflag;
    Double[] price, lat, lon;

    String userID;

    Double myLat, myLon;

    String sql_query, sql_query_prefix, sql_query_parameters;

    String cityName, searchRadius, startDate, endDate, startTime, endTime, startPrice, endPrice, eventType;
    String gmap_address;

    LinearLayout advsearch_button;
    TextView navbar_top_info;


    //------------------------------------- declare arraylists ---------------------------------
    public ArrayList<String> sku_arraylist = new ArrayList<String>();
    public ArrayList<String> eventname_arraylist = new ArrayList<String>();
    public ArrayList<String> buyurl_arraylist = new ArrayList<String>();
    public ArrayList<String> date_arraylist = new ArrayList<String>();
    public ArrayList<String> time_arraylist = new ArrayList<String>();
    public ArrayList<String> address_arraylist = new ArrayList<String>();
    public ArrayList<String> venue_arraylist = new ArrayList<String>();
    public ArrayList<String> city_arraylist = new ArrayList<String>();
    public ArrayList<String> state_arraylist = new ArrayList<String>();
    public ArrayList<String> catA_arraylist = new ArrayList<String>();
    public ArrayList<String> catB_arraylist = new ArrayList<String>();
    public ArrayList<String> pricerange_arraylist = new ArrayList<String>();
    public ArrayList<Double> price_arraylist = new ArrayList<Double>();
    public ArrayList<Double> lat_arraylist = new ArrayList<Double>();
    public ArrayList<Double> lon_arraylist = new ArrayList<Double>();
    public ArrayList<Integer> likeflag_arraylist = new ArrayList<Integer>();
    //#------------------------------------- declare arraylists ---------------------------------

    //public EventGrid() { }

    public void getEventsFromDB(){



        //sql_query = "http://99.235.237.216/extv/getTickets.php?"+"mylat="+myLat+"&mylon="+myLon;

        //------------------------------------- create SQL query ---------------------------------
        sql_query_prefix = "http://99.235.237.216/extv/GetLikedEvents.php?";

        sql_query_parameters = "userid="+userID;

        sql_query = sql_query_prefix + sql_query_parameters;
        //#------------------------------------- create SQL query ---------------------------------

        //------------------------------------- call InteractDB get JSON object ---------------------------------
        InteractDB myData = new InteractDB(sql_query);
        myData.execute();

        while(JSONcheck=="empty"){
            try{
                JSONcheck = myData.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        //------------------------------------- call InteractDB get JSON object ---------------------------------



        //------------------------------------- get event detals from database ---------------------------------
        try{
            JSONObject jsonObject = new JSONObject(JSONcheck);

            JSONArray jsonMainNode = jsonObject.getJSONArray("eventdetails");

            for (int i = 0; i < jsonMainNode.length(); i++)
            {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                //------------------------------------- assign JSON node values to variables ---------------------------------
                String sku = jsonChildNode.optString("sku");
                String eventname = jsonChildNode.optString("eventname");
                String buyurl = jsonChildNode.optString("buyurl");
                String date = jsonChildNode.optString("date");
                String time = jsonChildNode.optString("time");
                String address = jsonChildNode.optString("address");
                String venue = jsonChildNode.optString("venue");
                String city = jsonChildNode.optString("city");
                String state = jsonChildNode.optString("state");
                String catA = jsonChildNode.optString("catA");
                String catB = jsonChildNode.optString("catB");
                String pricerange = jsonChildNode.optString("pricerange");
                double price = Double.parseDouble(jsonChildNode.optString("price"));
                double lat = Double.parseDouble(jsonChildNode.optString("lat"));
                double lon = Double.parseDouble(jsonChildNode.optString("lon"));
                Integer likeflag = Integer.parseInt(jsonChildNode.optString("likeflag"));
                //#------------------------------------- assign JSON node values to variables ---------------------------------

                //------------------------------------- add variable value to arraylist ---------------------------------
                sku_arraylist.add(sku);
                eventname_arraylist.add(eventname);
                buyurl_arraylist.add(buyurl);
                date_arraylist.add(date);
                time_arraylist.add(time);
                address_arraylist.add(address);
                venue_arraylist.add(venue);
                city_arraylist.add(city);
                state_arraylist.add(state);
                catA_arraylist.add(catA);
                catB_arraylist.add(catB);
                pricerange_arraylist.add(pricerange);
                price_arraylist.add(price);
                lat_arraylist.add(lat);
                lon_arraylist.add(lon);
                likeflag_arraylist.add(likeflag);
                //#------------------------------------- add variable value to arraylist ---------------------------------

            }


        }
        catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
        //#------------------------------------- get event details from database ---------------------------------

        //------------------------------------- convert arraylists to array ---------------------------------
        sku = sku_arraylist.toArray(new String[sku_arraylist.size()]);
        eventname = eventname_arraylist.toArray(new String[eventname_arraylist.size()]);
        buyurl = buyurl_arraylist.toArray(new String[buyurl_arraylist.size()]);
        date = date_arraylist.toArray(new String[date_arraylist.size()]);
        time = time_arraylist.toArray(new String[time_arraylist.size()]);
        address = address_arraylist.toArray(new String[address_arraylist.size()]);
        venue = venue_arraylist.toArray(new String[venue_arraylist.size()]);
        city = city_arraylist.toArray(new String[city_arraylist.size()]);
        state = state_arraylist.toArray(new String[state_arraylist.size()]);
        catA = catA_arraylist.toArray(new String[catA_arraylist.size()]);
        catB = catB_arraylist.toArray(new String[catB_arraylist.size()]);
        pricerange = pricerange_arraylist.toArray(new String[pricerange_arraylist.size()]);
        price = price_arraylist.toArray(new Double[price_arraylist.size()]);
        lat = lat_arraylist.toArray(new Double[lat_arraylist.size()]);
        lon = lon_arraylist.toArray(new Double[lon_arraylist.size()]);
        likeflag = likeflag_arraylist.toArray(new Integer[likeflag_arraylist.size()]);
        //#------------------------------------- convert arraylists to array ---------------------------------


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile);

        //get user ID from SharedPreferences
        userID = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("user", "DEFAULT");
        Toast.makeText(getApplicationContext(), userID, Toast.LENGTH_SHORT).show();

        getEventsFromDB();  //call function get events near acquired latitude and longitude

        //Click on the View Friends button to open Friends screen
        (findViewById(R.id.viewfriends)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MyFriends.class));
            }
        });

        //Click on the Edit Profile button
        (findViewById(R.id.editprofile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditProfile.class));
            }
        });


        //------------------------------------- create grid ---------------------------------
        CustomGrid_likedevents adapter = new CustomGrid_likedevents(MyProfile.this, savedInstanceState, userID, sku, eventname, buyurl, date, time, address, venue, city,
                state, catA, catB, pricerange, price, lat, lon,likeflag);
        grid = (GridView)findViewById(R.id.grid_likedevents);
        grid.setAdapter(adapter);
        //#------------------------------------- create grid ---------------------------------

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
}