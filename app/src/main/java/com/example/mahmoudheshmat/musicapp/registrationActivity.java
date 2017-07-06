package com.example.mahmoudheshmat.musicapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class registrationActivity extends AppCompatActivity {

    EditText userName;
    EditText email;
    EditText password;
    Button signubtn;

    Context context;

    //sharedPreferences
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences.Editor editor;

    private AwesomeValidation awesomeValidation;

    Utilities utilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        context = this;

        userName = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        signubtn = (Button) findViewById(R.id.signup);


        // Validation for user data entry
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.username, RegularExpression.NAME_PATTERN, R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.email, RegularExpression.EMAIL_PATTERN, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.password, RegularExpression.PASSWORD_PATTERN, R.string.passwroderror);

    }

    public void register(final View view){
        utilities = new Utilities();

        final String userEmail = email.getText().toString();
        final String userPassword = password.getText().toString();
        final String userUserName = userName.getText().toString();

        if (awesomeValidation.validate()) {
            checkEmail(userUserName ,userEmail, userPassword);
        }

        checkEmail(userUserName ,userEmail, userPassword);
    }

    public void checkEmail(final String userUserName , final String userEmail, final String userPassword){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseUrl.url_chcekEmail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("found")){
                    Toast.makeText(context, "Email is Registered before Please check again", Toast.LENGTH_LONG).show();
                }else{
                    signUpVolley(userUserName, userEmail, userPassword);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("response",error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", userEmail);
                return params;
            }
        };

        Singleton.getInstance(context).addToRequestQueue(stringRequest);


    }

    public void signUpVolley(final String userUserName, final String userEmail, final String userPassword){
        final String token = FirebaseInstanceId.getInstance().getToken();
        final String encryptPassword = Cryptography.encryptIt(userPassword);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseUrl.url_signUp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("success")){
                    startActivity(new Intent(getApplicationContext(), Home.class));
                }else{
                    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("response","sign"+ error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", userEmail);
                params.put("user_name", userUserName);
                params.put("password", encryptPassword);
                params.put("token", token);
                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(stringRequest);
    }


}
