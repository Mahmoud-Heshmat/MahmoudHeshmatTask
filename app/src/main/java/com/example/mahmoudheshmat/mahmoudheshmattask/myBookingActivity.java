package com.example.mahmoudheshmat.mahmoudheshmattask;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class myBookingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private myBookingAdapter adapter;
    private List<items_booking> data_list;
    LinearLayoutManager linearLayout;
    Context context;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    String user_id;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.mybooking));
        toolbar.setNavigationIcon(R.drawable.back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        context = this;
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("userId", "");
        Log.d("response", user_id);

        data_list = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        linearLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayout);
        getMyBooking();

    }

    private void getMyBooking(){
        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseURL.getMyBooking_url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray list = json.getJSONArray("result");
                            for(int x =0; x<list.length(); x++){
                                JSONObject jsonObject = list.optJSONObject(x);
                                String doctor_id = jsonObject.getString("doctor_id");
                                String name = jsonObject.getString("name");
                                String address = jsonObject.getString("address");
                                String price = jsonObject.getString("price");
                                String type = jsonObject.getString("type");
                                String date_visit = jsonObject.getString("date_visit");
                                String medicine = jsonObject.getString("medicine");
                                String confirm = jsonObject.getString("confirm");


                                items_booking items = new items_booking(doctor_id, name, address, price, type, date_visit, medicine, confirm);
                                data_list.add(items);
                            }

                            adapter = new myBookingAdapter(context,data_list);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put("patient_id", user_id);
                return params;
            }
        };
        Singleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }
}
