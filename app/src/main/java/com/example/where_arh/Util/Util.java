package com.example.where_arh.Util;

import android.content.SharedPreferences;
import android.webkit.WebStorage;

import com.example.where_arh.ui.origins.OriginContent;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Util {
    //Private constructor for util class
    private Util(){}

    public static void saveOriginsIntoPreferences(SharedPreferences.Editor prefEditor, List<OriginContent.OriginItem> originitems){
        Set<String> location_ids = new HashSet<>();
        for (OriginContent.OriginItem o:originitems){
            location_ids.add(o.id);
            prefEditor.putString(o.id+"_name", o.name);
            prefEditor.putString(o.id+"_lat", String.valueOf(o.coords.latitude));
            prefEditor.putString(o.id+"_lng", String.valueOf(o.coords.longitude));
        }
        prefEditor.putStringSet("location_ids" , location_ids);
    }

    public static List<Place> readOriginsFromPreferencesAsPlaces(SharedPreferences mPreferences){
        ArrayList<Place> places = new ArrayList<>();
        Set<String> location_ids = mPreferences.getStringSet("location_ids", null);
        if (location_ids != null){
            for (String id:location_ids){
                LatLng latlng = new LatLng(Double.parseDouble(mPreferences.getString(id+"_lat","0")),  Double.parseDouble(mPreferences.getString(id+"_lng","0")));
                Place.Builder place_builder = Place.builder();
                place_builder.setName(mPreferences.getString(id+"_name",""));
                place_builder.setLatLng(latlng);
                Place place = place_builder.build();
                places.add(place);
            }
        }
        return places;
    }
}
