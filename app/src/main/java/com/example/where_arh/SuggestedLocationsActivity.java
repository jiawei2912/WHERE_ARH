package com.example.where_arh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class SuggestedLocationsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SuggestedLocationsViewAdapter viewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_locations);
        recyclerView = findViewById(R.id.suggested_locations_list);
        viewAdapter = new SuggestedLocationsViewAdapter(SuggestedLocationsContent.getItems());
        recyclerView.setAdapter(viewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SuggestedLocationsActivity.this));
        SuggestedLocationsContent.addViewAdapter(viewAdapter);
        List<SuggestedLocationsContent.SuggestedLocationsItem> stuff = viewAdapter.ITEMS;
        Log.d("TAG", stuff.toString());


    }
}