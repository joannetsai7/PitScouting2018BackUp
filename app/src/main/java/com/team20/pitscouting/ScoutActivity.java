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

    private void reassign(){
        listHash.put(listDataHolder.get(0),driveTrain);
        listHash.put(listDataHolder.get(1),wheel);
        listHash.put(listDataHolder.get(2),driveMotor);
        listHash.put(listDataHolder.get(3),programLang);
    }

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

    public void comments(View view){
        startActivityForResult(new Intent(ScoutActivity.this,CommentsPop.class), 5);
    }

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

                            RadioGroup height = (RadioGroup) findViewById(R.id.heightRobot);
                            int heightID = height.getCheckedRadioButtonId();
                            String dimensions = radioButton(view, heightID);
                            if (!dimensions.equals("")) { //Height selected
                                fullOutput += "\t" + dimensions;
                                shortOutput += "\t" + dimensions;

                                EditText editWeight = (EditText) findViewById(R.id.editWeight);
                                String weight = editWeight.getText().toString(); //Weight of robot
                                if (!weight.equals("")) { //Weight is entered
                                    int testWeight = Integer.parseInt(weight);
                                    if (testWeight <= 120){
                                        fullOutput += "\t" + weight;
                                        shortOutput += "\t" + weight;

                                        EditText editCapacity = (EditText) findViewById(R.id.editCapacity);
                                        String capacity = editCapacity.getText().toString(); //Fuel carrying capacity
                                        if (!capacity.equals("")) { //Capacity is entered
                                            fullOutput += "\t" + capacity;
                                            shortOutput += "\t" + capacity;

                                            RadioGroup gear = (RadioGroup) findViewById(R.id.gear);
                                            int gearID = gear.getCheckedRadioButtonId();
                                            String chosenGear = radioButton(view, gearID);
                                            if (!chosenGear.equals("")) { //Gear capability is selected
                                                fullOutput += "\t" + chosenGear;
                                                shortOutput += "\t" + chosenGear;

                                                RadioGroup shooting = (RadioGroup) findViewById(R.id.shooting);
                                                int shootingID = shooting.getCheckedRadioButtonId();
                                                String shootingCapability = radioButton(view, shootingID);
                                                if (!shootingCapability.equals("")) { //Shooting capability is selected
                                                    fullOutput += "\t" + shootingCapability;
                                                    shortOutput += "\t" + shootingCapability;

                                                    RadioGroup hopper = (RadioGroup) findViewById(R.id.hopper);
                                                    int hopperID = hopper.getCheckedRadioButtonId();
                                                    String hopperCapability = radioButton(view, hopperID);
                                                    if (!hopperCapability.equals("")) { //Hopper capability is selected
                                                        fullOutput += "\t" + hopperCapability;
                                                        shortOutput += "\t" + hopperCapability;

                                                        RadioGroup floorFuelCollect = (RadioGroup) findViewById(R.id.floorFuelCollect);
                                                        int floorFuelCollectID = floorFuelCollect.getCheckedRadioButtonId();
                                                        String floorFuelCollection = radioButton(view, floorFuelCollectID);
                                                        if (!floorFuelCollection.equals("")) { //Floor Fuel Collection capability is selected
                                                            fullOutput += "\t" + floorFuelCollection;
                                                            shortOutput += "\t" + floorFuelCollection;

                                                            RadioGroup floorGearCollect = (RadioGroup) findViewById(R.id.floorGearCollect);
                                                            int floorGearCollectID = floorGearCollect.getCheckedRadioButtonId();
                                                            String floorGearCollection = radioButton(view, floorGearCollectID);
                                                            if (!floorGearCollection.equals("")) { //Floor Gear Collection capability is selected
                                                                fullOutput += "\t" + floorGearCollection;
                                                                shortOutput += "\t" + floorFuelCollection;

                                                                RadioGroup humanPlayerFuel = (RadioGroup) findViewById(R.id.humanPlayerFuel);
                                                                int humanPlayerFuelID = humanPlayerFuel.getCheckedRadioButtonId();
                                                                String hpFuel = radioButton(view, humanPlayerFuelID);
                                                                if (!hpFuel.equals("")) { //Human Player Fuel Collection capability is selected
                                                                    fullOutput += "\t" + hpFuel;
                                                                    shortOutput += "\t" + hpFuel;

                                                                    RadioGroup humanPlayerGear = (RadioGroup) findViewById(R.id.humanPlayerGear);
                                                                    int humanPlayerGearID = humanPlayerGear.getCheckedRadioButtonId();
                                                                    String hpGear = radioButton(view, humanPlayerGearID);
                                                                    if (!hpGear.equals("")) { //Human Player Gear Collection capability is selected
                                                                        fullOutput += "\t" + hpGear;
                                                                        shortOutput += "\t" + hpGear;

                                                                        RadioGroup climbBUT = (RadioGroup) findViewById(R.id.climb);
                                                                        int climbID = climbBUT.getCheckedRadioButtonId();
                                                                        String climb = radioButton(view, climbID);
                                                                        if (!climb.equals("")) { //Climb capability is selected
                                                                            fullOutput += "\t" + climb;
                                                                            shortOutput += "\t" + climb;

                                                                            String autoPositions = "";
                                                                            CheckBox auto1 = (CheckBox) findViewById(R.id.auto1);
                                                                            autoPositions += checkBox(view, auto1, autoPositions);

                                                                            CheckBox auto2 = (CheckBox) findViewById(R.id.auto2);
                                                                            autoPositions += checkBox(view, auto2, autoPositions);

                                                                            CheckBox auto3 = (CheckBox) findViewById(R.id.auto3);
                                                                            autoPositions += checkBox(view, auto3, autoPositions);

                                                                            CheckBox auto4 = (CheckBox) findViewById(R.id.auto4);
                                                                            autoPositions += checkBox(view, auto4, autoPositions);
                                                                            if (!autoPositions.equals("")) { //Auto Preference is selected
                                                                                fullOutput += "\t" + autoPositions;
                                                                                shortOutput += "\t" + autoPositions;

                                                                                String autoAbility = "";
                                                                                CheckBox noAuto = (CheckBox) findViewById(R.id.noAuto);
                                                                                autoAbility += checkBox(view, noAuto, autoAbility);

                                                                                CheckBox baseLine = (CheckBox) findViewById(R.id.baseLine);
                                                                                autoAbility += checkBox(view, baseLine, autoAbility);

                                                                                CheckBox scoreGear = (CheckBox) findViewById(R.id.scoreGear);
                                                                                autoAbility += checkBox(view, scoreGear, autoAbility);

                                                                                CheckBox scoreFuel = (CheckBox) findViewById(R.id.scoreFuel);
                                                                                autoAbility += checkBox(view, scoreFuel, autoAbility);

                                                                                CheckBox fuelOnly = (CheckBox) findViewById(R.id.fuelOnly);
                                                                                autoAbility += checkBox(view, fuelOnly, autoAbility);

                                                                                CheckBox scoreFuelGear = (CheckBox) findViewById(R.id.scoreFuelGear);
                                                                                autoAbility += checkBox(view, scoreFuelGear, autoAbility);
                                                                                if (!autoAbility.equals("")) { //Auto Ability is selected
                                                                                    fullOutput += "\t" + autoAbility;
                                                                                    shortOutput += "\t" + autoAbility;

                                                                                    if (!comments.equals("")){//Any comments?
                                                                                        fullOutput += "\t" + comments;
                                                                                    } else {
                                                                                        fullOutput += "\tN/A";
                                                                                    }

                                                                                    CheckBox unreliableBox = (CheckBox) findViewById(R.id.unreliable);
                                                                                    String unreliablity = "";
                                                                                    System.err.println("Unreliable?");
                                                                                    unreliablity += checkBox(view, unreliableBox, unreliablity);
                                                                                    if (!unreliablity.equals("")){
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
                                                                            System.err.println("Climb capability is not selected");
                                                                            Toast.makeText(getApplicationContext(), "Climbing capability is not selected", Toast.LENGTH_SHORT).show();
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
                                                        System.err.println("Hopper capability is not selected");
                                                        Toast.makeText(getApplicationContext(), "Field Hopper capability is not selected", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    System.err.println("Shooting capability is not selected");
                                                    Toast.makeText(getApplicationContext(), "Shooting capability is not selected", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                System.err.println("Gear capability is not selected");
                                                Toast.makeText(getApplicationContext(), "Gear capability is not selected", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            System.err.println("Fuel carrying capacity is not entered");
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
                                System.err.println("Height is not selected");
                                Toast.makeText(getApplicationContext(), "Height is not selected", Toast.LENGTH_SHORT).show();
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

    public String radioButton(View view, int selectedRadioButtonID){
        if (selectedRadioButtonID != -1){
            RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
            String selectedRadioButtonText = selectedRadioButton.getText().toString(); //Radio button checked
            return selectedRadioButtonText;
        } else {
            return "";
        }

    }

    public String checkBox(View view, CheckBox check, String total){
        CheckBox unreliableBox = (CheckBox) findViewById(R.id.unreliable);
        if (check.isChecked()){
            int checkID = check.getId();
            String checkedBox = check.getText().toString(); //Box checked
            if (total.equals("")){
                if (checkID == unreliableBox.getId()){
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

    @Override
    public void onBackPressed() {
        startActivityForResult(new Intent(ScoutActivity.this,ExitPrompt.class), 6);
    }
}