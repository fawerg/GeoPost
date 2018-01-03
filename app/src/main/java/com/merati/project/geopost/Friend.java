package com.merati.project.geopost;


import com.google.android.gms.maps.model.LatLng;

public class Friend {
    private String name;
    private String last_status;
    private LatLng last_position;

    public Friend(String name, String status, double lat, double lon){
        this.name = name;
        this.last_status= status;
        this.last_position = new LatLng(lat, lon);
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

}
