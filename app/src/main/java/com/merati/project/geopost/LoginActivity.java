package com.merati.project.geopost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Model myModel = Model.getInstance();
    private String sessionId = null;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences("PREF", 0);
        sessionId = settings.getString("SESSION_ID", null);
        if(sessionId!=null){
            startBrowsing();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClickLogin(View view){
        final String username, password;
        username = ((EditText)findViewById(R.id.username)).getText().toString();
        password = ((EditText)findViewById(R.id.password)).getText().toString();
        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/login";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        sessionId=response;
                        editor= settings.edit();
                        editor.putString("SESSION_ID", sessionId);
                        editor.commit();
                        startBrowsing();
                    }},
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        sessionId= null;
                        Snackbar.make(findViewById(R.id.login_layout), "LOGIN ERROR", Snackbar.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }};
        queue.add(stringRequest);
    }

    public void startBrowsing(){
        if (sessionId!= null){
            Intent intent = new Intent(this, FollowedFriends.class);
            intent.putExtra("refresh", true);
            myModel.setSession(sessionId);
            startActivity(intent);
        }
    }
}
