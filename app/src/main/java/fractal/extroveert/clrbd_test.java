package fractal.extroveert;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by osadmin on 26/12/15.
 */
public class clrbd_test extends Activity{

    //List<TextView> tList = new ArrayList<TextView>();

    GradientDrawable bkG; //, bkGa, bkGb, bkGc;
    LinearLayout boxa, boxb, boxc, boxx;
    List<GradientDrawable> drawables = new ArrayList<GradientDrawable>();

    TextView show;

    int H,offset_random,range=5, H_offset_var=120, limit_l=0, limit_u=359;
    int S_offset_var=50;
    float S,V;

    Random rand = new Random();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clrbd_test);


        //TextView next = (TextView) findViewById(R.id.next);
        show = (TextView) findViewById(R.id.show);


        boxx = (LinearLayout) findViewById(R.id.clrboxx);
        boxa = (LinearLayout) findViewById(R.id.clrboxa);
        boxb = (LinearLayout) findViewById(R.id.clrboxb);
        boxc = (LinearLayout) findViewById(R.id.clrboxc);

        //get the GradientDrawable for the main circle
        bkG = (GradientDrawable) boxx.getBackground();

        //add the GradientDrawables for the circle to the drawables list
        drawables.add((GradientDrawable) boxa.getBackground());
        drawables.add((GradientDrawable) boxb.getBackground());
        drawables.add((GradientDrawable) boxc.getBackground());

        //call function to set colors and set listener for onclick behaviours
        setColorAndListener();

        /*
        //when next button is clicked
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call function to set colors and set listener for onclick behaviours
                setColorAndListener();
            }
        });
        */



        //when show button is clicked, show the original circle
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call function to set colors and set listener for onclick behaviours
                boxx.setVisibility(View.VISIBLE);
            }
        });

        //when next button is clicked
        boxx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call function to set colors and set listener for onclick behaviours

                boxx.setVisibility(View.INVISIBLE);

                boxa.setVisibility(View.VISIBLE);
                boxb.setVisibility(View.VISIBLE);
                boxc.setVisibility(View.VISIBLE);

                show.setVisibility(View.VISIBLE);
            }
        });



/*
//tList.get(0).setText("" + H);

TextView texta = (TextView) findViewById(R.id.texta);
TextView textb = (TextView) findViewById(R.id.textb);
TextView textc = (TextView) findViewById(R.id.textc);


aList.add(boxa);
aList.add(boxb);
aList.add(boxc);

tList.add(texta);
tList.add(textb);
tList.add(textc);
*/

    }


    //Check if the hue is in range; regulate if outside range
    public int regulate(int hue){

        if(hue<limit_l) return limit_u+hue;

        if(hue>limit_u) return hue-limit_u;

        return hue;

    }


    //Define the behaviour when a circle is clicked
    public void setCustomClickListener(LinearLayout box, GradientDrawable bList_item){

        final LinearLayout this_box = box;
        final GradientDrawable this_bList_item = bList_item;

        this_box.setOnClickListener(new View.OnClickListener() {
            @Override
            //check if the clicked graddraw for the clicked circle matches the graddraw for the 0th item in drawables list
            // (because 0th item is always the correct answer)
            public void onClick(View view) {
                if (((GradientDrawable) this_box.getBackground()).equals(this_bList_item))
                    setColorAndListener();
                else{
                    Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*
    Shuffle the circles
    Set the colors for the circles
    Set the click behaviours for each circle
    */

    public void setColorAndListener(){



        show.setVisibility(View.INVISIBLE);
        boxx.setVisibility(View.VISIBLE);


        //get the offset - the offset is so that each color doesn't have the same alternatives (wrong choices) every time
        offset_random = rand.nextInt(range)-(range/2);

        //get the color numbers
        H = rand.nextInt(limit_u);
        V = S = ( ((float)rand.nextInt(25)) + S_offset_var)/100;

        //set color for the main circle
        bkG.setColor(Color.HSVToColor(new float[]{H, S, V}));

        Collections.shuffle(drawables);

        drawables.get(0).setColor(Color.HSVToColor(new float[]{H, S, V}));
        drawables.get(1).setColor(Color.HSVToColor(new float[]{regulate(H + offset_random + H_offset_var), S, V}));
        drawables.get(2).setColor(Color.HSVToColor(new float[]{regulate(H + offset_random - H_offset_var), S, V}));

        setCustomClickListener(boxa, drawables.get(0));
        setCustomClickListener(boxb, drawables.get(0));
        setCustomClickListener(boxc, drawables.get(0));

        boxa.setVisibility(View.INVISIBLE);
        boxb.setVisibility(View.INVISIBLE);
        boxc.setVisibility(View.INVISIBLE);

    }
}