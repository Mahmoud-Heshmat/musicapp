package com.example.mahmoudheshmat.musicapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Singleton {
    private static Singleton minstance;
    private RequestQueue requestQueue;
    private static Context context;

    private Singleton(Context context){
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized Singleton getInstance(Context context){
        if(minstance == null){
            minstance = new Singleton(context);
        }
        return minstance;
    }

    public <T> void addToRequestQueue(Request<T> request){
        requestQueue.add(request);
    }
}
