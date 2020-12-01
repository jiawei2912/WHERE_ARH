package com.example.where_arh.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.example.where_arh.R;
import com.example.where_arh.Util.Util;
import com.example.where_arh.ui.origins.OriginContent;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;


public class HomeFragment extends Fragment
        implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    private SharedPreferences locationPreferences;
    private SharedPreferences mPreferences;
    private String locationsPrefFile = "com.example.where_arh.origins.locations_info";
    private List<Place> places;
    private FloatingActionButton to_origins_fab;
    private NavController navcontroller;

    @Override
    public void onCreate(Bundle SavedInstance) {
        super.onCreate(SavedInstance);
    }

    @Override
    public void onResume(){
        super.onResume();
        locationPreferences = this.getActivity().getSharedPreferences(locationsPrefFile, this.getActivity().MODE_PRIVATE);
        Set<String> location_ids = locationPreferences.getStringSet("location_ids", null);
        if (!OriginContent.isEmpty()){
            this.places = OriginContent.getComittedItemsAsPlaces();
        }else if(location_ids != null  && !OriginContent.isIntentionallyEmpty()){
            this.places = Util.readOriginsFromPreferencesAsPlaces(locationPreferences);
            OriginContent.setPlacesAsComittedItems(this.places);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        navcontroller = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        to_origins_fab = getActivity().findViewById(R.id.to_origins_fab);
        to_origins_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                navcontroller.navigate(R.id.nav_origins);
            }
        });
    }

    //MAP STUFF
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted = true;
    private static GoogleMap mMap;
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this case, we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to
     * install it inside the SupportMapFragment. This method will only be triggered once the
     * user has installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        LatLng home = new LatLng(1.29, 103.85);
        //map.addMarker(new MarkerOptions().position(home).title("Singapore"));
        map.moveCamera(CameraUpdateFactory.newLatLng(home));
        map.setMapType(1);
        map.moveCamera(CameraUpdateFactory.zoomTo(10));
        UiSettings uisettings = map.getUiSettings();
//        uisettings.setZoomControlsEnabled(true);
        uisettings.setCompassEnabled(true);
        //MY LOCATION
        // LocationManager - to ask for location updates
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }else{
            locationPermissionGranted = true;
            uisettings.setMyLocationButtonEnabled(true);
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
        }
        //load markers
        List<LatLng> latlnglist = null;
        if (places != null && places.isEmpty()){
            mMap.clear();
        }
        if (places != null && !places.isEmpty()){
            for(Place place:places){
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName()));
            }
            latlnglist = OriginContent.getComittedItemAsLatLngs();
        }
        //Find Center
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean unweighted_algorithm = mPreferences.getBoolean("ml_algo", false);
        if (!unweighted_algorithm){
            if(latlnglist!= null && !latlnglist.isEmpty()){
                // Calculated centre point
                double centerlat = 0;
                double centerlng = 0;
                for(LatLng latLng:latlnglist){
                    centerlat += latLng.latitude;
                    centerlng += latLng.longitude;
                }
                centerlat = centerlat/latlnglist.size();
                centerlng = centerlng/latlnglist.size();
                LatLng centre = new LatLng(centerlat, centerlng);
                mMap.addMarker(new MarkerOptions().position(centre).title("Center"));

                // Change v to set the amount to zoom
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centre,14));

                for (int i = 0; i < latlnglist.size(); i++){
                    PolygonOptions polygonOptions = new PolygonOptions().add(centre);
                    polygonOptions.add(latlnglist.get(i));
                    Polygon polygon = mMap.addPolygon(polygonOptions);
                    // Colours specified by hex digits A(FF), R(00), G(FF), B(00)
                    // To make it translucent, divide alpha channel by 2
                    polygon.setFillColor(0x7F00FF00);
                    polygon.setStrokeColor(0x7F00FF00);
                }
            }
        }else{
            //MACHINE LEARNING
            if(latlnglist!= null && latlnglist.size() > 1){
                //Parameters
                double step_size = 0.00001;

                //Initial point
                LatLng current_centre = Util.getCenterLatLng(latlnglist);

                //Main Loop of algorithm
                int safety = 1000;
                List<LatLng> working_latlnglist = new ArrayList<>(latlnglist);
                if (mPreferences.getBoolean("ml_algo_attachment", false)){
                    safety = 100000;
                }
                while(safety > 0){
                    Log.d("TAG", String.valueOf(safety));
                    //check exit_condition - if there are any latlngs left to try moving towards
                    if (working_latlnglist.isEmpty()){
                        //if none left to try, stop algorithm
                        break;
                    }

                    //cost = (average distance - distance_i)^2
                    double current_iter_cost = Util.getCost(latlnglist, current_centre);

                    //Get random latlang for SGD and remove it from the working list
                    LatLng target_latlng = Util.getRandomLatLng(working_latlnglist);
                    working_latlnglist.remove(target_latlng);

                    //use SGD - Move center toward target latlng
                    double new_lat = current_centre.latitude + step_size*target_latlng.latitude;
                    double new_long = current_centre.longitude + step_size*target_latlng.longitude;
                    LatLng proposed_new_centre = new LatLng(new_lat, new_long);

                    //get cost with proposed new center and update center if new cost is lower
                    double new_cost = Util.getCost(latlnglist, proposed_new_centre);
                    if (new_cost < current_iter_cost){
                        current_centre = proposed_new_centre;
                        //refresh working latlnglist each time center is updated
                        working_latlnglist = new ArrayList<>(latlnglist);
                    }
                    step_size = step_size*0.95;
                    safety = safety - 1;
                }

                LatLng centre = current_centre;
                mMap.addMarker(new MarkerOptions().position(centre).title("Center"));
                // Change v to set the amount to zoom
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centre,14));

                for (int i = 0; i < latlnglist.size(); i++){
                    PolygonOptions polygonOptions = new PolygonOptions().add(centre);
                    polygonOptions.add(latlnglist.get(i));
                    Polygon polygon = mMap.addPolygon(polygonOptions);
                    // Colours specified by hex digits A(FF), R(00), G(FF), B(00)
                    // To make it translucent, divide alpha channel by 2
                    polygon.setFillColor(0x7F00FF00);
                    polygon.setStrokeColor(0x7F00FF00);
                }


            }


        }


    }
    //Handles Perms
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        locationPermissionGranted = false;
        switch (requestCode){
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    locationPermissionGranted = true;
                    UiSettings uisettings = mMap.getUiSettings();
                    uisettings.setMyLocationButtonEnabled(true);
                    mMap.setMyLocationEnabled(true);
                    mMap.setOnMyLocationButtonClickListener(this);
                    mMap.setOnMyLocationClickListener(this);
                }else{
                    ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                }
            }
        }
    }
    //Get user location
    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this.getContext(), "Current location:\n" + location, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this.getContext(), "Centering to your location", Toast.LENGTH_SHORT)
                .show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onPause(){
        super.onPause();
        updateMyPlace();
    }

    private Place.Builder PLACE_BUILDER;
    //A bad idea - need to fix
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