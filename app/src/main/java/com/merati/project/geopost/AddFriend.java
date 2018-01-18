package com.merati.project.geopost;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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


public class AddFriend extends AppCompatActivity implements TextWatcher{
    Model myModel = Model.getInstance();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        overridePendingTransition(0,0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.d("session_id ", myModel.getSession());

        AutoCompleteTextView search = findViewById(R.id.username_add_friend);
        search.setThreshold(0);
        search.addTextChangedListener(this);
    }

    public void addFriend(View view){
        String username = ((TextView)findViewById(R.id.username_add_friend)).getText().toString();
        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/follow?session_id="+myModel.getSession()+"&username="+username;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AddFriend : ", "AddFriend response "+response);
                Snackbar.make(findViewById(R.id.add_friend_layout), response, Snackbar.LENGTH_SHORT).show();
                ((TextView)findViewById(R.id.username_add_friend)).setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(findViewById(R.id.add_friend_layout), new String(error.networkResponse.data), Snackbar.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    @Override
    public void onResume(){
        super.onResume();
        String userStart = ((TextView)findViewById(R.id.username_add_friend)).getText().toString();
        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/users?session_id="+myModel.getSession()+"&usernamestart="+userStart+"&limit="+10;

        RequestQueue queue = Volley.newRequestQueue(this);
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

                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, myModel.getUsers());
                AutoCompleteTextView autoCompleteView = (AutoCompleteTextView) findViewById(R.id.username_add_friend);
                autoCompleteView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("AddFriend : ", "Volley Error in fetcUsers()");
            }
        });
        queue.add(request);
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, myModel.getUsers());
        AutoCompleteTextView autoCompleteView = (AutoCompleteTextView) findViewById(R.id.username_add_friend);
        autoCompleteView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(this, FollowedFriends.class);
                intent.putExtra("refresh", true);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String userStart = ((TextView)findViewById(R.id.username_add_friend)).getText().toString();
        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/users?session_id="+myModel.getSession()+"&usernamestart="+userStart+"&limit="+10;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    myModel.clearUsers();
                    JSONArray jarray = response.getJSONArray("usernames");

                    for (int i = 0; i < jarray.length(); i++){
                        myModel.addUser(jarray.getString(i));
                        Log.d("user "+i, response.getJSONArray("usernames").getString(i));
                    }
                } catch (Exception e) {
                    Log.d("AddFriend: ", "Exception in fetchUsers()");
                    e.printStackTrace();
                }
                Log.d("AddFriend: users number ", ""+myModel.getUsers().size());
                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, myModel.getUsers());
                AutoCompleteTextView autoCompleteView = (AutoCompleteTextView) findViewById(R.id.username_add_friend);
                autoCompleteView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
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
    public void afterTextChanged(Editable editable) {
    }
}
