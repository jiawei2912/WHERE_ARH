package com.example.where_arh.ui.origins;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.where_arh.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A fragment representing a list of Items.
 */
public class OriginsFragment extends Fragment {
    //Parameters
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    //Widgets
    ImageView addOriginButton;
    private OriginsRecyclerViewAdapter orva;

    //Preferences
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.where_arh.origins.locations_info";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OriginsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static OriginsFragment newInstance(int columnCount) {
        OriginsFragment fragment = new OriginsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        List<OriginContent.OriginItem> places = OriginContent.getItems();
        SharedPreferences.Editor prefEditor = mPreferences.edit();
        Set<String> location_ids = new HashSet<>();

        for (OriginContent.OriginItem o:places){
            location_ids.add(o.id);
            prefEditor.putString(o.id+"_name", o.content);
            prefEditor.putString(o.id+"_lat", String.valueOf(o.coords.latitude));
            prefEditor.putString(o.id+"_lng", String.valueOf(o.coords.longitude));
            Log.d("TAG", o.coords.toString());
            Log.d("TAG", String.valueOf(o.coords.latitude));
            Log.d("TAG", String.valueOf(o.coords.longitude));
        }
        prefEditor.putStringSet("location_ids" , location_ids);
        prefEditor.apply();
        Log.d("TAG", "I SAVED");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View nview = inflater.inflate(R.layout.fragment_origins_list, container, false);
        View view = nview.findViewById(R.id.list);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            orva = new OriginsRecyclerViewAdapter(OriginContent.ITEMS);
            recyclerView.setAdapter(orva);
        }

        addOriginButton = nview.findViewById(R.id.add_origin_button);
        addOriginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!Places.isInitialized()){
                    Places.initialize(v.getContext(), "AIzaSyCaDa7yGGW4AuCHuN-BRm48dy8SxvFDvOE");
                }
                startAutocompleteActivity(v);
            }
        });

        mPreferences = this.getActivity().getSharedPreferences(sharedPrefFile, this.getActivity().MODE_PRIVATE);
        Set<String> location_ids = mPreferences.getStringSet("location_ids", null);
        if (location_ids != null){
            OriginContent.clearItems();
            for (String id:location_ids){
                LatLng latlng = new LatLng(Double.parseDouble(mPreferences.getString(id+"_lat","0")),  Double.parseDouble(mPreferences.getString(id+"_lng","0")));
                Place.Builder place_builder = Place.builder();
                place_builder.setName(mPreferences.getString(id+"_name",""));
                place_builder.setLatLng(latlng);
                Place place = place_builder.build();
                addToList(place);
            }
        }

        return nview;
    }
    final int AUTOCOMPLETE_REQUEST_CODE= 2;
    public void startAutocompleteActivity(View view) {

        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN,
                Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
                .setCountry("SG")
                .build(view.getContext());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == this.getActivity().RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                addToList(place);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
            } else if (resultCode == this.getActivity().RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void addToList(Place place){
        int id = OriginContent.ITEMS.size()+1;
        OriginContent.OriginItem originItem = new OriginContent.OriginItem(Integer.toString(id), place.getName(), place.getLatLng());
        OriginContent.addItem(originItem);
        orva.notifyDataSetChanged();
    }
}