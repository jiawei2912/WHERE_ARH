package com.example.where_arh.Util;

import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.WebStorage;

import com.example.where_arh.ui.origins.OriginContent;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
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

    public static void savePlacesAsOriginsIntoPreferences(SharedPreferences.Editor prefEditor, List<Place> places){
        if (places == null){
            return;
        }
        List<OriginContent.OriginItem> items = new ArrayList<>();
        int idx = 0;
        for (Place place:places){
            OriginContent.OriginItem originitem = new OriginContent.OriginItem(String.valueOf(idx), place.getName(), place.getLatLng());
            items.add(originitem);
            idx += 1;
        }
        saveOriginsIntoPreferences(prefEditor, items);
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

    //Machine Learning Algorithm helper functions

    public static double distanceBetweenLatLngs(LatLng l1, LatLng l2){
        double x_distance = l1.latitude - l2.latitude;
        double y_distance = l1.longitude - l2.longitude;
        return Math.pow(Math.pow(x_distance,2) + Math.pow(y_distance,2), 0.5);
    }

    public static double getAverageDistance(List<LatLng> latlnglist, LatLng dest){
        double total_distance = 0;
        for(LatLng latLng:latlnglist){
            total_distance += Util.distanceBetweenLatLngs(dest, latLng);
        }
        return total_distance/latlnglist.size();
    }

    public static double getMedianDistance(List<LatLng> latlnlist, LatLng dest){
        double[] distances = new double[latlnlist.size()];
        for (int i = 0; i < latlnlist.size(); i++){
            distances[i] = Util.distanceBetweenLatLngs(dest, latlnlist.get(i));
        }
        Arrays.sort(distances);
        if (distances.length%2 == 1){
            int choice = (distances.length/2) + 1 - 1;
            return distances[choice];
        }else{
            int choice1 = distances.length/2 - 1;
            return (distances[choice1] + distances[choice1+1])/2;
        }
    }

    public static double getCost(List<LatLng> latlnglist, LatLng dest){
        double average_distance = getAverageDistance(latlnglist, dest);
        double cost = 0;
        for(LatLng latlng:latlnglist){
            cost +=  Math.pow(Util.distanceBetweenLatLngs(dest, latlng) - average_distance, 2);
        }
        return cost;
    }

    public static LatLng getWorstCostLatLng(List<LatLng> latlnglist, LatLng dest){
        double average_distance = getMedianDistance(latlnglist, dest);
        double worst_cost = 0;
        LatLng worst_latlng = dest;
        for(LatLng latlng:latlnglist){
            double i_cost =  Math.pow(Util.distanceBetweenLatLngs(dest, latlng) - average_distance, 2);
            if (i_cost > worst_cost){
                worst_latlng = latlng;
                worst_cost = i_cost;
            }
        }
        return worst_latlng;
    }

    public static LatLng getRandomLatLng(List<LatLng> latlnglist){
        Random r1 = new Random(System.nanoTime());
        int pick = r1.nextInt(latlnglist.size());
        return latlnglist.get(pick);
    }

    public static LatLng getCenterLatLng(List<LatLng> latlnglist){
        double centerlat = 0;
        double centerlng = 0;
        for(LatLng latLng:latlnglist){
            centerlat += latLng.latitude;
            centerlng += latLng.longitude;
        }
        centerlat = centerlat/latlnglist.size();
        centerlng = centerlng/latlnglist.size();
        return new LatLng(centerlat, centerlng);
    }
}
