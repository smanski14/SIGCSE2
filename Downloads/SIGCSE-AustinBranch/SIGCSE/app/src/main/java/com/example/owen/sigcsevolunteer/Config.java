package com.example.owen.sigcsevolunteer;

/**
 * Created by Sarah for 11/23/15.
 * File Description: Contains final values for configuration of SIGCSE app
 */
public class Config {

    //Address of our scripts for getting StudentID, Activity IDs, and Activity information
    public static final String myIP = "146.113.79.247";
    public static final String URL_GET_ACTID = "http://"+myIP+":8888/getActivityIDs.php?student_id=";
    public static final String URL_GET_ST = "http://"+myIP+":8888/getStudentID.php?email=";
    public static final String URL_GET_ACT = "http://"+myIP+":8888/getActivities.php?activity_id=";


    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "student_id";
    public static final String TAG_NAME = "preferred_name";
    public static final String TAG_ACTIVITY_ID = "activity_id";
    public static final String TAG_ACTIVITY_NAME = "activity_name";
    public static final String TAG_ACTIVITY_START_TIME = "start_time";
    public static final String TAG_ACTIVITY_END_TIME = "end_time";
    public static final String TAG_ROOM = "room_location";
    public static final String TAG_INSTRUCTIONS = "instruction";

    //student ids and name to pass with intent
    public static final String ST_ID = "student_id";
    public static final String ST_NAME = "preferred_name";
    public static final String ACTIVITY_ID = "activity_id";
}