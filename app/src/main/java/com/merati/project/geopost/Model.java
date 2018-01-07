package com.merati.project.geopost;

import com.google.android.gms.maps.model.LatLng;

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
        friends.add(friend);
    }

    public void clearFriends(){
        friends.clear();
    }

    public void setProfile(Friend profile){
        this.profile=profile;
    }

    public Friend getProfile(){
        return profile;
    }

    public void sortFriends(){
        Collections.sort(friends);
    }
}
