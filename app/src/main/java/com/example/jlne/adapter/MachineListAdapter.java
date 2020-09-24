package com.example.jlne.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jlne.R;
import com.example.jlne.animation.Animations;
import com.example.jlne.fragment.DeleteMachineFragment;
import com.example.jlne.fragment.UpdateMachineFragment;
import com.example.jlne.fragment.ViewMachineFragment;
import com.example.jlne.model.Machine;
import com.google.android.material.textview.MaterialTextView;
import java.util.ArrayList;

public class MachineListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Machine> machines;
    private final Integer SHOW_OPTION = 1, HIDE_OPTION = 0;
    private FragmentManager fragmentManager;

    public MachineListAdapter(ArrayList<Machine> machines, FragmentManager fragmentManager) {
        this.machines = machines;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getItemViewType(int position) {
        if (machines.get(position).isOptioned()) {
            return SHOW_OPTION;
        } else {
            return HIDE_OPTION;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SHOW_OPTION) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.machine_option_item, parent, false);

            return new MenuHolder(view);
        }

        else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.machine_list_item, parent, false);

            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final Machine machine = machines.get(position);

            ((ViewHolder) holder).machineNameTV.setText(machine.getName());
            ((ViewHolder) holder).machineBrandTV.setText(machine.getBrand());
            ((ViewHolder) holder).machineModelTV.setText(machine.getModel());
            ((ViewHolder) holder).machineSerialTV.setText(machine.getSerial());
            ((ViewHolder) holder).machineOwnerTV.setText(machine.getOwner());
            ((ViewHolder) holder).machineChallanTV.setText(String.valueOf(machine.getChallan()));
            ((ViewHolder) holder).transportationDateTV.setText(machine.getDate());
            if (machine.isRunning_status()) {
                ((ViewHolder) holder).machineRunningTV.setText(R.string.boolean_true_text);
            } else {
                ((ViewHolder) holder).machineRunningTV.setText(R.string.boolean_false_text);
            }
            ((ViewHolder) holder).machineRentTV.setText(String.valueOf(machine.getAmount()));

            ((ViewHolder) holder).viewLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewMachineFragment viewMachineFragment = ViewMachineFragment.newInstance(machine.getId(), machine.getTransaction_id()
                            ,machine.getName(), machine.getBrand(), machine.getModel(), machine.getSerial()
                            , machine.getOwner(), machine.getChallan(), machine.getDate(), machine.getTransactionType()
                            , machine.getSentFrom(), machine.getSentTo(), machine.getAmount(), machine.getRemarks());

                    FragmentTransaction transaction = fragmentManager.beginTransaction()
                            .add(viewMachineFragment, "__ViewMachine").addToBackStack(null);
                    transaction.replace(R.id.main_host_FL, viewMachineFragment);
                    transaction.commit();
                }
            });

            ((ViewHolder) holder).expandIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean flag = toggleLayout(machine.isExpanded(), v, ((ViewHolder) holder).expandedLL);
                    machine.setExpanded(flag); // Set expanded or not expanded in model
                }
            });
        }

        else if (holder instanceof MenuHolder) {
            ((MenuHolder) holder).editLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Machine machine = machines.get(position);

                    UpdateMachineFragment updateMachineFragment =
                            UpdateMachineFragment.newInstance(machine.getId(), machine.getTransaction_id(), machine.getName()
                                    , machine.getBrand(), machine.getModel(), machine.getSerial(), machine.getOwner()
                                    , machine.getSentFrom(), machine.getSentTo(), machine.getChallan()
                                    , machine.getDate(), machine.getTransactionType(), machine.getAmount()
                                    , machine.getRemarks());

                    updateMachineFragment.show(fragmentManager, "__UpdateMachine");
                }
            });

            ((MenuHolder) holder).deleteLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Machine machine = machines.get(position);

                    DeleteMachineFragment deleteMachineFragment = DeleteMachineFragment.newInstance(
                            machine.getId(),
                            machine.getName(),
                            machine.getBrand(),
                            machine.getModel(),
                            machine.getSerial(),
                            machine.getChallan(),
                            machine.getDate(),
                            machine.getOwner(),
                            machine.getSentTo()
                    );

                    deleteMachineFragment.show(fragmentManager, "__DeleteMachine");
                }
            });

            ((MenuHolder) holder).closeLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideOptions(position);
                }
            });
        }
    }

    private boolean toggleLayout(boolean isExpanded, View v, LinearLayout expandLL) {
        boolean flag = Animations.toggleArrow(v, isExpanded);
        if (flag) {
            Animations.expand(expandLL);
        } else {
            Animations.collapse(expandLL);
        }

        return flag;
    }

    public void showOptions(int position) {
        machines.get(position).setOptioned(true);
        notifyDataSetChanged();
    }

    public void hideOptions(int position) {
        machines.get(position).setOptioned(false);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (machines != null) {
            return machines.size();
        }

        else return 0;
    }

    public void updateData(ArrayList<Machine> machines) {
        if (this.machines != machines) {
            this.machines = machines;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public MaterialTextView machineNameTV, machineBrandTV, machineModelTV, machineSerialTV, machineOwnerTV
                , machineChallanTV, transportationDateTV, machineRunningTV, machineRentTV;
        private CardView machineCV;
        public ImageView expandIV;
        public LinearLayout viewLL, expandedLL;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            machineNameTV = itemView.findViewById(R.id.machine_name_TV);
            machineBrandTV = itemView.findViewById(R.id.machine_brand_TV);
            machineModelTV = itemView.findViewById(R.id.machine_model_TV);
            machineSerialTV = itemView.findViewById(R.id.machine_serial_TV);
            machineOwnerTV = itemView.findViewById(R.id.machine_owner_TV);
            machineChallanTV = itemView.findViewById(R.id.machine_challan_TV);
            transportationDateTV = itemView.findViewById(R.id.transportation_date_TV);
            machineRunningTV = itemView.findViewById(R.id.machine_running_TV);
            machineRentTV = itemView.findViewById(R.id.machine_rent_TV);
            machineCV = itemView.findViewById(R.id.machine_CV);
            expandIV = itemView.findViewById(R.id.expand_IV);
            viewLL = itemView.findViewById(R.id.view_LL);
            expandedLL = itemView.findViewById(R.id.expanded_LL);
        }
    }

    public class MenuHolder extends RecyclerView.ViewHolder {
        public LinearLayout editLL, deleteLL, closeLL;

        public MenuHolder(@NonNull View itemView) {
            super(itemView);

            editLL = itemView.findViewById(R.id.edit_LL);
            deleteLL = itemView.findViewById(R.id.delete_LL);
            closeLL = itemView.findViewById(R.id.close_LL);
        }
    }
}
