package com.team20.pitscouting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

/**
 * Created by JT on 2/1/17.
 */
public class CommentsPop extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_popwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.5));
    }


    public void done(View v){
        EditText editComments = (EditText) findViewById(R.id.editComments);
        String comments = editComments.getText().toString(); //Comments
        Intent resultIntent = new Intent();
        resultIntent.putExtra("Comments", comments);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
