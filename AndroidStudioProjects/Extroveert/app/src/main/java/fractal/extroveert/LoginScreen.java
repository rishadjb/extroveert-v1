package fractal.extroveert;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
public class LoginScreen extends Activity {
    EditText email_text;
    EditText username_text;
    EditText password_text;
    TextView submit_button;
    TextView passcheck_text;

    String JSONcheck = "empty";
    String passcheck;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        email_text = (EditText) findViewById(R.id.email_text);
        username_text = (EditText) findViewById(R.id.username_text);
        password_text = (EditText) findViewById(R.id.password_text);
        submit_button = (TextView) findViewById(R.id.submit_button);
        passcheck_text = (TextView) findViewById(R.id.passcheck_text);

        submit_button.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {


                     JSONcheck="empty";

                     String username = username_text.getText().toString();
                     String password = password_text.getText().toString();

                     passcheck = check_login(username, password);

                     //invalid password
                     if (check_login(username, password).equals("0")){

                         //Toast.makeText(LoginScreen.this, passcheck, Toast.LENGTH_SHORT).show();
                         passcheck_text.setText("Invalid username or password! Please try again");
                         password_text.setText("");
                     }
                     //successful password - passcheck.equals("1")
                     else{
                         getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit().putString("user", username).apply();
                         Intent i = new Intent(getApplicationContext(), EventGrid.class);
                         startActivity(i);
                     }

                 }
             }
        );
    }
    public String check_login(String username, String password) {
        String passcheck = "";
        InteractDB myData = new InteractDB("http://www.extroveert.com/testconn_user.php?username="+username+"&password="+password);
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
}
