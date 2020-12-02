package com.example.where_arh.SupportedLocations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.where_arh.MainActivity;
import com.example.where_arh.R;
import com.example.where_arh.ui.home.NearbySearch;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResult;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class SuggestedLocationsActivity extends AppCompatActivity {
    Spinner categoriesSpinner;
    EditText keywordEditText;
    RecyclerView recyclerView;

    static SuggestedLocationsViewAdapter viewAdapter;
    static List<PlacesSearchResult> nearbylocations;
    static String api_key;
    static LatLng center;
    static String keyword = "Food";
    static PlaceType category = PlaceType.RESTAURANT;
    static List<PlaceType> categories;
    SharedPreferences mainPreferences;
    static int search_radius;

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

        //BACK BUTTON
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //SEARCH
        api_key = getResources().getString(R.string.google_maps_key);
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("center_lat",0);
        double lng = intent.getDoubleExtra("center_lng",0);
        center = new LatLng(lat,lng);
        updateNearbyLocations();

        categories = new ArrayList<>(EnumSet.allOf(PlaceType.class));
        categories.remove(PlaceType.RESTAURANT);
        categories.add(0, PlaceType.RESTAURANT);

        //parameter setting from user inputs
        categoriesSpinner = findViewById(R.id.input_category);
        categoriesSpinner.setAdapter(new ArrayAdapter<>(SuggestedLocationsActivity.this, android.R.layout.simple_spinner_dropdown_item, categories));
        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PlaceType selectedItem = (PlaceType) parent.getItemAtPosition(position);
                category = selectedItem;
                updateNearbyLocations();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = PlaceType.RESTAURANT;
                updateNearbyLocations();
            }
        });

        keywordEditText = findViewById(R.id.input_keyword);
        keywordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    keyword = keywordEditText.getText().toString();
                    updateNearbyLocations();
                    return true;
                }
                return false;
            }
        });

        mainPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        search_radius = Integer.parseInt(mainPreferences.getString("search radius","1000"));
    }

    public static void updateNearbyLocations(){
        nearbylocations = NearbySearch.getNearbyLocations(center, search_radius, api_key, category, keyword);
        SuggestedLocationsContent.clearItems(false);
        for (PlacesSearchResult psr:nearbylocations){
            SuggestedLocationsContent.addPlacesSearchResult(psr);
        }
        viewAdapter.notifyDataSetChanged();
    }
}