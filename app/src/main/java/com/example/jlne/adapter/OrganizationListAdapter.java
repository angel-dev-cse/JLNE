package com.example.jlne.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jlne.R;
import com.example.jlne.model.OrganizationListModel;
import java.util.ArrayList;

public class OrganizationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<OrganizationListModel> organizationList;
    private ItemClickListener itemClickListener;

    public OrganizationListAdapter(ArrayList<OrganizationListModel> organizationList) {
        this.organizationList = organizationList;
    }

    public interface ItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_elements, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        OrganizationListModel listModel = organizationList.get(position);

        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).organizationNameTV.setText(listModel.getName());
        }
    }

    @Override
    public int getItemCount() {
        if (organizationList != null) return organizationList.size();
        else return 0;
    }

    public void updateData(ArrayList<OrganizationListModel> organizationList) {
        this.organizationList = organizationList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener{
        public TextView organizationNameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            organizationNameTV = itemView.findViewById(R.id.category_TV);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
