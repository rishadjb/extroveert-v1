package fractal.extroveert;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;



/**
 * Created by root on 02/09/15.
 */
public class EventGrid extends FragmentActivity implements OnMapReadyCallback {
    GridView grid;
    LinearLayout event_invite_prompt;

    String JSONcheck = "empty";

    String[] sku, eventname, buyurl, date, time, address, venue, city, state, catA, catB, pricerange;
    Integer[] likeflag;
    Double[] price, lat, lon;

    Double myLat, myLon;

    String sql_query, sql_query_prefix, sql_query_parameters;

    String cityName, searchRadius, startDate, endDate, startTime, endTime, startPrice, endPrice, eventType;
    String gmap_address;
    Double gmap_lat,gmap_lon;

    String userID;

    private Context nContext;

    TextView navbar_top_info;
    String navbarinfo_string;



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

        //------------------------------------- urlEncode cityName ---------------------------------
        try{
            cityName = URLEncoder.encode(cityName,"UTF-8");
        }
        catch (UnsupportedEncodingException e){e.printStackTrace();}
        //#------------------------------------- urlEncode cityName ---------------------------------


        //sql_query = "http://99.235.237.216/extv/getTickets.php?"+"mylat="+myLat+"&mylon="+myLon;

        //------------------------------------- create SQL query ---------------------------------
        sql_query_prefix = "http://99.235.237.216/extv/getTickets.php?";
        sql_query_parameters = "mylat="+myLat+"&mylon="+myLon+"&cityName="+cityName+"&searchRadius="+searchRadius
                +"&startDate="+startDate+"&endDate="+endDate
                +"&startTime="+startTime+"&endTime="+endTime
                +"&startPrice="+startPrice+"&endPrice="+endPrice
                +"&eventType="+eventType+"&userID="+userID;

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


        //------------------------------------- get address name from places api textsearch ---------------------------------
        try{
            JSONObject jsonObject = new JSONObject(JSONcheck);
            JSONArray jsonMainNode = jsonObject.getJSONArray("addressdet");
            JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
            gmap_address = jsonChildNode.optString("addr");
            gmap_lat = Double.parseDouble(jsonChildNode.optString("myLat"));
            gmap_lon = Double.parseDouble(jsonChildNode.optString("myLon"));
        }
        catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
        //#------------------------------------- get address name from places api textsearch ---------------------------------

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

