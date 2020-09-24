package com.example.jlne.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jlne.R;
import com.example.jlne.model.Transportation;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class TransportationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final String TAG = "__MChallanAdapter";
    private ArrayList<Transportation> transportationData;

    public TransportationAdapter(ArrayList<Transportation> transportationData) {
        this.transportationData = transportationData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transportation_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        Transportation transportation = transportationData.get(position);

        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).noTV.setText(String.valueOf(position + 1));
            ((ViewHolder) holder).sentFromTV.setText(transportation.getSentFrom());
            ((ViewHolder) holder).sentToTV.setText(transportation.getSentTo());
            ((ViewHolder) holder).dateTV.setText(transportation.getDate());
        }
    }

    @Override
    public int getItemCount() {
        if (transportationData != null) {
            return transportationData.size();
        } else return 0;
    }

    public void updateData(ArrayList<Transportation> transportationData) {
        if (this.transportationData != transportationData) {
            this.transportationData = transportationData;
        }

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public MaterialTextView noTV, sentFromTV, sentToTV, dateTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noTV = itemView.findViewById(R.id.no_TV);
            sentFromTV = itemView.findViewById(R.id.sent_from_TV);
            sentToTV = itemView.findViewById(R.id.sent_to_TV);
            dateTV = itemView.findViewById(R.id.date_TV);
        }
    }
}
