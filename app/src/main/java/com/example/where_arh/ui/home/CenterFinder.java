package com.example.where_arh.ui.home;

import android.util.Log;

import com.example.where_arh.Util.Util;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public abstract class CenterFinder {

    private CenterFinder(){
    };

    abstract LatLng getCenter(List<LatLng> latlnglist);

    public static CenterFinder getCartesianFinder(){
        return new CartesianFinder();
    }

    public static CenterFinder getUnweightedFinder(boolean overpower){
        UnweightedFinder finder = new UnweightedFinder();
        finder.setOverpower(overpower);
        return (CenterFinder) finder;
    }

    //inner classes

    private static class CartesianFinder extends CenterFinder{
        @Override
        LatLng getCenter(List<LatLng> latlnglist) {
            if (latlnglist == null || latlnglist.size() < 1){
                Log.e("CenterFinder", "Latlnglist is problematic!");
                return null;
            }
            // Calculated centre point
            double centerlat = 0;
            double centerlng = 0;
            for (LatLng latLng : latlnglist) {
                centerlat += latLng.latitude;
                centerlng += latLng.longitude;
            }
            centerlat = centerlat / latlnglist.size();
            centerlng = centerlng / latlnglist.size();
            return new LatLng(centerlat, centerlng);

        }
    }

    private static class UnweightedFinder extends CenterFinder{
        private boolean overpower;
        public void setOverpower(boolean overpower){
            this.overpower = overpower;
        }
        @Override
        LatLng getCenter(List<LatLng> latlnglist) {
            if (latlnglist == null || latlnglist.size() < 1){
                Log.e("CenterFinder", "Latlnglist is problematic!");
                return null;
            }
            //MACHINE LEARNING
            //Parameters
            double step_size = 0.00001;

            //Initial point
            LatLng current_centre = Util.getCenterLatLng(latlnglist);

            //Main Loop of algorithm
            int safety = 1000;
            List<LatLng> working_latlnglist = new ArrayList<>(latlnglist);
            if (this.overpower){
                safety = 100000;
            }
            while(safety > 0){
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
            return current_centre;
        }
    }
}
