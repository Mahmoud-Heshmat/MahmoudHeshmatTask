package com.example.mahmoudheshmat.mahmoudheshmattask;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class bookTableEditFragment  extends android.support.v4.app.Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener{

    TextView doctorNameEditText;
    TextView doctorSpecEditText;
    TextView doctorAddressEditText;
    TextView doctorPriceEditText;
    TextView AppointmentDateEditText;
    EditText MedicineEditText;

    Button edit_book;

    String doctor_id ;
    String doctor_name ;
    String patient_id ;
    String doctor_address ;
    String doctor_spec ;
    String doctor_price ;
    String medicine;
    String visitingDate;
    String newDatePicker="";

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String userID;

    // Date picker Attribute
    DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int year, month, day;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_editbooking, container, false);

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
        MedicineEditText = (EditText) rootView.findViewById(R.id.Medicine_used);

        edit_book = (Button) rootView.findViewById(R.id.edit_book);
        edit_book.setOnClickListener(this);
        AppointmentDateEditText.setOnClickListener(this);

        doctorNameEditText.setText(doctor_name);
        doctorSpecEditText.setText(doctor_spec);
        doctorAddressEditText.setText(doctor_address);
        doctorPriceEditText.setText(doctor_price);
        AppointmentDateEditText.setText(visitingDate);
        MedicineEditText.setText(medicine);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v == edit_book){
            medicine = MedicineEditText.getText().toString();

            if(!newDatePicker.isEmpty()){
                if(checkDate()){
                    if(medicine.isEmpty()){
                        medicine = "";
                    }
                    edit_booking();

                }else{
                    Toast.makeText(getContext(), R.string.date_choose, Toast.LENGTH_LONG).show();
                }
            }else{
                newDatePicker = visitingDate;
                if(medicine.isEmpty()){
                    medicine = "";
                }
                edit_booking();
            }


        }else if(v == AppointmentDateEditText){
            datePickerDialog = new DatePickerDialog(
                    getContext(),bookTableEditFragment.this, year, month, day);
            datePickerDialog.show();
        }
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        newDatePicker = String.valueOf(dayOfMonth)+"-"+String.valueOf(month)+"-"+String.valueOf(year);
    }

    public boolean checkDate(){

        Boolean check = false;

        String DateNow = this.day+"-"+this.month+"-"+this.year;

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        Date datePickers = null;
        Date dateNow = null;
        try {
            datePickers = formatter.parse(newDatePicker);
            dateNow = formatter.parse(DateNow);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (datePickers.compareTo(dateNow)<0) {
            check = false;
        }else{
            check = true;
        }
        return check;
    }

    private void edit_booking(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseURL.editBooking_url, new Response.Listener<String>() {
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
                params.put("old_date_visit", visitingDate);
                params.put("new_date_visit", newDatePicker);
                params.put("medicine", medicine);
                return params;
            }
        };
        Singleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}