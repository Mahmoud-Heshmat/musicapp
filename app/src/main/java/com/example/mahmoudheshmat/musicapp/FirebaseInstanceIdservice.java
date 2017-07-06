package com.example.mahmoudheshmat.musicapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

public class FirebaseInstanceIdservice  extends FirebaseInstanceIdService {


    SharedPreferences sharedPreferences;
    String user_id;

    @Override
    public void onTokenRefresh() {
        sharedPreferences = getSharedPreferences(Constant.PREFERENCES_userData, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("User_id", null);

        String tokenRefresh = FirebaseInstanceId.getInstance().getToken();
        if(user_id != null){
            Refresh(tokenRefresh);
        }
    }

    private void Refresh(final String tokenRefresh) {
        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseUrl.url_onTokenRefresh, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Token response", response);
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("response", error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("newToken", tokenRefresh);
                return params;
            }
        };
        Singleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);

    }


}
