package com.team20.pitscouting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
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
    private String comments = "";

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
                        startActivityForResult(new Intent(ScoutActivity.this,OtherPop.class), 1);
                    } else {
                        driveTrainChosen = driveTrainChild;
                    }
                    name = "Drive Train: " + driveTrainChild;
                } else if (groupPosition == 1) {
                    String wheelChild = wheel.get(childPosition); //Wheel type chosen
                    if (wheelChild.equals("Other")){
                        startActivityForResult(new Intent(ScoutActivity.this,OtherPop.class), 2);
                    } else {
                        wheelChosen = wheelChild;
                    }
                    name = "Wheel Type: " + wheelChild;
                } else if (groupPosition == 2){
                    String driveMotorChild = driveMotor.get(childPosition); //Drive Train Motor type chosen
                    if (driveMotorChild.equals("Other")){
                        startActivityForResult(new Intent(ScoutActivity.this,OtherPop.class), 3);
                    } else {
                        driveMotorChosen = driveMotorChild;
                    }
                    name = "Drive Train Motors: " + driveMotorChild;
                } else {
                    String programLangChild = programLang.get(childPosition); //Programming Language type chosen
                    if (programLangChild.equals("Other")){
                        startActivityForResult(new Intent(ScoutActivity.this,OtherPop.class), 4);
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
        driveTrain.add("4 wheel tank");
        driveTrain.add("6 wheel tank");
        driveTrain.add("8 wheel tank");
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

    //Dont worry about this...Joanne will clean it up
    public void comments(View view){
        startActivityForResult(new Intent(ScoutActivity.this,CommentsPop.class), 5);
    }

    //Dont worry about this...Joanne will clean it up
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) { //Drive Train Other option
                    driveTrainChosen = "Other: " + data.getStringExtra("Other");
                }
                break;
            }
            case (2) : {
                if (resultCode == Activity.RESULT_OK) { //Wheel Type Other option
                    wheelChosen = "Other: " + data.getStringExtra("Other");
                }
                break;
            }
            case (3) : {
                if (resultCode == Activity.RESULT_OK) { //Drive Train Motors Other option
                    driveMotorChosen = "Other: " + data.getStringExtra("Other");
                }
                break;
            }
            case (4) : {
                if (resultCode == Activity.RESULT_OK) { //Drive Train Motors Other option
                    programLangChosen = "Other: " + data.getStringExtra("Other");
                }
                break;
            }
            case (5) : {
                if (resultCode == Activity.RESULT_OK) { //Comments
                    comments = data.getStringExtra("Comments");
                }
                break;
            }
            case (6) : {
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
        fullOutput += firstName + "\t" + lastName + "\t" + teamNumber;
        shortOutput += teamNumber;

        if (!driveTrainChosen.equals("")) { //Drive Train type is selected
            fullOutput += "\t" + driveTrainChosen;
            shortOutput += "\t" + driveTrainChosen;

            if (!wheelChosen.equals("")) { //Wheel type is selected
                fullOutput += "\t" + wheelChosen;
                shortOutput += "\t" + wheelChosen;

                if ((driveTrainChosen.equals("Mecanum") && wheelChosen.equals("Mecanum")) ||
                        (driveTrainChosen.equals("Omni drive") && wheelChosen.equals("Omni")) ||
                        (!driveTrainChosen.equals("Omni drive") && !wheelChosen.equals("Omni") && !driveTrainChosen.equals("Mecanum") && !wheelChosen.equals("Mecanum"))){ //Drive Train and Wheel type match

                    if (!driveMotorChosen.equals("")) { //Drive Train Motor is selected
                        fullOutput += "\t" + driveMotorChosen;
                        shortOutput += "\t" + driveMotorChosen;

                        if (!programLangChosen.equals("")){ //Programming Language is selected
                            fullOutput += "\t" + programLangChosen;
                            shortOutput += "\t" + programLangChosen;

                            EditText editLength = (EditText) findViewById(R.id.length);
                            String length = editLength.getText().toString();
                            EditText editWidth = (EditText) findViewById(R.id.width);
                            String width = editWidth.getText().toString();
                            EditText editHeight = (EditText) findViewById(R.id.height);
                            String height = editHeight.getText().toString();
                            if (!length.equals("") && !width.equals("") && !height.equals("")) { //Dimensions are not entered
                                int testLength = Integer.parseInt(length);
                                int testWidth = Integer.parseInt(width);
                                int testHeight = Integer.parseInt(height);

                                if (testLength <= 33) { //Check length is within rules
                                    if (testWidth <= 28) { //Check width is within rules
                                        if (testHeight <= 55) { //Check height is within rules
                                            fullOutput += "\t" + length + "\t" + width + "\t" + height;
                                            shortOutput += "\t" + length + "\t" + width + "\t" + height;

                                            EditText editWeight = (EditText) findViewById(R.id.weight);
                                            String weight = editWeight.getText().toString(); //Weight of robot
                                            if (!weight.equals("")) { //Weight is entered
                                                int testWeight = Integer.parseInt(weight);

                                                if (testWeight <= 120) {
                                                    fullOutput += "\t" + weight;
                                                    shortOutput += "\t" + weight;

                                                    String cubeCollect = "";
                                                    CheckBox portal = (CheckBox) findViewById(R.id.portal);
                                                    cubeCollect += checkBox(portal, cubeCollect);

                                                    CheckBox exchange = (CheckBox) findViewById(R.id.exchange);
                                                    cubeCollect += checkBox(exchange, cubeCollect);

                                                    CheckBox floor = (CheckBox) findViewById(R.id.floor);
                                                    cubeCollect += checkBox(floor, cubeCollect);

                                                    CheckBox pyramid = (CheckBox) findViewById(R.id.pyramid);
                                                    cubeCollect += checkBox(pyramid, cubeCollect);

                                                    if (!cubeCollect.equals("")) { //Power Cube Collection is entered
                                                        fullOutput += "\t" + cubeCollect;
                                                        shortOutput += "\t" + cubeCollect;

                                                        String cubeScore = "";
                                                        CheckBox gameSwitch = (CheckBox) findViewById(R.id.gameSwitch);
                                                        cubeScore += checkBox(gameSwitch, cubeScore);

                                                        CheckBox scale = (CheckBox) findViewById(R.id.scale);
                                                        cubeScore += checkBox(scale, cubeScore);
                                                        if (!cubeScore.equals("")) { //Power Cube Scoring capabilities is selected
                                                            fullOutput += "\t" + cubeScore;
                                                            shortOutput += "\t" + cubeScore;

                                                            String cubePickUp = "";
                                                            CheckBox side = (CheckBox) findViewById(R.id.side);
                                                            cubePickUp += checkBox(side, cubePickUp);

                                                            CheckBox flat = (CheckBox) findViewById(R.id.flat);
                                                            cubePickUp += checkBox(flat, cubePickUp);
                                                            if (!cubePickUp.equals("")) { //Power Cube Orientation capabilities is selected
                                                                fullOutput += "\t" + cubePickUp;
                                                                shortOutput += "\t" + cubePickUp;

                                                                String climb = "";
                                                                CheckBox rung = (CheckBox) findViewById(R.id.rung);
                                                                climb += checkBox(side, climb);

                                                                CheckBox sideClimb = (CheckBox) findViewById(R.id.sideClimb);
                                                                climb += checkBox(flat, climb);

                                                                CheckBox ramp = (CheckBox) findViewById(R.id.ramp);
                                                                climb += checkBox(ramp, climb);

                                                                CheckBox lift = (CheckBox) findViewById(R.id.lift);
                                                                climb += checkBox(lift, climb);
                                                                if (!climb.equals("")) { //Climbing capability is selected
                                                                    fullOutput += "\t" + climb;
                                                                    shortOutput += "\t" + climb;

                                                                    RadioGroup floorFuelCollect = (RadioGroup) findViewById(R.id.floorFuelCollect);
                                                                    int floorFuelCollectID = floorFuelCollect.getCheckedRadioButtonId();
                                                                    String floorFuelCollection = radioButton(floorFuelCollectID);
                                                                    if (!floorFuelCollection.equals("")) { //Floor Fuel Collection capability is selected
                                                                        fullOutput += "\t" + floorFuelCollection;
                                                                        shortOutput += "\t" + floorFuelCollection;

                                                                        RadioGroup floorGearCollect = (RadioGroup) findViewById(R.id.floorGearCollect);
                                                                        int floorGearCollectID = floorGearCollect.getCheckedRadioButtonId();
                                                                        String floorGearCollection = radioButton(floorGearCollectID);
                                                                        if (!floorGearCollection.equals("")) { //Floor Gear Collection capability is selected
                                                                            fullOutput += "\t" + floorGearCollection;
                                                                            shortOutput += "\t" + floorFuelCollection;

                                                                            RadioGroup humanPlayerFuel = (RadioGroup) findViewById(R.id.humanPlayerFuel);
                                                                            int humanPlayerFuelID = humanPlayerFuel.getCheckedRadioButtonId();
                                                                            String hpFuel = radioButton(humanPlayerFuelID);
                                                                            if (!hpFuel.equals("")) { //Human Player Fuel Collection capability is selected
                                                                                fullOutput += "\t" + hpFuel;
                                                                                shortOutput += "\t" + hpFuel;

                                                                                RadioGroup humanPlayerGear = (RadioGroup) findViewById(R.id.humanPlayerGear);
                                                                                int humanPlayerGearID = humanPlayerGear.getCheckedRadioButtonId();
                                                                                String hpGear = radioButton(humanPlayerGearID);
                                                                                if (!hpGear.equals("")) { //Human Player Gear Collection capability is selected
                                                                                    fullOutput += "\t" + hpGear;
                                                                                    shortOutput += "\t" + hpGear;

                                                                                        String autoPositions = "";
                                                                                        CheckBox auto1 = (CheckBox) findViewById(R.id.auto1);
                                                                                        autoPositions += checkBox(auto1, autoPositions);

                                                                                        CheckBox auto2 = (CheckBox) findViewById(R.id.auto2);
                                                                                        autoPositions += checkBox(auto2, autoPositions);

                                                                                        CheckBox auto3 = (CheckBox) findViewById(R.id.auto3);
                                                                                        autoPositions += checkBox(auto3, autoPositions);
                                                                                        if (!autoPositions.equals("")) { //Auto Preference is selected
                                                                                            fullOutput += "\t" + autoPositions;
                                                                                            shortOutput += "\t" + autoPositions;

                                                                                            String autoAbility = "";
                                                                                            CheckBox noAuto = (CheckBox) findViewById(R.id.noAuto);
                                                                                            autoAbility += checkBox(noAuto, autoAbility);

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
                                                                                            if (!autoAbility.equals("")) { //Auto Ability is selected
                                                                                                fullOutput += "\t" + autoAbility;
                                                                                                shortOutput += "\t" + autoAbility;

                                                                                                if (!comments.equals("")) {//Any comments?
                                                                                                    fullOutput += "\t" + comments;
                                                                                                } else {
                                                                                                    fullOutput += "\tN/A";
                                                                                                }

                                                                                                CheckBox unreliableBox = (CheckBox) findViewById(R.id.unreliable);
                                                                                                String unreliablity = "";
                                                                                                System.err.println("Unreliable?");
                                                                                                unreliablity += checkBox(unreliableBox, unreliablity);
                                                                                                if (!unreliablity.equals("")) {
                                                                                                    fullOutput += "\tYes";
                                                                                                } else {
                                                                                                    fullOutput += "\tNo";
                                                                                                }
                                                                                                //Done, everything is filled out
                                                                                                data.save(shortOutput, fullOutput);

                                                                                                finish();
                                                                                            } else {
                                                                                                System.err.println("Auto Ability is not selected");
                                                                                                Toast.makeText(getApplicationContext(), "Auto Ability is not selected", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        } else {
                                                                                            System.err.println("Preferred Auto Positions is not selected");
                                                                                            Toast.makeText(getApplicationContext(), "Auto Positions is not selected", Toast.LENGTH_SHORT).show();
                                                                                        }

                                                                                } else {
                                                                                    System.err.println("Human Player Gear Collection capability is not selected");
                                                                                    Toast.makeText(getApplicationContext(), "Human Player Gear Collection capability is not selected", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            } else {
                                                                                System.err.println("Human Player Fuel Collection capability is not selected");
                                                                                Toast.makeText(getApplicationContext(), "Human Player Fuel Collection capability is not selected", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        } else {
                                                                            System.err.println("Floor Gear Collection capability is not selected");
                                                                            Toast.makeText(getApplicationContext(), "Floor Gear Collection capability is not selected", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    } else {
                                                                        System.err.println("Floor Fuel Collection capability is not selected");
                                                                        Toast.makeText(getApplicationContext(), "Floor Fuel Collection capability is not selected", Toast.LENGTH_SHORT).show();
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
                                                            Toast.makeText(getApplicationContext(), "Gear capability is not selected", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        System.err.println("Power Cube Collection capabilities is not entered");
                                                        Toast.makeText(getApplicationContext(), "Fuel Carrying Capacity is not entered", Toast.LENGTH_SHORT).show();
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

    //Do not change, stays the same every year
    //Do not delete even if you do not end up using ir
    //Parameter: id tag of the radio button
    //Returns label of radio button
    //How to use:
    //RadioGroup floorFuelCollect = (RadioGroup) findViewById(R.id.floorFuelCollect);
    //int floorFuelCollectID = floorFuelCollect.getCheckedRadioButtonId();
    public String radioButton(int selectedRadioButtonID){
        if (selectedRadioButtonID != -1){
            RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
            String selectedRadioButtonText = selectedRadioButton.getText().toString(); //Radio button checked
            return selectedRadioButtonText;
        } else {
            return "";
        }

    }

    //Do not change, stays the same every year
    //Do not delete even if you do not end up using it
    //Parameters: id tag of the checkbox, string of all checkboxes checked
    //Needs string of all checkboxes because we have to separate all of the choices selected by a '|'
    //Returns label of checkbox
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

    //Do not change, stays the same every year
    //Purpose: prevents user from pressing back on accident and losing everything
    @Override
    public void onBackPressed() {
        startActivityForResult(new Intent(ScoutActivity.this,ExitPrompt.class), 6);
    }
}