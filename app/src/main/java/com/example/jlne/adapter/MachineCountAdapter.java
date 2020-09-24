package com.example.jlne.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jlne.R;
import java.util.HashMap;

public class MachineCountAdapter extends RecyclerView.Adapter<MachineCountAdapter.ViewHolder> {
    HashMap<String, Integer> machineSummaryData;

    public MachineCountAdapter(HashMap<String, Integer> machineSummaryData) {
        this.machineSummaryData = machineSummaryData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.machine_summary_element, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.machineNameTV.setText(machineSummaryData.keySet().toArray()[position].toString());
        holder.machineCountTV.setText(machineSummaryData.values().toArray()[position].toString());
    }

    @Override
    public int getItemCount() {
        if (machineSummaryData != null) {
            return machineSummaryData.size();
        }

        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView machineNameTV, machineCountTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            machineNameTV = itemView.findViewById(R.id.machine_name_TV);
            machineCountTV = itemView.findViewById(R.id.machine_count_TV);
        }
    }
}
