package fractal.extroveert;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

public class CustomGrid_myfriends extends BaseAdapter {
    private Context mContext;
    Bundle savedInstanceState;

    private final String[] name, imagelink;

    private String userID;


    //private final int[] Imageid;

    //public CustomGrid(Context c,String[] web,int[] Imageid ) {
    public CustomGrid_myfriends(Context c, final Bundle b, String userID,
                                  String[] name, String[] imagelink) {
        mContext = c;
        //this.Imageid = Imageid;
        this.name = name;
        this.imagelink = imagelink;
        this.savedInstanceState = b;

        this.userID = userID;
    }

    @Override
    //this returns the number of grid items
    public int getCount() {
        // TODO Auto-generated method stub
        return name.length;
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
            grid = inflater.inflate(R.layout.grid_friend, parent, false);

        } else {
            grid = (View) convertView;
        }


        //-----------------------------------assign views---------------------------------------
        ImageView imageview_image = (ImageView) grid.findViewById(R.id.friend_imagelink);
        String url = "http://www.mcraeandcompany.co.uk/Blog/wp-content/uploads/2014/12/1418151389_camera-alt-128.png";
        new ImageLoadTask(url, imageview_image).execute();
        //TextView imageview_image = (TextView) grid.findViewById(R.id.friend_imagelink);
        TextView textView_name = (TextView) grid.findViewById(R.id.friend_name);
        //#-----------------------------------assign views---------------------------------------

        //-----------------------------------setText---------------------------------------
        //imageview_image.setText(imagelink[position]);
        textView_name.setText(name[position]);
        //#-----------------------------------setText---------------------------------------

        //#-------------------------onclick event for like button - calls updatesdata to update the DB-----------------------------------

        return grid;
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }




}

