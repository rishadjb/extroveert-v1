package fractal.extroveert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by osadmin on 27/12/15.
 */
public class ForgotLogin extends Activity {

    EditText email_text;

    TextView recover_button;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.forgotlogin);

        // Username/Email and Password fields
        email_text = (EditText) findViewById(R.id.email_text);
        // Buttons
        recover_button = (TextView) findViewById(R.id.recover_button);


        recover_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email_text.getText().toString().matches("")) {
                    //TODO
                    //show error
                } else {
                    //TODO
                    //recover password
                }
            }
        });

    }


    public void clearStackAndStartActivity(){
        startActivity(new Intent(getApplicationContext(), AdvSearch.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

}
