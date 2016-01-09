package fractal.extroveert;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;

/**
 * Created by osadmin on 21/09/15.
 */
public class Startup_old extends FragmentActivity{

    TextView fblogin_button;
    TextView emaillogin_button;



    private fb_fragment mainFragment;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            mainFragment = new fb_fragment();
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, mainFragment).commit();
        } else {
            // Or set the fragment from restored state info
            mainFragment = (fb_fragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }


        //getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit().putString("user", "rishad").apply();

        //get the userid
        //if user has logged in before, it won't be blank
        String s = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("user","");

        //if userid id not blank take to home screen
        if(!s.equals("")){
            //startActivity(new Intent(getApplicationContext(), MyProfile.class));
            this.finishActivity(0);
        }


        // Buttons
        emaillogin_button = (TextView) findViewById(R.id.emaillogin_button);


        // view products click event
        emaillogin_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), AdvSearch.class);
                startActivity(i);

            }
        });



    }
}
