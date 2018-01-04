package com.merati.project.geopost;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.LocationSource;

public class UpdateStatus extends AppCompatActivity{
    Model myModel = Model.getInstance();
    String status = null;
    Location locationUpdate = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_status);

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationUpdate = location;
                Log.d("UpdateStatus: ", "onLocationChanged");
                if(status!= null){
                    myModel.updateStatus(status, locationUpdate);
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        try {
            Log.d("UpdateStatus: ", "requesting location updates");
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
        catch(SecurityException se){

        }
    }

    public void updateStatus(View view){

        String msg = ((TextView)findViewById(R.id.status)).getText().toString();
        if(locationUpdate != null) {
            Log.d("UpdateStatus: ", "updateStatus is calling the model");
            myModel.setContext(this);
            myModel.updateStatus(msg, locationUpdate);
        }
        else{
            status=msg;
        }
        Log.d("UpdateStatus" , "Button update clicked; msg: "+status+" location: "+ locationUpdate);
    }
}
