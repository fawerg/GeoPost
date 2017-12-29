package com.merati.project.geopost;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentProfile.OnFragmentInteractionListener, OnMapReadyCallback{
    public static String session_id;
    Fragment currentFragment;
    ActionBarDrawerToggle mDrawerToggle;
    LatLng last_location=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        session_id = intent.getStringExtra("session");

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
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getFragmentManager();
        currentFragment = new FragmentFollowed();

        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.view_group_id, currentFragment);
        ft.commit();

        setNavigationViewListner();
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
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        DrawerLayout mDrawerLayout;
        switch (item.getItemId()){
            case R.id.profile:
                Bundle bundle = new Bundle();
                bundle.putString("session", session_id);
                currentFragment = new FragmentProfile();

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
                currentFragment.setArguments(bundle);
                show_name();
                break;
            case R.id.add_friend:
                currentFragment = new FragmentAddFriend();
                break;
            case R.id.followed:
                currentFragment = new FragmentFollowed();
                break;
            case R.id.status_update:
                currentFragment = new FragmentStatusUpdate();
                break;
        }
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        ft.replace(R.id.view_group_id, currentFragment);
        ft.addToBackStack(null);
        ft.commit();
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void logout(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void onLogout(View view){
        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/logout?session_id="+session_id;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        logout();
                    }},
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){

                    }
                });
        queue.add(stringRequest);
    }

    public void show_name(){
        Log.d("session_id", session_id);
        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/profile?session_id="+session_id;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ((TextView) findViewById(R.id.profile_name)).setText(response.getString("username"));
                    ((TextView) findViewById(R.id.profile_status)).setText(""+response.getString("msg"));
                    setLastLocation(new LatLng(response.getDouble("lat"), response.getDouble("lon")));
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonRequest);
    }

    public void setLastLocation(LatLng location){
        last_location=location;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("Main Activity", "Map is ready");
        LatLng test = last_location;
        googleMap.addMarker(new MarkerOptions().position(test).title("sidney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(test));
    }
}

