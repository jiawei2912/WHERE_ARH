package com.example.where_arh.ui.origins;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.where_arh.R;

import java.util.List;
/**
 * {@link RecyclerView.Adapter} that can display a {@link OriginContent.OriginItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class OriginsRecyclerViewAdapter extends RecyclerView.Adapter<OriginsRecyclerViewAdapter.ViewHolder>{

    private List<OriginContent.OriginItem> mValues;

    public OriginsRecyclerViewAdapter(List<OriginContent.OriginItem> items) {
        mValues = items;
    }

    @Override
    public OriginsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_origins, parent, false);
        return new OriginsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OriginsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);
        holder.deleteButton.setText("-");
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int removedItemId = Integer.parseInt(holder.mItem.id);
                for (OriginContent.OriginItem target_item:mValues){
                    if(Integer.parseInt(target_item.id) > removedItemId){
                        target_item.id = Integer.toString(Integer.parseInt(target_item.id)-1);
                    }
                }
                mValues.remove(holder.mItem);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public OriginContent.OriginItem mItem;
        public final Button deleteButton;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            deleteButton = view.findViewById(R.id.deleteButton);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
