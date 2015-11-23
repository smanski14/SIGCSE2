package com.example.owen.sigcsevolunteer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView helloWorld;
    private String student_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        //retrieves student_id passed from Signin
        student_id= intent.getStringExtra(Config.ST_ID);

        //displays user's preferred name upon entering
        helloWorld = (TextView) findViewById(R.id.textView);
        helloWorld.setText("Welcome "+intent.getStringExtra(Config.ST_NAME));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //Creates an intent and starts the TaskActivity activity
    public void goTasks(View view){
        Intent intent = new Intent(this, TaskActivity.class);
        //passes student_id to taskActivity
        intent.putExtra(Config.ST_ID,student_id);
        startActivity(intent);
    }

    //Creates an intent and starts the OptionsActivity activity
    public void goOptions(View view){
        Intent intent = new Intent(this, OptionActivity.class);
        startActivity(intent);
    }

    //Creates an intent and starts the SignInActivity activity
    public void goSignIn(View view){
        Intent intent = new Intent(this, SignInActivity.class);

        startActivity(intent);
    }
}
