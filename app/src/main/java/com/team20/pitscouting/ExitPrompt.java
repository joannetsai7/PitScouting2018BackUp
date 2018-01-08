package com.team20.pitscouting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

/**
 * Created by JT on 3/4/17.
 */
public class ExitPrompt extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exit_prompt);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.1));
    }


    public void leave(View v){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("Choice", "leave");
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public void stay(View v){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("Choice", "stay");
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
