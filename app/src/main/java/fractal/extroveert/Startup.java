package fractal.extroveert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
public class Startup extends Activity {

    private String fbUserID;
    private String fbProfileName;
    private String fbAuthToken;
    private static final String TAG = "FacebookLogin";
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    //private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    TextView emaillogin_button;

    private final List<String> permissions;
    public Startup() {
        permissions = Arrays.asList("email","public_profile");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());


        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.startup);

        //info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(permissions);


        /*
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                fbAuthToken = currentAccessToken.getToken();
                fbUserID = currentAccessToken.getUserId();


                Log.d(TAG, "User id: " + fbUserID);
                Log.d(TAG, "Access token is: " + fbAuthToken);


            }
        };


        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                fbProfileName = currentProfile.getName();

                Log.d(TAG, "User name: " + fbProfileName );
            }
        };
        */



        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                    String email = me.optString("name");
                                    String id = me.optString("id");
                                    // send email and id to your web server

                                    //get the unique FB identifier
                                    //use the identifier to get the username
                                    //if identifier is not found, register user as a new user


                                    //------------------------------------- call InteractDB get JSON object ---------------------------------

                                }
                            }
                        }).executeAsync();
                /*
                info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );
                */

                //InteractDB myData = new InteractDB("http://99.235.237.216/extv/AccessToken.php?id="+loginResult.getAccessToken().getUserId()+"&token="+loginResult.getAccessToken().getToken());

                clearStackAndStartActivity();
            }

            @Override
            public void onCancel() {

                //info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                //info.setText("Login attempt failed.");
            }


        });


        // Buttons
        emaillogin_button = (TextView) findViewById(R.id.emaillogin_button);


        // view products click event
        emaillogin_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                clearStackAndStartActivity();
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void clearStackAndStartActivity(){
        startActivity(new Intent(getApplicationContext(), AdvSearch.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

}
