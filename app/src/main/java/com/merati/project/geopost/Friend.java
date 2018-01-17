package com.merati.project.geopost;



import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class Friend implements Comparable{
    private String name;
    private String last_status;
    private LatLng last_position;
    private float distance;
    public Friend(String name, String status, double lat, double lon, float distance){
        this.name = name;
        this.last_status= status;
        this.last_position = new LatLng(lat, lon);
        this.distance = distance;
    }

    public String getName(){
        return name;
    }

    public String getLast_status(){
        return last_status;
    }

    public LatLng getLastPosition(){
        return last_position;
    }

    public Float getDistance(){
        return distance;
    }
    @Override
    public int compareTo(@NonNull Object o) {
        if(this.distance > ((Friend)o).distance){
            return 1;
        }else if(this.distance == ((Friend)o).distance){
            return 0;
        }
        else
           return -1;
    }
}
