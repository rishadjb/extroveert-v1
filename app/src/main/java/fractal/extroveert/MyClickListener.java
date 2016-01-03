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

public class MyClickListener extends Activity implements View.OnClickListener {

    private Context context;
    private String this_classname;
    private Class target_class;

    public MyClickListener(Context context, String thisClassName, Class targetClass){
        this.context = context;
        this_classname = thisClassName;
        target_class = targetClass;
    }


    @Override
    public void onClick(View view) {
        //check if the target activity is same as the current activity


        if (!this_classname.equals(target_class.getSimpleName())) {

            Intent i = new Intent(context, target_class);

            if ((target_class.getSimpleName()).equals("Startup")) {

                //clear the saved user
                context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit().putString("user", "").apply();

                //clear the activity task so that pressing the back button exits the app
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            }

            context.startActivity(i);
        }
    }
}