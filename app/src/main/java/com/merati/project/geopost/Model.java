package com.merati.project.geopost;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fawerg on 1/3/18.
 */

public class Model {
    private static final Model ourInstance = new Model();
    private String session_id;
    private int LIMIT = 10;
    private List<String> users = new ArrayList<>();
    private List<Friend> friends = new ArrayList<>();

    public static Model getInstance() {
        return ourInstance;
    }

    private Model() {
    }

    public void setSession(String session_id){
        this.session_id= session_id;
    }

    public String getSession(){
        return session_id;
    }

    public void fetchUsers(){
        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/users?session_id="+session_id+"&usernamestart=&limit="+10000;
        final List<String> usernames = new ArrayList<>(LIMIT);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    for (int i = 0; i < LIMIT; i++){
                        usernames.add(response.getJSONArray("usernames").getString(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    public List<String> getUsers(){
        return users;
    }

    public void fetchFriends(){
        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/followed?session_id="+session_id;

        /*JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray Jfriends = response.getJSONArray("followed");
                    for (int i = 0; i < Jfriends.length(); i++){
                        friends.add(new Friend(Jfriends.getJSONObject(i).getString("username"), Jfriends.getJSONObject(i).getString("msg"), Jfriends.getJSONObject(i).getDouble("lat"), Jfriends.getJSONObject(i).getDouble("lon")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });*/
        friends.add(new Friend("gabri", "ciao mera", 12.5, 5.0));
        friends.add(new Friend("corra", "ciao merino", 14.5, 6.0));
    }

    public List<Friend> getFriends(){
        return friends;
    }
}
