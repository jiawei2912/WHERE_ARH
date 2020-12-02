package com.example.where_arh.SupportedLocations;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.where_arh.R;

import java.util.List;

public class SuggestedLocationsViewAdapter extends RecyclerView.Adapter<SuggestedLocationsViewAdapter.ViewHolder> {
    //information
    //List of children views
    public List<SuggestedLocationsContent.SuggestedLocationsItem> ITEMS;
    //Receive initial list of children views from OriginContent
    public SuggestedLocationsViewAdapter(List<SuggestedLocationsContent.SuggestedLocationsItem> items) {
        ITEMS = items;
    }
    //Updates list of items
    public void updateDataSet(List<SuggestedLocationsContent.SuggestedLocationsItem> items){
        this.ITEMS = items;
    }

    @NonNull
    @Override
    public SuggestedLocationsViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_suggested_locations_item, parent, false);
        return new SuggestedLocationsViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.item = ITEMS.get(position);
        holder.itemAddressNameView.setText(ITEMS.get(position).name);
        holder.itemAddressView.setText(ITEMS.get(position).address);
        holder.goButton.setText("Go");
        holder.goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "https://www.google.com/maps/search/?api=1&query=Eiffel%20Tower&query_place_id=" + ITEMS.get(position).address_id;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ITEMS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public SuggestedLocationsContent.SuggestedLocationsItem item;
        public final TextView itemAddressNameView;
        public final TextView itemAddressView;
        public final Button goButton;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            itemAddressNameView = (TextView) view.findViewById(R.id.suggested_location_name);
            itemAddressView = (TextView) view.findViewById(R.id.suggested_location_address);
            goButton = view.findViewById(R.id.goButton);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + itemAddressNameView.getText() + "'";
        }
    }
}
