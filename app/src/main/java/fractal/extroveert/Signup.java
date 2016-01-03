package fractal.extroveert;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;
/**
 * Created by root on 02/09/15.
 */
public class Signup extends Activity {

    String JSONcheck = "empty";
    String signup_check = "";
    EditText displayname, email, username, password1, password2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        displayname = (EditText) findViewById(R.id.displayname_text);
        email = (EditText) findViewById(R.id.email_text);
        username = (EditText) findViewById(R.id.username_text);
        password1 = (EditText) findViewById(R.id.password_text1);
        password2 = (EditText) findViewById(R.id.password_text2);
        final TextView submit_button = (TextView) findViewById(R.id.submit_button);
        final TextView errorcheck = (TextView) findViewById(R.id.passcheck_text);

        //Check if the passwords match -- only checked when password2 is changed. If p1 is changed after p2, error will be caught when submit button is clicked
        password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String strPass1 = password1.getText().toString();
                String strPass2 = password2.getText().toString();
                if(!strPass1.equals(strPass2)) errorcheck.setText("Passwords don't match!");
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 JSONcheck="empty";

                                                 errorcheck.setText(getError());

                                                 //if the passcheck if empty i.e. no errors
                                                 if(!validateField(errorcheck.getText().toString())){

                                                     //Check if signup is successful

                                                     //if signup is success
                                                     if( checkSignup ( displayname.getText().toString(), username.getText().toString(), email.getText().toString(), password1.getText().toString() ) ){
                                                         //remove error message
                                                         errorcheck.setText("");

                                                         getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit().putString("user", username.getText().toString()).apply();
                                                         Intent i = new Intent(getApplicationContext(), Startup.class);
                                                         startActivity(i);
                                                     }
                                                     //if signup is failure
                                                     errorcheck.setText(signup_check);
                                                 }

                                             }
                                         }
        );
    }

    public String getError(){

        if(!validateField(displayname.getText().toString())) return "Invalid Name!";
        if(!validateField(username.getText().toString())) return "Invalid Username!";
        if(!validateField(email.getText().toString())) return "Invalid Email";
        if(!validateField(password1.getText().toString())) return "Invalid Password"; //check if password is blank
        if(!(password1.getText().toString()).equals(password2.getText().toString())) return ("Passwords don't match!");

        return "";
    }

    public boolean checkSignup(String displayname, String username, String email, String password) {

        String signupCheck="";

        InteractDB myData = new InteractDB("http://www.extroveert.com/extv/Signup.php?displayname="+displayname+"&username="+username+"&email="+email+"&password="+password);
        //InteractDB myData = new InteractDB("http://www.extroveert.com/extv/Signup.php?displayname=rish&username=rish&email=rish&password=rish");

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
            JSONArray jsonMainNode = jsonObject.getJSONArray("signupcheck");
            JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
            signupCheck = jsonChildNode.optString("check");
        }
        catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        //if username or email exists, display the error and
        if(!signupCheck.equals("0")) {
            signup_check = signupCheck;
            return false;
        }

        return true;
    }

    public boolean validateField(String input){
        if(input.matches("")) return false;
        return true;
    }
}
