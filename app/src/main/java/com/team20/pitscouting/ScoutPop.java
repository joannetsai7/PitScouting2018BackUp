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

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        System.err.println("TABLETTTTT"+tabletSize);
        if (tabletSize) {
            getWindow().setLayout((int)(width*.8),(int)(height*.2));
        } else {
            getWindow().setLayout((int)(width*.8),(int)(height*.35));
        }


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
