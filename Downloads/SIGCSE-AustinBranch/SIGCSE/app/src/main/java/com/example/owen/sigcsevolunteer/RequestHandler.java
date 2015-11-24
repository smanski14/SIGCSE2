package com.example.owen.sigcsevolunteer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Belal on 10/24/2015.
 * Edited by Sarah for 11/23/15.
 * File Description: Methods for sending Get requests to a database via php files
 */

public class RequestHandler {



    //method for performing a GET with parameters attached
    public String sendGetRequestParam(String requestURL, String id){
        StringBuilder sb =new StringBuilder();
        try {
            URL url = new URL(requestURL+id);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String s;
            while((s=bufferedReader.readLine())!=null){
                sb.append(s+"\n");
            }
        }catch(Exception e){
        }
        return sb.toString();
    }

    //method for performing a GET with two parameters attached
    public String sendGetRequestParam(String requestURL, String email, String password){
        StringBuilder sb =new StringBuilder();
        try {
            //attaches email and password information to the URL
            URL url = new URL(requestURL+"\""+email+"\"&password=\""+password+"\"");
            System.out.println(url.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String s;
            while((s=bufferedReader.readLine())!=null){
                sb.append(s + "\n");
            }
            System.out.println(sb.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }


}
