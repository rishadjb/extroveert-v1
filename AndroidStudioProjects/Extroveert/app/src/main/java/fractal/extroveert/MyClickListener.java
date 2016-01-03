package fractal.extroveert;

/*
Class for custom onclicklistener for bottom nav bar button
 */



import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

public class MyClickListener implements View.OnClickListener {

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
            LinearLayout button = (LinearLayout) view;
            Intent i = new Intent(context, target_class);
            context.startActivity(i);
        }
    }
}