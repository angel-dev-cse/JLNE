package com.example.jlne.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.jlne.R;
import com.example.jlne.helper.AsyncTaskHelper;
import com.example.jlne.helper.URLS;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class DeleteMachineFragment extends DialogFragment {
    final String TAG = "__DeleteMachine";
    private String name, brand, model, serial, date, owner, location;
    private Integer id, challan;
    private MaterialTextView nameTV, brandTV, modelTV, serialTV, challanTV, dateTV, ownerTV, locationTV;
    private MaterialButton deleteBT, cancelBT;

    public DeleteMachineFragment() {
        // Required empty public constructor
    }

    public static DeleteMachineFragment newInstance(Integer id, String name, String brand, String model, String serial, Integer challan, String date, String owner, String location) {
        DeleteMachineFragment deleteMachineFragment = new DeleteMachineFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putString("name", name);
        bundle.putString("brand", brand);
        bundle.putString("model", model);
        bundle.putString("serial", serial);
        bundle.putInt("challan", challan);
        bundle.putString("date", date);
        bundle.putString("owner", owner);
        bundle.putString("location", location);

        deleteMachineFragment.setArguments(bundle);

        return deleteMachineFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            id = getArguments().getInt("id");
            name = getArguments().getString("name");
            brand = getArguments().getString("brand");
            model = getArguments().getString("model");
            serial = getArguments().getString("serial");
            challan = getArguments().getInt("challan");
            date = getArguments().getString("date");
            owner = getArguments().getString("owner");
            location = getArguments().getString("location");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow())
                .setBackgroundDrawable(new ColorDrawable(0));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delete_machine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameTV = view.findViewById(R.id.name_TV);
        brandTV = view.findViewById(R.id.brand_TV);
        modelTV = view.findViewById(R.id.model_TV);
        serialTV = view.findViewById(R.id.serial_TV);
        challanTV = view.findViewById(R.id.challan_TV);
        dateTV = view.findViewById(R.id.date_TV);
        ownerTV = view.findViewById(R.id.owner_TV);
        locationTV = view.findViewById(R.id.location_TV);
        deleteBT = view.findViewById(R.id.delete_BT);
        cancelBT = view.findViewById(R.id.cancel_BT);

        // Show user machine data from arguments
        nameTV.setText(name);
        brandTV.setText(brand);
        modelTV.setText(model);
        serialTV.setText(serial);
        challanTV.setText(String.valueOf(challan));
        dateTV.setText(date);
        ownerTV.setText(owner);
        locationTV.setText(location);

        deleteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("id", id.toString());

                AsyncTaskHelper asyncTaskHelper = (AsyncTaskHelper) new AsyncTaskHelper(URLS.DELETE_MACHINE_URL, "POST", parameters, new AsyncTaskHelper.AsyncResponse() {
                    @Override
                    public void processFinish(String result) {
                        // Result received here
                        Log.d(TAG, result);

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (!jsonObject.getBoolean("error")) {
                                // If machine list showing then refresh it
                                Fragment machineListFragment = getParentFragmentManager().findFragmentByTag("__MachineList");
                                if (machineListFragment != null && machineListFragment.isVisible()) {
                                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                    transaction.detach(machineListFragment).attach(machineListFragment).commit();
                                    Log.d(TAG, "Machine list refreshed!");
                                }

                                // If view machine is showing then replace with machine list
                                Fragment viewMachineFragment = getParentFragmentManager().findFragmentByTag("__ViewMachine");
                                if (viewMachineFragment != null && viewMachineFragment.isVisible()) {
                                    // First remove the dialog
                                    if (getDialog() != null) {
                                        getDialog().dismiss();
                                    }
                                    // Then simulate back button pressing
                                    if (getActivity() != null) {
                                        getActivity().onBackPressed();
                                    }

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
        });

        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getDialog() != null) {
                    getDialog().dismiss();
                }
            }
        });
    }

}