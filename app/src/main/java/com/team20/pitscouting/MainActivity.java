package com.team20.pitscouting;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static Data data = new Data();
    private static ArrayList<String> teams = new ArrayList<String>(); //List of teams
    private static ArrayList<String> finished = new ArrayList<String>(); //List of teams scouted
    private int numScouts = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View linearLayout =  findViewById(R.id.add); //Layout where the textviews for teams are going in
        for(int i = 0; i < numScouts; i++ ) { //Adding a textview for each scout
            TextView textView = new TextView(this);
            textView.setId(i);
            textView.setTextSize(25);
            ((LinearLayout) linearLayout).addView(textView);
            Space space = new Space(this);
            space.setMinimumHeight(15);
            ((LinearLayout) linearLayout).addView(space);
        }
        assignTeams();
    }

    @Override
    public void onResume(){
        super.onResume();
        assignTeams();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void next(View view) {
        Intent intent = new Intent(MainActivity.this, ScoutActivity.class);

        EditText editFirst = (EditText) findViewById(R.id.editFirstName);
        String firstName = editFirst.getText().toString(); //First Name
        if (!firstName.equals("")){ //First Name is entered
            intent.putExtra("First_Name", firstName);

            EditText editLast = (EditText) findViewById(R.id.editLastName);
            String lastName = editLast.getText().toString(); //Last Name
            if (!lastName.equals("")){ //Last Name is entered
                intent.putExtra("Last_Name", lastName);

                EditText editTeam = (EditText) findViewById(R.id.editTeam);
                String teamNumber = editTeam.getText().toString();
                if (!teamNumber.equals("")){ //Team Scouted is entered
                    intent.putExtra("Team_Number", teamNumber);

                    boolean teamValid = false;
                    for (int i = 0; i < teams.size(); i++){
                        if (teamNumber.equals(teams.get(i))){ //If team is attending (a valid team)
                            teamValid = true;
                        }
                    }

                    boolean scouted = false;
                    for (int i = 0; i < finished.size();i ++){ //If team is scouted
                        if (teamNumber.equals(finished.get(i))){
                            scouted = true;
                        }
                    }
                    if (teamValid){ //Team Scouted is a valid team

                        if (!scouted){ //Team wasn't scouted yet
                            startActivity(intent);
                        } else {
                            System.err.println("Team entered was already scouted");
                            Toast.makeText(getApplicationContext(), "Team entered was already scouted", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        System.err.println("Team entered is not a valid team number");
                        Toast.makeText(getApplicationContext(), "Team Scouted entered is not a valid team number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    System.err.println("Team Scouted is not entered");
                    Toast.makeText(getApplicationContext(), "Team Scouted is not entered", Toast.LENGTH_SHORT).show();
                }
            } else {
                System.err.println("Last Name is not entered");
                Toast.makeText(getApplicationContext(), "Last Name is not entered", Toast.LENGTH_SHORT).show();
            }
        } else {
            System.err.println("First Name is not entered");
            Toast.makeText(getApplicationContext(), "First Name is not entered", Toast.LENGTH_SHORT).show();
        }
    }

    public void assignTeams(){
        if (teams.size() == 0){
            teams = data.readTeams();
            finished = data.getDone();
        }
        int numTeams;
        int location = 0; //Current team on in list
        int extra = teams.size()%numScouts; //Left over teams if it doesn't divide equally
        for(int i = 0; i < numScouts; i++ ) { //Split the teams into text views
            TextView textView = (TextView) findViewById(i); //Text view of corresponding scout the teams are being assigned to
            String content = "";
            int scoutTeam = 0; //number of teams assigned to scout
            if (extra != 0){
                extra--;
                numTeams = teams.size()/numScouts + 1;
            } else {
                numTeams = teams.size()/numScouts;
            }
            for (; scoutTeam < numTeams; location++){ //Adding teams
                if (!finishedTeams(location)){ //If team wasn't already scouted
                    if (!content.equals("")){ //Adding tabs in front if not first team in textView
                        content+="\t";
                        if (teams.get(location-1).length() < 4){
                            content+="\t";
                            if (teams.get(location-1).length() < 3){
                                content+="\t";
                            }
                        }
                    }
                    content += teams.get(location);
                }
                scoutTeam++;
            }
            textView.setText(content);
        }
    }

    public boolean finishedTeams(int location){
        for (int k = 0; k < finished.size(); k++){ //Check if team has been done
            if (teams.get(location).equals(finished.get(k))){ //If team is in finished list
                return true;
            }
        }
        return false; //Add tab if not used so that it lines up nicely
    }
}
