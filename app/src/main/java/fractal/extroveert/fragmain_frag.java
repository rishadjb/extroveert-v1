package fractal.extroveert;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.facebook.login.widget.LoginButton;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
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

/**
 * Created by osadmin on 28/12/15.
 */
public class fragmain_frag extends Fragment {

    public static fragmain_frag newInstance(String text) {

        fragmain_frag f = new fragmain_frag();

        Bundle b = new Bundle();
        b.putString("text", text);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragmain_frag, container, false);


        LinearLayout authButton = (LinearLayout) v.findViewById(R.id.authButton);


        ((TextView) v.findViewById(R.id.tvFragText)).setText(getArguments().getString("text"));
        return v;
    }
}