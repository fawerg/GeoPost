package com.merati.project.geopost;

import android.app.IntentService;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class LoginActivity extends AppCompatActivity {
    public static boolean logged = false;

    protected String sessionId = null;
    protected EditText username_field;
    protected EditText password_field;
    boolean connectionError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username_field= (EditText)findViewById(R.id.username);
        password_field= (EditText)findViewById(R.id.password);

    }

    protected void onClickLogin(View view){
        String username, password;
        username = username_field.getText().toString();
        password = password_field.getText().toString();
        sessionId = loginRequest("username="+username+"&password="+password);
    }

    protected String loginRequest(String credentials){

        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/login";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        sessionId=response;
                        startBrowsing();
                    }},
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        connectionError=true;
                        sessionId= null;
                    }
                });
        queue.add(stringRequest);
        return sessionId;
    }

    public void startBrowsing(){
        if (sessionId!= null){
            logged= true;
            Intent intent = new Intent(this, ActivityFollowed.class);
            intent.putExtra("session", sessionId);
            startActivity(intent);
        }
    }
}
