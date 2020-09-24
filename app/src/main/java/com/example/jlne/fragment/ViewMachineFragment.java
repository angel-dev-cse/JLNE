package com.example.jlne.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jlne.R;
import com.example.jlne.adapter.TransportationAdapter;
import com.example.jlne.helper.AsyncTaskHelper;
import com.example.jlne.helper.URLS;
import com.example.jlne.model.Machine;
import com.example.jlne.model.Transportation;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewMachineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewMachineFragment extends Fragment {
    final String TAG = "__ViewMachine";
    private MaterialTextView nameTV, brandTV, modelTV, serialTV, ownerTV, challanTV, dateTV, typeTV, sentFromTV, sentToTV, amountTV, remarksTV;
    private RecyclerView transportationRV;
    private String name, brand, model, serial, owner, date, type, sentFrom, sentTo, remarks;
    private Integer id, transactionID, challan, amount;

    public ViewMachineFragment() {
        // Required empty public constructor
    }

    public static ViewMachineFragment newInstance(Integer id, Integer transactionID, String name, String brand
            , String model, String serial, String owner, Integer challan, String date
            , String type, String sentFrom, String sentTo, Integer amount, String remarks) {

        ViewMachineFragment viewMachineFragment = new ViewMachineFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putInt("transaction_id", transactionID);
        bundle.putString("name", name);
        bundle.putString("brand", brand);
        bundle.putString("model", model);
        bundle.putString("serial", serial);
        bundle.putString("owner", owner);
        bundle.putInt("challan", challan);
        bundle.putString("date", date);
        bundle.putString("type", type);
        bundle.putString("sentFrom", sentFrom);
        bundle.putString("sentTo", sentTo);
        bundle.putInt("amount", amount);
        bundle.putString("remarks", remarks);

        viewMachineFragment.setArguments(bundle);

        return viewMachineFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            id = getArguments().getInt("id");
            transactionID = getArguments().getInt("transaction_id");
            name = getArguments().getString("name");
            brand = getArguments().getString("brand");
            model = getArguments().getString("model");
            serial = getArguments().getString("serial");
            owner = getArguments().getString("owner");
            challan = getArguments().getInt("challan");
            date = getArguments().getString("date");
            type = getArguments().getString("type");
            sentFrom = getArguments().getString("sentFrom");
            sentTo = getArguments().getString("sentTo");
            amount = getArguments().getInt("amount");
            remarks = getArguments().getString("remarks");
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.view_machine_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();

        if (itemID == R.id.update_machine) {
            UpdateMachineFragment updateMachineFragment =
                    UpdateMachineFragment.newInstance(id, transactionID, name, brand, model, serial
                            , owner, sentFrom, sentTo, challan, date, type, amount, remarks);

            updateMachineFragment.show(getParentFragmentManager(), "__UpdateMachine");
        } else if (itemID == R.id.delete_machine) {
            DeleteMachineFragment deleteMachineFragment = DeleteMachineFragment.newInstance(
                    id, name, brand, model, serial, challan, date, owner, sentTo);

            deleteMachineFragment.show(getParentFragmentManager(), "__DeleteMachine");

        }

        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_machine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            nameTV = getActivity().findViewById(R.id.name_TV);
            brandTV = getActivity().findViewById(R.id.brand_TV);
            modelTV = getActivity().findViewById(R.id.model_TV);
            serialTV = getActivity().findViewById(R.id.serial_TV);
            ownerTV = getActivity().findViewById(R.id.owner_TV);
            challanTV = getActivity().findViewById(R.id.challan_TV);
            dateTV = getActivity().findViewById(R.id.date_TV);
            typeTV = getActivity().findViewById(R.id.type_TV);
            sentFromTV = getActivity().findViewById(R.id.sent_from_TV);
            sentToTV = getActivity().findViewById(R.id.sent_to_TV);
            amountTV = getActivity().findViewById(R.id.amount_TV);
            remarksTV = getActivity().findViewById(R.id.remarks_TV);
            transportationRV = getActivity().findViewById(R.id.transportation_RV);
        }

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("id", String.valueOf(id));
        parameters.put("transaction_id", String.valueOf(transactionID));
        AsyncTaskHelper asyncTaskHelper = (AsyncTaskHelper) new AsyncTaskHelper(URLS.GET_MACHINE, "POST", parameters, new AsyncTaskHelper.AsyncResponse() {
            @Override
            public void processFinish(String result) {
                // Data received here
                Log.d(TAG, result);
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    if (!jsonObject.getBoolean("error")) {
                        JSONObject machineObject  = jsonObject.getJSONObject("machine");

                        name = machineObject.getString("name");
                        brand = machineObject.getString("brand");
                        model = machineObject.getString("model");
                        serial = machineObject.getString("serial");
                        owner = machineObject.getString("owner");
                        challan = machineObject.getInt("challan");
                        date = machineObject.getString("date");
                        type = machineObject.getString("type");
                        sentFrom = machineObject.getString("sent_from");
                        sentTo = machineObject.getString("sent_to");
                        amount = machineObject.getInt("amount");
                        remarks = machineObject.getString("remarks");

                        // Setting up data
                        nameTV.setText(name);
                        brandTV.setText(brand);
                        modelTV.setText(model);
                        serialTV.setText(serial);
                        ownerTV.setText(owner);
                        challanTV.setText(String.valueOf(challan));
                        dateTV.setText(date);
                        typeTV.setText(type);
                        sentFromTV.setText(sentFrom);
                        sentToTV.setText(sentTo);
                        amountTV.setText(String.valueOf(amount));
                        remarksTV.setText(remarks);
                    }

                    Log.d(TAG, jsonObject.getString("message"));

                } catch (JSONException e) {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }
            }
        }).execute();

        // Setting up the transportation recycler view
        final TransportationAdapter transportationAdapter = new TransportationAdapter(null);
        transportationRV.setHasFixedSize(true);
        transportationRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        transportationRV.setAdapter(transportationAdapter);

        // Fetch machine transportation history
        parameters = new HashMap<>();
        parameters.put("machine_id", String.valueOf(id));

        asyncTaskHelper = (AsyncTaskHelper) new AsyncTaskHelper(URLS.GET_TRANSPORTATION_BY_MACHINE, "POST", parameters, new AsyncTaskHelper.AsyncResponse() {
            @Override
            public void processFinish(String result) {
                // Data received here
                Log.d(TAG, result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    ArrayList<Transportation> transportationData = new ArrayList<>();

                    if (!jsonObject.getBoolean("error")) {
                        JSONArray transportationArray = jsonObject.getJSONArray("transportations");

                        for (int i = 0; i < transportationArray.length(); i++) {
                            JSONObject transportationObject = transportationArray.getJSONObject(i);

                            Transportation transportation = new Transportation(
                                    transportationObject.getInt("transportation_id"),
                                    id,
                                    transportationObject.getInt("challan"),
                                    transportationObject.getInt("amount"),
                                    transportationObject.getString("sent_from"),
                                    transportationObject.getString("sent_to"),
                                    transportationObject.getString("date"),
                                    transportationObject.getString("type"),
                                    transportationObject.getString("remarks")
                            );

                            transportationData.add(transportation);
                        }

                        transportationAdapter.updateData(transportationData);
                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }
            }
        }).execute();
    }
}