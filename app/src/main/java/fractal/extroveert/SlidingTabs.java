package fractal.extroveert;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by osadmin on 08/01/16.
 */
public class SlidingTabs extends Activity {

    LinearLayout layout_personal, layout_searchpref, slide_bar;
    TextView button_searchpref, button_personal;
    Animation LeftSwipeIn, LeftSwipeOut, RightSwipeIn, RightSwipeOut, slide_bar_left, slide_bar_right;

    R.layout theLayout;


    private void SlidingTabs(R.layout r, R.id d ){

        //this,theLayout.

        //layout_personal = (LinearLayout) findViewById(R.id.layout_personal);

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile);

        layout_personal = (LinearLayout) findViewById(R.id.layout_personal);
        layout_searchpref = (LinearLayout) findViewById(R.id.layout_searchpref);

        button_searchpref = (TextView) findViewById(R.id.button_searchpref);
        button_personal = (TextView) findViewById(R.id.button_personal);

        RightSwipeIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
        RightSwipeOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
        LeftSwipeIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
        LeftSwipeOut = AnimationUtils.loadAnimation(this, R.anim.swipe_left_out);



        layout_searchpref.setVisibility(View.GONE);

        //------------------------------------- onClick action for People button ---------------------------------
        findViewById(R.id.button_searchpref).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (layout_searchpref.getVisibility() != View.VISIBLE) {
                    button_searchpref.setTextAppearance(getApplicationContext(), R.style.boldText);
                    button_personal.setTextAppearance(getApplicationContext(), R.style.normalText);

                    layout_searchpref.setVisibility(View.VISIBLE);
                    layout_personal.startAnimation(LeftSwipeOut);
                    layout_searchpref.startAnimation(LeftSwipeIn);
                    layout_personal.setVisibility(View.GONE);
                }

            }
        });
        //#------------------------------------- onClick action for People button ---------------------------------


        //------------------------------------- onClick action for Events button ---------------------------------
        findViewById(R.id.button_personal).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (layout_personal.getVisibility() != View.VISIBLE) {
                    button_personal.setTextAppearance(getApplicationContext(), R.style.boldText);
                    button_searchpref.setTextAppearance(getApplicationContext(), R.style.normalText);

                    layout_personal.setVisibility(View.VISIBLE);
                    layout_searchpref.startAnimation(RightSwipeOut);
                    layout_personal.startAnimation(RightSwipeIn);
                    layout_searchpref.setVisibility(View.GONE);
                }

            }
        });
        //#------------------------------------- onClick action for Events button ---------------------------------


    }

}
