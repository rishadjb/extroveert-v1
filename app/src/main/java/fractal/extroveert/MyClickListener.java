package fractal.extroveert;

/*
Class for custom onclicklistener for bottom nav bar button
 */



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.login.LoginManager;

public class MyClickListener extends Activity implements View.OnClickListener {

    private Context context;
    private String this_classname;
    private Class target_class;
    private Boolean logout=false;   //if exit button is clicked

    public MyClickListener(Context context, String thisClassName, Class targetClass){
        this.context = context;
        this_classname = thisClassName;
        target_class = targetClass;
    }

    public MyClickListener(Context context, String thisClassName, Class targetClass, Boolean logout){
        this.context = context;
        this_classname = thisClassName;
        target_class = targetClass;
        this.logout=logout;
    }


    @Override
    public void onClick(View view) {
        //check if the target activity is same as the current activity

        //log out of facebook
        if(logout) {
            LoginManager.getInstance().logOut();    //its ok even if user isn't logged in through FB

            //logout user if logged in through email/username
            //context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit().putString("user", "").apply();
        }


        if (!this_classname.equals(target_class.getSimpleName())) {

            Intent i = new Intent(context, target_class);

            if ((target_class.getSimpleName()).equals("Startup")) {

                //clear the saved user
                context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit().putString("user", "").apply();

                //clear the activity task so that pressing the back button exits the app
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            }


            if ((target_class.getSimpleName()).equals("EventGrid")) {
                //i.putExtra("cityName", getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("cityName", ""));
                i.putExtra("cityName", "New York, NY");
                i.putExtra("searchRadius", "25");

                i.putExtra("startDate", "");
                i.putExtra("endDate", "");

                i.putExtra("startTime", "");
                i.putExtra("endTime", "");

                i.putExtra("startPrice", "");
                i.putExtra("endPrice", "");

                i.putExtra("eventType", "");
            }



            context.startActivity(i);
        }
    }
}