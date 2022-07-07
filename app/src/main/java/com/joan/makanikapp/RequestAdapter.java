package com.joan.makanikapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    Context context;

    ArrayList<Request> list;


    public RequestAdapter(Context context, ArrayList<Request> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.request_item,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Request items = list.get(position);
        //holder.description.setText(items.getDescription());
        holder.customer.setText(items.getCustomer());
        holder.date.setText(items.getDate());
        //holder.time.setText(items.getTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView description, location,customer, date, time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //description = itemView.findViewById(R.id.nameBreakdown);
            //location = itemView.findViewById(R.id.locationBreakdown);
            date = itemView.findViewById(R.id.dateRequest);
            //time = itemView.findViewById(R.id.timeBreakdown);
            customer = itemView.findViewById(R.id.customerRequest);

        }
    }

}