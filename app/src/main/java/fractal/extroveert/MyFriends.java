package fractal.extroveert;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by osadmin on 22/12/15.
 */

public class MyFriends extends FragmentActivity {
    GridView grid;
    String JSONcheck = "empty";

    private Context nContext;
    String userID;

    String[] name, imagelink;
    //------------------------------------- declare arraylists ---------------------------------
    public ArrayList<String> name_arraylist = new ArrayList<String>();
    public ArrayList<String> imagelink_arraylist = new ArrayList<String>();
    //#------------------------------------- declare arraylists ---------------------------------

    String sql_query, sql_query_prefix, sql_query_parameters;

    public void getEventsFromDB(){
        //------------------------------------- create SQL query ---------------------------------
        sql_query_prefix = "http://99.235.237.216/extv/GetFriends.php?";

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

            JSONArray jsonMainNode = jsonObject.getJSONArray("friendlist");

            for (int i = 0; i < jsonMainNode.length(); i++)
            {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                //------------------------------------- assign JSON node values to variables ---------------------------------
                String name = jsonChildNode.optString("friendname");
                String image_link = jsonChildNode.optString("imagelink");
                //#------------------------------------- assign JSON node values to variables ---------------------------------

                //------------------------------------- add variable value to arraylist ---------------------------------
                name_arraylist.add(name);
                imagelink_arraylist.add(image_link);
                //#------------------------------------- add variable value to arraylist ---------------------------------

            }


        }
        catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
        //#------------------------------------- get event details from database ---------------------------------

        //------------------------------------- convert arraylists to array ---------------------------------
        name = name_arraylist.toArray(new String[name_arraylist.size()]);
        imagelink = imagelink_arraylist.toArray(new String[imagelink_arraylist.size()]);
        //#------------------------------------- convert arraylists to array ---------------------------------


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        nContext = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.myfriends);

        userID = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("user", "DEFAULT");

        getEventsFromDB();  //call function get events near acquired latitude and longitude

        //------------------------------------- create grid ---------------------------------
        CustomGrid_myfriends adapter = new CustomGrid_myfriends(MyFriends.this, savedInstanceState, userID, name, imagelink);
        grid=(GridView)findViewById(R.id.grid_friends);
        grid.setAdapter(adapter);
        //#------------------------------------- create grid ---------------------------------


        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //((TextView) view).setText("hey");
                Toast.makeText(MyFriends.this, name[position], Toast.LENGTH_SHORT).show();
            }

        });


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



