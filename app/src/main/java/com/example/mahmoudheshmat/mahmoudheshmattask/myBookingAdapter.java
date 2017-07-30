package com.example.mahmoudheshmat.mahmoudheshmattask;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class myBookingAdapter extends RecyclerView.Adapter<myBookingAdapter.ViewHolder>{

    private Context context;
    public List<items_booking> items;
    Context contexts;


    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String userID;

    public myBookingAdapter(Context context, List<items_booking> items){
        this.context = context;
        this.items = items;
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("userId", null);
    }


    @Override
    public myBookingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_booking, parent, false);
        return new myBookingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(myBookingAdapter.ViewHolder holder, final int position) {

        final String doctor_id = items.get(position).getDoctor_id();
        final String doctor_name = items.get(position).getName();
        final String doctor_spec = items.get(position).getType();
        final String doctor_address = items.get(position).getAddress();
        final String doctor_price = items.get(position).getPrice();
        final String visitingDate = items.get(position).getDate_visit();
        final String medicine = items.get(position).getMedicine();
        final String confirm = items.get(position).getConfirm();

        holder.doctorNameEditText.setText(doctor_name);
        holder.doctorSpecEditText.setText(doctor_spec);
        holder.doctorAddressEditText.setText(doctor_address);
        holder.doctorPriceEditText.setText(doctor_price);
        holder.AppointmentDateEditText.setText(visitingDate);
        holder.MedicineEditText.setText(medicine);


        if(confirm.equals("true")) {
            holder.linearLayout.setVisibility(View.GONE);
        }

        holder.cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_booking(userID,doctor_id, visitingDate,position);
            }
        });

        holder.edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("doctor_id", doctor_id);
                bundle.putString("patient_id", userID);
                bundle.putString("name", doctor_name);
                bundle.putString("address", doctor_address);
                bundle.putString("price", doctor_price);
                bundle.putString("spec", doctor_spec);
                bundle.putString("time", visitingDate);
                bundle.putString("medicine", medicine);

                bookTableEditFragment table = new bookTableEditFragment();
                table.setArguments(bundle);
                addCenterFragments(table, "Table");
            }
        });


    }

    public void delete_booking(final String patient_id, final String doctor_id, final String visitingDate,final int pos){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseURL.delete_mybooking_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                if(response.equals("success")) {
                    notifyItemRemoved(pos);
                    Toast.makeText(context, R.string.cancel_book, Toast.LENGTH_SHORT).show();
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
                params.put("visitingDate", visitingDate);
                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView doctorNameEditText;
        TextView doctorSpecEditText;
        TextView doctorAddressEditText;
        TextView doctorPriceEditText;
        TextView AppointmentDateEditText;
        TextView MedicineEditText;

        Button cancel_button;
        Button edit_button;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            doctorNameEditText = (TextView) itemView.findViewById(R.id.doctor_name);
            doctorSpecEditText = (TextView) itemView.findViewById(R.id.specialization);
            doctorAddressEditText = (TextView) itemView.findViewById(R.id.address);
            doctorPriceEditText = (TextView) itemView.findViewById(R.id.price);
            AppointmentDateEditText = (TextView) itemView.findViewById(R.id.appointment_date);
            MedicineEditText = (TextView) itemView.findViewById(R.id.Medicine_used);
            cancel_button = (Button) itemView.findViewById(R.id.cancel_book);
            edit_button = (Button) itemView.findViewById(R.id.edit_book);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.myBooking);

        }
    }

    private void addCenterFragments(Fragment fragment, String tag) {

        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
