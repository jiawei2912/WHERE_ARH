package com.example.where_arh.ui.home;

import com.google.android.gms.common.api.ApiException;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;

import java.io.IOException;

public class NearbySearch {
    public PlacesSearchResponse run(){
        PlacesSearchResponse request = new PlacesSearchResponse();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("MY_KEY")
                .build();
        LatLng location = new LatLng(-33.8670522, 151.1957362);

        try {
            request = PlacesApi.nearbySearchQuery(context, location)
                    .radius(5000)
                    .rankby(RankBy.PROMINENCE)
                    .keyword("cruise")
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
