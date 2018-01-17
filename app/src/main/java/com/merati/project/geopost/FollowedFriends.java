package com.merati.project.geopost;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class FollowedFriends extends AppCompatActivity implements com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener , OnMapReadyCallback{
    Model myModel = Model.getInstance();
    ActionBarDrawerToggle mDrawerToggle;
    GoogleMap mGoogleMap=null;
    MapFragment mapFragment;
    GoogleApiClient mGoogleApiClient = null;
    Location myLocation=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followed_friends);
        overridePendingTransition(0,0);
        Intent intent = getIntent();

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getProfileInfo(this);

        ((TabLayout)findViewById(R.id.tabLayout)).addOnTabSelectedListener(this);
        findViewById(R.id.map).setVisibility(View.INVISIBLE);
        findViewById(R.id.friends_list).setVisibility(View.VISIBLE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();

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
        setNavigationViewListner();


    }

    protected void onResume(){
        super.onResume();
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
                    Location mLocation = new Location("Me");
                    Location hLocation = new Location("He");
                    mLocation.setLatitude(myModel.getProfile().getLastPosition().latitude);
                    mLocation.setLongitude(myModel.getProfile().getLastPosition().longitude);
                    Float distance;
                    myModel.clearFriends();
                    JSONArray Jfriends = response.getJSONArray("followed");
                    for (int i = 0; i < Jfriends.length(); i++){
                        String name = Jfriends.getJSONObject(i).getString("username");
                        String msg = Jfriends.getJSONObject(i).getString("msg");
                        Double lat, lon;
                        if(!Jfriends.getJSONObject(i).getString("lat").equals("null")){
                            lat = Jfriends.getJSONObject(i).getDouble("lat");
                            lon = Jfriends.getJSONObject(i).getDouble("lon");
                        }
                        else{
                            lat=0.0;
                            lon=0.0;
                        }
                        hLocation.setLatitude(lat);
                        hLocation.setLongitude(lon);
                        DecimalFormat df = new DecimalFormat("#.##");
                        if(myLocation==null)
                            distance=hLocation.distanceTo(mLocation)/1000;
                        else
                            distance=myLocation.distanceTo(hLocation)/1000;
                        Log.d("Friend"+i, ": "+distance);
                        myModel.addFriend(new Friend(name, msg,lat ,lon, Float.parseFloat(df.format(distance))));
                    }
                    myModel.sortFriends();
                    ListView friends_list = findViewById(R.id.friends_list);
                    FriendsAdapter myAdapter = new FriendsAdapter(mContext, android.R.layout.list_content, myModel.getFriends());
                    friends_list.setAdapter(myAdapter);
                    showOnMap();

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

    public void getProfileInfo(final Context mContext){
        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/profile?session_id="+myModel.getSession();

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String name = response.getString("username");
                    String msg = response.getString("msg");
                    Double lat;
                    Double lon;
                    if(response.getString("lat")!=null){
                        lat = response.getDouble("lat");
                        lon = response.getDouble("lon");
                    }
                    else{
                        lat=0.0;
                        lon=0.0;
                    }
                    myModel.setProfile(new Friend(name,msg,lat,lon, 0));
                    Log.d("Followed:" ," Invoking fetchFriends method");
                    fetchFriends(mContext);
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
    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        if(tab.getText().toString().equals("Map")){
            Log.d("OnTabSelectd","Mappa visibile");
            findViewById(R.id.friends_list).setVisibility(View.INVISIBLE);
            findViewById(R.id.map).setVisibility(View.VISIBLE);
        }
        else{
            Log.d("OnTabSelectd","Lista visibile");
            findViewById(R.id.map).setVisibility(View.INVISIBLE);
            findViewById(R.id.friends_list).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
        if(myLocation!=null) {
            showOnMap();
        }
    }

    public void showOnMap(){
        Log.d("ShowOnMap:" , "Before of the if");
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        Marker marker=null;
        if(myModel.getFriends().size()!=0 && mGoogleMap!=null){
            for (int i =0; i< myModel.getFriends().size(); i++){
                marker = mGoogleMap.addMarker(new MarkerOptions().position(myModel.getFriends().get(i).getLastPosition()).title(myModel.getFriends().get(i).getName()+" : "+myModel.getFriends().get(i).getLast_status()));
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();
            Log.d("ShowOnMap:", "added elements");

            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 40));
            if(myModel.getFriends().size()==1)
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),10));

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("UpdateStatus", "checking permission...");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        myLocation=location;
        if(mGoogleMap!=null){
            fetchFriends(this);
        }
    }
}

