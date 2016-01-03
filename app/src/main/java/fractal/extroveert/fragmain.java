package fractal.extroveert;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

/**
 * Created by osadmin on 28/12/15.
 */
public class fragmain extends Activity {

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmain);

        LinearLayout fragContainer = (LinearLayout) findViewById(R.id.llFragmentContainer);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        ll.setId(12345);

        getFragmentManager().beginTransaction().add(ll.getId(), fragmain_frag.newInstance("I am frag 1"), "someTag1").commit();
        getFragmentManager().beginTransaction().add(ll.getId(), fragmain_frag.newInstance("I am frag 2"), "someTag2").commit();

        fragContainer.addView(ll);
    }
}