package com.example.where_arh;

import com.example.where_arh.Util.Util;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UtilUnitTests {
    @Test
    public void latlng_distance_formula_isCorrect() {
        LatLng l1 = new LatLng(1,1);
        LatLng l2 = new LatLng(0,0);
        assertEquals(Math.pow(2,0.5), Util.distanceBetweenLatLngs(l1, l2), 0);
    }

    @Test
    public void get_median_distance_isCorrect(){
        LatLng l1 = new LatLng(1,1);
        LatLng l2 = new LatLng(-1,-1);
        LatLng l3 = new LatLng(1, -1);
        LatLng l4 = new LatLng(10,10);
        LatLng l5 = new LatLng(0,0);
        List<LatLng> latlnglist = new ArrayList<>();
        latlnglist.add(l1);
        latlnglist.add(l2);
        latlnglist.add(l3);
        latlnglist.add(l4);
        assertEquals(Math.pow(2,0.5), Util.getMedianDistance(latlnglist, l5), 0);
    }

    @Test
    public void get_mean_distance_isCorrect(){
        LatLng l1 = new LatLng(1,1);
        LatLng l2 = new LatLng(-1,-1);
        LatLng l3 = new LatLng(1, -1);
        LatLng l4 = new LatLng(10,10);
        LatLng l5 = new LatLng(0,0);
        List<LatLng> latlnglist = new ArrayList<>();
        latlnglist.add(l1);
        latlnglist.add(l2);
        latlnglist.add(l3);
        latlnglist.add(l4);
        assertEquals(((3*Math.pow(2,0.5)+Math.pow(200,0.5))/4), Util.getAverageDistance(latlnglist, l5), 0);
    }

    @Test
    public void get_worstLatLng_isCorrect(){
        LatLng l1 = new LatLng(1,1);
        LatLng l2 = new LatLng(-1,-1);
        LatLng l3 = new LatLng(1, -1);
        LatLng l4 = new LatLng(10,10);
        LatLng l5 = new LatLng(0,0);
        List<LatLng> latlnglist = new ArrayList<>();
        latlnglist.add(l1);
        latlnglist.add(l2);
        latlnglist.add(l3);
        latlnglist.add(l4);
        assertEquals(l4, Util.getWorstCostLatLng(latlnglist, l5));
    }

    @Test
    public void get_center_latlng_isCorrect(){
        LatLng l1 = new LatLng(1,1);
        LatLng l2 = new LatLng(-1,-1);
        LatLng l3 = new LatLng(1, -1);
        LatLng l4 = new LatLng(-1,1);
        LatLng l5 = new LatLng(0,0);
        List<LatLng> latlnglist = new ArrayList<>();
        latlnglist.add(l1);
        latlnglist.add(l2);
        latlnglist.add(l3);
        latlnglist.add(l4);
        assertEquals(l5, Util.getCenterLatLng(latlnglist));
    }
}
