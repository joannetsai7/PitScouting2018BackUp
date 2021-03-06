package com.team20.pitscouting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

public class ScoutActivity extends AppCompatActivity{
    //Expandable List
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHolder = new ArrayList<>();
    private HashMap<String,List<String>> listHash = new HashMap<>();;
    private List<String> driveTrain = new ArrayList<>();
    private List<String> wheel = new ArrayList<>();
    private List<String> driveMotor = new ArrayList<>();
    private List<String> programLang = new ArrayList<>();

    //Data points
    private static Data data = new Data();
    private String firstName = "";
    private String lastName = "";
    private String teamNumber = "";
    private String driveTrainChosen = "";
    private String wheelChosen = "";
    private String driveMotorChosen = "";
    private String programLangChosen = "";
    private String driveTrainChosenOther = "";
    private String wheelChosenOther = "";
    private String driveMotorChosenOther = "";
    private String programLangChosenOther = "";

    //Do not change, stays the same every year
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout);

        //Info from previous screen
        firstName = getIntent().getStringExtra("First_Name");
        lastName = getIntent().getStringExtra("Last_Name");
        teamNumber = getIntent().getStringExtra("Team_Number");

        listView = (ExpandableListView)findViewById(R.id.lvExp);
        initData();
        listAdapter = new ExpandableListAdapter(this,listDataHolder,listHash);
        listView.setAdapter(listAdapter);

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String name = "";
                if (groupPosition == 0){
                    String driveTrainChild = driveTrain.get(childPosition); //Drive Train type chosen
                    if (driveTrainChild.equals("Other")){
                        Intent otherIntent = new Intent(ScoutActivity.this,OtherPop.class);
                        if (!driveTrainChosenOther.equals("")){
                            otherIntent.putExtra("prev", driveTrainChosenOther);
                        } else {
                            otherIntent.putExtra("prev", "");
                        }
                        startActivityForResult(otherIntent, 1);
                    } else {
                        driveTrainChosen = driveTrainChild;
                    }
                    name = "Drive Train: " + driveTrainChild;
                } else if (groupPosition == 1) {
                    String wheelChild = wheel.get(childPosition); //Wheel type chosen
                    if (wheelChild.equals("Other")){
                        Intent otherIntent = new Intent(ScoutActivity.this,OtherPop.class);
                        if (!wheelChosenOther.equals("")){
                            otherIntent.putExtra("prev", wheelChosenOther);
                        } else {
                            otherIntent.putExtra("prev", "");
                        }
                        startActivityForResult(otherIntent, 2);
                    } else {
                        wheelChosen = wheelChild;
                    }
                    name = "Wheel Type: " + wheelChild;
                } else if (groupPosition == 2){
                    String driveMotorChild = driveMotor.get(childPosition); //Drive Train Motor type chosen
                    if (driveMotorChild.equals("Other")){
                        Intent otherIntent = new Intent(ScoutActivity.this,OtherPop.class);
                        if (!driveMotorChosenOther.equals("")){
                            otherIntent.putExtra("prev", driveMotorChosenOther);
                        } else {
                            otherIntent.putExtra("prev", "");
                        }
                        startActivityForResult(otherIntent, 3);
                    } else {
                        driveMotorChosen = driveMotorChild;
                    }
                    name = "Drive Train Motors: " + driveMotorChild;
                } else {
                    String programLangChild = programLang.get(childPosition); //Programming Language type chosen
                    if (programLangChild.equals("Other")){
                        Intent otherIntent = new Intent(ScoutActivity.this,OtherPop.class);
                        if (!programLangChosenOther.equals("")){
                            otherIntent.putExtra("prev", programLangChosenOther);
                        } else {
                            otherIntent.putExtra("prev", "");
                        }
                        startActivityForResult(otherIntent, 4);
                    } else {
                        programLangChosen = programLangChild;
                    }
                    name = "Programming Language: " + programLangChild;
                }
                listDataHolder.set(groupPosition, name);
                reassign();
                Toast.makeText(getApplicationContext(), listDataHolder.get(groupPosition), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    //Do not change, stays the same every year
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

    //Do not change, stays the same every year
    private void initData(){
        listDataHolder.add("Drive Train");
        driveTrain.add("4 wheel");
        driveTrain.add("6 wheel");
        driveTrain.add("8 wheel");
        driveTrain.add("Mecanum");
        driveTrain.add("Omni drive");
        driveTrain.add("Swerve");
        driveTrain.add("Articulating");
        driveTrain.add("Treads");
        driveTrain.add("Other");

        listDataHolder.add("Wheel Type");
        wheel.add("Traction");
        wheel.add("Pneumatic Tires");
        wheel.add("Treads");
        wheel.add("Mecanum");
        wheel.add("Omni");
        wheel.add("Other");

        listDataHolder.add("Drive Train Motors");
        driveMotor.add("2 CIM");
        driveMotor.add("4 CIM");
        driveMotor.add("6 CIM");
        driveMotor.add("4 MiniCIM");
        driveMotor.add("6 MiniCIM");
        driveMotor.add("775 Pros");
        driveMotor.add("4 CIM + 2 MiniCIM");
        driveMotor.add("2 CIM + 2 MiniCIM");
        driveMotor.add("Other");

        listDataHolder.add("Programming Language");
        programLang.add("Java");
        programLang.add("C++");
        programLang.add("LabView");
        programLang.add("Other");

        reassign();
    }

    //Do not change, stays the same every year
    private void reassign(){
        listHash.put(listDataHolder.get(0),driveTrain);
        listHash.put(listDataHolder.get(1),wheel);
        listHash.put(listDataHolder.get(2),driveMotor);
        listHash.put(listDataHolder.get(3),programLang);
    }

    //Do not change, stays the same every year
    private void setListViewHeight(ExpandableListView listView, int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group)) || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null, listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10) {
            height = 200;
        }
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    //Do not change, stays the same every year
    //Purpose: prevents user from pressing back on accident and losing everything
    @Override
    public void onBackPressed() {
        startActivityForResult(new Intent(ScoutActivity.this,ExitPrompt.class), 5);
    }

    //Dont worry about this...Joanne will clean it up
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) { //Drive Train Other option
                    driveTrainChosenOther = data.getStringExtra("Other");
                    driveTrainChosen = "Other: " + driveTrainChosenOther;
                }
                break;
            }
            case (2) : {
                if (resultCode == Activity.RESULT_OK) { //Wheel Type Other option
                    wheelChosenOther = data.getStringExtra("Other");
                    wheelChosen = "Other: " + wheelChosenOther;
                }
                break;
            }
            case (3) : {
                if (resultCode == Activity.RESULT_OK) { //Drive Train Motors Other option
                    driveMotorChosenOther = data.getStringExtra("Other");
                    driveMotorChosen = "Other: " + driveMotorChosenOther;
                }
                break;
            }
            case (4) : {
                if (resultCode == Activity.RESULT_OK) { //Drive Train Motors Other option
                    programLangChosenOther = data.getStringExtra("Other");
                    programLangChosen = "Other: " + programLangChosenOther;
                }
                break;
            }
            case (5) : {
                if (resultCode == Activity.RESULT_OK) { //Back Arrow pressed
                    String choice = data.getStringExtra("Choice");
                    if (choice.equals("leave")){
                        finish();
                    }
                }
                break;
            }
        }
    }

    //CHANGE THIS
    public void done(View view){
        String shortOutput = "";
        String fullOutput = "";
        shortOutput += teamNumber;

        //Drive Train
        if (!driveTrainChosen.equals("")) {
            shortOutput += "\t" + driveTrainChosen;

            //Wheel Type
            if (!wheelChosen.equals("")) {
                shortOutput += "\t" + wheelChosen;

                //Matching Drive Train and Wheel Type
                if ((driveTrainChosen.equals("Mecanum") && wheelChosen.equals("Mecanum")) ||
                        (driveTrainChosen.equals("Omni drive") && wheelChosen.equals("Omni")) ||
                        (!driveTrainChosen.equals("Omni drive") && !wheelChosen.equals("Omni") && !driveTrainChosen.equals("Mecanum") && !wheelChosen.equals("Mecanum"))){

                    //Drive Train Motor
                    if (!driveMotorChosen.equals("")) {
                        shortOutput += "\t" + driveMotorChosen;

                        //Programming Language
                        if (!programLangChosen.equals("")){
                            shortOutput += "\t" + programLangChosen;

                            //Dimensions
                            EditText editLength = (EditText) findViewById(R.id.length);
                            String length = editLength.getText().toString();

                            EditText editWidth = (EditText) findViewById(R.id.width);
                            String width = editWidth.getText().toString();

                            EditText editHeight = (EditText) findViewById(R.id.height);
                            String height = editHeight.getText().toString();
                            if (!length.equals("") && !width.equals("") && !height.equals("")) {
                                int testLength = Integer.parseInt(length);
                                int testWidth = Integer.parseInt(width);
                                int testHeight = Integer.parseInt(height);
                                if (testLength <= 33) { //Check length is within rules
                                    if (testWidth <= 28) { //Check width is within rules
                                        if (testHeight <= 55) { //Check height is within rules
                                            shortOutput += "\t" + length + "\t" + width + "\t" + height;

                                            //Weight
                                            EditText editWeight = (EditText) findViewById(R.id.weight);
                                            String weight = editWeight.getText().toString();
                                            if (!weight.equals("")) { //Weight is entered
                                                int testWeight = Integer.parseInt(weight);
                                                if (testWeight <= 120) {
                                                    shortOutput += "\t" + weight;

                                                    //Wheel Diameter
                                                    EditText editDiameter = (EditText) findViewById(R.id.diameter);
                                                    String diameter = editDiameter.getText().toString();
                                                    if (!diameter.equals("")) {
                                                        shortOutput += "\t" + diameter;

                                                        EditText editBumper = (EditText) findViewById(R.id.bumper);
                                                        String bumper = editBumper.getText().toString();
                                                        if (!bumper.equals("")) {
                                                            int testbumper = Integer.parseInt(bumper);
                                                            if (testbumper <= 7.5) {
                                                                shortOutput += "\t" + bumper;

                                                                //Power Cube Collecting
                                                                String cubeCollect = "";
                                                                CheckBox portal = (CheckBox) findViewById(R.id.portal);
                                                                cubeCollect += checkBox(portal, cubeCollect);

                                                                CheckBox exchange = (CheckBox) findViewById(R.id.exchange);
                                                                cubeCollect += checkBox(exchange, cubeCollect);

                                                                CheckBox floor = (CheckBox) findViewById(R.id.floor);
                                                                cubeCollect += checkBox(floor, cubeCollect);

                                                                CheckBox pyramid = (CheckBox) findViewById(R.id.pyramid);
                                                                cubeCollect += checkBox(pyramid, cubeCollect);
                                                                if (!cubeCollect.equals("")) {
                                                                    shortOutput += "\t" + cubeCollect;

                                                                    //Power Cube Scoring
                                                                    String cubeScore = "";
                                                                    CheckBox gameSwitch = (CheckBox) findViewById(R.id.gameSwitch);
                                                                    cubeScore += checkBox(gameSwitch, cubeScore);

                                                                    CheckBox scale = (CheckBox) findViewById(R.id.scale);
                                                                    cubeScore += checkBox(scale, cubeScore);

                                                                    CheckBox exchangeScore = (CheckBox) findViewById(R.id.exchangeScore);
                                                                    cubeScore += checkBox(exchangeScore, cubeScore);
                                                                    if (!cubeScore.equals("")) {
                                                                        shortOutput += "\t" + cubeScore;

                                                                        //Power Cube Orientation
                                                                        String cubePickUp = "";
                                                                        CheckBox side = (CheckBox) findViewById(R.id.side);
                                                                        cubePickUp += checkBox(side, cubePickUp);

                                                                        CheckBox flat = (CheckBox) findViewById(R.id.flat);
                                                                        cubePickUp += checkBox(flat, cubePickUp);
                                                                        if (!cubePickUp.equals("")) {
                                                                            shortOutput += "\t" + cubePickUp;

                                                                            //Climbing
                                                                            String climb = "";
                                                                            CheckBox rung = (CheckBox) findViewById(R.id.rung);
                                                                            climb += checkBox(rung, climb);

                                                                            CheckBox sideClimb = (CheckBox) findViewById(R.id.sideClimb);
                                                                            climb += checkBox(sideClimb, climb);

                                                                            CheckBox ramp = (CheckBox) findViewById(R.id.ramp);
                                                                            climb += checkBox(ramp, climb);

                                                                            CheckBox lift = (CheckBox) findViewById(R.id.lift);
                                                                            climb += checkBox(lift, climb);

                                                                            CheckBox noClimb = (CheckBox) findViewById(R.id.noClimb);
                                                                            String noClimbCheck = checkBox(noClimb, climb);
                                                                            climb += noClimbCheck;
                                                                            if (!climb.equals("")) {
                                                                                if ((noClimbCheck.equals("None") && climb.equals("None")) || noClimbCheck.equals("")) {
                                                                                    shortOutput += "\t" + climb;

                                                                                    //Detect Color of Switch/Scale
                                                                                    RadioGroup detectColor = (RadioGroup) findViewById(R.id.detectColor);
                                                                                    int detectCapability = detectColor.getCheckedRadioButtonId();
                                                                                    String detection = radioButton(detectCapability);
                                                                                    if (!detection.equals("")) {
                                                                                        shortOutput += "\t" + detection;

                                                                                        //Auto Position Preferences
                                                                                        String autoPositions = "";
                                                                                        CheckBox auto1 = (CheckBox) findViewById(R.id.auto1);
                                                                                        autoPositions += checkBox(auto1, autoPositions);

                                                                                        CheckBox auto2 = (CheckBox) findViewById(R.id.auto2);
                                                                                        autoPositions += checkBox(auto2, autoPositions);

                                                                                        CheckBox auto3 = (CheckBox) findViewById(R.id.auto3);
                                                                                        autoPositions += checkBox(auto3, autoPositions);
                                                                                        if (!autoPositions.equals("")) {
                                                                                            shortOutput += "\t" + autoPositions;

                                                                                            //Auto Abilities
                                                                                            String autoAbility = "";
                                                                                            CheckBox noAuto = (CheckBox) findViewById(R.id.noAuto);
                                                                                            String noAutoCheck = checkBox(noAuto, autoAbility);
                                                                                            autoAbility += noAutoCheck;

                                                                                            CheckBox baseLine = (CheckBox) findViewById(R.id.baseLine);
                                                                                            autoAbility += checkBox(baseLine, autoAbility);

                                                                                            CheckBox scoreSwitch = (CheckBox) findViewById(R.id.scoreSwitch);
                                                                                            autoAbility += checkBox(scoreSwitch, autoAbility);

                                                                                            CheckBox scoreScale = (CheckBox) findViewById(R.id.scoreScale);
                                                                                            autoAbility += checkBox(scoreScale, autoAbility);

                                                                                            CheckBox scoreSwitchScale = (CheckBox) findViewById(R.id.scoreSwitchScale);
                                                                                            autoAbility += checkBox(scoreSwitchScale, autoAbility);

                                                                                            CheckBox scoreSwitch2 = (CheckBox) findViewById(R.id.scoreSwitch2);
                                                                                            autoAbility += checkBox(scoreSwitch2, autoAbility);

                                                                                            CheckBox scoreScale2 = (CheckBox) findViewById(R.id.scoreScale2);
                                                                                            autoAbility += checkBox(scoreScale2, autoAbility);
                                                                                            if (!autoAbility.equals("")) {
                                                                                                if ((noAutoCheck.equals("None") && autoAbility.equals("None")) || noAutoCheck.equals("")) {
                                                                                                    shortOutput += "\t" + autoAbility;

                                                                                                    EditText editComment = (EditText) findViewById(R.id.editComments);
                                                                                                    String comments = editComment.getText().toString();
                                                                                                    if (!comments.equals("")) {//Any comments?
                                                                                                        shortOutput += "\t" + comments;
                                                                                                    } else {
                                                                                                        shortOutput += "\tN/A";
                                                                                                    }

                                                                                                    CheckBox unreliableBox = (CheckBox) findViewById(R.id.unreliable);
                                                                                                    String unreliablity = "";
                                                                                                    System.err.println("Unreliable?");
                                                                                                    unreliablity += checkBox(unreliableBox, unreliablity);
                                                                                                    if (!unreliablity.equals("")) {
                                                                                                        shortOutput += "\tYes";
                                                                                                    } else {
                                                                                                        shortOutput += "\tNo";
                                                                                                    }

                                                                                                    fullOutput += firstName + "\t" + lastName + "\t" + shortOutput;
                                                                                                    //Done, everything is filled out
                                                                                                    data.save(shortOutput, fullOutput);

                                                                                                    finish();
                                                                                                } else {
                                                                                                    System.err.println("No other auto capabilities can be selected when no auto is selected");
                                                                                                    Toast.makeText(getApplicationContext(), "No other auto capabilities can be selected when no auto is selected", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            } else {
                                                                                                System.err.println("Auto Ability is not selected");
                                                                                                Toast.makeText(getApplicationContext(), "Auto Ability is not selected", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        } else {
                                                                                            System.err.println("Preferred Auto Positions is not selected");
                                                                                            Toast.makeText(getApplicationContext(), "Auto Positions is not selected", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    } else {
                                                                                        System.err.println("Color detection capability is not selected");
                                                                                        Toast.makeText(getApplicationContext(), "Color detection capability is not selected", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                } else {
                                                                                    System.err.println("No other climbing capabilities can be selected when none is selected");
                                                                                    Toast.makeText(getApplicationContext(), "No other climbing capabilities can be selected when none is selected", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            } else {
                                                                                System.err.println("Climbing capabilities is not selected");
                                                                                Toast.makeText(getApplicationContext(), "Climbing capabilities is not selected", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        } else {
                                                                            System.err.println("Power Cube Orientation capabilities is not selected");
                                                                            Toast.makeText(getApplicationContext(), "Power Cube Orientation capabilities is not selected", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    } else {
                                                                        System.err.println("Power Cube Scoring capabilities is not selected");
                                                                        Toast.makeText(getApplicationContext(), "Power Cube Scoring capabilities is not selected", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } else {
                                                                    System.err.println("Power Cube Collection capabilities is not entered");
                                                                    Toast.makeText(getApplicationContext(), "Power Cube Collection capabilities is not entered", Toast.LENGTH_SHORT).show();
                                                                }
                                                            } else {
                                                                System.err.println("Bumper height is too high");
                                                                Toast.makeText(getApplicationContext(), "Bumper height is too high", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            System.err.println("Bumper height is not entered");
                                                            Toast.makeText(getApplicationContext(), "Bumper height is not entered", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        System.err.println("Wheel diameter is not entered");
                                                        Toast.makeText(getApplicationContext(), "Wheel diameter is not entered", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    System.err.println("Weight is too high");
                                                    Toast.makeText(getApplicationContext(), "Weight is too high", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                System.err.println("Weight is not entered");
                                                Toast.makeText(getApplicationContext(), "Weight is not entered", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            System.err.println("Robot height is too tall");
                                            Toast.makeText(getApplicationContext(), "Robot height is too tall", Toast.LENGTH_SHORT).show();
                                        }
                                    } else{
                                        System.err.println("Robot width is too wide");
                                        Toast.makeText(getApplicationContext(), "Robot width is too wide", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    System.err.println("Robot length is too long");
                                    Toast.makeText(getApplicationContext(), "Robot length is too long", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                System.err.println("Dimensions are not entered");
                                Toast.makeText(getApplicationContext(), "Dimensions are not entered", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            System.err.println("Programming Language is not selected");
                            Toast.makeText(getApplicationContext(), "Programming Language is not selected", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        System.err.println("Drive Train Motor type is not selected");
                        Toast.makeText(getApplicationContext(), "Drive Train Motor type is not selected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    System.err.println("Wheel type does not match Drive Train type");
                    Toast.makeText(getApplicationContext(), "Wheel type does not match Drive Train type", Toast.LENGTH_SHORT).show();
                }
            } else {
                System.err.println("Wheel type is not selected");
                Toast.makeText(getApplicationContext(), "Wheel Type is not selected", Toast.LENGTH_SHORT).show();
            }
        } else {
            System.err.println("Drive Train type is not selected");
            Toast.makeText(getApplicationContext(), "Drive Train type is not selected", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    Do not change, stays the same every year
    Do not delete even if you do not end up using ir
    Parameter: id tag of the radio button selected
    Returns label of radio button selected
    How to use:
    RadioGroup floorFuelCollect = (RadioGroup) findViewById(R.id.floorFuelCollect);
    int floorFuelCollectID = floorFuelCollect.getCheckedRadioButtonId();
    String floorFuelCapability = radioButton(floorFuelCollectID);
    */
    public String radioButton(int selectedRadioButtonID){
        if (selectedRadioButtonID != -1){
            RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
            String selectedRadioButtonText = selectedRadioButton.getText().toString(); //Radio button checked
            return selectedRadioButtonText;
        } else {
            return "";
        }

    }

    /*
    Do not change, stays the same every year
    Do not delete even if you do not end up using it
    Parameters: id tag of the checkbox, string of all checkboxes checked
    Needs string of all checkboxes because we have to separate all of the choices selected by a '|'
    Returns label of checkbox
    How to use:
    String cubeCollect = "";
    CheckBox portal = (CheckBox) findViewById(R.id.portal);
    cubeCollect += checkBox(portal, cubeCollect);
     */
    public String checkBox(CheckBox check, String total){
        CheckBox unreliableBox = (CheckBox) findViewById(R.id.unreliable);
        if (check.isChecked()){
            int checkID = check.getId();
            String checkedBox = check.getText().toString(); //Box checked
            if (total.equals("")){
                if (checkID == unreliableBox.getId()){ //The unreliable checkbox, since there is no label
                    return "Unreliable";
                } else {
                    return checkedBox;
                }
            } else {
                return "|" + checkedBox;
            }
        } else {
            return "";
        }
    }
}