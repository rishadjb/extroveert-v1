package fractal.extroveert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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


public class CreateInvite extends Activity{

    AutoCompleteTextView usersearch_field;
    TextView friendlist[] = new TextView[15];
    LinearLayout invitelist_layout;
    Integer invitee_counter=0;


    //Integer daylist_flag[] = new Integer[7];
    Integer[] daylist_flag = {0,0,0,0,0,0,0};
    TextView daylist[] = new TextView[7];
    Integer dayctr = 0;


    Context mContext;


    String userID, user_list;
    String JSONcheck="empty";
    String sql_query;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createinvite);

        mContext = this;

        userID= getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("user", "DEFAULT");

        //encode the username for the sql query
        try{
            userID = URLEncoder.encode(userID, "UTF-8");
        }
        catch (UnsupportedEncodingException e){e.printStackTrace();}

        invitelist_layout = (LinearLayout) findViewById(R.id.invitelist_layout);


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

                //----------------------Add name to invitee list by creating a textview------------------------------------
                friendlist[invitee_counter] = new TextView(mContext);

                //text, background, padding
                friendlist[invitee_counter].setText(usersearch_field.getText().toString());
                friendlist[invitee_counter].setBackgroundResource(R.drawable.invitee);
                friendlist[invitee_counter].setPadding(5, 0, 5, 0);

                //set margins
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                llp.setMargins(5, 0, 5, 0);
                friendlist[invitee_counter].setLayoutParams(llp);

                invitelist_layout.addView(friendlist[invitee_counter]);
                //#----------------------Add name to invitee list by creating a textview------------------------------------

                //----------------------Action to remove invitee name------------------------------------
                friendlist[invitee_counter].setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                           v.setVisibility(View.GONE);
                               //Toast.makeText(getApplicationContext(), invitee_counter.toString(), Toast.LENGTH_LONG).show();
                           //friendlist[invitee_counter].setText("");
                       }
                   }
                );
                //#----------------------Action to remove invitee name------------------------------------


                if (invitee_counter == 0) {
                    String remove_invitee_message = "Select the name of the invitee to remove";
                    Toast.makeText(getApplicationContext(), remove_invitee_message, Toast.LENGTH_LONG).show();
                }

                invitee_counter++;  //increase counter

                usersearch_field.setText("");   //make dropdown field blank

            }
        });
        //#----------------------Action for when dropdown item is selected------------------------------------


        //------------------------------------- onClick action day buttons ---------------------------------------


        daylist[0] = (TextView) findViewById(R.id.button_su);
        daylist[1] = (TextView) findViewById(R.id.button_mo);
        daylist[2] = (TextView) findViewById(R.id.button_tu);
        daylist[3] = (TextView) findViewById(R.id.button_we);
        daylist[4] = (TextView) findViewById(R.id.button_th);
        daylist[5] = (TextView) findViewById(R.id.button_fr);
        daylist[6] = (TextView) findViewById(R.id.button_sa);

        for (dayctr = 0; dayctr <= 6; dayctr++) {
            daylist[dayctr].setOnClickListener(new View.OnClickListener() {

                //using dayctr directly without j doesn't work because dayctr is always 7
                Integer j = dayctr;

                @Override
                public void onClick(View v) {

                    if (v.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.day_button_unselected).getConstantState())) {
                        v.setBackgroundResource(R.drawable.day_button_selected);
                        daylist_flag[j]=1;
                    }
                    else if (v.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.day_button_selected).getConstantState())) {
                        v.setBackgroundResource(R.drawable.day_button_unselected);
                        daylist_flag[j]=0;
                    }
                }
            });
        }
        //#------------------------------------- onClick action day buttons ---------------------------------------


        //------------------------------------- onClick action for advanced search button ---------------------------------
        TextView send_button;
        send_button = (TextView) findViewById(R.id.button_sendinvite);

        // view products click event
        send_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CreateInvite();
            }
        });
        //#------------------------------------- onClick action for advanced search button ---------------------------------


        //===========================================================================================================
        //------------------------------------------ BOTTOM NAV BAR SETUP--------------------------------------------
        //===========================================================================================================
        String className = this.getClass().getSimpleName();
        (findViewById(R.id.button_search)).setOnClickListener(new MyClickListener(this, className, AdvSearch.class));
        (findViewById(R.id.button_profile)).setOnClickListener(new MyClickListener(this, className, MyProfile.class));
        //===========================================================================================================
        //------------------------------------------ BOTTOM NAV BAR SETUP--------------------------------------------
        //===========================================================================================================






    }

    public void GetFriendList()
    {

        sql_query = "http://99.235.237.216/extv/FindFriends.php?userID="+userID;

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
            JSONArray jsonMainNode = jsonObject.getJSONArray("friendlist");
            JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
            user_list = jsonChildNode.optString("friends");
        }
        catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
        //#------------------------------------ create friendlist from JSON array ---------------------------------

    }

    /*
    Method that is called for creating invite
    */
    public void CreateInvite() {

        String friendo="";
        String days="";

        for (Integer i = 0; i<invitee_counter; i++)
            if (friendlist[i].getVisibility() == View.VISIBLE) {
                friendo = friendo + friendlist[i].getText().toString() + ",";
            }

        for (Integer i = 0; i<7; i++){
            if (daylist_flag[i] == 1)
            {
                days += daylist[i].getText().toString() + ",";
            }
        }
        Toast.makeText(getApplicationContext(), days, Toast.LENGTH_LONG).show();




        //InteractDB myData = new InteractDB("http://99.235.237.216/extv/CreateInvite.php?userid="+userID);
        //myData.execute();
    }

}
