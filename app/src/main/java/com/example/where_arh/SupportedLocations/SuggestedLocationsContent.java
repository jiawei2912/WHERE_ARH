package com.example.where_arh.SupportedLocations;

import android.util.Log;

import com.google.maps.model.PlacesSearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuggestedLocationsContent {
    //private constructor
    private SuggestedLocationsContent(){};

    //information
    private static final List<SuggestedLocationsItem> ITEMS = new ArrayList<>();
    private static final Map<String, SuggestedLocationsItem> ITEM_MAP = new HashMap<>();
    private static final ArrayList<SuggestedLocationsViewAdapter> OBSERVERS = new ArrayList<>();

    //getters
    public static List<SuggestedLocationsItem> getItems(){
        return ITEMS;
    }
    //setters - adders
    public static void addItem(SuggestedLocationsItem item) {
        addItem(item, false);
    }
    public static void addItem(SuggestedLocationsItem item, boolean notify) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
        if (notify){
            notifyViewAdapters();
        }
    }
    public static void addPlacesSearchResult(PlacesSearchResult psr){
        SuggestedLocationsItem item = new SuggestedLocationsItem(String.valueOf(ITEMS.size()+1), psr.name, psr.formattedAddress, psr.placeId);
        addPlacesSearchResult(psr, false);
    }
    public static void addPlacesSearchResult(PlacesSearchResult psr, boolean notify){
        SuggestedLocationsItem item = new SuggestedLocationsItem(String.valueOf(ITEMS.size()+1), psr.name, psr.vicinity, psr.placeId);
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
        if (notify){
            notifyViewAdapters();
        }
    }
    //setters - removers
    public static void removeItem(SuggestedLocationsItem item){
        removeItem(item, false);
    }
    public static void removeItem(SuggestedLocationsItem item, boolean notify){
        ITEMS.remove(item);
        if (notify){
            notifyViewAdapters();
        }
    }
    //setters - clearers
    public static void clearItems(){
        ITEMS.clear();
        clearItems(false);
    }
    public static void clearItems(boolean notify){
        ITEMS.clear();
        if(notify) {
            notifyViewAdapters();
        }
    }


    //Some version of observer-subscriber dp
    public static void addViewAdapter(SuggestedLocationsViewAdapter va){
        OBSERVERS.add(va);
    }
    public static void removeViewAdapter(SuggestedLocationsViewAdapter va){
        OBSERVERS.remove(va);
    }
    public static void notifyViewAdapters(){
        for (SuggestedLocationsViewAdapter va:OBSERVERS){
            va.updateDataSet(ITEMS);
            va.notifyDataSetChanged();
        }
    }



    //Item
    public static class SuggestedLocationsItem{
        public String id;
        public final String name;
        public final String address;
        public final String address_id;
        public SuggestedLocationsItem(String id, String name, String address, String address_id) {
            this.id = id;
            this.name = name;
            this.address = address;
            this.address_id = address_id;
        }
        @Override
        public String toString() {
            return name;
        }
    }
}
