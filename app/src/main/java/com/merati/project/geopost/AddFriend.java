package com.merati.project.geopost;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddFriend extends AppCompatActivity{
    Model myModel = Model.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.d("session_id ", myModel.getSession());
        fetchUsers(this);
    }

    public void addFriend(View view){
        TextView username = findViewById(R.id.username_add_friend);
        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/follow?session_id="+myModel.getSession()+"&username="+username;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AddFriend : ", "AddFriend response "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    private void fetchUsers(final Context mContext){
        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/users?session_id="+myModel.getSession()+"&usernamestart=&limit="+1000;

        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    myModel.clearUsers();
                    JSONArray jarray = response.getJSONArray("usernames");

                    for (int i = 0; i < jarray.length(); i++){
                        myModel.addUser(jarray.getString(i));
                        //Log.d("user "+i, response.getJSONArray("usernames").getString(i));
                    }
                } catch (Exception e) {
                    Log.d("AddFriend: ", "Exception in fetchUsers()");
                    e.printStackTrace();
                }
                Log.d("AddFriend: users number ", ""+myModel.getUsers().size());

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, myModel.getUsers());
                AutoCompleteTextView autoCompleteView = (AutoCompleteTextView) findViewById(R.id.username_add_friend);
                autoCompleteView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("AddFriend : ", "Volley Error in fetcUsers()");
            }
        });
        queue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
