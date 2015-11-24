package com.example.owen.sigcsevolunteer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Owen on 10/19/15.
 * Edited by Sarah for 11/23/15
 * File Description: Authenticates user information through signIn by accessing student table in SIGCSE database.
 */
public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    //Defining views
    private EditText editTextEmail;
    private EditText editTextPassword;
    public String email;
    public String password;

    private Button buttonSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Initializing views and button
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.signIn);

        //Setting listeners to button
        buttonSignIn.setOnClickListener(this);
    }

    @Override
    //OnClick for signIn button that validates user information and sends to main activity
    public void onClick(View v) {
        if(v == buttonSignIn) {
            email = editTextEmail.getText().toString().trim();
            password = editTextPassword.getText().toString().trim();
            signIn();

        }
    }

    //Parses information retrieved from database and authenticates signIn
    private void signInBackground(String s)
    {
        try {
            //parses JSON information retrieved from doInBackground
            JSONObject jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String student_id = c.getString(Config.TAG_ID);

            //If username or password are invalid, informs the user
            if(student_id.equals("error")){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Invalid Email or Password");

                alertDialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){};});

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            //if username and password are valid, starts MainActivity and passes the student_id and preferred_name retrieved from signIn AsyncTask
            else{
                String preferred_name = c.getString(Config.TAG_NAME);
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(Config.ST_ID, student_id);
                intent.putExtra(Config.ST_NAME, preferred_name);
                startActivity(intent);}

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //runs through signIn
    private void signIn()
    {
        class SignIn extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SignInActivity.this, "Signing In...", "Wait...", false, false);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                signInBackground(s);
            }

            @Override
            protected String doInBackground(Void... params) {

                //retrieves student_id and preferred_name from database using given email and password
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_ST, email, password);
                System.out.println(s);
                return s;
            }
        }
        SignIn si = new SignIn();
        si.execute();

    }
}
