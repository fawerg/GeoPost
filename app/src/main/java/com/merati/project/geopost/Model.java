package com.merati.project.geopost;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by franc on 31/10/2017.
 */

public class Model {
    LoginActivity myLogin;
    public Model(LoginActivity myLogin){
        this.myLogin = myLogin;
    }

    protected String loginRequest(String credentials){
        String sessionId = null;
        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/login";

        RequestQueue queue = Volley.newRequestQueue(myLogin);
        StringRequest stringRequest = new StringRequest();


        return sessionId;
    }
}
