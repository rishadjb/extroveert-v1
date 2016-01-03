package fractal.extroveert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

/**
 * Created by osadmin on 02/10/15.
 */
public class ViewFriends extends Activity{

    GridView grid;


    String userID;
    String sql_query;
    String JSONcheck="empty";

    String friend_list;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewfriends);

        userID= getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("user", "DEFAULT");

        //encode the username for the sql query
        try{
            userID = URLEncoder.encode(userID, "UTF-8");
        }
        catch (UnsupportedEncodingException e){e.printStackTrace();}

        //------------------------------------- create SQL query ---------------------------------
        sql_query = "http://99.235.237.216/extv/FindFriends.php?userID="+userID;
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
            JSONArray jsonMainNode = jsonObject.getJSONArray("friendlist");
            JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
            friend_list = jsonChildNode.optString("friends");
        }
        catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
        //#------------------------------------- get address name from places api textsearch ---------------------------------



        //------------------------------------Search Bar onClick actions--------------------------------------------------
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

        //------------------------------------- onClick action for profile button ------------------------------------------
        ImageView myprofile_button;
        myprofile_button = (ImageView) findViewById(R.id.navbar_top_profilepic);

        // view products click event
        myprofile_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), MyProfile.class);
                startActivity(i);
            }
        });
        //#------------------------------------- onClick action for profile button ---------------------------------------
        //#------------------------------------Search Bar onClick actions--------------------------------------------------


    }


}
