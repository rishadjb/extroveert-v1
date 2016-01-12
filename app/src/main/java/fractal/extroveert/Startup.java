package fractal.extroveert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by osadmin on 27/12/15.
 */
public class Startup extends Activity {

    Context context;

    SharedPreferences sharedpref;


    String JSONcheck = "empty";
    String signup_check = "";

    private String fbUserID;
    private String fbProfileName;
    private String fbAuthToken;
    private static final String TAG = "FacebookLogin";
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    //private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    EditText email_text, password_text;

    TextView emaillogin_button;
    TextView googlelogin_button;
    TextView fblogin_button;

    TextView passcheck_text, forgotlogin_text;
    String passcheck;


    private final List<String> permissions;
    public Startup() {
        permissions = Arrays.asList("email", "public_profile");

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        context = this;
        sharedpref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        //--------------------- GO TO HOME SCREEN if userlogin is saved-------------------
        if(!sharedpref.getString("user", "").equals("")) goToHomeScreen();
        //--------------------- GO TO HOME SCREEN if userlogin is saved-------------------


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


        // Username/Email and Password fields
        email_text = (EditText) findViewById(R.id.email_text);
        password_text = (EditText) findViewById(R.id.password_text);

        // Buttons
        emaillogin_button = (TextView) findViewById(R.id.emaillogin_button);
        googlelogin_button = (TextView) findViewById(R.id.googlelogin_button);
        fblogin_button = (TextView) findViewById(R.id.fblogin_button);

        forgotlogin_text = (TextView) findViewById(R.id.forgotlogin_text);
        passcheck_text = (TextView) findViewById(R.id.passcheck_text);

        emaillogin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email_text.getText().toString().matches("") || password_text.getText().toString().matches("")) {
                    startActivity(new Intent(getApplicationContext(), Signup.class));
                    //Toast.makeText(getApplicationContext(), "re", Toast.LENGTH_SHORT).show();

                }
                else {

                    JSONcheck="empty";

                    String username = email_text.getText().toString();
                    String password = password_text.getText().toString();

                    passcheck = check_login(username, password);

                    //invalid password
                    if (check_login(username, password).equals("0")){

                        //oast.makeText(Startup.this, passcheck, Toast.LENGTH_SHORT).show();
                        passcheck_text.setText("Invalid username or password! Please try again");
                        password_text.setText("");
                    }
                    //successful password - passcheck.equals("1")
                    else{
                        //getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit().putString("user", "rishad").apply();
                        getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit().putString("user", username).apply();

                        //String className = this.getClass().getSimpleName();
                        //new MyClickListener(getBaseContext(), getBaseContext().getClass().getSimpleName(), EventGrid.class);

                        //clearStackAndStartActivity();

                        goToHomeScreen();

                    }


                }
            }
        });

        forgotlogin_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgotLogin.class));
            }
        });


        fblogin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
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

    public String check_login(String username, String password) {
        String passcheck = "";
        InteractDB myData = new InteractDB("http://www.extroveert.com/extv/testconn_user.php?username="+username+"&password="+password);
        //InteractDB myData = new InteractDB("http://www.extroveert.com/testconn_user.php?username=rishadjb&password=Ghostcat0()");
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
            JSONArray jsonMainNode = jsonObject.getJSONArray("passcheck");
            JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
            passcheck = jsonChildNode.optString("check");
        }
        catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
        return passcheck;
    }

    //-------------------------------- GO TO HOME SCREEN ---------------------------------------
    public void goToHomeScreen(){
        Intent i = new Intent(context, EventGrid.class);

        i.putExtra("cityName", sharedpref.getString("default_location", ""));
        i.putExtra("searchRadius", sharedpref.getString("default_searchRadius", ""));
        i.putExtra("startDate", "");
        i.putExtra("endDate", "");
        i.putExtra("startTime", sharedpref.getString("default_startTime", ""));
        i.putExtra("endTime", sharedpref.getString("default_endTime", ""));
        i.putExtra("startPrice", sharedpref.getString("default_startPrice", ""));
        i.putExtra("endPrice", sharedpref.getString("default_endPrice", ""));
        i.putExtra("eventType", sharedpref.getString("default_eventType", ""));

        context.startActivity(i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
    //#-------------------------------- GO TO HOME SCREEN ---------------------------------------

}
