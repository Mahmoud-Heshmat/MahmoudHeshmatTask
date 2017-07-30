package com.example.mahmoudheshmat.mahmoudheshmattask;

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


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String user_id;
    @Override
    public void onTokenRefresh() {
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("User_id", null);

        String tokenRefresh = FirebaseInstanceId.getInstance().getToken();
        if(user_id != null){
            Refresh(tokenRefresh);
        }
    }

    private void Refresh(final String tokenRefresh) {
        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseURL.onTokenRefresh_url, new Response.Listener<String>() {

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
