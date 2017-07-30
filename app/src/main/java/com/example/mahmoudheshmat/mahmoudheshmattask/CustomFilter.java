package com.example.mahmoudheshmat.mahmoudheshmattask;


import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class CustomFilter  extends Filter {

    doctorAdapter doctorAdapter;
    List<items_doctor> filterList;

    public CustomFilter(List<items_doctor> filterList, doctorAdapter ChildAdapter)
    {
        this.doctorAdapter=ChildAdapter;
        this.filterList=filterList;
    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            List<items_doctor> filteredPlayers=new ArrayList<>();
            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getName().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredPlayers.add(filterList.get(i));
                }
            }
            results.count=filteredPlayers.size();
            results.values=filteredPlayers;
        }else {
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        doctorAdapter.items= (ArrayList<items_doctor>) results.values;
        doctorAdapter.notifyDataSetChanged();
    }
}