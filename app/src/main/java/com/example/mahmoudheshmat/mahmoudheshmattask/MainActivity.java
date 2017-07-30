package com.example.mahmoudheshmat.mahmoudheshmattask;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences slided_sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Slided_PREFERENCES = "SLIDER" ;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        slided_sharedPreferences = getSharedPreferences(Slided_PREFERENCES, Context.MODE_PRIVATE);
        //sharedPreferences.edit().clear().apply();
        String slider = slided_sharedPreferences.getString("key",null);
        if(slider!=null){
            String user_type = sharedPreferences.getString("userType",null);
            if(user_type != null){
                if(user_type.equals("1")){
                    startActivity(new Intent(context, HomeActivity.class));
                    finish();
                }else if(user_type.equals("2")){
                    startActivity(new Intent(context, HomeDoctorsActivity.class));
                    finish();
                }
            }
        }else{
            startActivity(new Intent(context, sliderActivity.class));
        }

    }

    public void goTOLogin(View view) {
        startActivity(new Intent(context, loginActivity.class));
    }

    public void goTOSignUp(View view) {
        startActivity(new Intent(context, registrationActivity.class));
    }
}
