package com.merati.project.geopost;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fawerg on 1/3/18.
 */

public class Model {
    private static final Model ourInstance = new Model();
    private String session_id;
    private List<String> users = new ArrayList<>();
    private List<Friend> friends = new ArrayList<>();
    private Context currentContext = null;
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

    public void setContext(Context context){
        currentContext = context;
    }

    public void follow(String user){

        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/follow?session_id="+session_id+"&username="+user;
        RequestQueue queue = Volley.newRequestQueue(currentContext);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                 Log.d("Model : ", "AddFriend response "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    public void updateStatus(String msg, Location location){
        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/status_update?session_id="+session_id+"&message="+msg+"&lat="+location.getLatitude()+"&lon="+location.getLongitude();
        RequestQueue quesue = Volley.newRequestQueue(currentContext);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Model: ", "updateStatus response");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Model: ", "updateStatus error");
            }
        });
        quesue.add(stringRequest);
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
}
