package com.example.jlne.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.jlne.AddMachineActivity;
import com.example.jlne.R;
import com.example.jlne.helper.AsyncTaskHelper;
import com.example.jlne.helper.URLS;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AddMachineFragment extends DialogFragment {
    final String TAG = "__AddMachine";
    private AutoCompleteTextView nameET, brandET, modelET, ownerET, sentFromET, sentToET;
    private TextInputEditText serialET, challanET, dateET, amountET, remarksET;
    private ProgressBar addPB;
    private String type;
    private MaterialButton dateBT, addBT;
    private RadioGroup typeRG;

    public AddMachineFragment() {
        // Required empty public constructor
    }

    public static AddMachineFragment newInstance() {
        AddMachineFragment fragment = new AddMachineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_machine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        nameET = view.findViewById(R.id.name_ET);
        brandET = view.findViewById(R.id.brand_ET);
        modelET = view.findViewById(R.id.model_ET);
        ownerET = view.findViewById(R.id.owner_ET);
        sentFromET = view.findViewById(R.id.sent_from_ET);
        sentToET = view.findViewById(R.id.sent_to_ET);
        serialET = view.findViewById(R.id.serial_ET);
        challanET = view.findViewById(R.id.challan_ET);
        dateET = view.findViewById(R.id.date_ET);
        amountET = view.findViewById(R.id.amount_ET);
        remarksET = view.findViewById(R.id.remarks_ET);
        dateBT = view.findViewById(R.id.set_date_BT);
        addBT = view.findViewById(R.id.add_BT);
        addPB = view.findViewById(R.id.add_PB);
        typeRG = view.findViewById(R.id.type_radio_group);

        // Populate selection list
        AsyncTaskHelper asyncTaskHelper = (AsyncTaskHelper) new AsyncTaskHelper(URLS.GET_MACHINE_FIELDS_URL, "GET", new AsyncTaskHelper.AsyncResponse() {
            @Override
            public void processFinish(String result) {
                try {
                    // Data is received here
                    Log.d(TAG, result);

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray machineNames = jsonObject.getJSONArray("machine_names");
                    JSONArray brandNames = jsonObject.getJSONArray("brand_names");
                    JSONArray modelNames = jsonObject.getJSONArray("model_names");

                    ArrayList<String> nameList = new ArrayList<>();
                    for (int i = 0; i < machineNames.length(); i++) {
                        nameList.add(machineNames.get(i).toString());
                    }

                    ArrayList<String> brandList = new ArrayList<>();
                    for (int i = 0; i < brandNames.length(); i++) {
                        brandList.add(brandNames.get(i).toString());
                    }

                    ArrayList<String> modelList = new ArrayList<>();
                    for (int i = 0; i < modelNames.length(); i++) {
                        modelList.add(modelNames.get(i).toString());
                    }

                    ArrayAdapter<String> machineNameAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, nameList);
                    nameET.setAdapter(machineNameAdapter);

                    ArrayAdapter<String> brandNameAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, brandList);
                    brandET.setAdapter(brandNameAdapter);

                    ArrayAdapter<String> modelNameAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, modelList);
                    modelET.setAdapter(modelNameAdapter);

                } catch (JSONException e) {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }
            }
        }).execute();

        asyncTaskHelper = (AsyncTaskHelper) new AsyncTaskHelper(URLS.GET_ALL_ORGANIZATION_URL, "GET", new AsyncTaskHelper.AsyncResponse() {
            @Override
            public void processFinish(String result) {
                try {
                    // Data is received here
                    Log.d(TAG, result);

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray organizationNames = jsonObject.getJSONArray("organization_names");

                    ArrayList<String> data = new ArrayList<>();
                    for (int i = 0; i < organizationNames.length(); i++) {
                        data.add(organizationNames.get(i).toString());
                    }

                    ArrayAdapter<String> organizationAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, data);
                    organizationAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);

                    ownerET.setAdapter(organizationAdapter);
                    sentFromET.setAdapter(organizationAdapter);
                    sentToET.setAdapter(organizationAdapter);

                } catch (JSONException e) {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }
            }
        }).execute();

        // Date Picker Configuration
        MaterialDatePicker.Builder<?> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select date");
        final MaterialDatePicker<?> materialDatePicker = builder.build();

        // Configure Date
        dateBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Object>() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                dateET.setText(materialDatePicker.getHeaderText());
            }
        });

        // Default value for type
        type = "Rent";

        typeRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                type = ((RadioButton) view.findViewById(checkedId)).getText().toString();

                if (type.equals("Rent")) {
                    amountET.setHint("Rent amount");
                } else {
                    amountET.setHint("Sale price");
                }
                amountET.requestFocus();
            }
        });

        addBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMachine();
            }
        });
    }

    public void addMachine() {
        String name = nameET.getText().toString().trim();
        String brand = brandET.getText().toString().trim();
        String model = modelET.getText().toString().trim();
        String serial = Objects.requireNonNull(serialET.getText()).toString().trim();
        String owner = ownerET.getText().toString().trim();
        String sentFrom =  sentFromET.getText().toString().trim();
        String sentTo =  sentToET.getText().toString().trim();
        String challan = Objects.requireNonNull(challanET.getText()).toString().trim();
        String date = Objects.requireNonNull(dateET.getText()).toString().trim();
        String amount = Objects.requireNonNull(amountET.getText()).toString().trim();
        String remarks = Objects.requireNonNull(remarksET.getText()).toString().trim();

        if (name.isEmpty()) {
            nameET.setError("Field cannot be empty!");
            nameET.requestFocus();
            return;
        }

        if (brand.isEmpty()) {
            brandET.setError("Field cannot be empty!");
            brandET.requestFocus();
            return;
        }

        if (model.isEmpty()) {
            modelET.setError("Field cannot be empty!");
            modelET.requestFocus();
            return;
        }

        if (serial.isEmpty()) {
            serialET.setError("Field cannot be empty!");
            serialET.requestFocus();
            return;
        }

        if (owner.isEmpty()) {
            ownerET.setError("Field cannot be empty!");
            ownerET.requestFocus();
            return;
        }

        if (sentFrom.isEmpty()) {
            sentFromET.setError("Field cannot be empty!");
            sentFromET.requestFocus();
            return;
        }

        if (sentTo.isEmpty()) {
            sentToET.setError("Field cannot be empty!");
            sentToET.requestFocus();
            return;
        }

        if (challan.isEmpty()) {
            challanET.setError("Field cannot be empty!");
            challanET.requestFocus();
            return;
        }

        if (date.isEmpty()) {
            dateET.setError("Field cannot be empty!");
            return;
        }

        if (type.isEmpty()) {
            Toast.makeText(getActivity(), "Please select the machine purpose!", Toast.LENGTH_LONG).show();
            return;
        }

        if (amount.isEmpty()) {
            amountET.setError("Field cannot be empty!");
            amountET.requestFocus();
            return;
        }

        if (remarks.isEmpty()) {
            remarksET.setError("Field cannot be empty!");
            remarksET.requestFocus();
            return;
        }

        // Set up progressbar to show
        addPB.setVisibility(View.VISIBLE);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("brand", brand);
        parameters.put("model", model);
        parameters.put("serial", serial);
        parameters.put("owner", owner);
        parameters.put("sent_from", sentFrom);
        parameters.put("sent_to", sentTo);
        parameters.put("challan", challan);
        parameters.put("date", date);
        parameters.put("type", type);
        parameters.put("amount", amount);
        parameters.put("remarks", remarks);

        Log.d(TAG, parameters.toString());

        AsyncTaskHelper asyncTaskHelper = (AsyncTaskHelper) new AsyncTaskHelper(URLS.ADD_MACHINE_URL, "POST", parameters, new AsyncTaskHelper.AsyncResponse() {
            @Override
            public void processFinish(String result) {
                addPB.setVisibility(View.GONE);
                Log.d(TAG, result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }
            }
        }).execute();
    }

}