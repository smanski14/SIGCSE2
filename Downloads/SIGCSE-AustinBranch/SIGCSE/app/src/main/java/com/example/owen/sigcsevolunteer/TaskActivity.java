package com.example.owen.sigcsevolunteer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;




/** This class provides the main Task display page of the application.
 * Information is displayed through a list view (see XML for details). Information is obtained through a php file whose location
 * is specified in Config (along with quite a number of variable names.)
 * @author Owen Galvin, 10/19/2015
 * @modified Will Lewis 10/25/2015
 * @modified Sarah Manski for 11/23/15
 *
 */
public class TaskActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView listView;
    private String JSON_STRING;
    public static String student_id;
    private String activity_id;
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<HashMap<String,String>> listForView = new ArrayList<HashMap<String, String>>();
    private ArrayList<String> dateTimeList = new ArrayList<String>();
    private PendingIntent pendingIntent;

    //Basic initialization, also sets a listener which we use to start ViewActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        listView = (ListView) findViewById(R.id.listView);
        Intent intent = getIntent();
        student_id=intent.getStringExtra(Config.ST_ID);
        getActivityIDs();


        listView.setOnItemClickListener(this);
    }

    //populates listView from information retrieved from getJSON and showActivity
    private void getActivityIDBackground(String s)
    {


        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String activity_id = jo.getString(Config.TAG_ACTIVITY_ID);
                list.add(activity_id);
                System.out.println(activity_id);
            }

        }

         catch (JSONException e) {
            e.printStackTrace();
        }

        for(int j=0; j<list.size();j++){
            HashMap<String, String> wait = getJSON(list.get(j));
            listForView.add(wait);
        }
        System.out.println("listForView: "+listForView.size());
        ListAdapter adapter = new SimpleAdapter(
                TaskActivity.this, listForView, R.layout.list_item,
                new String[]{Config.TAG_ACTIVITY_NAME,Config.TAG_ACTIVITY_START_TIME},
                new int[]{R.id.name, R.id.time});

        listView.setAdapter(adapter);
    }

    //retrieves activityIDs from the database based on student_id and then calls getActivityIDsBackground
    private void getActivityIDs()
    {
        class GetActivityIDs extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TaskActivity.this, "Fetching Data...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                getActivityIDBackground(s);



                int listLength = dateTimeList.size();
                ArrayList<Intent> intents = new ArrayList<Intent>();
                for(int i = 0; i < listLength; i++) {

                    Calendar currentTime = Calendar.getInstance();
                    Calendar calendar = Calendar.getInstance();

                    String datetime = dateTimeList.get(i);
                    System.out.println(datetime);
                    //splits datetime into date and time
                    String[] partA = datetime.split(" ");
                    //partA1 is date, partA2 is time
                    String partA1 = partA[0];
                    String partA2 = partA[1];

                    //partB is the time - Hours/minutes, seconds does not get used, since it's bullshit
                    String[] partB = partA2.split(":");
                    //partB1 is hour, partB2 is minute, partB3 is second
                    String partB1 = partB[0];
                    String partB2 = partB[1];
                    String partB3 = partB[2];
                    int hour = Integer.parseInt(partB1);
                    int minutes = Integer.parseInt(partB2);

                    //partC is the date split
                    String[] partC = partA1.split("-");
                    //partC1 is year, partC2 is month, partC3 is day
                    String partC1 = partC[0];
                    String partC2 = partC[1];
                    String partC3 = partC[2];
                    int year = Integer.parseInt(partC1);
                    int month = Integer.parseInt(partC2);
                    int day = Integer.parseInt(partC3);

                    //set notification for date --> day month year hour:minute:second
                    calendar.set(Calendar.MONTH, month -1);
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.DAY_OF_MONTH, day);

                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minutes);
                    calendar.set(Calendar.SECOND, 0);
                    //calendar.set(Calendar.AM_PM, Calendar.PM);

                    int comparison = currentTime.compareTo(calendar);
                    if (comparison < 0){
                        System.out.println("here");
                        Intent myIntent = new Intent(TaskActivity.this, MyReceiver.class);
                        myIntent.putExtra(Config.ST_ID, TaskActivity.student_id);
                        intents.add(myIntent);

                        pendingIntent = PendingIntent.getBroadcast(TaskActivity.this, 0, intents.get(intents.size()-1), 0);

                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
                    }

                }

            }

            @Override
            protected String doInBackground(Void... params) {

                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_ACTID, student_id);
                System.out.println(s);

                return s;
            }
        }
        GetActivityIDs gaid = new GetActivityIDs();
        try {
            gaid.execute().get(1000, TimeUnit.MILLISECONDS);
        }
        catch(Exception e)
        {}

    }
    //creats and returns a HashMap for the returned information from GetJSON for use in the listView
    private HashMap<String, String> showActivity(String s){
        JSONObject jsonObject;
        HashMap<String,String> people = new HashMap<String,String>();
        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);


                JSONObject jo = result.getJSONObject(0);
                String name = jo.getString(Config.TAG_ACTIVITY_NAME);
                String time = jo.getString(Config.TAG_ACTIVITY_START_TIME);
                dateTimeList.add(time);
            System.out.println(name);
            System.out.println(time);



                people.put(Config.TAG_ACTIVITY_NAME,name);
                people.put(Config.TAG_ACTIVITY_START_TIME,time);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return people;
    }

    //retrieves activity information from database including activity_name, room_location, start_time, end_time, and instruction
    private HashMap<String, String> getJSON(String ID){
        final String id = ID;

        class GetJSON extends AsyncTask<Void,Void,String>{
            HashMap<String, String> result = new HashMap<String, String>();

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TaskActivity.this,"Fetching Data","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;

            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_ACT, id);
                System.out.println(s);
                result=showActivity(s);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        try{
        gj.execute().get(2000, TimeUnit.MILLISECONDS);}
        catch(Exception e){}
        return gj.result;
    }

    @Override
    //goes to ViewActivity when an item in the listView is clicked
    //Note that activity_id is sent in an extra, this is so that ViewActivity may use said id for data retrivial. 
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ViewActivity.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String activityId = list.get(position);
        intent.putExtra("map", map);
        intent.putExtra(Config.ACTIVITY_ID, activityId);
        startActivity(intent);
    }

}
