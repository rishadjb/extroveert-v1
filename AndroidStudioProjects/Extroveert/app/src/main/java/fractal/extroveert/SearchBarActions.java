package fractal.extroveert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by osadmin on 07/10/15.
 */
public class SearchBarActions extends Activity{

    SearchBarActions(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.advsearch);

        LinearLayout advsearch_button;
        advsearch_button = (LinearLayout) findViewById(R.id.button_search);

        // view products click event
        advsearch_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), AdvSearch.class);
                startActivity(i);
            }
        });

    }


}
