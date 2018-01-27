package com.merati.project.geopost;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class History extends AppCompatActivity {
    Model myModel;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myModel = Model.getInstance();
        overridePendingTransition(0,0);
        username = getIntent().getStringExtra("user");
        Log.d("Username", username);
        setTitle("History");
        ((TextView)findViewById(R.id.user)).setText(username);
        fetchHistory(this);
    }

    public void fetchHistory(final Context mContext){
        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/history?session_id="+myModel.getSession()+"&username="+username;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("History:" ," Invoking fetchHistory method");
                    myModel.deserializeHistory(response);
                    ListView history_list = findViewById(R.id.history_list);
                    HistoryAdapter myAdapter = new HistoryAdapter(mContext, android.R.layout.list_content, myModel.getHistory());
                    history_list.setAdapter(myAdapter);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(findViewById(R.id.history), new String(error.networkResponse.data), Snackbar.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonRequest);
    }

}
