package fractal.extroveert;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import java.text.DecimalFormat;

public class CustomGrid_likedevents extends BaseAdapter {
    private Context mContext;
    Bundle savedInstanceState;

    private final String[] sku, eventname, buyurl, date, time, address, venue, city, state, catA, catB, pricerange;
    private final Integer[] likeflag;
    private final Double[] price, lat, lon;

    private String userID;

    ImageView imageview_likebutton;

    RelativeLayout event_layout;



    //private final int[] Imageid;

    //public CustomGrid(Context c,String[] web,int[] Imageid ) {
    public CustomGrid_likedevents(Context c, final Bundle b, String userID,
                      String[] sku, String[] eventname, String[] buyurl,
                      String[] date, String[] time, String[] address,  String[] venue, String[] city, String[] state,
                      String[] catA, String[] catB, String[] pricerange, Double[] price, Double[] lat, Double[] lon,
                      Integer[] likeflag) {
        mContext = c;
        //this.Imageid = Imageid;
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


        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single_likedevents, parent, false);

        } else {
            grid = (View) convertView;
        }

        event_layout = (RelativeLayout) grid.findViewById(R.id.event_layout);

        //-----------------------------------assign views---------------------------------------
        TextView textView_eventname = (TextView) grid.findViewById(R.id.event_name);
        TextView textView_venue = (TextView) grid.findViewById(R.id.event_venue);
        TextView textView_date = (TextView) grid.findViewById(R.id.event_datetime);
        ImageView imageview_likebutton = (ImageView) grid.findViewById(R.id.event_heart);
        //#-----------------------------------assign views---------------------------------------

        //if event is liked set background to likebutton_active
        if(likeflag[posCoord] == 1) imageview_likebutton.setBackgroundResource(R.mipmap.like); //imageview_likebutton.setColorFilter(Color.argb(255, 255, 0, 0), PorterDuff.Mode.SRC_ATOP); //imageview_likebutton.setBackgroundColor(0xFFFFFFFF);
        //without else the images get screwed up when scrolling
        else imageview_likebutton.setBackgroundResource(R.mipmap.unlike); //imageview_likebutton.setBackgroundColor(0xFF3e3e3e);


        //-----------------------------------setText---------------------------------------
        textView_eventname.setText(eventname[position]);
        textView_venue.setText(venue[position] + ", "+city[position]);

        textView_date.setText(time[position]+" "+date[position]);
        //#-----------------------------------setText---------------------------------------

        //-------------------------onclick event for like button - calls updatesdata to update the DB-----------------------------------
        imageview_likebutton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        //if event is already liked
                                                        if (likeflag[posCoord] == 1) {
                                                            likeflag[posCoord] = 0;
                                                            v.setBackgroundResource(R.mipmap.unlike);
                                                            update_like(sku[posCoord], "remove");
                                                        }
                                                        //if event is not liked
                                                        else {
                                                            likeflag[posCoord] = 1;
                                                            v.setBackgroundResource(R.mipmap.like);
                                                            update_like(sku[posCoord], "add");
                                                        }
                                                    }
                                                }
        );
        //#-------------------------onclick event for like button - calls updatesdata to update the DB-----------------------------------

        //-------------------------onclick event event layout-----------------------------------
        event_layout.setOnClickListener(new View.OnClickListener() {
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
        //#-------------------------onclick event event layout-----------------------------------

        return grid;
    }

    /*
    Method that is called for updating database when user likes or unlikes an event
    */
    public void update_like(String sku, String action) {

        InteractDB myData = new InteractDB("http://99.235.237.216/extv/UpdateLike.php?userid="+userID+"&sku="+sku+"&action="+action);
        myData.execute();
    }



}

