package com.example.mahmoudheshmat.mahmoudheshmattask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeDoctorsActivity extends AppCompatActivity  implements SearchView.OnQueryTextListener {

    RecyclerView recyclerView;
    private doctorAdapter adapter;
    private List<items_doctor> data_list;
    LinearLayoutManager linearLayout;
    Context context;

    Toolbar toolbar;

    //Drawer
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    utilits utilit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_doctors);
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
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "");

        utilit = new utilits();

        if(utilit.internetConnection(context)){
        }else{
            Toast.makeText(context, R.string.internet_connection, Toast.LENGTH_LONG).show();
        }

        data_list = new ArrayList<>();
        getAllDoctors();
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        linearLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayout);



        //Navigation Drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        TextView headerText = (TextView) headerView.findViewById(R.id.header_name);
        headerText.setText(userName);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavigationItemSelected(item.getItemId());
                return false;
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    public void NavigationItemSelected(int selectedItem) {
        switch (selectedItem) {

            case R.id.Patient_booking:
                if(utilit.internetConnection(context)){
                    startActivity(new Intent(context, doctorBookingActivity.class));
                }else{
                    Toast.makeText(context, R.string.internet_connection, Toast.LENGTH_LONG).show();
                }
                drawerLayout.closeDrawers();
                break;

            case R.id.logout:
                sharedPreferences.edit().clear().apply();
                startActivity(new Intent(context, MainActivity.class));
                break;
        }
    }

    private void getAllDoctors() {
        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, DatabaseURL.getDoctors_url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray list = json.getJSONArray("result");
                            for (int x = 0; x < list.length(); x++) {
                                JSONObject jsonObject = list.optJSONObject(x);
                                String id = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                String address = jsonObject.getString("address");
                                String price = jsonObject.getString("price");
                                String spec_type = jsonObject.getString("type");

                                items_doctor items = new items_doctor(id, name, spec_type, address, price);
                                data_list.add(items);
                            }

                            adapter = new doctorAdapter(context, data_list);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        Singleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        adapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //adapter.getFilter().filter(query);
        return false;
    }
}
