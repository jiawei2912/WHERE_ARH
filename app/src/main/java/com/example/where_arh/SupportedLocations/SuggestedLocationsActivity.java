package com.example.where_arh.SupportedLocations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.where_arh.R;
import com.example.where_arh.ui.home.NearbySearch;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

import java.util.ArrayList;
import java.util.List;

public class SuggestedLocationsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SuggestedLocationsViewAdapter viewAdapter;
    List<PlacesSearchResult> nearbylocations;
    String api_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_locations);
        recyclerView = findViewById(R.id.suggested_locations_list);
        viewAdapter = new SuggestedLocationsViewAdapter(SuggestedLocationsContent.getItems());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(viewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SuggestedLocationsActivity.this));
        SuggestedLocationsContent.addViewAdapter(viewAdapter);
        //don't touch anything above this line

        api_key = getResources().getString(R.string.google_maps_key);
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("center_lat",0);
        double lng = intent.getDoubleExtra("center_lng",0);
        com.google.maps.model.LatLng center = new com.google.maps.model.LatLng(lat,lng);

        nearbylocations = NearbySearch.getNearbyLocations(center, 1000, api_key, PlaceType.RESTAURANT, "food");
        for (PlacesSearchResult psr:nearbylocations){
            SuggestedLocationsContent.addPlacesSearchResult(psr);
        }
        viewAdapter.notifyDataSetChanged();
    }
}