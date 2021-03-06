package com.merati.project.geopost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;


public class Profile extends AppCompatActivity implements OnMapReadyCallback{
    Model myModel = Model.getInstance();
    GoogleMap mGoogleMap=null;
    MapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");
        overridePendingTransition(0,0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        showProfileInfo();
    }

    @Override
    public void onResume(){
        super.onResume();
        mapFragment.getMapAsync(this);
    }


    public void onLogout(View view){
        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/logout?session_id="+myModel.getSession();
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

    public void logout(){
        SharedPreferences settings= getSharedPreferences("PREF", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("SESSION_ID", null);
        editor.commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(myModel.getProfile()!=null && mGoogleMap!=null){
            mGoogleMap.addMarker(new MarkerOptions().position(myModel.getProfile().getLastPosition()).title("Me: "+myModel.getProfile().getLast_status()));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myModel.getProfile().getLastPosition(),10));
        }
        mGoogleMap=googleMap;
    }

    public void showProfileInfo(){
        ((TextView)findViewById(R.id.username)).setText(myModel.getProfile().getName());
        ((TextView)findViewById(R.id.status)).setText(myModel.getProfile().getLast_status());
        if(mGoogleMap!=null){
            mGoogleMap.addMarker(new MarkerOptions().position(myModel.getProfile().getLastPosition()).title("Me: "+myModel.getProfile().getLast_status()));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myModel.getProfile().getLastPosition(),10));
        }
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
