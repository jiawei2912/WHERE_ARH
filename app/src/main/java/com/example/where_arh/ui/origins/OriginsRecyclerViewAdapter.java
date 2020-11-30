package com.example.where_arh.ui.origins;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.where_arh.R;

import java.util.List;

public class OriginsRecyclerViewAdapter extends RecyclerView.Adapter<OriginsRecyclerViewAdapter.ViewHolder>{
    //List of children views
    private List<OriginContent.OriginItem> ITEMS;
    //Receive initial list of children views from OriginContent
    public OriginsRecyclerViewAdapter(List<OriginContent.OriginItem> items) {
        ITEMS = items;
    }
    //Updates list of items
    public void updateDataSet(List<OriginContent.OriginItem> items){
        this.ITEMS = items;
    }

    @Override
    public OriginsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_origins, parent, false);
        return new OriginsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OriginsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.item = ITEMS.get(position);
        holder.itemIdView.setText(ITEMS.get(position).id);
        holder.itemContentView.setText(ITEMS.get(position).name);
        holder.itemDeleteButton.setText("-");
        holder.itemDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int removedItemId = Integer.parseInt(holder.item.id);
                for (OriginContent.OriginItem target_item: ITEMS){
                    if(Integer.parseInt(target_item.id) > removedItemId){
                        target_item.id = Integer.toString(Integer.parseInt(target_item.id)-1);
                    }
                }
                OriginContent.removeItem(holder.item, true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ITEMS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView itemIdView;
        public final TextView itemContentView;
        public OriginContent.OriginItem item;
        public final Button itemDeleteButton;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            itemIdView = (TextView) view.findViewById(R.id.item_number);
            itemContentView = (TextView) view.findViewById(R.id.content);
            itemDeleteButton = view.findViewById(R.id.deleteButton);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + itemContentView.getText() + "'";
        }
    }
}
