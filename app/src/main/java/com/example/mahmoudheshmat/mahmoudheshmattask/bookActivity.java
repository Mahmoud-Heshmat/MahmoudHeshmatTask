package com.example.mahmoudheshmat.mahmoudheshmattask;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class bookActivity extends AppCompatActivity implements View.OnClickListener,DatePickerDialog.OnDateSetListener{

    Spinner dynamicSpinner;
    TextView time;
    EditText MedicineEdit;

    String doctor_id ;
    String doctor_name ;
    String patient_id ;
    String doctor_address ;
    String doctor_spec ;
    String doctor_price ;
    String medicine;

    TextView doctorNameEditText;
    TextView doctorSpecEditText;
    TextView doctorAddressEditText;
    TextView doctorPriceEditText;

    // Date picker Attribute
    DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int year, month, day;
    String datePicker="";

    String diseaseType;

    Context context;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.book));
        toolbar.setNavigationIcon(R.drawable.back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        context = this;

        time = (TextView) findViewById(R.id.choose_the_time);
        MedicineEdit = (EditText) findViewById(R.id.medicine);
        doctorNameEditText = (TextView) findViewById(R.id.doctor_name);
        doctorSpecEditText = (TextView) findViewById(R.id.doctor_spec);
        doctorAddressEditText = (TextView) findViewById(R.id.doctor_address);
        doctorPriceEditText = (TextView) findViewById(R.id.doctor_price);

        time.setOnClickListener(this);

        Bundle extra = getIntent().getExtras();
        if(extra != null){
            getDataIntent(extra);
        }



        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onClick(View v) {
        if(v == time){

            datePickerDialog = new DatePickerDialog(
                    context, bookActivity.this, year, month, day);
            datePickerDialog.show();
        }
    }


    public void getDataIntent(Bundle extra){
        doctor_id = extra.getString("doctor_id");
        doctor_name = extra.getString("name");
        patient_id = extra.getString("user_id");
        doctor_address = extra.getString("address");
        doctor_spec = extra.getString("spec");
        doctor_price = extra.getString("price");

        Log.d("response", doctor_id + " " + patient_id);

        doctorNameEditText.setText(doctor_name);
        doctorSpecEditText.setText(doctor_spec);
        doctorAddressEditText.setText(doctor_address);
        doctorPriceEditText.setText(doctor_price);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        datePicker = String.valueOf(dayOfMonth)+"-"+String.valueOf(month)+"-"+String.valueOf(year);
        time.setText(datePicker);
        time.setTextColor(getResources().getColor(R.color.ROSE));
    }

    public void book(View view) {
        medicine = MedicineEdit.getText().toString();

        if(!datePicker.isEmpty()){
            if(checkDate()){
                if(medicine.isEmpty()){
                    medicine = "";
                }
                bookTableFragment table = new bookTableFragment();
                addCenterFragments(table, "Table");

            }else{
                Toast.makeText(context, R.string.date_choose, Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(context, R.string.visit_date, Toast.LENGTH_LONG).show();
        }

    }

    public boolean checkDate(){

        Boolean check = false;

        String DateNow = this.day+"-"+this.month+"-"+this.year;

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        Date datePickers = null;
        Date dateNow = null;
        try {
            datePickers = formatter.parse(datePicker);
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

    private void addCenterFragments(Fragment fragment, String tag) {

        Bundle bundle = new Bundle();
        bundle.putString("doctor_id", doctor_id);
        bundle.putString("patient_id", patient_id);
        bundle.putString("name", doctor_name);
        bundle.putString("address", doctor_address);
        bundle.putString("price", doctor_price);
        bundle.putString("spec", doctor_spec);
        bundle.putString("time", datePicker);
        bundle.putString("medicine", medicine);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);

        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

}