        nContext = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_grid);

        //get user ID from SharedPreferences
        userID = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("user", "DEFAULT");
        //GetUserID userIDs = new GetUserID();
        //userID = userIDs.returnUserID();


        //------------------------------------- get variables passed via intent ---------------------------------
        cityName = getIntent().getStringExtra("cityName");

        searchRadius = getIntent().getStringExtra("searchRadius");
        if(searchRadius.equals("")) searchRadius = "5";

        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
        startTime = getIntent().getStringExtra("startTime");
        endTime = getIntent().getStringExtra("endTime");
        startPrice = getIntent().getStringExtra("startPrice");
        endPrice = getIntent().getStringExtra("endPrice");
        eventType = getIntent().getStringExtra("eventType");

        //------------------------------------- get current location coordinates ---------------------------------
        GetMyLocation getloc = new GetMyLocation(this);

        myLat = getloc.GetCurrentLat();
        myLon = getloc.GetCurrentLon();
        //#------------------------------------- get current location coordinates ---------------------------------

        getEventsFromDB();  //call function get events near acquired latitude and longitude


        if (cityName.equals("")) cityName = "you";
        else{
            try{
                cityName = URLDecoder.decode(cityName, "UTF-8");
            }
            catch (UnsupportedEncodingException e){e.printStackTrace();}
        }


        navbarinfo_string = "Within " + searchRadius + " km of " + cityName;
        navbar_top_info = (TextView) findViewById(R.id.navbar_top_info);
        navbar_top_info.setText(navbarinfo_string);


        //------------------------------------- create grid ---------------------------------
        CustomGrid adapter = new CustomGrid(this, EventGrid.this, savedInstanceState, userID, sku, eventname, buyurl, date, time, address, venue, city,
                state, catA, catB, pricerange, price, lat, lon,likeflag);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        //#------------------------------------- create grid ---------------------------------



        /*

        //------------------------------------- put location details in search bar ---------------------------------
        TextView navbar_top_info;
        navbar_top_info =  (TextView) findViewById(R.id.navbar_top_info);

        //if the address returned by places api textsearch is "", then change it to 'you'
        if (gmap_address.equals("")) {gmap_address="you";}

        navbar_top_info.setText("Today, within " + searchRadius + " km of " + gmap_address);
        //------------------------------------- put location details in search bar ---------------------------------


        //------------------------------------- onClick action for advanced search button ---------------------------------
        ImageView advsearch_button;
        advsearch_button = (ImageView) findViewById(R.id.navbar_top_advsearch);

        // view products click event
        advsearch_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), AdvSearch.class);
                startActivity(i);
            }
        });
        //#------------------------------------- onClick action for advanced search button ---------------------------------

        //------------------------------------- onClick action for profile button ---------------------------------
        ImageView myprofile_button;
        myprofile_button = (ImageView) findViewById(R.id.navbar_top_profilepic);

        myprofile_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), MyProfile.class);
                startActivity(i);
            }
        });
        //#------------------------------------- onClick action for profile button ---------------------------------
*/

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

        //------------------------------------- onClick action for View Map button ---------------------------------
        TextView viewmap_button;
        viewmap_button = (TextView) findViewById(R.id.viewmap_button);

        viewmap_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                //Animation LeftSwipeOut = AnimationUtils.loadAnimation(EventGrid.this, R.anim.swipe_left_out);

                //findViewById(R.id.viewmap_button).startAnimation(LeftSwipeOut);
                findViewById(R.id.viewmap_button).setVisibility(View.GONE);
                findViewById(R.id.viewlist_button).setVisibility(View.VISIBLE);
                findViewById(R.id.grid).setVisibility(View.GONE);
                findViewById(R.id.mapview).setVisibility(View.VISIBLE);

            }
        });
        //#------------------------------------- onClick action for View Map button ---------------------------------

        //------------------------------------- onClick action for View as List button ---------------------------------
        TextView viewlist_button;
        viewlist_button = (TextView) findViewById(R.id.viewlist_button);

        viewlist_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                findViewById(R.id.viewlist_button).setVisibility(View.GONE);
                findViewById(R.id.viewmap_button).setVisibility(View.VISIBLE);
                findViewById(R.id.grid).setVisibility(View.VISIBLE);
                findViewById(R.id.mapview).setVisibility(View.GONE);
            }
        });
        //#------------------------------------- onClick action for View as List button ---------------------------------


        //-------------------------generate MAP for event-----------------------------------
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapview);
        mapFragment.getMapAsync(this);

        findViewById(R.id.mapview).setVisibility(View.GONE);

        //#-------------------------generate MAP for event-----------------------------------


        //Toast.makeText(getApplicationContext(), sql_query, Toast.LENGTH_LONG).show();


        //------------------------------------- onClick action for View as List button ---------------------------------
        event_invite_prompt = (LinearLayout) findViewById(R.id.event_invite_prompt);

        event_invite_prompt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                event_invite_prompt.setVisibility(View.GONE);
            }
        });
        //#------------------------------------- onClick action for View as List button ---------------------------------


    }

    @Override
    public void onMapReady(GoogleMap map) {

        map.addMarker(new MarkerOptions().position(new LatLng(gmap_lat, gmap_lon)).title("You"));

        LatLng ME = new LatLng(gmap_lat, gmap_lon);
        for (int i=0; i<lat.length; i++)
        {
            map.addMarker(new MarkerOptions().position(new LatLng(lat[i], lon[i])));
        }


        //map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(ME));
    }


    //Don't think this is being used
    public class setButtons extends Activity{

        Bundle savedInstanceState;

        setButtons(Bundle savedstate){

            savedInstanceState = savedstate;

            setContentView(R.layout.event_grid);


        }

        //------------------------------------- onClick action for advanced search button ---------------------------------

    }
}
