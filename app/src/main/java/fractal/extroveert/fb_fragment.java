package fractal.extroveert;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;


public class fb_fragment extends Fragment {

    private CallbackManager callbackManager;

    public static fb_fragment newInstance(){

        fb_fragment f = new fb_fragment();
        return f;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fb_fragment, container, false);
        // Looks for Login button
        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setFragment(this);

        authButton.setReadPermissions(Arrays.asList("public_profile"));


        authButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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


                                    //------------------------------------- call InteractDB get JSON object ---------------------------------

                                }
                            }
                        }).executeAsync();


                InteractDB myData = new InteractDB("http://99.235.237.216/extv/AccessToken.php?id=" + loginResult.getAccessToken().getUserId() + "&token=" + loginResult.getAccessToken().getToken());
                myData.execute();

                //startActivity(new Intent(getApplicationContext(), LoginScreen.class));
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
            }


        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

/*

    @Override
    public void onResume() {
        super.onResume();
        callback.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }



*/











}