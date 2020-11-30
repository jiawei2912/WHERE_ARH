package com.example.where_arh.ui.origins;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//The class that is adapted by the adaptor before being placed into the originsfragment.
//Contains the information we want to present.
//No gui information
public class OriginContent {
    //Private constructor
    private OriginContent(){};

    //List and map storing the origincontents
    private static final List<OriginItem> ITEMS = new ArrayList<OriginItem>();
    private static final Map<String, OriginItem> ITEM_MAP = new HashMap<String, OriginItem>();
    private static final ArrayList<OriginsRecyclerViewAdapter> OBSERVERS = new ArrayList<>();
    private static boolean intentionally_empty = false;
    private static List<OriginItem> COMMITTED_ITEMS = new ArrayList<>();

    //Getters and setters for COMMITTED_ITEMS
    public static void commitItems(){
        if (ITEMS.isEmpty()){
            intentionally_empty = true;
        }else{
            intentionally_empty = false;
        }
        COMMITTED_ITEMS = new ArrayList<>(ITEMS);
    }
    public static void setPlacesAsComittedItems(List<Place> comittedplaces){
        int id = 0;
        for (Place place:comittedplaces){
            OriginItem o = new OriginItem(String.valueOf(id), place.getName(), place.getLatLng());
            COMMITTED_ITEMS.add(o);
            id += 1;
        }
    }
    public static List<OriginItem> getCommittedItems(){
        return COMMITTED_ITEMS;
    }
    public static List<LatLng> getComittedItemAsLatLngs(){
        ArrayList<LatLng> coords = new ArrayList<>();
        for (OriginItem o:COMMITTED_ITEMS){
            coords.add(o.coords);
        }
        return coords;
    }
    public static List<Place> getComittedItemsAsPlaces(){
        List<Place> places = new ArrayList<>();
        for (OriginContent.OriginItem o:COMMITTED_ITEMS){
            Place.Builder place_builder = Place.builder();
            place_builder.setName(o.name);
            place_builder.setLatLng(o.coords);
            Place place = place_builder.build();
            places.add(place);
        }
        return places;
    }
    public static boolean isEmpty(){
        if (COMMITTED_ITEMS.isEmpty()){
            return true;
        }
        return false;
    }
    public static boolean isIntentionallyEmpty(){
        return intentionally_empty;
    }

    //Getters
    public static List<OriginItem> getItems(){
        return ITEMS;
    }
    public static List<LatLng> getItemAsLatLngs(){
        ArrayList<LatLng> coords = new ArrayList<>();
        for (OriginItem o:ITEMS){
            coords.add(o.coords);
        }
        return coords;
    }
    public static List<Place> getItemsAsPlaces(){
        List<Place> places = new ArrayList<>();
        for (OriginContent.OriginItem o:ITEMS){
            Place.Builder place_builder = Place.builder();
            place_builder.setName(o.name);
            place_builder.setLatLng(o.coords);
            Place place = place_builder.build();
            places.add(place);
        }
        return places;
    }
    //Setters
    public static void addItem(OriginItem item) {
        addItem(item, false);
    }
    public static void addItem(OriginItem item, boolean notify) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
        if (notify){
            notifyViewAdapters();
        }
    }
    public static void addItem(String name, LatLng latlng){
        addItem(name, latlng, false);
    }
    public static void addItem(String name, LatLng latlng, boolean notify){
        OriginItem originitem = new OriginItem(String.valueOf(ITEMS.size()+1), name, latlng);
        addItem(originitem);
        if (notify){
            notifyViewAdapters();
        }
    }
    public static void addPlaceAsItem(Place place){
        addPlaceAsItem(place, false);
    }
    public static void addPlaceAsItem(Place place, boolean notify){
        OriginItem originitem = new OriginItem(String.valueOf(ITEMS.size()+1), place.getName(), place.getLatLng());
        addItem(originitem);
        if (notify){
            notifyViewAdapters();
        }
    }
    public static void removeItem(OriginItem originitem){
        removeItem(originitem, false);
    }
    public static void removeItem(OriginItem originitem, boolean notify){
        ITEMS.remove(originitem);
        if (notify){
            notifyViewAdapters();
        }
    }
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
    public static boolean itemWithNameInOriginContent(String name){
        for (OriginItem originitem:ITEMS){
            if (originitem.name.equals(name)){
                return true;
            }
        }
        return false;
    }
    public static void deleteItemWithNameIfExists(String name){
        for (OriginItem originitem:ITEMS){
            if (originitem.name.equals(name)){
                ITEMS.remove(originitem);
                return;
            }
        }
    }

    //Some version of observer-subscriber dp
    public static void addViewAdapter(OriginsRecyclerViewAdapter va){
        OBSERVERS.add(va);
    }
    public static void removeViewAdapter(OriginsRecyclerViewAdapter va){
        OBSERVERS.remove(va);
    }
    public static void notifyViewAdapters(){
        for (OriginsRecyclerViewAdapter va:OBSERVERS){
            va.updateDataSet(ITEMS);
            va.notifyDataSetChanged();
        }
    }


    //OriginItem
    public static class OriginItem {
        public String id;
        public final String name;
        public final LatLng coords;
        public OriginItem(String id, String name, LatLng coords) {
            this.id = id;
            this.name = name;
            this.coords = coords;
        }
        @Override
        public String toString() {
            return name;
        }
    }
}