package fractal.extroveert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
public class UserProfile extends Activity{

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
        setContentView(R.layout.userprofile);


        USERA = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("user", "DEFAULT");


        try{
            USERA_enc = URLEncoder.encode(USERA,"UTF-8");
        }
        catch (UnsupportedEncodingException e){e.printStackTrace();}




        //----------------------Autocompletetextview for userlist----------------------------------
        usersearch_field = (AutoCompleteTextView) findViewById(R.id.usersearch_field);

        user_list = getUserList();
        String userList[] = user_list.split(",,");

        ArrayAdapter<String> userlist_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, userList);

        usersearch_field.setThreshold(2);  //2 characters before options are shown
        usersearch_field.setAdapter(userlist_adapter);

        //----------------------Action for when dropdown item is selected------------------------------------
        usersearch_field.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                USERB = usersearch_field.getText().toString();

                //encode the username for the sql query
                try{
                    USERB_enc = URLEncoder.encode(USERB,"UTF-8");
                }
                catch (UnsupportedEncodingException e){e.printStackTrace();}


                GetUserProfile();
                Toast.makeText(getApplicationContext(), usersearch_field.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });
        //#----------------------Action for when dropdown item is selected------------------------------------
        //#----------------------Autocompletetextview for userlist----------------------------------

        userprofile_layout = (LinearLayout) findViewById(R.id.userprofile_layout);
        userprofile_layout.setVisibility(View.GONE);


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



    public void GetUserProfile()
    {

        userprofile_layout.setVisibility(View.VISIBLE);

        check_friend_status();

        username = (TextView) findViewById(R.id.username);
        username.setText(USERB);

        addremove_button = (TextView) findViewById(R.id.addremove_button);
        block_button = (TextView) findViewById(R.id.block_button);

        if (current_status.equals(""))
        {
            addremove_button.setText("ADD");
            block_button.setText("BLOCK");
        }
        else if (current_status.equals("friend"))
        {
            addremove_button.setText("REMOVE");
            block_button.setText("BLOCK");
        }
        else if (current_status.equals("unfriend"))
        {
            addremove_button.setText("ADD");
            block_button.setText("BLOCK");
        }
        else if (current_status.equals("blocked"))
        {
            addremove_button.setText("ADD");

            if (USERB.equals(USERA_db)){
                block_button.setText("BLOCK");
            }
            else block_button.setText("UNBLOCK");

        }
        else if (current_status.equals("unblocked"))
        {
            addremove_button.setText("ADD");
            block_button.setText("BLOCK");
        }



        //USERB = username.getText().toString();
        ADD_ACTION = addremove_button.getText().toString();
        BLOCK_ACTION = block_button.getText().toString();

        //-------------------------onclick event for add button-----------------------------------
        addremove_button.setOnClickListener(new View.OnClickListener(){
                                                @Override
                                                public void onClick(View v) {

                                                    if (USERB.equals(USERA_db) && current_status.equals("blocked")){
                                                        Toast.makeText(getApplicationContext(), "You have been blocked by "+USERB, Toast.LENGTH_LONG).show();
                                                    }
                                                    else{
                                                        //if you want to add user
                                                        if(ADD_ACTION.equals("ADD"))
                                                        {
                                                            addremove_button.setText("REMOVE");
                                                            ADD_ACTION = "REMOVE";
                                                            block_button.setText("BLOCK");
                                                            BLOCK_ACTION = "BLOCK";
                                                            update_friend("add");
                                                        }
                                                        //if event is not liked
                                                        else if(ADD_ACTION.equals("REMOVE"))
                                                        {
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
        block_button.setOnClickListener(new View.OnClickListener(){
                                            @Override
                                            public void onClick(View v) {

                                                if (USERB.equals(USERA_db) && current_status.equals("blocked")){
                                                    Toast.makeText(getApplicationContext(), "You have been blocked by "+USERB, Toast.LENGTH_LONG).show();
                                                }
                                                else{
                                                    //if you want to block user
                                                    if(BLOCK_ACTION.equals("BLOCK"))
                                                    {
                                                        block_button.setText("UNBLOCK");
                                                        BLOCK_ACTION = "UNBLOCK";
                                                        addremove_button.setText("ADD");
                                                        ADD_ACTION = "ADD";
                                                        update_friend("block");
                                                    }
                                                    //if you want to unblock user
                                                    else if(BLOCK_ACTION.equals("UNBLOCK"))
                                                    {
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

        InteractDB myData = new InteractDB("http://99.235.237.216/extv/AddRemoveFriend.php?USERA="+ USERA_enc+"&USERB="+USERB_enc+"&ACTION="+ACTION);
        myData.execute();
    }


    public void check_friend_status() {

        JSONcheck="empty";


        InteractDB myData = new InteractDB("http://99.235.237.216/extv/AddRemoveFriend.php?USERA="+USERA_enc+"&USERB="+USERB_enc+"&ACTION=");

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
        try{
            JSONObject jsonObject = new JSONObject(JSONcheck);
            JSONArray jsonMainNode = jsonObject.getJSONArray("friendstatus");
            JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
            USERA_db = jsonChildNode.optString("USERAdb");
            USERB_db = jsonChildNode.optString("USERBdb");
            current_status = jsonChildNode.optString("status");
        }
        catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }



    public String getUserList() {
        InteractDB myData = new InteractDB("http://www.extroveert.com/extv/GetUserList.php");

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
        try{
            JSONObject jsonObject = new JSONObject(JSONcheck);
            JSONArray jsonMainNode = jsonObject.getJSONArray("userlist");
            JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
            user_list = jsonChildNode.optString("users");
        }
        catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        //myData = null;



        return user_list;
    }


}
