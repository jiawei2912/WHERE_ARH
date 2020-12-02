package com.example.where_arh;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.where_arh.ui.origins.OriginContent;
import com.example.where_arh.ui.origins.OriginsRecyclerViewAdapter;

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
        holder.itemContentView.setText(ITEMS.get(position).name);
        holder.goButton.setText("->");
        holder.goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int removedItemId = Integer.parseInt(holder.item.id);
                for (SuggestedLocationsContent.SuggestedLocationsItem target_item: ITEMS){
                    if(Integer.parseInt(target_item.id) > removedItemId){
                        target_item.id = Integer.toString(Integer.parseInt(target_item.id)-1);
                    }
                }
                SuggestedLocationsContent.removeItem(holder.item, true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ITEMS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView itemContentView;
        public SuggestedLocationsContent.SuggestedLocationsItem item;
        public final Button goButton;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            itemContentView = (TextView) view.findViewById(R.id.suggested_location_content);
            goButton = view.findViewById(R.id.goButton);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + itemContentView.getText() + "'";
        }
    }
}
