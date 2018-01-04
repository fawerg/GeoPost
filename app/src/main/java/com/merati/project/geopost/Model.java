package com.merati.project.geopost;

import android.app.VoiceInteractor;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
    private int LIMIT = 1000;
    private List<String> users = new ArrayList<>();
    private List<Friend> friends = new ArrayList<>();
    private Context currentContext = null;
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

        Log.d("Model: ", "in fetchUser()");
        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/users?session_id="+session_id+"&usernamestart=&limit="+1000;
        final List<String> usernames = new ArrayList<>(LIMIT);
        RequestQueue queue = Volley.newRequestQueue(currentContext);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    users.clear();
                    JSONArray jarray = response.getJSONArray("usernames");
                    Log.d("Model: ", "try");
                    for (int i = 0; i < jarray.length(); i++){
                        users.add(jarray.getString(i));
                        //Log.d("user "+i, response.getJSONArray("usernames").getString(i));
                    }
                } catch (Exception e) {
                    Log.d("Model: ", "Exception in fetchUsers()");
                    e.printStackTrace();
                }
                Log.d("Model: users number ", ""+users.size());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Model : ", "Volley Error in fetcUsers()");
            }
        });
        queue.add(request);
    }

    public List<String> getUsers(){
        return users;
    }

    public void fetchFriends(){
        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/followed?session_id="+session_id;
        RequestQueue queue = Volley.newRequestQueue(currentContext);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    friends.clear();
                    JSONArray Jfriends = response.getJSONArray("followed");
                    for (int i = 0; i < Jfriends.length(); i++){
                        String name = Jfriends.getJSONObject(i).getString("username");
                        String msg = Jfriends.getJSONObject(i).getString("msg");
                        Double lat, lon;
                        if(Jfriends.getJSONObject(i).getString("lat")!=null){
                            lat = Jfriends.getJSONObject(i).getDouble("lat");
                            lon = Jfriends.getJSONObject(i).getDouble("lon");
                        }
                        else{
                            lat=0.0;
                            lon=0.0;
                        }
                        friends.add(new Friend(name, msg,lat ,lon ));
                        Log.d("Model: ", "friends cardinality "+friends.size());
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
        queue.add(request);
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
}
