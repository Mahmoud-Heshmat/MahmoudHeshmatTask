package com.example.mahmoudheshmat.mahmoudheshmattask;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class bookTableFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

    TextView doctorNameEditText;
    TextView doctorSpecEditText;
    TextView doctorAddressEditText;
    TextView doctorPriceEditText;
    TextView AppointmentDateEditText;
    TextView MedicineEditText;

    Button confirm_book;
    Button cancel_book;
    Button edit_book;

    String doctor_id ;
    String doctor_name ;
    String patient_id ;
    String doctor_address ;
    String doctor_spec ;
    String doctor_price ;
    String medicine;
    String visitingDate;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_booktable, container, false);

        sharedPreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("userId", null);

        Bundle bundle = getArguments();

        doctor_id = bundle.getString("doctor_id");
        doctor_name = bundle.getString("name");
        patient_id = bundle.getString("patient_id");
        doctor_address = bundle.getString("address");
        doctor_spec = bundle.getString("spec");
        doctor_price = bundle.getString("price");
        medicine = bundle.getString("medicine");
        visitingDate = bundle.getString("time");


        doctorNameEditText = (TextView) rootView.findViewById(R.id.doctor_name);
        doctorSpecEditText = (TextView) rootView.findViewById(R.id.specialization);
        doctorAddressEditText = (TextView) rootView.findViewById(R.id.address);
        doctorPriceEditText = (TextView) rootView.findViewById(R.id.price);
        AppointmentDateEditText = (TextView) rootView.findViewById(R.id.appointment_date);
        MedicineEditText = (TextView) rootView.findViewById(R.id.Medicine_used);

        confirm_book = (Button) rootView.findViewById(R.id.confirm_book);
        cancel_book = (Button) rootView.findViewById(R.id.cancel_book);
        edit_book = (Button) rootView.findViewById(R.id.edit_book);

        confirm_book.setOnClickListener(this);
        cancel_book.setOnClickListener(this);
        edit_book.setOnClickListener(this);

        doctorNameEditText.setText(doctor_name);
        doctorSpecEditText.setText(doctor_spec);
        doctorAddressEditText.setText(doctor_address);
        doctorPriceEditText.setText(doctor_price);
        AppointmentDateEditText.setText(visitingDate);
        MedicineEditText.setText(medicine);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v == confirm_book){
            check_booking();
        }else if(v == cancel_book){
            getContext().startActivity(new Intent(getContext(), HomeActivity.class));
        }else if(v == edit_book){

        }
    }
    private void check_booking(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseURL.checkBooking_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("found")) {
                    Toast.makeText(getContext(), "You already make booking at " + visitingDate, Toast.LENGTH_SHORT).show();
                }else{
                    confirm_booking();
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
                params.put("patient_id", patient_id);
                params.put("doctor_id", doctor_id);
                params.put("date_visit", visitingDate);
                return params;
            }
        };
        Singleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void confirm_booking(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseURL.addBooking_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                if(response.equals("success")) {
                    getContext().startActivity(new Intent(getContext(), HomeActivity.class));
                }else{
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
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
                params.put("patient_id", userID);
                params.put("doctor_id", doctor_id);
                params.put("disease_type", doctor_spec);
                params.put("date_visit", visitingDate);
                params.put("medicine", medicine);
                return params;
            }
        };
        Singleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}