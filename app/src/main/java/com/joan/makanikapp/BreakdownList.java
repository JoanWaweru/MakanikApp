package com.joan.makanikapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BreakdownList extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    BreakdownAdapter breakdownAdapter;
    ArrayList<Breakdown> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_breakdown_history);

        recyclerView = findViewById(R.id.breakdownRecyclerView);
        database = FirebaseDatabase.getInstance().getReference("breakdowns");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        breakdownAdapter = new BreakdownAdapter(this,list);
        recyclerView.setAdapter(breakdownAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Breakdown breakdown = dataSnapshot.getValue(Breakdown.class);
                    list.add(breakdown);


                }
                breakdownAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}