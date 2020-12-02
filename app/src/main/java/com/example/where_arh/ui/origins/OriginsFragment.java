package com.example.where_arh.ui.origins;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.location.Location;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.where_arh.R;
import com.example.where_arh.Util.Util;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
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
    private ImageView addOriginButton;
    private Button originConfirmButton;
    private NavController navcontroller;
    private ImageButton addOwnLocationButton;

    //Preferences
    private SharedPreferences originPreferences;
    private String originPrefFile = "com.example.where_arh.origins.locations_info";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OriginsFragment() {
    }

    //can customise parameter init here
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
        List<OriginContent.OriginItem> originitems = OriginContent.getItems();
        SharedPreferences.Editor prefEditor = originPreferences.edit();
        Util.saveOriginsIntoPreferences(prefEditor, originitems);
        prefEditor.apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View nview = inflater.inflate(R.layout.fragment_origins_list, container, false);
        View view = nview.findViewById(R.id.origin_list);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            OriginsRecyclerViewAdapter orva = new OriginsRecyclerViewAdapter(OriginContent.getItems());
            recyclerView.setAdapter(orva);
            OriginContent.addViewAdapter(orva);
        }

        addOriginButton = nview.findViewById(R.id.add_origin_button);
        addOriginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!Places.isInitialized()){
                    Places.initialize(v.getContext(), getResources().getString(R.string.google_maps_key));
                }
                startAutocompleteActivity(v);
            }
        });

        addOwnLocationButton = nview.findViewById(R.id.add_own_location_button);
        addOwnLocationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                updateMyPlace();
                Place myplace = OriginContent.getMyplace();
                if (myplace == null){
                    Toast errortoast = Toast.makeText(v.getContext(), "Error: Cannot find your location", Toast.LENGTH_SHORT);
                    errortoast.show();
                }else{
                    OriginContent.deleteItemWithNameIfExists("Your Location");
                    OriginContent.addItem("Your Location", myplace.getLatLng(), true);
                }
            }
        });

        navcontroller = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        originConfirmButton = nview.findViewById(R.id.origins_confirm_button);
        originConfirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                OriginContent.commitItems();
                navcontroller.navigate(R.id.nav_home);
            }
        });

        //load saved origins
        originPreferences = this.getActivity().getSharedPreferences(originPrefFile, this.getActivity().MODE_PRIVATE);
        Set<String> location_ids = originPreferences.getStringSet("location_ids", null);
        if (location_ids != null){
            List<Place> places = Util.readOriginsFromPreferencesAsPlaces(originPreferences);
            OriginContent.clearItems();
            for (Place place:places){
                OriginContent.addPlaceAsItem(place);
            }
            OriginContent.notifyViewAdapters();
        }
        return nview;
    }

    //AutoComplete Activity
    final int AUTOCOMPLETE_REQUEST_CODE= 2;
    public void startAutocompleteActivity(View view) {

        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN,
                Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
                .setCountry("SG")
                .build(view.getContext());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }
    //On Activity result for the above
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == this.getActivity().RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                OriginContent.addPlaceAsItem(place, true);
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

    private Place.Builder PLACE_BUILDER;
    @SuppressLint("MissingPermission")
    public void updateMyPlace(){
        PLACE_BUILDER = Place.builder();
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    PLACE_BUILDER.setName("Your Location");
                    PLACE_BUILDER.setLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                    OriginContent.setMyPlace(PLACE_BUILDER.build());
                }
            }
        });
    }
}