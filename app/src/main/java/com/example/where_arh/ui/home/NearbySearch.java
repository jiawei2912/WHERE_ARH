package com.example.where_arh.ui.home;

import com.example.where_arh.R;
import com.google.android.gms.common.api.ApiException;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;

import java.io.IOException;

public class NearbySearch {
    public static PlacesSearchResponse run(LatLng center, int radius, String api_key){
        PlacesSearchResponse request = new PlacesSearchResponse();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(api_key)
                .build();
        try {
            request = PlacesApi.nearbySearchQuery(context, center)
                    .radius(radius)
                    .rankby(RankBy.PROMINENCE)
                    .keyword("food")
                    .language("en")
                    .type(PlaceType.RESTAURANT)
                    .await();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            return request;
        }
    }
}
