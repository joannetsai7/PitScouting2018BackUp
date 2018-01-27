package com.team20.pitscouting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by JT on 1/25/17.
 */

//Dont worry about this...Joanne will clean it up
public class OtherPop extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_popwindow);

        String prev = getIntent().getStringExtra("prev");
        EditText content = (EditText) findViewById(R.id.content);
        content.setText(prev, TextView.BufferType.EDITABLE);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.2));
    }


    public void done(View v){
        EditText editOther = (EditText) findViewById(R.id.content);
        String other = editOther.getText().toString(); //other option
        Intent resultIntent = new Intent();
        resultIntent.putExtra("Other", other);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
