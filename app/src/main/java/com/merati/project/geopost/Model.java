package com.merati.project.geopost;

import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by fawerg on 1/3/18.
 */

public class Model {
    private static final Model ourInstance = new Model();
    private String session_id;
    private List<String> users = new ArrayList<>();
    private ArrayList<Friend> friends = new ArrayList<>();
    public static Model getInstance() {
        return ourInstance;
    }
    private Friend profile = null;
    private Model() {
    }

    public void setSession(String session_id){
        this.session_id= session_id;
    }

    public String getSession(){
        return session_id;
    }

    public List<String> getUsers(){
        return users;
    }

    public List<Friend> getFriends(){
        return friends;
    }

    public void clearUsers(){
        users.clear();
    }

    public void addUser(String user){
        users.add(user);
    }

    public void addFriend(Friend friend){
        if (!friend.getLast_status().equals("null")){
            friends.add(friend);
        }
        else{
            friends.add(new Friend(friend.getName(), "", friend.getLastPosition().latitude, friend.getLastPosition().longitude, friend.getDistance()));
        }

    }

    public void clearFriends(){
        friends.clear();
    }

    public void setProfile(Friend profile){
        if(!profile.getLast_status().equals("null")) {
            this.profile = profile;
        }
        else
            this.profile=new Friend(profile.getName(), "", profile.getLastPosition().latitude, profile.getLastPosition().longitude, profile.getDistance());
    }

    public Friend getProfile(){
        return profile;
    }

    public void sortFriends(){
        Collections.sort(friends);
    }

    public void deserializeProfile(JSONObject response){
        try {
            String name = response.getString("username");
            String msg = response.getString("msg");
            Double lat;
            Double lon;
            if (response.getString("lat") != null) {
                lat = response.getDouble("lat");
                lon = response.getDouble("lon");
            } else {
                lat = 0.0;
                lon = 0.0;
            }
            setProfile(new Friend(name, msg, lat, lon, 0));
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void deserializeFriends(JSONObject response, Location myLocation){
        Location mLocation = new Location("Me");
        Location hLocation = new Location("He");
        mLocation.setLatitude(profile.getLastPosition().latitude);
        mLocation.setLongitude(profile.getLastPosition().longitude);
        Float distance;
        clearFriends();
        try {
            JSONArray Jfriends = response.getJSONArray("followed");
            for (int i = 0; i < Jfriends.length(); i++) {
                String name = Jfriends.getJSONObject(i).getString("username");
                String msg = Jfriends.getJSONObject(i).getString("msg");
                Double lat, lon;
                if (!Jfriends.getJSONObject(i).getString("lat").equals("null")) {
                    lat = Jfriends.getJSONObject(i).getDouble("lat");
                    lon = Jfriends.getJSONObject(i).getDouble("lon");
                    hLocation.setLatitude(lat);
                    hLocation.setLongitude(lon);
                    DecimalFormat df = new DecimalFormat("#.##");
                    if (myLocation == null)
                        distance = hLocation.distanceTo(mLocation) / 1000;
                    else
                        distance = myLocation.distanceTo(hLocation) / 1000;
                    Log.d("Friend" + i, ": " + distance);
                    addFriend(new Friend(name, msg, lat, lon, Float.parseFloat(df.format(distance))));
                }
            }
            sortFriends();
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }
}
