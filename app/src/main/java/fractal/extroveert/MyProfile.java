package fractal.extroveert;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;



/**
 * Created by root on 02/09/15.
 */
public class MyProfile extends Activity {
    GridView grid;
    String JSONcheck = "empty";

    SharedPreferences sharedpref;

    String[] sku, eventname, buyurl, date, time, address, venue, city, state, catA, catB, pricerange;
    Integer[] likeflag;
    Double[] price, lat, lon;

    String userID;

    String sql_query, sql_query_prefix, sql_query_parameters;


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



    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView profile_pic;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile);

        //get user ID from SharedPreferences
        sharedpref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        userID = sharedpref.getString("user", "DEFAULT");

        if(!sharedpref.getString("profilepic_path", "").equals("")){
            selectSavedImagefromGallery(sharedpref.getString("profilepic_path", ""));
        }
        //Toast.makeText(getApplicationContext(), pro, Toast.LENGTH_SHORT).show();

        getEventsFromDB();  //call function get events near acquired latitude and longitude


        //------------------------------------- edit profile picture ---------------------------------
        profile_pic = (ImageView) findViewById(R.id.profile_pic);
        profile_pic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        //#------------------------------------- edit profile picture ---------------------------------



        //------------------------Click on the View Friends button to open Friends screen -------------
        (findViewById(R.id.viewfriends)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MyFriends.class));
            }
        });
        //#-----------------------Click on the View Friends button to open Friends screen -------------


        //------------------------Click on the Edit Profile button -------------
        (findViewById(R.id.editprofile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditProfile.class));
            }
        });
        //#------------------------Click on the Edit Profile button -------------



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



    //===========================================================================================================
    //--------------------------------------- GET LIKED EVENTS FROM DB-------------------------------------------
    //===========================================================================================================

    public void getEventsFromDB(){
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
    //===========================================================================================================
    //--------------------------------------- GET LIKED EVENTS FROM DB-------------------------------------------
    //===========================================================================================================




    //===========================================================================================================
    //------------------------- FUNCTIONS FOR SETTING, SAVING AND UPLOADING PROFILE PIC--------------------------
    //===========================================================================================================
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        profile_pic.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);
        sharedpref.edit().putString("profilepic_path", selectedImagePath).apply();


        Toast.makeText(getApplicationContext(), selectedImagePath, Toast.LENGTH_SHORT).show();

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        profile_pic.setImageBitmap(bm);

    }

    //===========================================================================================================
    //------------------------- FUNCTIONS FOR SETTING, SAVING AND UPLOADING PROFILE PIC--------------------------
    //===========================================================================================================
    @SuppressWarnings("deprecation")
    private void selectSavedImagefromGallery(String savedImagePath) {

        Toast.makeText(getApplicationContext(), savedImagePath, Toast.LENGTH_SHORT).show();
        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(savedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;

        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(savedImagePath, options);

        //profile_pic.setImageBitmap(bm);
    }
}