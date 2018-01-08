package com.team20.pitscouting;

import android.os.Environment;
import android.os.SystemClock;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by JT on 2/24/17.
 */
public class Data{
    private static ArrayList <String> short_data = new ArrayList<String>(); //Data without comments, names, etc
    private static ArrayList <String> full_data = new ArrayList<String>(); //All data
    private static ArrayList <String> teams_list = new ArrayList<String>();
    private static ArrayList <String> teams_done = new ArrayList<String>();
    private String shortFile = "DATA.txt"; //CHANGE TEXT FILE NAMES HERE
    private String fullFile = "FULL_DATA.txt";
    private String shortBackup = "BACKUP_DATA.txt";
    private String fullBackup = "FULL_BACKUP_DATA.txt";
    private String teams = "TEAMS.txt";
    private File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "PitScouting/");

    public Data (){
        if(!dir.exists()){
            dir.mkdir();
        }
    }

    public void save(String shortOutput, String fullOutput){
        File mainFile = new File(dir, shortFile); //NUMBER DATA TEXT FILE
        File completeFile = new File(dir, fullFile); //COMPLETE DATA TEXT FILE
        File backupFile = new File(dir, shortBackup); //NUMBER BACK UP DATA TEXT FILE
        File completeBackupFile = new File(dir, fullBackup); //COMPLETE BACK UP DATA TEXT FILE


        /*
        IF ARRAY LIST IS EMPTY, CHECK IF THERE IS PREVIOUS DATA,
        EX: IF KID RESTARTED APP BY ACCIDENT, DON'T WANT TO ERASE ALL DATA
         */
        if (short_data.size() == 0){
            if(mainFile.exists()){ //Load short data from main
                loadContent(shortFile, short_data);
            } else if (backupFile.exists()){ //Load short data from backup
                loadContent(shortBackup, short_data);
            }
        }
        if (full_data.size() == 0){
            if(mainFile.exists()){ //Load all data from main
                loadContent(fullFile, full_data);
            } else if (backupFile.exists()){ //Load all data from backup
                loadContent(fullBackup, full_data);
            }
        }

        //Update finished teams list
        int end = shortOutput.indexOf("\t"); //Get team from output string
        String team = shortOutput.substring(0,end);
        teams_done.add(team);

        //Add data to array
        short_data.add(shortOutput);
        full_data.add(fullOutput);

        //Write out to array
        write(mainFile, short_data);
        write(backupFile, short_data);
        write(completeFile, full_data);
        write(completeBackupFile, full_data);
    }

    public void loadContent(String name, ArrayList list){
        FileInputStream is;
        BufferedReader reader;
        File file = new File(dir, name);
        try {
            is = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            while(line != null){ //Reading in data line by line
                list.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(File file, ArrayList list){
        try {
            FileWriter fw = new FileWriter(file);
            PrintWriter pw = new PrintWriter(fw);
            for (int i = 0; i < list.size(); i++){
                pw.println(list.get(i));
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList readTeams(){ //Called when app is first loaded
        FileInputStream is;
        BufferedReader reader;
        File file = new File(dir, teams);
        System.err.println(file);
        try {
            System.err.println("READING TEAMS");
            is = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            while(line != null){ //Separate data line by line (Each team in TEAMS.txt should be on a separate line)
                teams_list.add(line);
                line = reader.readLine();
            }
            System.err.println("DONE");
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadContent(shortFile, short_data);
        return teams_list;
    }

    public ArrayList getDone(){
        for (int i = 0; i < short_data.size(); i++){
            int end = short_data.get(i).indexOf("\t"); //Get teams from output string
            String team = short_data.get(i).substring(0,end);
            teams_done.add(team);
        }
        return teams_done;
    }
}

