package fractal.extroveert;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomGrid extends BaseAdapter {

    private final Activity act;
    private Context mContext;
    Bundle savedInstanceState;

    private final String[] sku, eventname, buyurl, date, time, address, venue, city, state, catA, catB, pricerange;
    private final Integer[] likeflag;
    private final Double[] price, lat, lon;

    private String userID;

    LinearLayout event_invite_prompt;

    public CustomGrid(Activity act, Context c, final Bundle b, String userID,
                      String[] sku, String[] eventname, String[] buyurl,
                      String[] date, String[] time, String[] address,  String[] venue, String[] city, String[] state,
                      String[] catA, String[] catB, String[] pricerange, Double[] price, Double[] lat, Double[] lon,
                      Integer[] likeflag) {
        mContext = c;
        this.act = act;

        this.sku = sku;
        this.eventname = eventname;
        this.buyurl = buyurl;
        this.address = address;
        this.venue = venue;
        this.city = city;
        this.state = state;
        this.catA = catA;
        this.catB = catB;
        this.pricerange = pricerange;
        this.price = price;
        this.date = date;
        this.time = time;
        this.lat = lat;
        this.lon = lon;
        this.savedInstanceState = b;

        this.likeflag = likeflag;

        this.userID = userID;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return eventname.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;



        final int posCoord = position;
        final LatLng HAMBURG = new LatLng(lat[position], lon[position]);

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, parent, false);

        } else {
            grid = (View) convertView;
        }



        //-----------------------------------assign views---------------------------------------
        TextView textView_eventname = (TextView) grid.findViewById(R.id.event_name);
        TextView textView_address = (TextView) grid.findViewById(R.id.event_address);
        TextView textView_venue = (TextView) grid.findViewById(R.id.event_venue);
        TextView textView_pricerange = (TextView) grid.findViewById(R.id.event_price);
        TextView textView_description = (TextView) grid.findViewById(R.id.event_description);
        TextView textView_date = (TextView) grid.findViewById(R.id.event_date);
        TextView textView_time = (TextView) grid.findViewById(R.id.event_time);


        TextView textView_likebutton = (TextView) grid.findViewById(R.id.button_like);
        TextView textView_buybutton = (TextView) grid.findViewById(R.id.button_imin);
        TextView textView_invitebutton = (TextView) grid.findViewById(R.id.button_invite);
        //#-----------------------------------assign views---------------------------------------


        //event_invite_prompt = (TextView) grid.findViewById(R.id.event_invite_prompt);
        event_invite_prompt = (LinearLayout) act.findViewById(R.id.event_invite_prompt);


        //-----------------------------------setText---------------------------------------
        textView_eventname.setText(eventname[position]);
        textView_address.setText(address[position]);
        textView_venue.setText(venue[position]);


        DecimalFormat format_price = new DecimalFormat("0.#");
        textView_pricerange.setText("$"+String.valueOf(format_price.format(price[position])));
        //textView_description.setText(pricerange[position]);

        /*
        try {
            DateFormat format_date = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
            Date date_date = format_date.parse(date[position]);
            finaldate = convertStringToDate(date_date);
        } catch (ParseException e) {}
        */

        textView_date.setText(date[position]);
        textView_time.setText(time[position]);
        //#-----------------------------------setText---------------------------------------


        //if event is liked set background to likebutton_active
        if(likeflag[posCoord] == 1){textView_likebutton.setBackgroundResource(R.drawable.likebutton_active);}
        else textView_likebutton.setBackgroundResource(R.drawable.layout_sharebutton);

        //-----------------------------------onclick event for event name---------------------------------------
        textView_eventname.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      Intent i = new Intent(mContext, EventDetails.class);

                                                      i.putExtra("sku", sku[posCoord]);
                                                      i.putExtra("eventname", eventname[posCoord]);
                                                      i.putExtra("buyurl", buyurl[posCoord]);
                                                      i.putExtra("date", date[posCoord]);
                                                      i.putExtra("time", time[posCoord]);
                                                      i.putExtra("address", address[posCoord]);
                                                      i.putExtra("venue", venue[posCoord]);
                                                      i.putExtra("price", (double)price[posCoord]);
                                                      i.putExtra("lat", (double)lat[posCoord]);
                                                      i.putExtra("lon", (double)lon[posCoord]);
                                                      i.putExtra("likeflag", (int)likeflag[posCoord]);

                                                      mContext.startActivity(i);
                                                  }
                                              }
        );
        //#-----------------------------------onclick event for event name---------------------------------------



        //-----------------------------------onclick event for BUY button---------------------------------------
        textView_buybutton.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(buyurl[posCoord]));
                                                      mContext.startActivity(i);
                                                  }
                                              }
        );
        //#-----------------------------------onclick event for BUY button---------------------------------------

        //-----------------------------------onclick event for BUY button---------------------------------------
        textView_invitebutton.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      //Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(buyurl[posCoord]));
                                                      //mContext.startActivity(i);
                                                      //maplayout.setVisibility(View.INVISIBLE);
                                                      event_invite_prompt.setVisibility(View.VISIBLE);
                                                  }
                                              }
        );
        //#-----------------------------------onclick event for BUY button---------------------------------------


        //-------------------------onclick event for like button - calls updatesdata to update the DB-----------------------------------
        textView_likebutton.setOnClickListener(new View.OnClickListener(){
                                                   @Override
                                                   public void onClick(View v) {

                                                       //if event is already liked
                                                       if(likeflag[posCoord] == 1)
                                                       {
                                                           likeflag[posCoord] = 0;
                                                           v.setBackgroundResource(R.drawable.layout_sharebutton);
                                                           update_like(sku[posCoord], "remove");
                                                       }
                                                       //if event is not liked
                                                       else {
                                                           likeflag[posCoord] = 1;
                                                           v.setBackgroundResource(R.drawable.likebutton_active);
                                                           update_like(sku[posCoord], "add");
                                                       }
                                                   }
                                               }
        );
        //#-------------------------onclick event for like button - calls updatesdata to update the DB-----------------------------------



        //-------------------------generate MAP for event-----------------------------------
        MapView mapView = (MapView) grid.findViewById(R.id.event_map);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();
        mapView.getMapAsync(
                new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googlemap) {
                        final GoogleMap map = googlemap;
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                        MapsInitializer.initialize(mContext);

                        map.setMyLocationEnabled(true);
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                        LatLng sydney = new LatLng(lat[posCoord], lon[posCoord]);
                        String markerTitle = "Sydney";

                        if (markerTitle != null) {
                            map.addMarker(new MarkerOptions()
                                    //.title(markerTitle)
                                    .position(HAMBURG));
                        }

                        map.setMyLocationEnabled(true);
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));

                    }
                }
        );
        //#-------------------------generate MAP for event-----------------------------------

        return grid;
    }


    public void onClick(View v){}

    public String convertStringToDate(Date indate)
    {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("EEE, MMM d");
   /*you can also use DateFormat reference instead of SimpleDateFormat
    * like this: DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
    */
        try{
            dateString = sdfr.format( indate );
        }catch (Exception ex ){
            System.out.println(ex);
        }
        return dateString;
    }


    /*
    Method that is called for updating database when user likes or unlikes an event
    */
    public void update_like(String sku, String action) {

        InteractDB myData = new InteractDB("http://99.235.237.216/extv/UpdateLike.php?userid="+userID+"&sku="+sku+"&action="+action);
        myData.execute();
    }





}

