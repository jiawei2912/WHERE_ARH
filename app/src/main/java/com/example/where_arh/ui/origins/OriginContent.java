package com.example.where_arh.ui.origins;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class OriginContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<OriginItem> ITEMS = new ArrayList<OriginItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, OriginItem> ITEM_MAP = new HashMap<String, OriginItem>();

    private static final int COUNT = 0;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createOriginItem(i, new LatLng(0,0)));
        }
    }

    public static List<OriginItem> getItems(){
        return ITEMS;
    }

    public static List<LatLng> getItemLatLngs(){
        ArrayList<LatLng> coords = new ArrayList<>();
        for (OriginItem o:ITEMS){
            coords.add(o.coords);
        }
        return coords;
    }

    public static void addItem(OriginItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void clearItems(){
        ITEMS.clear();
    }

    private static OriginItem createOriginItem(int position, LatLng coords) {
//        return new OriginItem(String.valueOf(position), "Item " + position, makeDetails(position));
        return new OriginItem(String.valueOf(position), "Item " + position, coords);
    }

//    private static String makeDetails(int position) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("Details about Item: ").append(position);
//        for (int i = 0; i < position; i++) {
//            builder.append("\nMore details information here.");
//        }
//        return builder.toString();
//    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class OriginItem {
        public String id;
        public final String content;
        public final LatLng coords;

        public OriginItem(String id, String content, LatLng coords) {
            this.id = id;
            this.content = content;
            this.coords = coords;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}