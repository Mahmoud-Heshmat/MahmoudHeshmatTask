package com.example.mahmoudheshmat.mahmoudheshmattask;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.Filterable;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class doctorPatientAdapter extends RecyclerView.Adapter<doctorPatientAdapter.ViewHolder> implements DatePickerDialog.OnDateSetListener{

    private Context context;
    public List<items_booking> items;
    Context contexts;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String userID;

    // Date picker Attribute
    DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int year, month, day;
    String newDatePicker="";
    String oldDatePicker;
    String E_patient;

    static int pos;

    public doctorPatientAdapter(Context context, List<items_booking> items){
        this.context = context;
        this.items = items;
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("userId", null);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }


    @Override
    public doctorPatientAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_doctor_booking, parent, false);
        return new doctorPatientAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final doctorPatientAdapter.ViewHolder holder, final int position) {

        final String id = items.get(position).getPatient_id();
        final String name = items.get(position).getName();
        final String medicine = items.get(position).getMedicine();
        final String appointment = items.get(position).getDate_visit();
        final String confirm = items.get(position).getConfirm();

        holder.name.setText(name);
        holder.medicine.setText(medicine);
        holder.appointment.setText(appointment);

        if(confirm.equals("true")){
            holder.confirm_book.setVisibility(View.GONE);
        }

        holder.confirm_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm_booking(id, userID);
                holder.confirm_book.setVisibility(View.GONE);
                pos = position;
            }
        });

        holder.cancel_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_booking(id, userID, position);
            }
        });

        holder.delay_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldDatePicker = appointment;
                E_patient = id;
                datePickerDialog = new DatePickerDialog(
                        context,doctorPatientAdapter.this, year, month, day);
                datePickerDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        newDatePicker = String.valueOf(dayOfMonth)+"-"+String.valueOf(month)+"-"+String.valueOf(year);

        if(!newDatePicker.isEmpty()){
            if(checkDate()){
                showDelayDialog(context,newDatePicker);
            }else{
                Toast.makeText(context, R.string.date_choose, Toast.LENGTH_LONG).show();
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button confirm_book;
        public Button cancel_book;
        public Button delay_book;

        public TextView name;
        public TextView medicine;
        public TextView appointment;

        public ViewHolder(View itemView) {
            super(itemView);
            confirm_book = (Button) itemView.findViewById(R.id.confirm_book);
            cancel_book = (Button) itemView.findViewById(R.id.cancel_book);
            delay_book = (Button) itemView.findViewById(R.id.delay);

            name = (TextView) itemView.findViewById(R.id.patient_name);
            medicine = (TextView) itemView.findViewById(R.id.medicine);
            appointment = (TextView) itemView.findViewById(R.id.appointment_date);
        }
    }

    public void confirm_booking(final String patient_id, final String doctor_id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseURL.confirmBooking_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("success")) {
                    Toast.makeText(context, R.string.confirmed, Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void delete_booking(final String patient_id, final String doctor_id, final int pos){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseURL.delete_booking_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                if(response.equals("success")) {
                    Toast.makeText(context, R.string.cancel_book, Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, HomeDoctorsActivity.class));
                }else{
                    Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(stringRequest);
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

    public void showDelayDialog(final Context context, final String newDatePicker) {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Delay Booking");
        builder.setMessage("Are you sure you want to Delay " + newDatePicker + " ?");
        builder.setPositiveButton("Delay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edit_booking();
            }
        });
        builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void edit_booking(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseURL.DelayBooking_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                if(response.equals("success")) {
                    context.startActivity(new Intent(context, HomeDoctorsActivity.class));
                }else{
                    Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
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
                params.put("patient_id", E_patient);
                params.put("doctor_id", userID);
                params.put("old_date_visit", oldDatePicker);
                params.put("new_date_visit", newDatePicker);
                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}

