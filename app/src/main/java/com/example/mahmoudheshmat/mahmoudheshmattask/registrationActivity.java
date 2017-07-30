package com.example.mahmoudheshmat.mahmoudheshmattask;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class registrationActivity extends AppCompatActivity {

    EditText userName;
    EditText email;
    EditText password;

    Context context;

    private AwesomeValidation awesomeValidation;

    utilits utilit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        utilit = new utilits();

        userName = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        context = this;

        // Validation for user data entry
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.username, RegularExpression.NAME_PATTERN, R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.email, RegularExpression.EMAIL_PATTERN, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.password, RegularExpression.PASSWORD_PATTERN, R.string.passwroderror);
    }


    public void signUp(View view) {

        if(utilit.internetConnection(context)){
            final String userEmail = email.getText().toString();
            final String userPassword = password.getText().toString();
            final String userUserName = userName.getText().toString();

            SendMail sm = new SendMail(context, userEmail, "Clinic APP", items_email.message);
            sm.execute();

            if (awesomeValidation.validate()) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseURL.checkUserEmail_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        if(response.equals("found")){
                            Toast.makeText(context, R.string.check_email, Toast.LENGTH_LONG).show();
                        }else{
                            final String token = FirebaseInstanceId.getInstance().getToken();

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseURL.register_url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("success")){
                                        Toast.makeText(context, "Message send to your"+ userEmail, Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), loginActivity.class));
                                    }else{
                                        Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("user_name", userUserName);
                                    params.put("email", userEmail);
                                    params.put("password", userPassword);
                                    params.put("token", token);
                                    params.put("userType", "1");
                                    return params;
                                }
                            };
                            Singleton.getInstance(context).addToRequestQueue(stringRequest);

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("response", error.toString());
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
        }else{
            Toast.makeText(context, R.string.internet_connection, Toast.LENGTH_LONG).show();
        }

    }
}
