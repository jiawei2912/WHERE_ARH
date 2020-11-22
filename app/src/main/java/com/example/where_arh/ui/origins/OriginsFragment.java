package com.example.where_arh.ui.origins;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.where_arh.R;

/**
 * A fragment representing a list of Items.
 */
public class OriginsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    Button addOriginButton;
    private OriginsRecyclerViewAdapter orva;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OriginsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static OriginsFragment newInstance(int columnCount) {
        OriginsFragment fragment = new OriginsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View nview = inflater.inflate(R.layout.fragment_origins_list, container, false);
        View view = nview.findViewById(R.id.list);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            orva = new OriginsRecyclerViewAdapter(OriginContent.ITEMS);
            recyclerView.setAdapter(orva);
        }

        addOriginButton = nview.findViewById(R.id.add_origin_button);
        addOriginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int id = OriginContent.ITEMS.size()+1;
                OriginContent.OriginItem originItem = new OriginContent.OriginItem(Integer.toString(id), "");
                OriginContent.ITEMS.add(originItem);
                orva.notifyDataSetChanged();
            }
        });

        return nview;
    }
}