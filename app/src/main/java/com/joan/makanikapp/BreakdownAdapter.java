package com.joan.makanikapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BreakdownAdapter extends RecyclerView.Adapter<BreakdownAdapter.MyViewHolder> {

    Context context;

    ArrayList<Breakdown> list;


    public BreakdownAdapter(Context context, ArrayList<Breakdown> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.breakdown_item,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Breakdown items = list.get(position);
        //holder.description.setText(items.getDescription());
        holder.mechanic.setText(items.getMechanic());
        holder.date.setText(items.getDate());
        //holder.time.setText(items.getTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView description, location, mechanic, date, time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //description = itemView.findViewById(R.id.nameBreakdown);
            //location = itemView.findViewById(R.id.locationBreakdown);
            date = itemView.findViewById(R.id.dateBreakdown);
            //time = itemView.findViewById(R.id.timeBreakdown);
            mechanic = itemView.findViewById(R.id.mechanicBreakdown);

        }
    }

}