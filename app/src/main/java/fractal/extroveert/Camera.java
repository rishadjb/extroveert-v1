package fractal.extroveert;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by osadmin on 10/01/16.
 */
public class Camera extends Activity{

    //int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Button btnSelect;
    ImageView ivImage;

    Activity act = this;

    Bundle saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saved = savedInstanceState;
        setContentView(R.layout.selectimage);
        btnSelect = (Button) findViewById(R.id.btnSelectPhoto);
        btnSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //new SelectImage(act, saved, btnSelect, ivImage);
            }
        });
        ivImage = (ImageView) findViewById(R.id.ivImage);
    }

}
