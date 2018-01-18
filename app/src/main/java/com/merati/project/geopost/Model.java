package com.merati.project.geopost;

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
}
