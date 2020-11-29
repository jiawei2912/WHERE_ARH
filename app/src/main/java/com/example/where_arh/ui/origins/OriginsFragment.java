package com.example.where_arh.ui.origins;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.where_arh.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;

/**
 * A fragment representing a list of Items.
 */
public class OriginsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    ImageView addOriginButton;
    private OriginsRecyclerViewAdapter orva;

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
        //Places.initialize(nview.getContext(), getResources().getString(R.string.google_maps_key));
        Places.initialize(nview.getContext(), "AIzaSyCaDa7yGGW4AuCHuN-BRm48dy8SxvFDvOE");
        addOriginButton = nview.findViewById(R.id.add_origin_button);
        addOriginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startAutocompleteActivity(v);
            }
        });

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
                //textView.setText(place.getId() + place.getName() + place.getLatLng());
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