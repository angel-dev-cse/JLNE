package com.example.jlne.fragment;

import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jlne.R;
import com.example.jlne.adapter.MachineListAdapter;
import com.example.jlne.helper.AsyncTaskHelper;
import com.example.jlne.helper.URLS;
import com.example.jlne.model.Machine;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.crypto.Mac;

public class MachineListFragment extends Fragment {
    final String TAG = "__MachineList";
    private MaterialTextView organizationNameTV;
    private RecyclerView machineListRV;
    private MachineListAdapter machineListAdapter;
    private String organizationName;

    public MachineListFragment() {
        // Required empty public constructor
    }


    public static MachineListFragment newInstance(String organizationName, String type) {
        MachineListFragment machineListFragment = new MachineListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("organization_name", organizationName);
        bundle.putString("type", type);
        machineListFragment.setArguments(bundle);

        return machineListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            organizationName = getArguments().getString("organization_name");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_machine_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            organizationNameTV = getActivity().findViewById(R.id.organization_name_TV);
            organizationNameTV.setText(organizationName);

            machineListRV = getActivity().findViewById(R.id.machine_list_RV);
        }

        machineListAdapter = new MachineListAdapter(null, getParentFragmentManager());

        machineListRV.setAdapter(machineListAdapter);
        machineListRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        machineListRV.setHasFixedSize(true);

        // Setting up showing options while swiped
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            private final ColorDrawable background = new ColorDrawable(getResources().getColor(R.color.md_blue_50));
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                machineListAdapter.showOptions(viewHolder.getAdapterPosition());
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;

                if (dX > 0) {
                    background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX), itemView.getBottom());
                } else if (dX < 0) {
                    background.setBounds(itemView.getRight() + ((int) dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                } else {
                    background.setBounds(0, 0, 0, 0);
                }

                background.draw(c);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(machineListRV);

        // Fetch Machine Data
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("organization_name", organizationName);

        AsyncTaskHelper asyncTaskHelper = (AsyncTaskHelper) new AsyncTaskHelper(URLS.GET_MACHINES, "POST", parameters, new AsyncTaskHelper.AsyncResponse() {
            @Override
            public void processFinish(String result) {
                // Data received here
                Log.d(TAG, result);
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    if (!jsonObject.getBoolean("error")) {
                        JSONArray machinesArray = jsonObject.getJSONArray("machines");

                        ArrayList<Machine> machines = new ArrayList<>();

                        for (int i = 0; i < machinesArray.length(); i++) {
                            JSONObject machine = machinesArray.getJSONObject(i);

                            Machine machineArray = new Machine(
                                    machine.getInt("id"),
                                    machine.getString("name"),
                                    machine.getString("brand"),
                                    machine.getString("model"),
                                    machine.getString("serial"),
                                    machine.getString("owner"),
                                    machine.getString("running_status"),
                                    machine.getInt("transaction_id"),
                                    machine.getString("sent_from"),
                                    machine.getString("sent_to"),
                                    machine.getString("date"),
                                    machine.getString("type"),
                                    machine.getInt("challan"),
                                    machine.getInt("amount"),
                                    machine.getString("remarks")
                            );

                            machines.add(machineArray);
                        }

                        machineListAdapter.updateData(machines);
                    }

                } catch (JSONException e) {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }
            }
        }).execute();
    }
}