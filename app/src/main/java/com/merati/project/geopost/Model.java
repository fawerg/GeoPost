package com.merati.project.geopost;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

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
}
