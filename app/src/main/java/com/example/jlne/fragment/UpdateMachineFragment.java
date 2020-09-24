package com.example.jlne.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.Toolbar;

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

public class UpdateMachineFragment extends DialogFragment {
    final String TAG = "__UpdateMachine";
    private AutoCompleteTextView nameET, brandET, modelET, ownerET, sentFromET, sentToET;
    private TextInputEditText serialET, challanET, dateET, amountET, remarksET;
    private ProgressBar updatePB;
    private MaterialButton dateBT, addBT;
    private RadioGroup typeRG;

    private String name, brand, model, serial, owner, sentFrom, sentTo, date, type, remarks;
    private Integer id, transaction_id, challan, amount;

    public UpdateMachineFragment() {
        // Required empty public constructor
    }

    public static UpdateMachineFragment newInstance(Integer id, Integer transactionID, String name, String brand, String model
            , String serial, String owner, String sentFrom, String sentTo, Integer challan, String date, String type
            , Integer amount, String remarks) {
        UpdateMachineFragment fragment = new UpdateMachineFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putInt("transaction_id", transactionID);
        bundle.putString("name", name);
        bundle.putString("brand", brand);
        bundle.putString("model", model);
        bundle.putString("serial", serial);
        bundle.putString("owner", owner);
        bundle.putString("sent_from", sentFrom);
        bundle.putString("sent_to", sentTo);
        bundle.putInt("challan", challan);
        bundle.putString("date", date);
        bundle.putString("type", type);
        bundle.putInt("amount", amount);
        bundle.putString("remarks", remarks);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Bundle arguments = getArguments();

            id = arguments.getInt("id");
            transaction_id = arguments.getInt("transaction_id");
            name = arguments.getString("name");
            brand = arguments.getString("brand");
            model = arguments.getString("model");
            serial = arguments.getString("serial");
            owner = arguments.getString("owner");
            sentFrom = arguments.getString("sent_from");
            sentTo = arguments.getString("sent_to");
            challan = arguments.getInt("challan");
            date = arguments.getString("date");
            type = arguments.getString("type");
            amount = arguments.getInt("amount");
            remarks = arguments.getString("remarks");
        }

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
        return inflater.inflate(R.layout.fragment_update_machine, container, false);
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
        updatePB = view.findViewById(R.id.update_PB);
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

        // Setup Previous Data
        nameET.setText(name);
        brandET.setText(brand);
        modelET.setText(model);
        serialET.setText(serial);
        ownerET.setText(owner);
        sentFromET.setText(sentFrom);
        sentToET.setText(sentTo);
        challanET.setText(String.valueOf(challan));
        dateET.setText(date);
        amountET.setText(String.valueOf(amount));
        remarksET.setText(remarks);

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

        // Default value for type from Previous Data
        if (type.equals("Rent")) {
            typeRG.check(R.id.rent_radio_button);
        } else {
            typeRG.check(R.id.sale_radio_button);
        }


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
                updateMachine();
            }
        });
    }

    public void updateMachine() {
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
        updatePB.setVisibility(View.VISIBLE);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("id", id.toString());
        parameters.put("transaction_id", transaction_id.toString());
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

        AsyncTaskHelper asyncTaskHelper = (AsyncTaskHelper) new AsyncTaskHelper(URLS.UPDATE_MACHINE_URL, "POST", parameters, new AsyncTaskHelper.AsyncResponse() {
            @Override
            public void processFinish(String result) {
                updatePB.setVisibility(View.GONE);
                Log.d(TAG, result);
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    if (!jsonObject.getBoolean("error")) {
                        Fragment machineListFragment = getParentFragmentManager().findFragmentByTag("__MachineList");
                        Fragment viewMachineFragment = getParentFragmentManager().findFragmentByTag("__ViewMachine");

                        // If machine list fragment is showing refresh it
                        if (machineListFragment != null && machineListFragment.isVisible()) {
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            transaction.detach(machineListFragment).attach(machineListFragment).commit();
                            Log.d(TAG, "Machine list refreshed!");
                        }

                        // If view machine fragment is showing refresh it
                        else if (viewMachineFragment != null && viewMachineFragment.isVisible()) {
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            transaction.detach(viewMachineFragment).attach(viewMachineFragment).commit();
                            Log.d(TAG, "View machine refreshed!");
                        }

                        if (getDialog() != null) {
                            getDialog().dismiss();
                        }

                    }

                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }
            }
        }).execute();
    }

}