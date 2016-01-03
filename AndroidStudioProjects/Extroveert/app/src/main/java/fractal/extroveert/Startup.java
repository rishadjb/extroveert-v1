package fractal.extroveert;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by osadmin on 21/09/15.
 */
public class Startup extends Activity{

    TextView fblogin_button;
    TextView emaillogin_button;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);


        getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit().putString("user", "rishad").apply();


        // Buttons
        fblogin_button = (TextView) findViewById(R.id.fblogin_button);
        emaillogin_button = (TextView) findViewById(R.id.emaillogin_button);

        // view products click event
        fblogin_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity

                Intent i = new Intent(getApplicationContext(), CreateInvite.class);
                i.putExtra("cityName","Kitchener");
                startActivity(i);

            }
        });


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
