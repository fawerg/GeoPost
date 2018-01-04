package com.merati.project.geopost;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddFriend extends AppCompatActivity{
    Model myModel = Model.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Log.d("session_id ", myModel.getSession());
        myModel.setContext(this);
        myModel.fetchUsers();
        List<String> usernames = myModel.getUsers();
        Log.d("AddFriend: ", "username size "+usernames.size());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, usernames);
        AutoCompleteTextView autoCompleteView = (AutoCompleteTextView) findViewById(R.id.username_add_friend);

        autoCompleteView.setAdapter(adapter);

    }

    public void addFriend(View view){
        TextView username = findViewById(R.id.username_add_friend);
        myModel.follow(username.getText().toString());
    }
}
