package fractal.extroveert;

import android.app.Activity;
import android.content.Context;

//THIS CLASS IS NOT BEING USED

public class GetUserID extends Activity{

    private String userID;

    GetUserID(){

        //get user ID from SharedPreferences
        userID = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("user", "DEFAULT");
    }

    String returnUserID(){
        return userID;}
}
