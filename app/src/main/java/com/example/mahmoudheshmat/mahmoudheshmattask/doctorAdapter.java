package com.example.mahmoudheshmat.mahmoudheshmattask;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.List;

public class doctorAdapter extends RecyclerView.Adapter<doctorAdapter.ViewHolder> implements Filterable {

    private Context context;
    public List<items_doctor> items;
    Context contexts;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String userID;

    List<items_doctor> filterList;
    CustomFilter filter;

    public doctorAdapter(Context context, List<items_doctor> items){
        this.context = context;
        this.items = items;
        this.filterList = items;
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("userId", null);
    }


    @Override
    public doctorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_doctors, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(doctorAdapter.ViewHolder holder, final int position) {

        final String id = items.get(position).getId();
        final String name = items.get(position).getName();
        final String address = items.get(position).getAddress();
        final String spec = items.get(position).getSpec();
        final String price = items.get(position).getPrice();

        holder.name.setText(name);
        holder.address.setText(address);
        holder.spec_type.setText(spec);
        holder.price.setText(price);

        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contexts = v.getContext();
                Intent intent = new Intent(contexts, bookActivity.class);
                intent.putExtra("doctor_id", id);
                intent.putExtra("user_id", userID);
                intent.putExtra("name", name);
                intent.putExtra("address", address);
                intent.putExtra("spec", spec);
                intent.putExtra("price", price);
                contexts.startActivity(intent);
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
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new CustomFilter(filterList, this);
        }
        return filter;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button book;
        public TextView name;
        public TextView address;
        public TextView spec_type;
        public TextView price;

        public ViewHolder(View itemView) {
            super(itemView);
            book = (Button) itemView.findViewById(R.id.book);
            name = (TextView) itemView.findViewById(R.id.doctor_name);
            address = (TextView) itemView.findViewById(R.id.doctor_address);
            spec_type = (TextView) itemView.findViewById(R.id.doctor_spec);
            price = (TextView) itemView.findViewById(R.id.doctor_price);

        }
    }
}
