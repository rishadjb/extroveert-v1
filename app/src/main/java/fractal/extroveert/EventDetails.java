package fractal.extroveert;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by osadmin on 11/10/15.
 */
public class EventDetails extends FragmentActivity implements OnMapReadyCallback{
    
    private String sku, eventname, buyurl, date, time, address, venue;
    private Integer likeflag;
    private Double price, lat, lon;

    AutoCompleteTextView usersearch_field;
    TextView friendlist[] = new TextView[15];
    TextView see_invitees, close_invitees;
    LinearLayout invitelist_layout, friendlist_popup;
    Integer invitee_counter=0;

    String userID, user_list;
    String JSONcheck="empty";
    String sql_query;

    Context mContext;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventdetails);

        mContext = this;

        userID = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("user", "DEFAULT");



        //-----------------------------------assign views---------------------------------------
        TextView textView_eventname = (TextView) findViewById(R.id.event_name);
        TextView textView_address = (TextView) findViewById(R.id.event_address);
        TextView textView_venue = (TextView) findViewById(R.id.event_venue);
        TextView textView_pricerange = (TextView) findViewById(R.id.event_price);
        TextView textView_description = (TextView) findViewById(R.id.event_description);
        TextView textView_date = (TextView) findViewById(R.id.event_date);
        TextView textView_time = (TextView) findViewById(R.id.event_time);


        TextView textView_likebutton = (TextView) findViewById(R.id.button_like);
        TextView textView_buybutton = (TextView) findViewById(R.id.button_imin);


        see_invitees = (TextView) findViewById(R.id.see_invitees);
        close_invitees = (TextView) findViewById(R.id.friendlist_popup_close);

        invitelist_layout = (LinearLayout) findViewById(R.id.invitelist_layout);
        //friendlist_popup = (LinearLayout) findViewById(R.id.friendlist_popup);
        //#-----------------------------------assign views---------------------------------------

        sku = getIntent().getStringExtra("sku");
        eventname = getIntent().getStringExtra("eventname");
        buyurl = getIntent().getStringExtra("buyurl");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        address = getIntent().getStringExtra("address");
        venue = getIntent().getStringExtra("venue");
        price = getIntent().getExtras().getDouble("price");
        lat = getIntent().getExtras().getDouble("lat");
        lon = getIntent().getExtras().getDouble("lon");
        likeflag = getIntent().getExtras().getInt("likeflag");


        //-----------------------------------setText---------------------------------------
        textView_eventname.setText(eventname);
        textView_address.setText(address);
        textView_venue.setText(venue);


        DecimalFormat format_price = new DecimalFormat("0.#");
        textView_pricerange.setText("$"+String.valueOf(format_price.format(price)));
        textView_date.setText(date);
        textView_time.setText(time);

        //#-----------------------------------setText---------------------------------------

        //----------------------Autocompletetextview for userlist----------------------------------
        usersearch_field = (AutoCompleteTextView) findViewById(R.id.usersearch_field);

        GetFriendList();    //creates user_list
        String userList[] = user_list.split(",,");

        ArrayAdapter<String> userlist_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, userList);

        usersearch_field.setThreshold(2);  //2 characters before options are shown
        usersearch_field.setAdapter(userlist_adapter);
        //#----------------------Autocompletetextview for userlist----------------------------------


        //----------------------Action for when dropdown item is selected------------------------------------
        usersearch_field.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String friend_added = usersearch_field.getText().toString();
                Boolean adding_flag=true;

                //check if username has already been added

                //----------------------Add name to invitee list by creating a textview------------------------------------

                for(int i=0; i<invitee_counter; i++){
                    if (friend_added.equals(friendlist[i].getText().toString())){
                        Toast.makeText(getApplicationContext(), friend_added+" has already been added", Toast.LENGTH_SHORT).show();
                        adding_flag = false;
                    }


                }

                if(adding_flag){
                    friendlist[invitee_counter] = new TextView(mContext);
                    //text, background, padding

                    friendlist[invitee_counter].setText(usersearch_field.getText().toString());
                    friendlist[invitee_counter].setBackgroundResource(R.drawable.invitee);
                    friendlist[invitee_counter].setPadding(5, 0, 5, 5);
                    friendlist[invitee_counter].setGravity(Gravity.CENTER);
                    friendlist[invitee_counter].setTextSize(12);    //set text size in SP
                    //set margins
                    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    llp.setMargins(5, 0, 5, 5);
                    friendlist[invitee_counter].setLayoutParams(llp);

                    invitelist_layout.addView(friendlist[invitee_counter]);

                    //#----------------------Add name to invitee list by creating a textview------------------------------------

                    //----------------------Action to remove invitee name------------------------------------
                    friendlist[invitee_counter].setOnClickListener(new View.OnClickListener() {
                                                                       @Override
                                                                       public void onClick(View v) {
                                                                           v.setVisibility(View.GONE);

                                                                           //remove the name of the textview so the person can be re-added
                                                                           ((TextView) v).setText("");
                                                                           //Toast.makeText(getApplicationContext(), friendlist[0].getText().toString(), Toast.LENGTH_LONG).show();
                                                                       }
                                                                   }
                    );
                    //#----------------------Action to remove invitee name------------------------------------


                    if (invitee_counter == 0) {
                        String remove_invitee_message = "To remove an invitee select their name";
                        Toast.makeText(getApplicationContext(), remove_invitee_message, Toast.LENGTH_LONG).show();
                    }

                    invitee_counter++;  //increase counter


                }
                usersearch_field.setText("");   //make dropdown field blank
            }
        });
        //#----------------------Action for when dropdown item is selected------------------------------------


        //if event is liked set background to likebutton_active
        if(likeflag == 1){textView_likebutton.setBackgroundResource(R.drawable.likebutton_active);}
        else textView_likebutton.setBackgroundResource(R.drawable.layout_sharebutton);

        //Toast.makeText(EventDetails.this, eventVenue, Toast.LENGTH_SHORT).show();


        //-----------------------------------onclick event for see_invitees---------------------------------------
        ((TextView) findViewById(R.id.see_invitees)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  ((LinearLayout) findViewById(R.id.friendlist_popup)).setVisibility(View.VISIBLE);
                }
            }
        );
        //#-----------------------------------onclick event for see_invitees---------------------------------------

        //-----------------------------------onclick event for see_invitees---------------------------------------
        ((TextView) findViewById(R.id.friendlist_popup_close)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((LinearLayout) findViewById(R.id.friendlist_popup)).setVisibility(View.GONE);
                }
            }
        );
        //#-----------------------------------onclick event for see_invitees---------------------------------------



        //-----------------------------------onclick event for BUY button---------------------------------------
        textView_buybutton.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(buyurl));
                                                      startActivity(i);
                                                  }
                                              }
        );
        //#-----------------------------------onclick event for BUY button---------------------------------------


        //-------------------------onclick event for like button - calls updatesdata to update the DB-----------------------------------
        textView_likebutton.setOnClickListener(new View.OnClickListener(){
                                                   @Override
                                                   public void onClick(View v) {

                                                       //if event is already liked
                                                       if(likeflag == 1)
                                                       {
                                                           likeflag = 0;
                                                           v.setBackgroundResource(R.drawable.layout_sharebutton);
                                                           update_like(sku, "remove");
                                                       }
                                                       //if event is not liked
                                                       else {
                                                           likeflag = 1;
                                                           v.setBackgroundResource(R.drawable.likebutton_active);
                                                           update_like(sku, "add");
                                                       }
                                                   }
                                               }
        );
        //#-------------------------onclick event for like button - calls updatesdata to update the DB-----------------------------------



        //-------------------------generate MAP for event-----------------------------------
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapview);
        mapFragment.getMapAsync(this);

        //#-------------------------generate MAP for event-----------------------------------


    }

    /*
    Method that is called for updating database when user likes or unlikes an event
    */
    public void update_like(String sku, String action) {
        InteractDB myData = new InteractDB("http://99.235.237.216/extv/UpdateLike.php?userid="+userID+"&sku="+sku+"&action="+action);
        myData.execute();
        //new InteractDB("http://99.235.237.216/extv/UpdateLike.php?userid=" + userID + "&sku=" + sku + "&action=" + action).execute();
    }

    @Override
    public void onMapReady(GoogleMap map) {

        map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(address));

        LatLng ME = new LatLng(lat, lon);
        map.addMarker(new MarkerOptions().position(ME));


        //map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(ME));
    }


    public void GetFriendList()
    {

        sql_query = "http://99.235.237.216/extv/GetFriends.php?userid="+userID;

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


        //------------------------------------ create friendlist from JSON array ---------------------------------
        try{
            JSONObject jsonObject = new JSONObject(JSONcheck);
            JSONArray jsonMainNode = jsonObject.getJSONArray("invitelist");
            JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
            user_list = jsonChildNode.optString("friends");
        }
        catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
        //#------------------------------------ create friendlist from JSON array ---------------------------------

    }


}