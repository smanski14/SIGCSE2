package com.example.owen.sigcsevolunteer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by William on 11/19/2015.
 * Edited by Sarah for 11/23/15
 * File Description: Displays activity information based on a given activity_id
 */

public class ViewActivity extends AppCompatActivity {
    //Variables to save important information in.
    private TextView editTextName;
    private TextView editTextStartTime;
    private TextView editTextEndTime;
    private TextView editTextRoom;
    private TextView editTextInstr;

    private String id;

    //Simple onCreate, see XML file for details.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_activity);

        Intent intent = getIntent();

        id = intent.getStringExtra(Config.ACTIVITY_ID);

        editTextName = (TextView) findViewById(R.id.editTextName);
        editTextStartTime = (TextView) findViewById(R.id.editTextStartTime);
        editTextEndTime = (TextView) findViewById(R.id.editTextEndTime);
        editTextRoom = (TextView) findViewById(R.id.editTextRoom);
        editTextInstr = (TextView) findViewById(R.id.editTextInstr);


        getPerson();
    }

    //gets activity information based on activity_id
    private void getPerson() {
        class GetPerson extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewActivity.this, "Fetching...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showPerson(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_ACT, id);
                return s;
            }
        }
        GetPerson ge = new GetPerson();
        ge.execute();
    }

    //formats information from getPerson into JSON and displays in textViews
    private void showPerson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String activity = c.getString(Config.TAG_ACTIVITY_NAME);
            String startTime = c.getString(Config.TAG_ACTIVITY_START_TIME);
            String endTime = c.getString(Config.TAG_ACTIVITY_END_TIME);
            String room = c.getString(Config.TAG_ROOM);
            String instr = c.getString(Config.TAG_INSTRUCTIONS);

            editTextName.setText(activity);
            editTextRoom.setText(room);
            editTextInstr.setText(instr);
            editTextStartTime.setText(startTime);
            editTextEndTime.setText(endTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
