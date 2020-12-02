package com.example.where_arh.ui.home;

import com.example.where_arh.R;
import com.google.android.gms.common.api.ApiException;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.RankBy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NearbySearch {
    public static PlacesSearchResponse run(LatLng center, int radius, String api_key, PlaceType placetype, String keyword){
        PlacesSearchResponse request = new PlacesSearchResponse();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(api_key)
                .build();
        try {
            request = PlacesApi.nearbySearchQuery(context, center)
                    .radius(radius)
                    .rankby(RankBy.PROMINENCE)
                    .keyword(keyword)
                    .language("en")
                    .type(placetype) //PlaceType.RESTAURANT
                    .await();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            return request;
        }
    }

    public static List<PlacesSearchResult> getNearbyLocations(LatLng center, int radius, String api_key, PlaceType placetype, String keyword){
        PlacesSearchResponse psr = run(center, radius, api_key, placetype, keyword);
        List<PlacesSearchResult> nearby_locations = new ArrayList<>();
        PlacesSearchResult[] results = psr.results;
        for (PlacesSearchResult result:results){
            nearby_locations.add(result);
        }
        return nearby_locations;
    }
}
