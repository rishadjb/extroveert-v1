package fractal.extroveert;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

/**
 * Created by osadmin on 19/10/15.
 */
public class testslide extends Activity {

    LinearLayout pinklayout, purplelayout;

    Animation LeftSwipeIn,LeftSwipeOut,RightSwipeIn,RightSwipeOut;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testslide);

        pinklayout = (LinearLayout) findViewById(R.id.pinklayout);
        purplelayout = (LinearLayout) findViewById(R.id.purplelayout);


        RightSwipeIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
        RightSwipeOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
        LeftSwipeIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
        LeftSwipeOut = AnimationUtils.loadAnimation(this, R.anim.swipe_left_out);

        purplelayout.setVisibility(View.GONE);

        //------------------------------------- onClick action for View Map button ---------------------------------
        pinklayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //pinklayout.startAnimation(LeftSwipe);
                purplelayout.setVisibility(View.VISIBLE);
                pinklayout.startAnimation(LeftSwipeOut);
                purplelayout.startAnimation(LeftSwipeIn);
                pinklayout.setVisibility(View.GONE);
            }
        });
        //#------------------------------------- onClick action for View Map button ---------------------------------


        //------------------------------------- onClick action for View Map button ---------------------------------
        purplelayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //pinklayout.startAnimation(LeftSwipe);
                pinklayout.setVisibility(View.VISIBLE);
                purplelayout.startAnimation(RightSwipeOut);
                pinklayout.startAnimation(RightSwipeIn);
                purplelayout.setVisibility(View.GONE);
            }
        });
        //#------------------------------------- onClick action for View Map button ---------------------------------


    }


}
