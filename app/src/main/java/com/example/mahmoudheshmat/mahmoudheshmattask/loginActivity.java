package com.example.mahmoudheshmat.mahmoudheshmattask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class loginActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    CheckBox show_hide_password;

    //Facebook Login
    LoginButton facebooklogin;
    CallbackManager callbackManager;
    String faceEmail;
    String faceFirstName;
    String faceLastName;
    String faceId ;

    Context context;

    utilits utilit ;

    //Shared Preferences
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences.Editor editor;

    private AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        utilit = new utilits();

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        show_hide_password = (CheckBox) findViewById(R.id.show_hide_password);
        show_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button,
                                         boolean isChecked) {
                Show_hide_passwordFun(isChecked);
            }
        });

        context = this;

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Validation for user data entry
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.email, RegularExpression.EMAIL_PATTERN, R.string.emailerror);


        //Facebook Login
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager= CallbackManager.Factory.create();
        facebooklogin=(LoginButton) findViewById(R.id.login_button);
        facebooklogin.setReadPermissions(Arrays.asList("public_profile","email","user_friends","user_location"));
        facebooklogin.registerCallback(callbackManager,new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess (LoginResult loginResult){
                graphRequest(loginResult.getAccessToken());
                //Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel () {
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError (FacebookException error){
                Log.d("response", error.toString());
            }
        });
    }

    public void login(View view) {
        if(utilit.internetConnection(context)){
            awesomeValidation.addValidation(this, R.id.password, RegularExpression.PASSWORD_PATTERN, R.string.passwroderror);
            if (awesomeValidation.validate()) {
                checkvervication();
            }
        }else{
            Toast.makeText(context, R.string.internet_connection, Toast.LENGTH_LONG).show();
        }
    }

    public void checkvervication() {

        final String userEmail = email.getText().toString();
        final String userPassword = password.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseURL.login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                if(response.equals("nofound")) {
                    Toast.makeText(context, R.string.login_check, Toast.LENGTH_SHORT).show();
                }else{
                    editor.remove(MyPREFERENCES).commit();
                    JSONObject jsonObject = null;
                    JSONArray jsonArray = null;
                    JSONObject json = null;
                    try {
                        jsonObject = new JSONObject(response);
                        jsonArray = jsonObject.getJSONArray("result");
                        json = jsonArray.getJSONObject(0);

                        String user_id = json.getString("id");
                        String user_username = json.getString("userName");
                        String user_email = json.getString("email");
                        String user_userType = json.getString("user_type");

                        Toast.makeText(context, user_username, Toast.LENGTH_LONG).show();

                        editor.putString("userId", user_id);
                        editor.putString("userName", user_username);
                        editor.putString("userEmail", user_email);
                        editor.putString("userType", user_userType);
                        editor.commit();

                        if (user_userType.equals("1")) {
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }else{
                            startActivity(new Intent(getApplicationContext(), HomeDoctorsActivity.class));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
                params.put("password", userPassword);
                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void Show_hide_passwordFun(Boolean isChecked){
        if (isChecked) {
            show_hide_password.setText("Hide Password");// change
            password.setInputType(InputType.TYPE_CLASS_TEXT);
            password.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());// show password
        } else {
            show_hide_password.setText("Show Password");// change
            password.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            password.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());// hide password
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //Facebook Login
    public void graphRequest(AccessToken token){
        GraphRequest request = GraphRequest.newMeRequest(token,new GraphRequest.GraphJSONObjectCallback(){

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    JSONObject jsonObject = new JSONObject(object.toString());

                    faceFirstName = jsonObject.getString("first_name");
                    faceLastName = jsonObject.getString("last_name");
                    faceEmail = jsonObject.getString("email");
                    faceId = jsonObject.getString("id");

                    if(faceLastName.isEmpty()){
                        faceLastName = "";
                    }

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseURL.login_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response", response);
                            if(response.equals("nofound")) {
                                editor.remove(MyPREFERENCES).commit();
                                editor.putString("User_id", faceId);
                                editor.putString("User_username", faceFirstName + " "+ faceLastName);
                                editor.putString("User_email", faceEmail);
                                editor.commit();

                                FacebookSignUp(faceFirstName + " " + faceLastName, faceEmail, faceId);
                            }else{
                                startActivity(new Intent(getApplicationContext(), loginActivity.class));
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
                            params.put("email", faceEmail);
                            params.put("password", faceId);
                            return params;
                        }
                    };
                    Singleton.getInstance(context).addToRequestQueue(stringRequest);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        Bundle b = new Bundle();
        b.putString("fields","id,email,first_name,last_name, location{location}");
        request.setParameters(b);
        request.executeAsync();

    }

    public void FacebookSignUp(final String userUserName, final String userEmail, final String userPassword){

        final String token = FirebaseInstanceId.getInstance().getToken();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseURL.register_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("success")){
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