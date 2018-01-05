package com.merati.project.geopost;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FollowedFriends extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Model myModel = Model.getInstance();
    ActionBarDrawerToggle mDrawerToggle;
    private List<Friend> friends = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followed_friends);

        DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d("onDrawerClosed", "onDrawerClosed: " + getTitle());

                invalidateOptionsMenu();
            }
        };

        fetchFriends(this);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationViewListner();
    }

    protected void onResume(){
        super.onResume();
        fetchFriends(this);

    }
    private void setNavigationViewListner() {
        NavigationView navigationView = (NavigationView)findViewById(R.id.menulaterale);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
    @Override

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        DrawerLayout mDrawerLayout;
        switch (item.getItemId()){
            case R.id.profile:
                intent = new Intent(this, Profile.class);
                startActivity(intent);
                break;
            case R.id.add_friend:
                intent = new Intent(this, AddFriend.class);
                startActivity(intent);
                break;

            case R.id.status_update:
                intent = new Intent(this, UpdateStatus.class);
                startActivity(intent);
                break;
        }
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }

    private void fetchFriends(final Context mContext){
        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/followed?session_id="+myModel.getSession();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    myModel.clearFriends();
                    JSONArray Jfriends = response.getJSONArray("followed");
                    for (int i = 0; i < Jfriends.length(); i++){
                        String name = Jfriends.getJSONObject(i).getString("username");
                        String msg = Jfriends.getJSONObject(i).getString("msg");
                        Double lat, lon;
                        if(Jfriends.getJSONObject(i).getString("lat")!=null){
                            lat = Jfriends.getJSONObject(i).getDouble("lat");
                            lon = Jfriends.getJSONObject(i).getDouble("lon");
                        }
                        else{
                            lat=0.0;
                            lon=0.0;
                        }
                        myModel.addFriend(new Friend(name, msg,lat ,lon ));
                        Log.d("Model: ", "friends cardinality "+friends.size());
                        ListView friends_list = findViewById(R.id.friends_list);
                        FriendsAdapter myAdapter = new FriendsAdapter(mContext, android.R.layout.list_content, myModel.getFriends());
                        friends_list.setAdapter(myAdapter);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }
}

