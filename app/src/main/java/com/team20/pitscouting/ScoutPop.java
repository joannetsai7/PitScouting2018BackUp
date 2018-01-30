package com.team20.pitscouting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

/**
 * Created by JT on 1/27/18.
 */
public class ScoutPop extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scout_pop);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.2));
    }


    public void done(View v){
        EditText editContent = (EditText) findViewById(R.id.content);
        String content = editContent.getText().toString(); //other option
        Intent resultIntent = new Intent();
        resultIntent.putExtra("NumScouts", content);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}
