package com.example.jlne.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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

import com.example.jlne.R;
import com.example.jlne.helper.AsyncTaskHelper;
import com.example.jlne.helper.URLS;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TransferFragment extends DialogFragment {
    final String TAG = "__Transfer";
    private MaterialTextView nameTV, brandTV, modelTV, serialTV, ownerTV, locationTV;
    private TextInputEditText sentFromET, challanET, dateET, amountET, remarksET;
    private AutoCompleteTextView sentToET;
    private RadioGroup typeRG;
    private MaterialButton dateBT, transferBT;
    private ProgressBar transferPB;
    private String name, brand, model, serial, owner, location, sentFrom, sentTo, challan, date, type, amount, remarks;
    private Integer id;

    public TransferFragment() {
        // Required empty public constructor
    }


    public static TransferFragment newInstance(Integer id, String name, String brand, String model, String serial, String owner, String location) {
        TransferFragment transferFragment = new TransferFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putString("name", name);
        bundle.putString("brand", brand);
        bundle.putString("model", model);
        bundle.putString("serial", serial);
        bundle.putString("owner", owner);
        bundle.putString("location", location);
        transferFragment.setArguments(bundle);

        return transferFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Bundle arguments = getArguments();
            id = arguments.getInt("id");
            name = arguments.getString("name");
            brand = arguments.getString("brand");
            model = arguments.getString("model");
            serial = arguments.getString("serial");
            owner = arguments.getString("owner");
            location = arguments.getString("location");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transfer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameTV = view.findViewById(R.id.name_TV);
        brandTV = view.findViewById(R.id.brand_TV);
        modelTV = view.findViewById(R.id.model_TV);
        serialTV = view.findViewById(R.id.serial_TV);
        ownerTV = view.findViewById(R.id.owner_TV);
        locationTV = view.findViewById(R.id.location_TV);
        sentFromET = view.findViewById(R.id.sent_from_ET);
        sentToET = view.findViewById(R.id.sent_to_ET);
        challanET = view.findViewById(R.id.challan_ET);
        dateET = view.findViewById(R.id.date_ET);
        dateBT = view.findViewById(R.id.date_BT);
        typeRG = view.findViewById(R.id.type_RG);
        amountET = view.findViewById(R.id.amount_ET);
        remarksET = view.findViewById(R.id.remarks_ET);
        transferBT = view.findViewById(R.id.transfer_BT);
        transferPB = view.findViewById(R.id.transfer_PB);

        // setup to show machine information
        nameTV.setText(name);
        brandTV.setText(brand);
        modelTV.setText(model);
        serialTV.setText(serial);
        ownerTV.setText(owner);
        locationTV.setText(location);
        sentFromET.setText(location);

        AsyncTaskHelper asyncTaskHelper = (AsyncTaskHelper) new AsyncTaskHelper(URLS.GET_ALL_ORGANIZATION_URL, "GET", new AsyncTaskHelper.AsyncResponse() {
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
                    amountET.setText("");
                    amountET.setHint("Rent amount");
                    amountET.setEnabled(true);
                    amountET.requestFocus();
                } else if (type.equals("Sale")){
                    amountET.setText("");
                    amountET.setHint("Sale price");
                    amountET.setEnabled(true);
                    amountET.requestFocus();
                } else {
                    amountET.setText("0");
                    amountET.setHint("Not required");
                    amountET.setEnabled(false);
                }

            }
        });


        transferBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transferMachine();
            }
        });

    }

    public void transferMachine() {
        sentFrom = sentFromET.getText().toString().trim();
        sentTo = sentToET.getText().toString().trim();
        challan = challanET.getText().toString().trim();
        date = dateET.getText().toString().trim();
        amount = amountET.getText().toString().trim();
        remarks = remarksET.getText().toString().trim();

        // Sent From is set by default
        // Check if others are empty if transfer button is clicked and notify user

        if (sentFrom.isEmpty()) {
            sentFromET.setError("Field cannot be empty!");
            sentFromET.requestFocus();
        } else if (sentTo.isEmpty()) {
            sentToET.setError("Field cannot be empty!");
            sentToET.requestFocus();
        } else if (challan.isEmpty()) {
            challanET.setError("Field cannot be empty!");
            challanET.requestFocus();
        } else if (date.isEmpty()) {
            dateET.setError("Field cannot be empty!");
            dateET.requestFocus();
        } else if (amount.isEmpty()) {
            if (type.equals("Rent") || type.equals("Sale")) {
                amountET.setError("Field cannot be empty!");
                amountET.requestFocus();
            }
        } else if (remarks.isEmpty()) {
            remarksET.setError("Field cannot be empty!");
            remarksET.requestFocus();
        }

        // Set up progressbar to show
        transferPB.setVisibility(View.VISIBLE);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("id", String.valueOf(id));
        parameters.put("sent_from", sentFrom);
        parameters.put("sent_to", sentTo);
        parameters.put("challan", challan);
        parameters.put("date", date);
        parameters.put("type", type);
        parameters.put("owner", owner);
        parameters.put("amount", amount);
        parameters.put("remarks", remarks);

        AsyncTaskHelper asyncTaskHelper = (AsyncTaskHelper) new AsyncTaskHelper(URLS.TRANSFER_MACHINE_URL, "POST", parameters, new AsyncTaskHelper.AsyncResponse() {
            @Override
            public void processFinish(String result) {
                // Data is received here
                transferPB.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (!jsonObject.getBoolean("error")) {
                        Objects.requireNonNull(getDialog()).dismiss();
                        Objects.requireNonNull(getActivity()).onBackPressed();
                    }
                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }
            }
        }).execute();
    }
}